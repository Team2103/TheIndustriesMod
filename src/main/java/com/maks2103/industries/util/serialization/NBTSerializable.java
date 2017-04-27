package com.maks2103.industries.util.serialization;

import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;
import java.io.Serializable;

/**
 * <code>NBTSerializable</code> class means that class can be serialized and deserialized into NBT.
 * Class for deserialization instantiated without calling any constructor!
 *
 * @see NBTSerializableSerializer
 */
public interface NBTSerializable extends Serializable {
    /**
     * Serialize all important data to nbt
     */
    @Nonnull
    NBTTagCompound serializeNBT();

    /**
     * Deserialize data form nbt. This method will be called on "empty" object, what means object has no initialized fields
     *
     * @param compound the compound
     */
    void deserializeNBT(@Nonnull NBTTagCompound compound);
}
