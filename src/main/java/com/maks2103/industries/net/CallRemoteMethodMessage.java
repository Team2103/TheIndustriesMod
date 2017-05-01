package com.maks2103.industries.net;

import com.maks2103.industries.util.serialization.NBTSerializable;
import com.maks2103.industries.util.serialization.NBTSerializableSerializer;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;
import java.util.stream.Stream;

/**
 * DO NOT USE IT
 */
public final class CallRemoteMethodMessage implements IMessage {
    private static final int LIST_TAG_TYPE;

    static {
        NBTTagList list = new NBTTagList();
        list.appendTag(NBTSerializableSerializer.serialize(new NBTSerializable() {
            @Nonnull
            @Override
            public NBTTagCompound serializeNBT() {
                return new NBTTagCompound();
            }

            @Override
            public void deserializeNBT(@Nonnull NBTTagCompound compound) {

            }
        }));
        LIST_TAG_TYPE = list.getTagType();
    }

    private List<Object> params;
    private String methodName;
    private String className;

    CallRemoteMethodMessage(List<Object> params, String methodName, String className) {
        this.params = params;
        this.methodName = methodName;
        this.className = className;
    }

    public CallRemoteMethodMessage() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        deserializeNBT(ByteBufUtils.readTag(buf));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf, serializeNBT());
    }

    private void deserializeNBT(NBTTagCompound compound) {
        this.params = new ArrayList<>();

        NBTTagList params = compound.getTagList("params", LIST_TAG_TYPE);
        for(int i = 0; i < params.tagCount(); i++) {
            this.params.add(NBTSerializableSerializer.deserialize(params.getCompoundTagAt(i)));
        }
        methodName = compound.getString("methodName");
        className = compound.getString("className");
    }

    @SuppressWarnings("RedundantCast")
    private NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setString("methodName", methodName);
        compound.setString("className", className);
        NBTTagList list = new NBTTagList();
        params.forEach(o -> list.appendTag(NBTSerializableSerializer.serialize((Object & NBTSerializable) o)));
        compound.setTag("params", list);
        return compound;
    }

    public static final class Handler implements IMessageHandler<CallRemoteMethodMessage, IMessage> {

        @SuppressWarnings("MethodCallSideOnly")
        @Override
        public IMessage onMessage(CallRemoteMethodMessage message, MessageContext ctx) {
            if(ctx.side == Side.SERVER) {
                MinecraftServer server = ctx.getServerHandler().playerEntity.getServer();
                if(server == null) {
                    System.err.println("WTF!");
                    return null;
                }
                server.addScheduledTask(() -> processMessage(message, ctx));
            } else {
                Minecraft.getMinecraft().addScheduledTask(() -> processMessage(message, ctx));
            }
            return null;
        }

        private void processMessage(CallRemoteMethodMessage message, MessageContext ctx) {
            try {
                Class<?> clazz = Class.forName(message.className);
                Object[] params = message.params.toArray();
                Class<?>[] classes = Stream.of(params)
                        .map(Object::getClass)
                        .toArray((IntFunction<Class<?>[]>) Class[]::new);
                Stream.of(clazz.getDeclaredMethods())
                        .filter(method -> method.getName().equals(message.methodName))
                        .filter(m -> {
                            try {
                                int retClassCounter = 0;
                                for(Class<?> aClass : m.getParameterTypes()) {
                                    if(!(MessageContext.class.isAssignableFrom(aClass) || classes[retClassCounter++].isAssignableFrom(aClass))) {
                                        return false;
                                    }
                                }
                                return true;
                            } catch (ArrayIndexOutOfBoundsException e) {
                                e.printStackTrace();
                                return false;
                            }
                        })
                        .findAny()
                        .ifPresent(m -> {
                            List<Object> ret = new ArrayList<>();
                            int retClassCounter = 0;
                            for(Class<?> aClass : m.getParameterTypes()) {
                                if(MessageContext.class.isAssignableFrom(aClass)) {
                                    ret.add(ctx);
                                } else if(classes[retClassCounter].isAssignableFrom(aClass)) {
                                    ret.add(params[retClassCounter++]);
                                } else {
                                    ret.add(null);
                                }
                            }
                            Object[] methodParams = ret.toArray();
                            m.setAccessible(true);
                            try {
                                m.invoke(null, methodParams);
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        });
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
