package com.maks2103.industries.item.ingot;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

public final class Ingot {
    private final Item ingot;
    private final Block block;
    private final Item blockItem;

    private final boolean hasOre;
    private final Block ore;
    private final Item oreItem;

    Ingot(Item ingot, Block block, Item blockItem, boolean hasOre, Block ore, Item oreItem) {
        this.ingot = ingot;
        this.block = block;
        this.blockItem = blockItem;
        this.hasOre = hasOre;
        this.ore = ore;
        this.oreItem = oreItem;
    }

    public Item getIngot() {
        return ingot;
    }

    public Block getBlock() {
        return block;
    }

    public Item getBlockItem() {
        return blockItem;
    }

    public boolean hasOre() {
        return hasOre;
    }

    public Block getOre() {
        return ore;
    }

    public Item getOreItem() {
        return oreItem;
    }
}
