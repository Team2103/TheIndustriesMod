package com.maks2103.industries.gui;

import com.maks2103.industries.assembler.AssemblerRecipe;
import com.maks2103.industries.container.AssemblerContainer;
import com.maks2103.industries.net.RemoteCaller;
import com.maks2103.industries.tileEntity.AssemblerTileEntity;
import com.maks2103.industries.util.SerializableMutableBlockPos;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.Color;
import java.io.IOException;
import java.lang.reflect.Method;

public class AssemblerGui extends GuiContainer {
    /**
     * Color of text in required items list
     */
    private static final int LIST_TEXT_COLOR = new Color(0, 224, 34).getRGB();

    private static final int PREV_BUTTON = 1;
    private static final int NEXT_BUTTON = 2;
    private static final int START_BUTTON = 3;

    private static final Method PREW_RECIPE_METHOD;
    private static final Method NEXT_RECIPE_METHOD;
    private static final Method TRY_CRAFT_METHOD;

    static {
        try {
            Class<?> clazz = AssemblerTileEntity.RemoteMethods.class;
            PREW_RECIPE_METHOD = clazz.getDeclaredMethod("prewRecipe", MessageContext.class, SerializableMutableBlockPos.class);
            NEXT_RECIPE_METHOD = clazz.getDeclaredMethod("nextRecipe", MessageContext.class, SerializableMutableBlockPos.class);
            TRY_CRAFT_METHOD = clazz.getDeclaredMethod("tryCraft", MessageContext.class, SerializableMutableBlockPos.class);
        } catch (NoSuchMethodException e) {
            throw new Error(e);
        }
    }

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

        addButton(new GuiButton(PREV_BUTTON, startX + 9, startY + 74, 26, 14, ""));
        addButton(new GuiButton(NEXT_BUTTON, startX + 60, startY + 74, 26, 14, ""));
        addButton(new GuiButton(START_BUTTON, startX + 42, startY + 74, 13, 13, ""));
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
                tryCraft();
                break;
            default:
                System.err.println("Unexpected button id " + button.id);
        }
    }

    private void previousRecipe() {
        RemoteCaller.callRemote(PREW_RECIPE_METHOD, Side.SERVER, new SerializableMutableBlockPos(assemblerContainer.getTileEntity().getPos()));
    }

    private void nextRecipe() {
        RemoteCaller.callRemote(NEXT_RECIPE_METHOD, Side.SERVER, new SerializableMutableBlockPos(assemblerContainer.getTileEntity().getPos()));
    }

    private void tryCraft() {
        RemoteCaller.callRemote(TRY_CRAFT_METHOD, Side.SERVER, new SerializableMutableBlockPos(assemblerContainer.getTileEntity().getPos()));
    }

    @SideOnly(Side.CLIENT)
    private static void craftOk() {
        Minecraft.getMinecraft().player.closeScreen();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.mc.getTextureManager().bindTexture(textureResourceLocation1);
        drawTexturedModalRect(startX, startY, 0, 0, 256, 256); //Render first part
        this.mc.getTextureManager().bindTexture(textureResourceLocation2);
        drawTexturedModalRect(startX + 512 / 2, startY, 0, 0, 15, 159); //Render second part

        AssemblerTileEntity tileEntity = assemblerContainer.getTileEntity();
        IEnergyStorage energyStorage = tileEntity.getEnergyStorage();
        float i = energyStorage.getEnergyStored() * 1F / energyStorage.getMaxEnergyStored() * 100;
        int height = (int) (0.615f * i) + ((i > 0) ? 1 : 0);
        drawTexturedModalRect(startX + 239, startY + 155 - height, 42, 67, 26, height); // Draw containing energy

        AssemblerRecipe recipe;
        if((recipe = tileEntity.getCurrentRecipe()) != null) {
            int j = 0; //Count lines
            for(ItemStack itemStack : recipe.getCraftItems()) {
                String str = fontRendererObj.trimStringToWidth(String.valueOf(itemStack.getCount()) + 'x' + itemStack.getDisplayName(), 200); //Amount + 'x' + displayName
                fontRendererObj.drawString(str, startX + 147, startY + 9 + j * fontRendererObj.getWordWrappedHeight(str, 200), LIST_TEXT_COLOR);
                j++;
            }
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        GlStateManager.enableBlend();

        this.mc.getTextureManager().bindTexture(textureResourceLocation2);
        drawTexturedModalRect(9, 74, 16, 144, 27, 15); //Render prev button
        drawTexturedModalRect(60, 74, 43, 144, 27, 15); //Render next button

        if(assemblerContainer.getTileEntity().canCraft()) {
            drawTexturedModalRect(41, 74, 84, 145, 14, 14); //Render green start button
        } else {
            drawTexturedModalRect(41, 74, 70, 145, 14, 14); //Render red start button
        }

        drawTexturedModalRect(145, 6, 30, 0, 102, 66); //Render required items screen helper
        drawTexturedModalRect(239, 93, 15, 67, 26, 62); //Render energy screen helper

        GlStateManager.disableBlend();

        AssemblerRecipe recipe;
        if((recipe = assemblerContainer.getTileEntity().getCurrentRecipe()) != null) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(26.5f, 5, 1);
            recipe.getPreviewModel().render();
            GlStateManager.popMatrix();
        }
    }
}
