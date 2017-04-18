package com.maks2103.industries;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import javax.annotation.Nullable;

@Mod(modid = IndustriesMod.MODID, version = IndustriesMod.VERSION)
public class IndustriesMod {
    public static final String MODID = "industries";
    public static final String VERSION = "0.1";
    public static ItemResearchBook researchBook = new ItemResearchBook();
    @Mod.Instance
    public static IndustriesMod instance;

    public IndustriesMod() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @EventHandler
    public void onFMLInitialization(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(this, Proxy.instance);
    }

    @SubscribeEvent
    public void onRegisterItem(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(researchBook);
    }

    public static class Proxy implements IGuiHandler {
        @SidedProxy
        public static Proxy instance;

        @Nullable
        @Override
        public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
            if (id == 0) {
                return new ContainerResearchBook();
            }
            return null;
        }

        @Nullable
        @Override
        public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
            return null;
        }

        public static class ServerProxy extends Proxy {
        }

        public static class ClientProxy extends Proxy {
            @Nullable
            @Override
            public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
                if (id == 0) {
                    return new GuiResearchBook(new ContainerResearchBook());
                }
                return null;
            }
        }
    }
}