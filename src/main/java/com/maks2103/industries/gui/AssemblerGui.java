package com.maks2103.industries.gui;

import com.maks2103.industries.assembler.AssemblerRecipe;
import com.maks2103.industries.container.AssemblerContainer;
import com.maks2103.industries.tileEntity.AssemblerTileEntity;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.energy.IEnergyStorage;

import java.awt.Color;
import java.io.IOException;

public class AssemblerGui extends GuiContainer {
    /**
     * Color of text in required items list
     */
    private static final int LIST_TEXT_COLOR = new Color(0, 224, 34).getRGB();

    private static final int PREV_BUTTON = 1;
    private static final int NEXT_BUTTON = 2;
    private static final int START_BUTTON = 3;

    private static final ResourceLocation textureResourceLocation1 = new ResourceLocation("industries:textures/gui/assemblergui1.png");
    private static final ResourceLocation textureResourceLocation2 = new ResourceLocation("industries:textures/gui/assemblergui2.png");

    private final AssemblerContainer assemblerContainer;
    private int startX;
    private int startY;

    public AssemblerGui(AssemblerContainer inventorySlotsIn) {
        super(inventorySlotsIn);
        this.assemblerContainer = inventorySlotsIn;
        xSize = 542 / 2;
        ySize = 318 / 2;
    }

    @Override
    public void initGui() {
        super.initGui();

        startX = (width - xSize) / 2;
        startY = (height - ySize) / 2;

        addButton(new GuiButton(PREV_BUTTON, startX + 18 / 2, startY + 149 / 2, 53 / 2, 29 / 2, ""));
        addButton(new GuiButton(NEXT_BUTTON, startX + 121 / 2, startY + 149 / 2, 53 / 2, 29 / 2, ""));
        addButton(new GuiButton(START_BUTTON, startX + 84 / 2, startY + 149 / 2, 26 / 2, 27 / 2, ""));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case PREV_BUTTON:
                previousRecipe();
                break;
            case NEXT_BUTTON:
                nextRecipe();
                break;
            case START_BUTTON:
                tryStart();
                break;
            default:
                System.err.println("Unexpected button id " + button.id);
        }
    }

    private void previousRecipe() {

    }

    private void nextRecipe() {

    }

    private void tryStart() {

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.mc.getTextureManager().bindTexture(textureResourceLocation1);
        drawTexturedModalRect(startX, startY, 0, 0, 512 / 2, 512 / 2);
        this.mc.getTextureManager().bindTexture(textureResourceLocation2);
        drawTexturedModalRect(startX + 512 / 2, startY, 0, 0, 30 / 2, 318 / 2);

        AssemblerTileEntity tileEntity = assemblerContainer.getAssemblerTileEntity();
        IEnergyStorage energyStorage = tileEntity.getEnergyStorage();
        float i = energyStorage.getEnergyStored() * 1F / energyStorage.getMaxEnergyStored() * 100;
        int height = (int) ((123F / 100) * i) / 2 + ((i > 0) ? 1 : 0);
        drawTexturedModalRect(startX + 478 / 2, startY + 310 / 2 - height, 84 / 2, 67, 26, height);

        AssemblerRecipe recipe;
        if((recipe = tileEntity.getCurrentRecipe()) != null) {
            int j = 0;
            for(ItemStack itemStack : recipe.getCraftItems()) {
                String str = fontRendererObj.trimStringToWidth(String.valueOf(itemStack.getCount()) + "x" + itemStack.getDisplayName(), 200);
                fontRendererObj.drawString(str, startX + 290 / 2 + 2, startY + 2 + 14 / 2 + j * fontRendererObj.getWordWrappedHeight(str, 200), LIST_TEXT_COLOR);
                j++;
            }
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

        this.mc.getTextureManager().bindTexture(textureResourceLocation2);
        drawTexturedModalRect(9, 74, 31 / 2 + 1, 290 / 2 - 1, 54 / 2, 30 / 2);
        drawTexturedModalRect(120 / 2, 74, 86 / 2, 290 / 2 - 1, 54 / 2, 30 / 2);

        if(assemblerContainer.getAssemblerTileEntity().canCraft()) {
            drawTexturedModalRect(82 / 2, 149 / 2, 168 / 2, 291 / 2, 28 / 2, 28 / 2);
        } else {
            drawTexturedModalRect(82 / 2, 149 / 2, 140 / 2, 291 / 2, 28 / 2, 28 / 2);
        }

        drawTexturedModalRect(145, 12 / 2, 30, 0, 205 / 2, 132 / 2);
        drawTexturedModalRect(478 / 2, 187 / 2, 31 / 2, 133 / 2 + 1, 26, 62);
    }
}
