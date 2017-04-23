package com.maks2103.industries.handler.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;
import java.util.concurrent.ConcurrentHashMap;

public final class GuiHandler implements IGuiHandler {
    private static final ConcurrentHashMap<Integer, GuiFactory> guiMap = new ConcurrentHashMap<Integer, GuiFactory>();
    private static final GuiHandler INSTANCE = new GuiHandler();

    private GuiHandler() {
    }

    @Nullable
    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        GuiFactory registeredGui = guiMap.get(id);
        return (registeredGui == null) ? null : registeredGui.getServerGuiElement(player, world, new BlockPos(x, y, z));
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        GuiFactory registeredGui = guiMap.get(id);
        return (registeredGui == null) ? null : registeredGui.getClientGuiElement(player, world, new BlockPos(x, y, z));
    }

    public static void registerGuiFactory(GuiFactory guiFactory) {
        guiMap.put(guiFactory.getId(), guiFactory);
    }

    public static GuiHandler getInstance() {
        return INSTANCE;
    }
}
