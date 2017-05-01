package com.maks2103.industries.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class AssemblerOutputGui extends GuiContainer {
    private static final ResourceLocation texture = new ResourceLocation("industries:textures/gui/assembler_output_gui.png");

    private int startX;
    private int startY;

    public AssemblerOutputGui(Container inventorySlotsIn) {
        super(inventorySlotsIn);
        xSize = 512 / 2;
        ySize = 284 / 2;
    }

    @Override
    public void initGui() {
        super.initGui();

        startX = (width - xSize) / 2;
        startY = (height - ySize) / 2;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(texture);
        drawTexturedModalRect(startX, startY, 0, 0, xSize, ySize);
    }
}
