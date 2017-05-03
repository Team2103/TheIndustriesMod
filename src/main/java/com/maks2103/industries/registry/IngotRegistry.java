package com.maks2103.industries.registry;

import com.maks2103.industries.item.ingot.Ingot;
import com.maks2103.industries.item.ingot.IngotFactory;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.IForgeRegistry;

import javax.annotation.Nonnull;
import java.util.concurrent.atomic.AtomicBoolean;

final class IngotRegistry {
    static Ingot ALUMINIUM_INGOT;

    private static final AtomicBoolean isInitiated = new AtomicBoolean(false);

    private IngotRegistry() {
    }

    static void registerItems(@Nonnull IForgeRegistry<Item> registry) {
        checkInit();
        registry.register(ALUMINIUM_INGOT.getIngot());
        registry.register(ALUMINIUM_INGOT.getBlockItem());
        if(ALUMINIUM_INGOT.hasOre())
            registry.register(ALUMINIUM_INGOT.getOreItem());
    }

    static void registerBlock(@Nonnull IForgeRegistry<Block> registry) {
        checkInit();
        registry.register(ALUMINIUM_INGOT.getBlock());
        if(ALUMINIUM_INGOT.hasOre())
            registry.register(ALUMINIUM_INGOT.getOre());
    }

    private static void checkInit() {
        if(!isInitiated.get())
            init();
    }

    private static void init() {
        ALUMINIUM_INGOT = IngotFactory.newIngot("aluminium", "aluminium", true);

        isInitiated.set(true);
    }
}
