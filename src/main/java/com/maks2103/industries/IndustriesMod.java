package com.maks2103.industries;

import net.minecraft.item.ItemBook;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = IndustriesMod.MODID, version = IndustriesMod.VERSION)
public class IndustriesMod
{
    public static final String MODID = "industries";
    public static final String VERSION = "0.1";
    public static ItemBook book;
    
    @EventHandler
    public void init(FMLInitializationEvent event) {
        book = new ItemBook();
        GameRegistry.register(book);
    }
}