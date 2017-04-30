package com.maks2103.industries.assembler;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public interface AssemblerRecipe {
    /**
     * Time in seconds
     */
    int getCraftingTime();

    /**
     * 8 items maximum!
     */
    @Nonnull
    ItemStack[] getCraftItems();

    @Nonnull
    ItemStack getOutputItem();

    @Nonnull
    AssemblerPreviewModel getPreviewModel();

    int getId();
}
