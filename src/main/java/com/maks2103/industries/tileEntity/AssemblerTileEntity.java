package com.maks2103.industries.tileEntity;

import com.maks2103.industries.assembler.AssemblerRecipe;
import com.maks2103.industries.assembler.AssemblerRecipeManager;
import com.maks2103.industries.gui.AssemblerGui;
import com.maks2103.industries.net.RemoteCaller;
import com.maks2103.industries.util.ListenableItemStackHandler;
import com.maks2103.industries.util.SerializableEnergyStorage;
import com.maks2103.industries.util.SerializableMutableBlockPos;
import com.maks2103.industries.util.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.atomic.AtomicReference;


public class AssemblerTileEntity extends TileEntity implements SerializableEnergyStorage.Listener, ListenableItemStackHandler.Listener, ITickable {
    private static final int MAX_ENERGY = 64000;
    private static final int MAX_RECEIVE = 30;
    private static final Method CRAFT_OK_METHOD;

    static {
        try {
            CRAFT_OK_METHOD = AssemblerGui.class.getDeclaredMethod("craftOk");
        } catch (NoSuchMethodException e) {
            throw new Error(e);
        }
    }

    private final ListenableItemStackHandler itemHandler;
    private final SerializableEnergyStorage energyStorage;

    private int lastEnergy = 0;
    private boolean canCraft = false;
    private AssemblerRecipe currentRecipe = null;

    private AtomicReference<State> state;
    private AtomicReference<ItemStack> output;
    private int progress;

    public AssemblerTileEntity() {
        itemHandler = new ListenableItemStackHandler(9);
        itemHandler.setListener(this);
        energyStorage = new SerializableEnergyStorage(MAX_ENERGY, MAX_RECEIVE, 30);
        energyStorage.setListener(this);
        state = new AtomicReference<>(State.ERROR);
        output = new AtomicReference<>(ItemStack.EMPTY);
        progress = 0;
        validate();

        checkCraftState();

        markDirty();
    }

    public boolean canCraft() {
        return canCraft;
    }

    public AssemblerRecipe getCurrentRecipe() {
        return currentRecipe;
    }

    public State getState() {
        return state.get();
    }

    public void setState(State state) {
        this.state.set(state);
    }

    public ItemStack getOutput() {
        return output.get();
    }

    @SuppressWarnings("MethodCallSideOnly") // Method closeScreen called in remote world
    public ItemStack takeOutput() {
        ItemStack out = output.get();
        output.set(ItemStack.EMPTY);
        state.set(State.READY);

        markDirty();
        IBlockState blockState = world.getBlockState(getPos());
        world.notifyBlockUpdate(getPos(), blockState, blockState, 3);

        return out;
    }

