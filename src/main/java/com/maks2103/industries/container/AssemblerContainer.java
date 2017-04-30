package com.maks2103.industries.container;

import com.maks2103.industries.tileEntity.AssemblerTileEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class AssemblerContainer extends Container {
    private final AssemblerTileEntity assemblerTileEntity;

    public AssemblerContainer(IInventory playerInventory, AssemblerTileEntity assemblerTileEntity) {
        this.assemblerTileEntity = assemblerTileEntity;

        IItemHandler itemHandler = assemblerTileEntity.getItemHandler();
        int index = 0;
        if(assemblerTileEntity.getState() == AssemblerTileEntity.State.READY) {
            for(int i = 0; i < 3; i++) {
                for(int j = 0; j < 3; j++) {
                    addSlotToContainer(new SlotItemHandler(itemHandler, index++, 180 / 2 + 34 / 2 * i + 1, 59 / 2 + 34 / 2 * j + 1));
                }
            }
        } else {
            addSlotToContainer(new TakeOnlySlot(assemblerTileEntity, 10, 10));
        }

        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(playerInventory, (i == 3) ? j : (i * 9 + j + 9), 165 / 2 + j * 34 / 2, 182 / 2 + i * 34 / 2));
            }
        }
    }

    public AssemblerTileEntity getAssemblerTileEntity() {
        return assemblerTileEntity;
    }

    @Override
    public boolean canInteractWith(@Nonnull EntityPlayer playerIn) {
        return true;
    }

    @Nonnull
    @Override
    public ItemStack transferStackInSlot(@Nonnull EntityPlayer playerIn, int index) {
        Slot slot = this.inventorySlots.get(index);
        return slot != null ? slot.getStack() : ItemStack.EMPTY;
    }

    private static final class TakeOnlySlot extends Slot {

        public TakeOnlySlot(AssemblerTileEntity tileEntity, int xPosition, int yPosition) {
            super(new IInventory() {
                @Override
                public int getSizeInventory() {
                    return 1;
                }

                @Override
                public boolean isEmpty() {
                    return tileEntity.getOutput().isEmpty();
                }

                @Override
                public ItemStack getStackInSlot(int index) {
                    return tileEntity.getOutput();
                }

                @Nonnull
                @Override
                public ItemStack decrStackSize(int index, int count) {
                    ItemStack stack = tileEntity.takeOutput();
                    markDirty();
                    return stack;
                }

                @Nonnull
                @Override
                public ItemStack removeStackFromSlot(int index) {
                    ItemStack stack = tileEntity.takeOutput();
                    markDirty();
                    return stack;
                }

                @Override
                public void setInventorySlotContents(int index, @Nonnull ItemStack stack) {
                    //Nope
                }

                @Override
                public int getInventoryStackLimit() {
                    return tileEntity.getOutput().getMaxStackSize();
                }

                @Override
                public void markDirty() {
                    tileEntity.markDirty();
                    World world = tileEntity.getWorld();
                    BlockPos pos = tileEntity.getPos();
                    IBlockState blockState = world.getBlockState(pos);
                    world.notifyBlockUpdate(pos, blockState, blockState, 3);
                }

                @Override
                public boolean isUsableByPlayer(@Nonnull EntityPlayer player) {
                    return true;
                }

                @Override
                public void openInventory(@Nonnull EntityPlayer player) {
                    //Ok
                }

                @Override
                public void closeInventory(@Nonnull EntityPlayer player) {

                }

                @Override
                public boolean isItemValidForSlot(int index, ItemStack stack) {
                    return false;
                }

                @Override
                public int getField(int id) {
                    return 0;
                }

                @Override
                public void setField(int id, int value) {

                }

                @Override
                public int getFieldCount() {
                    return 0;
                }

                @Override
                public void clear() {

                }

                @Nonnull
                @Override
                public String getName() {
                    return "tempInv";
                }

                @Override
                public boolean hasCustomName() {
                    return false;
                }

                @Nonnull
                @Override
                public ITextComponent getDisplayName() {
                    return new TextComponentString("");
                }
            }, 0, xPosition, yPosition);
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            return false;
        }
    }
}
