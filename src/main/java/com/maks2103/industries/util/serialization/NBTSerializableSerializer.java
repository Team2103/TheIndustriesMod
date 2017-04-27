package com.maks2103.industries.util.serialization;

import net.minecraft.nbt.NBTTagCompound;
import sun.misc.Unsafe;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;

/**
 * Serialize {@link NBTSerializable} classes to nbt tag. This tag contains <code>class</code> - class name for instantiate
 * and <code>data</code> - nbt tag form {@link NBTSerializable#serializeNBT()}
 */
public final class NBTSerializableSerializer {
    //Change later to more safe way to create classes
    private static final Unsafe UNSAFE;

    static {
        try {
            Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            UNSAFE = (Unsafe) unsafeField.get(null);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new Error(e);
        }
    }

    private NBTSerializableSerializer() {
    }

    /**
     * Serialize object to nbt tag
     *
     * @param object the object
     * @param <T>    {@link NBTSerializable} object
     * @return serialized object
     */
    public static <T extends Object & NBTSerializable> NBTTagCompound serialize(@Nonnull T object) {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setTag("data", object.serializeNBT());
        compound.setString("class", object.getClass().getName());
        return compound;
    }

    /**
     * Deserialize object from nbt tag. Do not call any constructor then allocating new object!
     *
     * @param compound serialized object
     * @param <T>      {@link NBTSerializable} object
     * @return deserialized object
     */
    @SuppressWarnings("unchecked")
    public static <T extends Object & NBTSerializable> T deserialize(@Nonnull NBTTagCompound compound) {
        try {
            Class<?> clazz = Class.forName(compound.getString("class"));
            T instance = (T) UNSAFE.allocateInstance(clazz);
            instance.deserializeNBT(compound.getCompoundTag("data"));
            return instance;
        } catch (ClassNotFoundException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}
