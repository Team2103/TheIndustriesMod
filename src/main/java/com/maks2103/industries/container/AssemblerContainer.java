package com.maks2103.industries.container;

import com.maks2103.industries.tileEntity.AssemblerTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class AssemblerContainer extends Container {
    private final AssemblerTileEntity assemblerTileEntity;

    public AssemblerContainer(IInventory playerInventory, AssemblerTileEntity assemblerTileEntity) {
        this.assemblerTileEntity = assemblerTileEntity;

        IItemHandler itemHandler = assemblerTileEntity.getItemHandler();
        int index = 0;

        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                addSlotToContainer(new SlotItemHandler(itemHandler, index++, 180 / 2 + 29 * i, 59 / 2 + 29 * j));
            }
        }

        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(playerInventory, index++, 181 / 2 + j * 29, 198 / 2 + i * 29));
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
}