package com.maks2103.industries.tileEntity;

import com.maks2103.industries.assembler.AssemblerRecipe;
import com.maks2103.industries.assembler.AssemblerRecipeManager;
import com.maks2103.industries.util.SerializableEnergyStorage;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


public class AssemblerTileEntity extends TileEntity implements SerializableEnergyStorage.Listener {
    private static final int MAX_ENERGY = 64000;
    private static final int MAX_RECEIVE = 30;

    private final ItemStackHandler itemHandler;
    private final SerializableEnergyStorage energyStorage;

    private int lastEnergy = 0;
    private boolean canCraft = false;
    private AssemblerRecipe currentRecipe = null;

    public AssemblerTileEntity() {
        itemHandler = new ItemStackHandler(9);
        energyStorage = new SerializableEnergyStorage(MAX_ENERGY, MAX_RECEIVE, -1);
        energyStorage.setListener(this);
        validate();

        currentRecipe = AssemblerRecipeManager.getForId(1242);
    }

    public boolean canCraft() {
        return canCraft;
    }

    public AssemblerRecipe getCurrentRecipe() {
        return currentRecipe;
    }

    public void setCurrentRecipe(AssemblerRecipe currentRecipe) {
        this.currentRecipe = currentRecipe;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || capability == CapabilityEnergy.ENERGY || super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (T) itemHandler;
        } else if(capability == CapabilityEnergy.ENERGY) {
            return (T) energyStorage;
        } else {
            return super.getCapability(capability, facing);
        }
    }

    public IItemHandler getItemHandler() {
        return itemHandler;
    }

    public IEnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        NBTTagCompound itemHandlerCompound = compound.getCompoundTag("inventory");
        itemHandler.deserializeNBT(itemHandlerCompound);
        NBTTagCompound energy = compound.getCompoundTag("energy");
        energyStorage.deserializeNBT(energy);

        canCraft = compound.getBoolean("canCraft");
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound compound) {
        NBTTagCompound nbtTagCompound = super.writeToNBT(compound);
        nbtTagCompound.setTag("inventory", itemHandler.serializeNBT());
        nbtTagCompound.setTag("energy", energyStorage.serializeNBT());
        nbtTagCompound.setBoolean("canCraft", canCraft);
        return nbtTagCompound;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound compound = new NBTTagCompound();
        writeToNBT(compound);
        return new SPacketUpdateTileEntity(this.getPos(), 1, compound);
    }

    @Override
    public void onReceive(boolean simulate) {
        if(!simulate && !world.isRemote && lastEnergy != energyStorage.getEnergyStored()) {
            this.markDirty();
            IBlockState blockState = world.getBlockState(getPos());
            world.notifyBlockUpdate(getPos(), blockState, blockState, 3);
            lastEnergy = energyStorage.getEnergyStored();
        }
    }

    @Override
    public void onExtract(boolean simulate) {

    }
}
