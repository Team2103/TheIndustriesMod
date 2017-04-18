package com.maks2103.industries;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class GuiResearchBook extends GuiContainer {
    public ContainerResearchBook container;

    public GuiResearchBook(ContainerResearchBook container) {
        super(container);
        this.container = container;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
//        GlStateManager.pushMatrix();
//        GlStateManager.translate((width - xSize) / 2, (height - ySize) / 2, 0);
//        Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(Items.APPLE), ItemCameraTransforms.TransformType.GUI);
//        GlStateManager.popMatrix();
        this.mc.getTextureManager().bindTexture(GUI_TEXTURE);
        drawTexturedModalRect((width - xSize) / 2, (height - ySize) / 2, 0, 0, xSize, ySize);
    }
}
