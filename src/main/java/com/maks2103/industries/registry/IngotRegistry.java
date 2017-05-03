package com.maks2103.industries.registry;

import com.maks2103.industries.item.ingot.Ingot;
import com.maks2103.industries.item.ingot.IngotFactory;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.concurrent.atomic.AtomicBoolean;

final class IngotRegistry {
    private static final String ALUMINIUM_INGOT_REGISTRY_NAME = "aluminium";
    private static final String BRONZE_INGOT_REGISTRY_NAME = "bronze";
    private static final String COPPER_INGOT_REGISTRY_NAME = "copper";
    private static final String DURAL_INGOT_REGISTRY_NAME = "dural";
    private static final String LEAD_INGOT_REGISTRY_NAME = "lead";
    private static final String NICKEL_INGOT_REGISTRY_NAME = "nickel";
    private static final String SILVER_INGOT_REGISTRY_NAME = "silver";
    private static final String STEEL_INGOT_REGISTRY_NAME = "steel";
    private static final String TIN_INGOT_REGISTRY_NAME = "tin";
    private static final String TITANIUM_INGOT_REGISTRY_NAME = "titanium";

    static Ingot ALUMINIUM_INGOT;
    static Ingot BRONZE_INGOT;
    static Ingot COPPER_INGOT;
    static Ingot DURAL_INGOT;
    static Ingot LEAD_INGOT;
    static Ingot NICKEL_INGOT;
    static Ingot SILVER_INGOT;
    static Ingot STEEL_INGOT;
    static Ingot TIN_INGOT;
    static Ingot TITANIUM_INGOT;

    private static final AtomicBoolean isInitiated = new AtomicBoolean(false);

    private IngotRegistry() {
    }

    @SideOnly(Side.CLIENT)
    static void registerItemModels() {
        ItemModelMesher modelMesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
        registerIngotItemModel(ALUMINIUM_INGOT_REGISTRY_NAME, modelMesher, ALUMINIUM_INGOT);
        registerIngotItemModel(BRONZE_INGOT_REGISTRY_NAME, modelMesher, BRONZE_INGOT);
        registerIngotItemModel(COPPER_INGOT_REGISTRY_NAME, modelMesher, COPPER_INGOT);
        registerIngotItemModel(DURAL_INGOT_REGISTRY_NAME, modelMesher, DURAL_INGOT);
        registerIngotItemModel(LEAD_INGOT_REGISTRY_NAME, modelMesher, LEAD_INGOT);
        registerIngotItemModel(NICKEL_INGOT_REGISTRY_NAME, modelMesher, NICKEL_INGOT);
        registerIngotItemModel(SILVER_INGOT_REGISTRY_NAME, modelMesher, SILVER_INGOT);
        registerIngotItemModel(STEEL_INGOT_REGISTRY_NAME, modelMesher, STEEL_INGOT);
        registerIngotItemModel(TIN_INGOT_REGISTRY_NAME, modelMesher, TIN_INGOT);
        registerIngotItemModel(TITANIUM_INGOT_REGISTRY_NAME, modelMesher, TITANIUM_INGOT);
    }

    static void registerBlock(@Nonnull IForgeRegistry<Block> registry) {
        checkInit();
        registerIngotBlocks(registry, ALUMINIUM_INGOT);
        registerIngotBlocks(registry, BRONZE_INGOT);
        registerIngotBlocks(registry, COPPER_INGOT);
        registerIngotBlocks(registry, DURAL_INGOT);
        registerIngotBlocks(registry, LEAD_INGOT);
        registerIngotBlocks(registry, NICKEL_INGOT);
        registerIngotBlocks(registry, SILVER_INGOT);
        registerIngotBlocks(registry, STEEL_INGOT);
        registerIngotBlocks(registry, TIN_INGOT);
        registerIngotBlocks(registry, TITANIUM_INGOT);
    }

    static void registerItems(@Nonnull IForgeRegistry<Item> registry) {
        checkInit();

        registerIngotItem(registry, ALUMINIUM_INGOT);
        registerIngotItem(registry, BRONZE_INGOT);
        registerIngotItem(registry, COPPER_INGOT);
        registerIngotItem(registry, DURAL_INGOT);
        registerIngotItem(registry, LEAD_INGOT);
        registerIngotItem(registry, NICKEL_INGOT);
        registerIngotItem(registry, SILVER_INGOT);
        registerIngotItem(registry, STEEL_INGOT);
        registerIngotItem(registry, TIN_INGOT);
        registerIngotItem(registry, TITANIUM_INGOT);
    }


    private static void registerIngotItem(@Nonnull IForgeRegistry<Item> registry, @Nonnull Ingot ingot) {
        registry.register(ingot.getIngot());
        registry.register(ingot.getBlockItem());
        if(ingot.hasOre())
            registry.register(ingot.getOreItem());
    }

    @SideOnly(Side.CLIENT)
    private static void registerIngotItemModel(String registryName, @Nonnull ItemModelMesher modelMesher, Ingot ingot) {
        modelMesher.register(ingot.getIngot(), 0, new ModelResourceLocation("industries:" + registryName + "_ingot", "inventory"));
        modelMesher.register(ingot.getBlockItem(), 0, new ModelResourceLocation("industries:" + registryName + "_block_item", "inventory"));
        if(ingot.hasOre())
            modelMesher.register(ingot.getOreItem(), 0, new ModelResourceLocation("industries:" + registryName + "_ore_item", "inventory"));
    }


    private static void registerIngotBlocks(@Nonnull IForgeRegistry<Block> registry, @Nonnull Ingot ingot) {
        registry.register(ingot.getBlock());
        if(ingot.hasOre())
            registry.register(ingot.getOre());
    }

    private static void checkInit() {
        if(!isInitiated.get())
            init();
    }

    private static void init() {
        ALUMINIUM_INGOT = IngotFactory.newIngot(ALUMINIUM_INGOT_REGISTRY_NAME, "aluminium", true);
        BRONZE_INGOT = IngotFactory.newIngot(BRONZE_INGOT_REGISTRY_NAME, "bronze", false);
        COPPER_INGOT = IngotFactory.newIngot(COPPER_INGOT_REGISTRY_NAME, "copper", true);
        DURAL_INGOT = IngotFactory.newIngot(DURAL_INGOT_REGISTRY_NAME, "dural", false);
        LEAD_INGOT = IngotFactory.newIngot(LEAD_INGOT_REGISTRY_NAME, "lead", true);
        NICKEL_INGOT = IngotFactory.newIngot(NICKEL_INGOT_REGISTRY_NAME, "nickel", true);
        SILVER_INGOT = IngotFactory.newIngot(SILVER_INGOT_REGISTRY_NAME, "silver", true);
        STEEL_INGOT = IngotFactory.newIngot(STEEL_INGOT_REGISTRY_NAME, "steel", false);
        TIN_INGOT = IngotFactory.newIngot(TIN_INGOT_REGISTRY_NAME, "tin", true);
        TITANIUM_INGOT = IngotFactory.newIngot(TITANIUM_INGOT_REGISTRY_NAME, "titanium", true);
        isInitiated.set(true);
    }
}
