package com.maks2103.industries.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class ContainerResearchBook extends Container {
    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }
}
