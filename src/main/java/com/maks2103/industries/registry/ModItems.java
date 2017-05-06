package com.maks2103.industries.registry;

import com.maks2103.industries.IndustriesMod;
import com.maks2103.industries.item.ResearchBookItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class ModItems {
    public static final ResearchBookItem RESEARCH_BOOK = new ResearchBookItem();
    public static final ItemBlock ASSEMBLER_BLOCK_ITEM = new ItemBlock(ModBlocks.ASSEMBLER_BLOCK);

    private ModItems() {
    }

    public static void register(IForgeRegistry<Item> registry) {
        registry.register(RESEARCH_BOOK);

        ASSEMBLER_BLOCK_ITEM.setRegistryName("assembler_block_item");
        ASSEMBLER_BLOCK_ITEM.setUnlocalizedName("assembler_block_item");
        ASSEMBLER_BLOCK_ITEM.setCreativeTab(IndustriesMod.CREATIVE_TAB);
        registry.register(ASSEMBLER_BLOCK_ITEM);

        IngotRegistry.registerItems(registry);
    }

    @SideOnly(Side.CLIENT)
    public static void registerClient() {
        IngotRegistry.registerItemModels();
    }
}
