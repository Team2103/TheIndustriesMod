package com.maks2103.industries.assembler;

import net.minecraft.item.ItemStack;

public interface AssemblerRecipe {
    int getCraftingTime();

    /**
     * 8 items maximum!
     */
    ItemStack[] getCraftItems();

    ItemStack getOutputItem();

    AssemblerPreviewModel getPreviewModel();

    int getId();
}
