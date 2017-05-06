package com.maks2103.industries.proxy;

import com.maks2103.industries.IndustriesMod;
import com.maks2103.industries.assembler.AssemblerRecipeManager;
import com.maks2103.industries.assembler.TestAsseblerRecipe2;
import com.maks2103.industries.assembler.TestAssemblerRecipe;
import com.maks2103.industries.handler.gui.GuiHandler;
import com.maks2103.industries.net.CallRemoteMethodMessage;
import com.maks2103.industries.registry.ModBlocks;
import com.maks2103.industries.registry.ModItems;
import com.maks2103.industries.registry.ModTileEntities;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CommonProxy {
    @SidedProxy
    private static CommonProxy proxy;

    public static CommonProxy getProxy() {
        return proxy;
    }

    public void preInit(FMLPreInitializationEvent event) {
        OreDictWriter.register();
    }

    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(IndustriesMod.getInstance(), GuiHandler.getInstance());
        ModTileEntities.register();

        AssemblerRecipeManager.addRecipe(new TestAssemblerRecipe());
        AssemblerRecipeManager.addRecipe(new TestAsseblerRecipe2());

        IndustriesMod.getNetworkWrapper().registerMessage(CallRemoteMethodMessage.Handler.class, CallRemoteMethodMessage.class, 0, Side.SERVER);

    }

    public void postInit(FMLPostInitializationEvent event) {
    }

    public void onRegisterItem(RegistryEvent.Register<Item> event) {
        ModItems.register(event.getRegistry());

    }

    public void onRegisterBlock(RegistryEvent.Register<Block> event) {
        ModBlocks.register(event.getRegistry());
    }


    public static void registerOre(String name, Item item) {
        OreDictWriter.add(name, item);
    }

    public static void registerOre(String name, Block block) {
        OreDictWriter.add(name, block);
    }

    public static void registerOre(String name, ItemStack stack) {
        OreDictWriter.add(name, stack);
    }
    
    public void addScheduledTask(MessageContext context, Runnable runnable) {
        if(context.getServerHandler().playerEntity.world instanceof WorldServer) {
            ((WorldServer) context.getServerHandler().playerEntity.world).addScheduledTask(runnable);
        }
    }

    public static class ServerProxy extends CommonProxy {
    }

    @SideOnly(Side.CLIENT)
    public static class ClientProxy extends CommonProxy {
        @SideOnly(Side.CLIENT)
        @Override
        public void init(FMLInitializationEvent event) {
            super.init(event);

            IndustriesMod.getNetworkWrapper().registerMessage(CallRemoteMethodMessage.Handler.class, CallRemoteMethodMessage.class, 0, Side.CLIENT);

            ModItems.registerClient();
        }

        @SideOnly(Side.CLIENT)
        @Override
        public void addScheduledTask(MessageContext context, Runnable runnable) {
            if(context.side == Side.CLIENT)
                Minecraft.getMinecraft().addScheduledTask(runnable);
            else
                super.addScheduledTask(context, runnable);
        }
    }
}
