package com.maks2103.industries;

import com.maks2103.industries.proxy.CommonProxy;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@SuppressWarnings("unused")
@Mod(modid = IndustriesMod.MODID, version = IndustriesMod.VERSION, name = IndustriesMod.NAME)
public class IndustriesMod {
    public static final String MODID = "industries";
    public static final String VERSION = "0.1";
    public static final String NAME = "Industries mod";

    @Mod.Instance
    private static IndustriesMod instance;

    public IndustriesMod() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @EventHandler
    public void onFMLPreInitialization(FMLPreInitializationEvent event) {
        getProxy().preInit(event);
    }

    @EventHandler
    public void onFMLInitialization(FMLInitializationEvent event) {
        getProxy().init(event);
    }

    @EventHandler
    public void onFMLPostInitialization(FMLPostInitializationEvent event) {
        getProxy().postInit(event);
    }

    @SubscribeEvent
    public void onRegisterItem(RegistryEvent.Register<Item> event) {
        getProxy().onRegisterItem(event);
    }

    private CommonProxy getProxy() {
        return CommonProxy.getProxy();
    }

    public static IndustriesMod getInstance() {
        return instance;
    }
}