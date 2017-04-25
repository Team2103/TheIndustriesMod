package com.maks2103.industries.assembler;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public final class TestAssemblerRecipe implements AssemblerRecipe {
    @Override
    public int getCraftingTime() {
        return 100;
    }

    @Override
    public ItemStack[] getCraftItems() {
        return new ItemStack[]{new ItemStack(Items.APPLE, 4), new ItemStack(Items.ARROW, 32)};
    }

    @Override
    public ItemStack getOutputItem() {
        return new ItemStack(Blocks.BRICK_BLOCK, 20);
    }

    @Override
    public AssemblerPreviewModel getPreviewModel() {
        return new AssemblerPreviewModel() {
            @Override
            public void render() {

            }
        };
    }

    @Override
    public int getId() {
        return 1242;
    }
}
