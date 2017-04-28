package com.maks2103.industries.util;

import com.maks2103.industries.util.serialization.NBTSerializable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;

public class SerializableMutableBlockPos extends BlockPos.MutableBlockPos implements NBTSerializable {
    public SerializableMutableBlockPos() {
    }

    public SerializableMutableBlockPos(BlockPos pos) {
        super(pos);
    }

    public SerializableMutableBlockPos(int x, int y, int z) {
        super(x, y, z);
    }

    @Nonnull
    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setInteger("x", getX());
        compound.setInteger("y", getY());
        compound.setInteger("z", getZ());
        return compound;
    }

    @Override
    public void deserializeNBT(@Nonnull NBTTagCompound compound) {
        x = compound.getInteger("x");
        y = compound.getInteger("y");
        z = compound.getInteger("z");
    }
}