    public void setCurrentRecipe(AssemblerRecipe currentRecipe) {
        this.currentRecipe = currentRecipe;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || capability == CapabilityEnergy.ENERGY || super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (T) itemHandler;
        } else if(capability == CapabilityEnergy.ENERGY) {
            return (T) energyStorage;
        } else {
            return super.getCapability(capability, facing);
        }
    }

    public IItemHandler getItemHandler() {
        return itemHandler;
    }

    public IEnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        NBTTagCompound itemHandlerCompound = compound.getCompoundTag("inventory");
        itemHandler.deserializeNBT(itemHandlerCompound);
        NBTTagCompound energy = compound.getCompoundTag("energy");
        energyStorage.deserializeNBT(energy);

        canCraft = compound.getBoolean("canCraft");
        currentRecipe = AssemblerRecipeManager.fromNBT(compound.getCompoundTag("recipe"));
        String state = compound.getString("state");
        this.state.set(State.valueOf(state.isEmpty() ? State.READY.name() : state));
        progress = compound.getInteger("progress");
        output.set(new ItemStack(compound.getCompoundTag("output")));
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound compound) {
        NBTTagCompound nbtTagCompound = super.writeToNBT(compound);
        nbtTagCompound.setTag("inventory", itemHandler.serializeNBT());
        nbtTagCompound.setTag("energy", energyStorage.serializeNBT());
        nbtTagCompound.setBoolean("canCraft", canCraft);
        nbtTagCompound.setTag("recipe", AssemblerRecipeManager.toNBT(currentRecipe));
        nbtTagCompound.setString("state", state.get().name());
        nbtTagCompound.setInteger("progress", progress);

        NBTTagCompound outputTag = new NBTTagCompound();
        output.get().writeToNBT(outputTag);
        nbtTagCompound.setTag("output", outputTag);

        return nbtTagCompound;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
    }

    @Nonnull
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound compound = new NBTTagCompound();
        writeToNBT(compound);
        return new SPacketUpdateTileEntity(this.getPos(), 1, compound);
    }

    @Override
    public void onReceive(boolean simulate) {
        checkEnergy(simulate);
    }

    @Override
    public void onExtract(boolean simulate) {
        checkEnergy(simulate);
    }

    private void checkEnergy(boolean simulate) {
        if(!simulate && !world.isRemote && lastEnergy != energyStorage.getEnergyStored()) {
            this.markDirty();
            IBlockState blockState = world.getBlockState(getPos());
            world.notifyBlockUpdate(getPos(), blockState, blockState, 3);
            lastEnergy = energyStorage.getEnergyStored();
        }
    }

    public void sync() {
        markDirty();
        IBlockState state = getWorld().getBlockState(getPos());
        getWorld().notifyBlockUpdate(getPos(), state, state, 3);
    }

    private void prewRecipe() {
        List<AssemblerRecipe> recipes = AssemblerRecipeManager.getRecipes();
        int idx = (currentRecipe == null) ? 0 : recipes.indexOf(currentRecipe);
        currentRecipe = (idx - 1 < 0) ? recipes.get(recipes.size() - 1) : recipes.get(idx - 1);

        IBlockState blockState = world.getBlockState(getPos());
        checkCraftState();
        world.notifyBlockUpdate(getPos(), blockState, blockState, 3);
    }

    private void nextRecipe() {
        List<AssemblerRecipe> recipes = AssemblerRecipeManager.getRecipes();
        int idx = (currentRecipe == null) ? 0 : recipes.indexOf(currentRecipe);
        currentRecipe = (idx + 1 == recipes.size()) ? recipes.get(0) : recipes.get(idx + 1);

        IBlockState blockState = world.getBlockState(getPos());
        checkCraftState();
        world.notifyBlockUpdate(getPos(), blockState, blockState, 3);
    }

    @SuppressWarnings("MethodCallSideOnly") // Method closeScreen called in remote world
    private void tryCraft() {
        if(canCraft && state.get() == State.READY) {
            AssemblerRecipe recipe = currentRecipe;
            state.set(State.CRAFTING);
            takeItems(recipe);
            progress = currentRecipe.getCraftingTime() * 20; //x sec * 20 ticks/sec
            sync();
            RemoteCaller.callRemote(CRAFT_OK_METHOD, Side.CLIENT);
        }
    }
    private void takeItems(AssemblerRecipe recipe) {
        List<ItemStack> splicedIn = Utils.spliceItemStackList(Arrays.asList(recipe.getCraftItems()));
        List<ItemStack> ourInv = getInventoryItemStacks();

        ListIterator<ItemStack> inIterator = splicedIn.listIterator();

        while(inIterator.hasNext()) {
            ItemStack nextRequiredItem = inIterator.next();
            int[] inElemOreDictIds = OreDictionary.getOreIDs(nextRequiredItem);
            ListIterator<ItemStack> tileInvIterator = ourInv.listIterator();
            for(int slot = 0; tileInvIterator.hasNext(); slot++) {
                ItemStack nextInv = tileInvIterator.next();
                if(nextInv.isEmpty()) continue;

                int[] outElemOreDictIds = OreDictionary.getOreIDs(nextInv);
                if(nextRequiredItem.getItem() == nextInv.getItem() || Utils.containsAny(inElemOreDictIds, outElemOreDictIds)) {
                    if(nextRequiredItem.getCount() > nextInv.getCount()) {
                        tileInvIterator.remove();
                        itemHandler.setStackInSlot(slot, ItemStack.EMPTY);
                        nextRequiredItem.setCount(nextRequiredItem.getCount() - nextInv.getCount());
                    } else if(nextRequiredItem.getCount() == nextInv.getCount()) {
                        tileInvIterator.remove();
                        itemHandler.setStackInSlot(slot, ItemStack.EMPTY);
                        inIterator.remove();
                    } else {
                        inIterator.remove();
                        nextInv.setCount(nextInv.getCount() - nextRequiredItem.getCount());
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void onContentsChanged(int slot) {
        boolean last = canCraft;
        checkCraftState();
        if(canCraft != last) {
            IBlockState blockState = world.getBlockState(getPos());
            world.notifyBlockUpdate(getPos(), blockState, blockState, 3);
        }
    }

    /**
     * Do not notify update
     */
    public void checkCraftState() {
        if(currentRecipe == null) {
            canCraft = false;
            return;
        }
        canCraft = Utils.canCraftItem(getInventoryItemStacks(), Arrays.asList(currentRecipe.getCraftItems()));
    }

    private List<ItemStack> getInventoryItemStacks() {
        List<ItemStack> ret = new ArrayList<>();
        for(int i = 0; i < itemHandler.getSlots(); i++) {
            ret.add(itemHandler.getStackInSlot(i));
        }
        return ret;
    }

    @Override
    public void update() {
        if(!world.isRemote && state.get() == State.CRAFTING) {
            int extracted = energyStorage.extractEnergy(30, false);
            if(extracted == 0) return;

            if(progress > 0)
                progress--;
            else {
                progress = 0;
                state.set(State.DONE);
                output.set(currentRecipe.getOutputItem().copy());
            }
            sync();
        }
    }

    public void dropAllItems() {
        BlockPos pos = getPos();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        for(int i = 0; i < itemHandler.getSlots(); i++) {
            ItemStack stack = itemHandler.getStackInSlot(i);
            if(!stack.isEmpty()) {
                InventoryHelper.spawnItemStack(world, x, y, z, stack);
            }
        }
        if(!output.get().isEmpty()) {
            InventoryHelper.spawnItemStack(world, x, y, z, output.get());
        }
    }

    public static final class RemoteMethods {
        private RemoteMethods() {
        }

        public static void prewRecipe(MessageContext context, SerializableMutableBlockPos blockPos) {
            AssemblerTileEntity tileEntity = (AssemblerTileEntity) context.getServerHandler().playerEntity.world.getTileEntity(blockPos);
            if(tileEntity != null)
                tileEntity.prewRecipe();
        }

        public static void nextRecipe(MessageContext context, SerializableMutableBlockPos blockPos) {
            AssemblerTileEntity tileEntity = (AssemblerTileEntity) context.getServerHandler().playerEntity.world.getTileEntity(blockPos);
            if(tileEntity != null)
                tileEntity.nextRecipe();
        }

        public static void tryCraft(MessageContext context, SerializableMutableBlockPos blockPos) {
            AssemblerTileEntity tileEntity = (AssemblerTileEntity) context.getServerHandler().playerEntity.world.getTileEntity(blockPos);
            if(tileEntity != null)
                tileEntity.tryCraft();
        }
    }

    public enum State {
        /**
         * Assembler has errors. Do not show any gui
         */
        ERROR,
        /**
         * Assembler is ready. Show main gui
         */
        READY,
        /**
         * Assembler is working. Do not show gui
         */
        CRAFTING,
        /**
         * Assembler complete work. Show completion gui
         */
        DONE
    }
}
