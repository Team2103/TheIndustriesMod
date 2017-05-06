package com.maks2103.industries.item.ingot;

import com.maks2103.industries.IndustriesMod;
import com.maks2103.industries.proxy.CommonProxy;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

class OreIngot extends Block {
    OreIngot(String registryName, String oreDict) {
        super(Material.ROCK);
        setRegistryName(registryName);
        setUnlocalizedName(registryName);
        setCreativeTab(IndustriesMod.CREATIVE_TAB);
        CommonProxy.registerOre(oreDict, this);
    }
}
