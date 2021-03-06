package com.maks2103.industries.assembler;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@Deprecated //TODO remove test recipe
public class TestAsseblerRecipe2 implements AssemblerRecipe {
    @Override
    public int getCraftingTime() {
        return 100;
    }

    @Override
    public ItemStack[] getCraftItems() {
        return new ItemStack[]{new ItemStack(Blocks.BONE_BLOCK, 100), new ItemStack(Items.APPLE, 13)};
    }

    @Override
    public ItemStack getOutputItem() {
        return new ItemStack(Blocks.BRICK_BLOCK);
    }

    @Override
    public AssemblerPreviewModel getPreviewModel() {
        return new AssemblerPreviewModel() {
            @SideOnly(Side.CLIENT)
            @Override
            public void render() {
                GlStateManager.scale(5, 5, 5);
//                GlStateManager.rotate(90, 0, 0, 1);
                GL11.glColor3f(0.5f, 0.5f, 1.0f);

                GL11.glBegin(GL11.GL_QUADS);
                GL11.glColor3f(1.0f, 1.0f, 0.0f);
                GL11.glVertex3f(1.0f, 1.0f, -1.0f);
                GL11.glVertex3f(-1.0f, 1.0f, -1.0f);
                GL11.glVertex3f(-1.0f, 1.0f, 1.0f);
                GL11.glVertex3f(1.0f, 1.0f, 1.0f);
                GL11.glColor3f(1.0f, 0.5f, 0.0f);
                GL11.glVertex3f(1.0f, -1.0f, 1.0f);
                GL11.glVertex3f(-1.0f, -1.0f, 1.0f);
                GL11.glVertex3f(-1.0f, -1.0f, -1.0f);
                GL11.glVertex3f(1.0f, -1.0f, -1.0f);
                GL11.glColor3f(1.0f, 0.0f, 0.0f);
                GL11.glVertex3f(1.0f, 1.0f, 1.0f);
                GL11.glVertex3f(-1.0f, 1.0f, 1.0f);
                GL11.glVertex3f(-1.0f, -1.0f, 1.0f);
                GL11.glVertex3f(1.0f, -1.0f, 1.0f);
                GL11.glColor3f(1.0f, 1.0f, 0.0f);
                GL11.glVertex3f(1.0f, -1.0f, -1.0f);
                GL11.glVertex3f(-1.0f, -1.0f, -1.0f);
                GL11.glVertex3f(-1.0f, 1.0f, -1.0f);
                GL11.glVertex3f(1.0f, 1.0f, -1.0f);
                GL11.glColor3f(0.0f, 0.0f, 1.0f);
                GL11.glVertex3f(-1.0f, 1.0f, 1.0f);
                GL11.glVertex3f(-1.0f, 1.0f, -1.0f);
                GL11.glVertex3f(-1.0f, -1.0f, -1.0f);
                GL11.glVertex3f(-1.0f, -1.0f, 1.0f);
                GL11.glColor3f(1.0f, 0.0f, 1.0f);
                GL11.glVertex3f(1.0f, 1.0f, -1.0f);
                GL11.glVertex3f(1.0f, 1.0f, 1.0f);
                GL11.glVertex3f(1.0f, -1.0f, 1.0f);
                GL11.glVertex3f(1.0f, -1.0f, -1.0f);
                GL11.glEnd();
            }
        };
    }

    @Override
    public int getId() {
        return 123;
    }
}
