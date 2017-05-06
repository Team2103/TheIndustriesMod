package com.maks2103.industries.item.ingot;

import com.maks2103.industries.IndustriesMod;
import com.maks2103.industries.proxy.CommonProxy;
import net.minecraft.item.Item;

import javax.annotation.Nonnull;

class ItemIngot extends Item {
    public ItemIngot(@Nonnull String registryName, @Nonnull String oreDict) {
        setRegistryName(registryName);
        setUnlocalizedName(registryName);
        setCreativeTab(IndustriesMod.CREATIVE_TAB);
        CommonProxy.registerOre(oreDict, this);
    }
}
