package com.github.x3rmination.core.util;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class EnergyHelper {

    EnergyHelper(){}

    public static boolean isValidEnergyReceiver(@Nonnull World level, BlockPos pos, @Nullable Direction direction) {
        TileEntity tile = level.getBlockEntity(pos);
        if(tile != null && tile.getCapability(CapabilityEnergy.ENERGY, direction).isPresent()) {
            IEnergyStorage storage = tile.getCapability(CapabilityEnergy.ENERGY, direction).orElse(null);
            return storage.canReceive() && storage.getEnergyStored() < storage.getMaxEnergyStored();
        }
        return false;
    }
    public static boolean isValidEnergyExtractor(@Nonnull World level, BlockPos pos, @Nullable Direction direction) {
        TileEntity tile = level.getBlockEntity(pos);
        if(tile != null && tile.getCapability(CapabilityEnergy.ENERGY, direction).isPresent()) {
            IEnergyStorage storage = tile.getCapability(CapabilityEnergy.ENERGY, direction).orElse(null);
            return storage.canExtract() && storage.getEnergyStored() > 0;
        }
        return false;
    }

    public static boolean isValidRecOrExt(@Nonnull World level, BlockPos pos, @Nullable Direction direction) {
        return isValidEnergyReceiver(level, pos, direction) || isValidEnergyExtractor(level, pos, direction);
    }

    public static boolean transferEnergy(TileEntity tileEntitySender, TileEntity tileEntityReceiver, int amount, int maxThrough) {
        Direction direction = isPosAdjacent(tileEntitySender.getBlockPos(), tileEntityReceiver.getBlockPos(), tileEntityReceiver.getLevel());
        if(direction == null || !isValidEnergyReceiver(tileEntityReceiver.getLevel(), tileEntityReceiver.getBlockPos(), direction.getOpposite())) {
            return false;
        }
        IEnergyStorage energyStorageS = tileEntitySender.getCapability(CapabilityEnergy.ENERGY, direction).orElse(null);
        IEnergyStorage energyStorageR = tileEntityReceiver.getCapability(CapabilityEnergy.ENERGY, direction.getOpposite()).orElse(null);
        int energyLoss = Math.min(energyStorageS.getEnergyStored(), maxThrough);
        energyStorageS.extractEnergy(energyStorageR.receiveEnergy(energyLoss, false), false);
        return true;
    }

    public static boolean transferAsOther(TileEntity fakeSender, TileEntity tileEntitySender, TileEntity tileEntityReceiver, int amount, int maxThrough) {
        if(fakeSender == null) {
            return false;
        }
        Direction direction = isPosAdjacent(fakeSender.getBlockPos(), tileEntityReceiver.getBlockPos(), tileEntitySender.getLevel());
        if(direction == null || !isValidEnergyReceiver(tileEntityReceiver.getLevel(), tileEntityReceiver.getBlockPos(), direction.getOpposite())) {
            return false;
        }
        IEnergyStorage energyStorageS = tileEntitySender.getCapability(CapabilityEnergy.ENERGY, direction).orElse(null);
        IEnergyStorage energyStorageR = tileEntityReceiver.getCapability(CapabilityEnergy.ENERGY, direction.getOpposite()).orElse(null);
        int energyLoss = Math.min(energyStorageS.getEnergyStored(), maxThrough);
        energyStorageS.extractEnergy(energyStorageR.receiveEnergy(energyLoss, false), false);
        return true;
    }

    @Nullable
    public static Direction isPosAdjacent(BlockPos pos1, BlockPos pos2, World level) {
        for(Direction direction : CableHelper.getDirectionList()) {
            if(pos1.relative(direction).equals(pos2)) {
                return direction;
            }
        }
        return null;
    }
}
