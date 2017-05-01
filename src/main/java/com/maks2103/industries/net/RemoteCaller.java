package com.maks2103.industries.net;

import com.maks2103.industries.IndustriesMod;
import com.maks2103.industries.util.serialization.NBTSerializable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import scala.actors.threadpool.Arrays;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

/**
 * This class call messages on clients/server.
 *
 * If method has {@link net.minecraftforge.fml.common.network.simpleimpl.MessageContext}, then current message context
 * will be passed into argument
 */
public final class RemoteCaller {
    private RemoteCaller() {
    }

    @SafeVarargs
    public static <T extends Object & NBTSerializable> void callRemote(@Nonnull Method method, @Nonnull Side side, T... params) {
        CallRemoteMethodMessage message = createMessage(method, params);
        if(side.isServer()) {
            IndustriesMod.getNetworkWrapper().sendToServer(message);
        } else {
            IndustriesMod.getNetworkWrapper().sendToAll(message);
        }
    }

    @SafeVarargs
    public static <T extends Object & NBTSerializable> void callOnClient(@Nonnull Method method, @Nonnull EntityPlayerMP player, T... params) {
        IndustriesMod.getNetworkWrapper().sendTo(createMessage(method, params), player);
    }

    @SafeVarargs
    public static <T extends Object & NBTSerializable> void callOnAllClientAround(@Nonnull Method method, @Nonnull NetworkRegistry.TargetPoint point, T... params) {
        IndustriesMod.getNetworkWrapper().sendToAllAround(createMessage(method, params), point);
    }

    @SafeVarargs
    public static <T extends Object & NBTSerializable> void callOnDimensionClients(@Nonnull Method method, int dimId, T... params) {
        IndustriesMod.getNetworkWrapper().sendToDimension(createMessage(method, params), dimId);
    }

    @SuppressWarnings("unchecked")
    private static <T extends Object & NBTSerializable> CallRemoteMethodMessage createMessage(Method method, T[] params) {
        if(!Modifier.isStatic(method.getModifiers())) {
            throw new RuntimeException("Method must be static!");
        }
        List<Object> paramList = Arrays.asList(params);
        return new CallRemoteMethodMessage(paramList, method.getName(), method.getDeclaringClass().getName());
    }
}
