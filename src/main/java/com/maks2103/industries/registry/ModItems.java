package com.maks2103.industries.registry;

import com.maks2103.industries.item.ItemResearchBook;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.IForgeRegistry;

public final class ModItems {
    private static final ItemResearchBook RESEARCH_BOOK = new ItemResearchBook();

    public static void register(IForgeRegistry<Item> registry) {
        registry.register(RESEARCH_BOOK);
    }
}
