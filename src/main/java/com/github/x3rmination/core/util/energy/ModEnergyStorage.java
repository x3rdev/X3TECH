package com.github.x3rmination.core.util.energy;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.IEnergyStorage;

public class ModEnergyStorage implements IEnergyStorage, INBTSerializable<CompoundNBT> {

    private int energy;
    private int capacity;
    private int maxThrough = 100000000;
    private TileEntity tile;

    public ModEnergyStorage(TileEntity tileEntity, int energy, int capacity) {
        this.energy = energy;
        this.capacity = capacity;
        this.tile = tileEntity;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int energyReceived = Math.min(capacity - energy, Math.min(this.maxThrough, maxReceive));

        if (!simulate) {
            energy += energyReceived;
        }

        return energyReceived;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public int removeEnergy(int maxExtract, int energy, boolean simulate) {
        int energyExtracted = Math.min(energy, Math.min(this.maxThrough, maxExtract));
        energy -= energyExtracted;
        return energyExtracted;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return 0;
    }

    @Override
    public int getEnergyStored() {
        return this.energy;
    }

    @Override
    public int getMaxEnergyStored() {
        return this.capacity;
    }

    @Override
    public boolean canExtract() {
        return false;
    }

    @Override
    public boolean canReceive() {
        return true;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();
        tag.putInt("energy", this.energy);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.energy = nbt.getInt("energy");
    }
}
