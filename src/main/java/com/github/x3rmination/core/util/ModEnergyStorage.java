package com.github.x3rmination.core.util;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.IEnergyStorage;

public class ModEnergyStorage implements IEnergyStorage, INBTSerializable<CompoundNBT> {

    private TileEntity tile;
    private int energy;
    private int capacity;
    private int maxThrough;
    private boolean canExtract;
    private boolean canReceive;

    public ModEnergyStorage(TileEntity tileEntity, int energy, int capacity, int maxThrough, boolean canExtract, boolean canReceive) {
        this.tile = tileEntity;
        this.energy = energy;
        this.capacity = capacity;
        this.maxThrough = maxThrough;
        this.canExtract = canExtract;
        this.canReceive = canReceive;
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

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int energyExtracted = Math.min(energy, Math.min(this.maxThrough, maxExtract));
        energy -= energyExtracted;
        return energyExtracted;
    }

    @Override
    public int getEnergyStored() {
        return this.energy;
    }

    @Override
    public int getMaxEnergyStored() {
        return this.capacity;
    }

    public int getMaxThrough(){
        return this.maxThrough;
    }

    @Override
    public boolean canExtract() {
        return this.canExtract;
    }

    @Override
    public boolean canReceive() {
        return this.canReceive;
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
