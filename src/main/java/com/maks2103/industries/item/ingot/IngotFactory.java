package com.maks2103.industries.item.ingot;

import com.maks2103.industries.IndustriesMod;
import com.maks2103.industries.proxy.CommonProxy;
import com.maks2103.industries.util.Utils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public final class IngotFactory {
    private IngotFactory() {
    }

    /**
     * @param registryName
     * @param oreDictName
     * @param requiredOre
     * @return triple with ingot item, ingot block and ore
     */
    public static Ingot newIngot(String registryName, String oreDictName, boolean requiredOre) {
        ItemIngot ingot = new ItemIngot(registryName + "_ingot", "ingot" + Utils.capitalizeString(oreDictName));
        BlockIngot blockIngot = new BlockIngot(registryName + "_block", "block" + Utils.capitalizeString(oreDictName));

        Item blockItem = new ItemBlock(blockIngot);
        blockItem.setUnlocalizedName(registryName + "_block_item");
        blockItem.setRegistryName(registryName + "_block_item");
        blockItem.setCreativeTab(IndustriesMod.CREATIVE_TAB);
        CommonProxy.registerOre("block" + Utils.capitalizeString(oreDictName), blockItem);

        OreIngot ore = null;
        Item oreItem = null;
        if(requiredOre) {
            ore = new OreIngot(registryName + "_ore", "ore" + Utils.capitalizeString(oreDictName));
            oreItem = new ItemBlock(ore);
            oreItem.setRegistryName(registryName + "_ore_item");
            oreItem.setUnlocalizedName(registryName + "_ore_item");
            oreItem.setCreativeTab(IndustriesMod.CREATIVE_TAB);
            CommonProxy.registerOre("ore" + Utils.capitalizeString(oreDictName), oreItem);
        }
        return new Ingot(ingot, blockIngot, blockItem, requiredOre, ore, oreItem);
    }

}
