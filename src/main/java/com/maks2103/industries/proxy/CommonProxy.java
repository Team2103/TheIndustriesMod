package com.maks2103.industries.proxy;

import com.maks2103.industries.IndustriesMod;
import com.maks2103.industries.handler.gui.GuiHandler;
import com.maks2103.industries.registry.ModItems;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CommonProxy {
    @SidedProxy
    private static CommonProxy proxy;

    public static CommonProxy getProxy() {
        return proxy;
    }

    public void preInit(FMLPreInitializationEvent event) {

    }

    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(IndustriesMod.getInstance(), GuiHandler.getInstance());
    }

    public void postInit(FMLPostInitializationEvent event) {

    }

    public void onRegisterItem(RegistryEvent.Register<Item> event) {
        ModItems.register(event.getRegistry());
    }

    public static class ServerProxy extends CommonProxy {
    }

    public static class ClientProxy extends CommonProxy {
    }
}
