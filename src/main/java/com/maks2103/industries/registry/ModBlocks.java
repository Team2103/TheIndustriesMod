package com.maks2103.industries.registry;

import com.maks2103.industries.block.AssemblerBlock;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.IForgeRegistry;

public final class ModBlocks {
    public static final AssemblerBlock ASSEMBLER_BLOCK = new AssemblerBlock();

    private ModBlocks() {
    }

    public static void register(IForgeRegistry<Block> blockRegistry) {
        blockRegistry.register(ASSEMBLER_BLOCK);
    }
}
