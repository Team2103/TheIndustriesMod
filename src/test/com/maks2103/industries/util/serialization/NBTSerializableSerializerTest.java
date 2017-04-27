package com.maks2103.industries.util.serialization;

import net.minecraft.nbt.NBTTagCompound;
import org.junit.Test;

import javax.annotation.Nonnull;

import static org.junit.Assert.assertEquals;

public class NBTSerializableSerializerTest {
    @Test
    public void serializeAndDeserialize() throws Exception {
        TestNBTSerializableClass clazz = new TestNBTSerializableClass("asd");
        NBTTagCompound ser = NBTSerializableSerializer.serialize(clazz);
        TestNBTSerializableClass deser = NBTSerializableSerializer.deserialize(ser);
        assertEquals(clazz.getString(), deser.getString());
    }

    private static final class TestNBTSerializableClass implements NBTSerializable {
        private String string;

        TestNBTSerializableClass(String string) {
            this.string = string;
        }

        public TestNBTSerializableClass() {
        }

        @Nonnull
        @Override
        public NBTTagCompound serializeNBT() {
            NBTTagCompound compound = new NBTTagCompound();
            compound.setString("test", string);
            return compound;
        }

        @Override
        public void deserializeNBT(@Nonnull NBTTagCompound compound) {
            string = compound.getString("test");
        }

        String getString() {
            return string;
        }
    }
}