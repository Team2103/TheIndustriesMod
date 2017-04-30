package com.maks2103.industries.tileEntity;

import com.maks2103.industries.assembler.AssemblerRecipe;
import com.maks2103.industries.assembler.AssemblerRecipeManager;
import com.maks2103.industries.util.ListenableItemStackHandler;
import com.maks2103.industries.util.SerializableEnergyStorage;
import com.maks2103.industries.util.SerializableMutableBlockPos;
import com.maks2103.industries.util.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicReference;


public class AssemblerTileEntity extends TileEntity implements SerializableEnergyStorage.Listener, ListenableItemStackHandler.Listener {
    private static final int MAX_ENERGY = 64000;
    private static final int MAX_RECEIVE = 30;
    private static final Timer TIMER = new Timer("AssemblerTileEntity-craft-timer", true);

    private final ListenableItemStackHandler itemHandler;
    private final SerializableEnergyStorage energyStorage;

    private int lastEnergy = 0;
    private boolean canCraft = false;
    private AssemblerRecipe currentRecipe = null;

    private AtomicReference<State> state;
    private AtomicReference<ItemStack> output;

    public AssemblerTileEntity() {
        itemHandler = new ListenableItemStackHandler(9);
        itemHandler.setListener(this);
        energyStorage = new SerializableEnergyStorage(MAX_ENERGY, MAX_RECEIVE, -1);
        energyStorage.setListener(this);
        state = new AtomicReference<>(State.READY);
        output = new AtomicReference<>(ItemStack.EMPTY);
        validate();

        checkCraftState();
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

    public ItemStack getOutput() {
        return output.get();
    }

    public ItemStack takeOutput() {
        ItemStack out = output.get();
        output.set(ItemStack.EMPTY);
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

    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound compound) {
        NBTTagCompound nbtTagCompound = super.writeToNBT(compound);
        nbtTagCompound.setTag("inventory", itemHandler.serializeNBT());
        nbtTagCompound.setTag("energy", energyStorage.serializeNBT());
        nbtTagCompound.setBoolean("canCraft", canCraft);
        nbtTagCompound.setTag("recipe", AssemblerRecipeManager.toNBT(currentRecipe));
        return nbtTagCompound;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound compound = new NBTTagCompound();
        writeToNBT(compound);
        return new SPacketUpdateTileEntity(this.getPos(), 1, compound);
    }

    @Override
    public void onReceive(boolean simulate) {
        if(!simulate && !world.isRemote && lastEnergy != energyStorage.getEnergyStored()) {
            this.markDirty();
            IBlockState blockState = world.getBlockState(getPos());
            world.notifyBlockUpdate(getPos(), blockState, blockState, 3);
            lastEnergy = energyStorage.getEnergyStored();
        }
    }

    @Override
    public void onExtract(boolean simulate) {

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

    private void tryCraft() {
        if(canCraft) {
            AssemblerRecipe recipe = currentRecipe;
            state.set(State.CRAFTING);
            takeItems(recipe);
            TIMER.schedule(new TimerTask() {
                @Override
                public void run() {
                    System.out.println("Complete");
                    output.set(recipe.getOutputItem());
                    state.set(State.DONE);
                }
            }, currentRecipe.getCraftingTime() * 1000);
        }
    }

    private void takeItems(AssemblerRecipe recipe) {

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
    private void checkCraftState() {
        if(currentRecipe == null) {
            canCraft = false;
            return;
        }
        дада canCraft = Utils.allMatch(getInventoryItemStacks(), Arrays.asList(currentRecipe.getCraftItems()));
    }

    private List<ItemStack> getInventoryItemStacks() {
        List<ItemStack> ret = new ArrayList<>();
        for(int i = 0; i < itemHandler.getSlots(); i++) {
            ret.add(itemHandler.getStackInSlot(i));
        }
        return ret;
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
