package com.maks2103.industries.registry;

import com.maks2103.industries.container.ContainerResearchBook;
import com.maks2103.industries.gui.GuiResearchBook;
import com.maks2103.industries.handler.gui.GuiFactory;
import com.maks2103.industries.handler.gui.GuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Register all {@link com.maks2103.industries.handler.gui.GuiFactory} for mod and contains it's ids
 */
public enum ModGuis {
    RESEARCH_BOOK(0);

    static {
        GuiHandler.registerGuiFactory(new ResearchBookGuiFactory());
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
            return new GuiResearchBook(new ContainerResearchBook());
        }

        @SideOnly(Side.SERVER)
        @Override
        public Object getServerGuiElement(EntityPlayer player, World world, BlockPos blockPos) {
            return new ContainerResearchBook();
        }
    }
}
