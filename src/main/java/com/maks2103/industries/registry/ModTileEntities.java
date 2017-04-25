package com.maks2103.industries.registry;

import com.maks2103.industries.tileEntity.AssemblerTileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class ModTileEntities {
    public static final String ASSEMBLER_TILE_ENTITY_ID = "IndustriesModAssemblerTileEntity";

    private ModTileEntities() {
    }

    public static void register() {
        GameRegistry.registerTileEntity(AssemblerTileEntity.class, ASSEMBLER_TILE_ENTITY_ID);
    }
}
