package com.maks2103.industries.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.energy.EnergyStorage;

/**
 * {@link EnergyStorage} with serialize/deserialize nbt
 */
public class SerializableEnergyStorage extends EnergyStorage {
    private Listener listener = null;

    public SerializableEnergyStorage(int capacity) {
        super(capacity);
    }

    public SerializableEnergyStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    public SerializableEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    public SerializableEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setInteger("capacity", this.capacity);
        compound.setInteger("maxReceive", this.maxReceive);
        compound.setInteger("maxExtract", this.maxExtract);
        compound.setInteger("energy", this.energy);
        return compound;
    }

    public void deserializeNBT(NBTTagCompound compound) {
        this.capacity = compound.getInteger("capacity");
        this.maxReceive = compound.getInteger("maxReceive");
        this.maxExtract = compound.getInteger("maxExtract");
        this.energy = compound.getInteger("energy");
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if(listener != null)
            listener.onExtract(simulate);
        return super.extractEnergy(maxExtract, simulate);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if(listener != null)
            listener.onReceive(simulate);
        return super.receiveEnergy(maxReceive, simulate);
    }

    public interface Listener {
        void onReceive(boolean simulate);

        void onExtract(boolean simulate);
    }
}
