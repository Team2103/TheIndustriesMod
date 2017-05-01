package com.maks2103.industries.assembler;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

/**
 * Assembler can be mutable
 */
public interface AssemblerRecipe {
    /**
     * Time in seconds
     */
    int getCraftingTime();

    /**
     * Return items, required for craft.
     * 8 items maximum!
     */
    @Nonnull
    ItemStack[] getCraftItems();


    @Nonnull
    ItemStack getOutputItem();

    @Nonnull
    AssemblerPreviewModel getPreviewModel();

    /**
     * Recipe id. Must be unique!
     */
    int getId();
}
