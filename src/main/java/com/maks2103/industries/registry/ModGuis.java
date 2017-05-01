package com.maks2103.industries.registry;

import com.maks2103.industries.container.AssemblerContainer;
import com.maks2103.industries.container.ResearchBookContainer;
import com.maks2103.industries.gui.AssemblerGui;
import com.maks2103.industries.gui.AssemblerOutputGui;
import com.maks2103.industries.gui.ResearchBookGui;
import com.maks2103.industries.handler.gui.GuiFactory;
import com.maks2103.industries.handler.gui.GuiHandler;
import com.maks2103.industries.tileEntity.AssemblerTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Register all {@link com.maks2103.industries.handler.gui.GuiFactory} for mod and contains it's ids
 */
public enum ModGuis {
    RESEARCH_BOOK(0),
    ASSEMBLER(1),
    ASSEMBLER_OUTPUT(2);

    static {
        GuiHandler.registerGuiFactory(new ResearchBookGuiFactory());
        GuiHandler.registerGuiFactory(new AssemblerGuiFactory());
        GuiHandler.registerGuiFactory(new AssemblerOutputGuiFacotry());
    }

    private final int id;

    ModGuis(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    private static final class ResearchBookGuiFactory implements GuiFactory {

        @Override
        public int getId() {
            return RESEARCH_BOOK.getId();
        }

        @SideOnly(Side.CLIENT)
        @Override
        public Object getClientGuiElement(EntityPlayer player, World world, BlockPos blockPos) {
            return new ResearchBookGui(new ResearchBookContainer());
        }

        @Override
        public Object getServerGuiElement(EntityPlayer player, World world, BlockPos blockPos) {
            return new ResearchBookContainer();
        }
    }

    private static final class AssemblerGuiFactory implements GuiFactory {

        @Override
        public int getId() {
            return ASSEMBLER.getId();
        }

        @SideOnly(Side.CLIENT)
        @Override
        public Object getClientGuiElement(EntityPlayer player, World world, BlockPos blockPos) {
            return new AssemblerGui(new AssemblerContainer(player, (AssemblerTileEntity) world.getTileEntity(blockPos)));
        }

        @Override
        public Object getServerGuiElement(EntityPlayer player, World world, BlockPos blockPos) {
            return new AssemblerContainer(player, (AssemblerTileEntity) world.getTileEntity(blockPos));
        }
    }

    private static final class AssemblerOutputGuiFacotry implements GuiFactory {

        @Override
        public int getId() {
            return ASSEMBLER_OUTPUT.getId();
        }

        @SideOnly(Side.CLIENT)
        @Override
        public Object getClientGuiElement(EntityPlayer player, World world, BlockPos blockPos) {
            return new AssemblerOutputGui(new AssemblerContainer(player, (AssemblerTileEntity) world.getTileEntity(blockPos)));
        }

        @Override
        public Object getServerGuiElement(EntityPlayer player, World world, BlockPos blockPos) {
            return new AssemblerContainer(player, (AssemblerTileEntity) world.getTileEntity(blockPos));
        }
    }
}
