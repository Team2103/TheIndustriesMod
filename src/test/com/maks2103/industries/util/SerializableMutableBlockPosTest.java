package com.maks2103.industries.util;

import net.minecraft.nbt.NBTTagCompound;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SerializableMutableBlockPosTest {
    @Test
    public void serializeNadDeserialize() throws Exception {
        SerializableMutableBlockPos blockPos = new SerializableMutableBlockPos(1, 2, 4);
        NBTTagCompound compound = blockPos.serializeNBT();

        SerializableMutableBlockPos blockPos1 = new SerializableMutableBlockPos();
        blockPos1.deserializeNBT(compound);

        assertEquals(blockPos, blockPos1);
    }
}