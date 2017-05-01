package com.maks2103.industries.container;

import com.maks2103.industries.tileEntity.AssemblerTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public final class AssemblerContainer extends Container {
    private final AssemblerTileEntity tileEntity;

    @SuppressWarnings("MethodCallSideOnly") //Method closeScreen called only in remote world
    public AssemblerContainer(EntityPlayer player, AssemblerTileEntity tileEntity) {
        this.tileEntity = tileEntity;

        if(!tileEntity.getWorld().isRemote) {
            ((EntityPlayerMP) player).connection.sendPacket(tileEntity.getUpdatePacket()); //Try update tileEntity manually
        }

        IItemHandler itemHandler = tileEntity.getItemHandler();
        int index = 0;

        int userInvXPos;
        int userInvYPos;
        if(tileEntity.getState() == AssemblerTileEntity.State.READY) {
            for(int i = 0; i < 3; i++) {
                for(int j = 0; j < 3; j++) {
                    addSlotToContainer(new SlotItemHandler(itemHandler, index++, 91 + 17 * i, 30 + 17 * j));
                }
            }
            userInvXPos = 82;
            userInvYPos = 91;
        } else if(tileEntity.getState() == AssemblerTileEntity.State.DONE) {
            addSlotToContainer(new TakeOnlySlot(tileEntity, 122, 28));
            userInvXPos = 54;
            userInvYPos = 66;
        } else {
            userInvXPos = -1000;
            userInvYPos = -1000;
            if(tileEntity.getWorld().isRemote) {
                Minecraft.getMinecraft().player.closeScreen();
            }
        }

        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(player.inventory, (i == 3) ? j : (i * 9 + j + 9), userInvXPos + j * 17, userInvYPos + i * 17));
            }
        }
    }

    public AssemblerTileEntity getTileEntity() {
        return tileEntity;
    }

    @Override
    public boolean canInteractWith(@Nonnull EntityPlayer playerIn) {
        return true;
    }

    @Nonnull
    @Override
    public ItemStack transferStackInSlot(@Nonnull EntityPlayer playerIn, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if(slot != null && slot.getHasStack()) {
            ItemStack stackOriginal = slot.getStack();
            stack = stackOriginal.copy();

            if(tileEntity.getState() == AssemblerTileEntity.State.DONE) {
                if(!this.mergeItemStack(stackOriginal, 1, 37, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if(index < 9) { //0 ... 8 - crafting input
                    if(!this.mergeItemStack(stackOriginal, 9, 45, true)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if(!this.mergeItemStack(stackOriginal, 0, 9, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                tileEntity.checkCraftState();
                tileEntity.sync();
            }

            if(stackOriginal.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if(stackOriginal.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, stackOriginal);
        }

        return stack;
    }

    private static final class TakeOnlySlot extends Slot {

        TakeOnlySlot(AssemblerTileEntity tileEntity, int xPosition, int yPosition) {
            super(new IInventory() {
                @Override
                public int getSizeInventory() {
                    return 1;
                }

                @Override
                public boolean isEmpty() {
                    return tileEntity.getOutput().isEmpty();
                }

                @Nonnull
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
                    tileEntity.sync();
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
                public boolean isItemValidForSlot(int index, @Nonnull ItemStack stack) {
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
