package com.github.x3rmination.core.util;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;

public final class EnergyHelper {

    EnergyHelper(){}

    public static boolean isValidEnergyReceiver(@Nonnull World level, BlockPos pos) {
        TileEntity tile = level.getBlockEntity(pos);
        if(tile != null && tile.getCapability(CapabilityEnergy.ENERGY).isPresent()) {
            return tile.getCapability(CapabilityEnergy.ENERGY).orElse(null).canReceive();
        }
        return false;
    }
    public static boolean isValidEnergyExtractor(@Nonnull World level, BlockPos pos) {
        TileEntity tile = level.getBlockEntity(pos);
        if(tile != null && tile.getCapability(CapabilityEnergy.ENERGY).isPresent()) {
            return tile.getCapability(CapabilityEnergy.ENERGY).orElse(null).canExtract();
        }
        return false;
    }

    public static void transferEnergy(TileEntity tileEntitySender, TileEntity tileEntityReceiver, int amount) {
        IEnergyStorage senderCap = tileEntitySender.getCapability(CapabilityEnergy.ENERGY).orElse(null);
        IEnergyStorage receiverCap = tileEntityReceiver.getCapability(CapabilityEnergy.ENERGY).orElse(null);
        amount = Math.min(Math.min(amount, senderCap.extractEnergy(amount, true)), Math.min(senderCap.getMaxEnergyStored(), receiverCap.receiveEnergy(amount, true)));
        senderCap.extractEnergy(amount, false);
        receiverCap.receiveEnergy(amount, false);
    }
}
