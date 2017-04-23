package com.maks2103.industries.handler.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Create client and server gui elements for specific gui
 */
public interface GuiFactory {
    /**
     * Gui id
     */
    int getId();

    Object getClientGuiElement(EntityPlayer player, World world, BlockPos blockPos);

    Object getServerGuiElement(EntityPlayer player, World world, BlockPos blockPos);
}
