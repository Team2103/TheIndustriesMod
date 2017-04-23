package com.maks2103.industries.gui;

import com.maks2103.industries.container.ContainerResearchBook;
import net.minecraft.client.gui.inventory.GuiContainer;

public class GuiResearchBook extends GuiContainer {

    public GuiResearchBook(ContainerResearchBook container) {
        super(container);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
//        GlStateManager.pushMatrix();
//        GlStateManager.translate((width - xSize) / 2, (height - ySize) / 2, 0);
//        Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(Items.APPLE), ItemCameraTransforms.TransformType.GUI);
//        GlStateManager.popMatrix();
//        this.mc.getTextureManager().bindTexture(GUI_TEXTURE);
        drawTexturedModalRect((width - xSize) / 2, (height - ySize) / 2, 0, 0, xSize, ySize);
    }
}
