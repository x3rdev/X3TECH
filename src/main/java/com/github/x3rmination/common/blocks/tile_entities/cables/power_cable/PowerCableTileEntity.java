package com.github.x3rmination.common.blocks.tile_entities.cables.power_cable;

import com.github.x3rmination.core.util.energy.ModEnergyStorage;
import com.github.x3rmination.registry.init.TileEntityTypeInit;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class PowerCableTileEntity extends TileEntity implements ITickableTileEntity {

    private final ModEnergyStorage cableEnergyStorage;
    private final LazyOptional<ModEnergyStorage> energyHandler;

    private static final int MAX_REDSTONE_FLUX = 1000;
    private static final int MAX_THROUGH = 1000;
    private int energy = 0;

    public PowerCableTileEntity() {
        super(TileEntityTypeInit.POWER_CABLE.get());
        this.cableEnergyStorage = new ModEnergyStorage(this, 0, MAX_REDSTONE_FLUX, MAX_THROUGH, true, true);
        this.energyHandler = LazyOptional.of(() -> this.cableEnergyStorage);
    }

    @Override
    public void tick() {
        this.getBlockState().getBlock();
        PowerCableBlock cableBlock = (PowerCableBlock) this.getBlockState().getBlock();
        if (cableBlock.getPowerCableNetwork() != null) {
            List<BlockPos> outputConnections = cableBlock.getPowerCableNetwork().getOutputConnections();
            for (BlockPos receiverBlock : outputConnections) {
                if (this.getLevel().getBlockEntity(receiverBlock) == null) {
                    ((PowerCableBlock) this.getBlockState().getBlock()).getPowerCableNetwork().pruneImportConnections(this.getLevel());
                    ((PowerCableBlock) this.getBlockState().getBlock()).getPowerCableNetwork().pruneOutputConnections(this.getLevel());
                } else {
                    this.cableEnergyStorage.extractEnergy(this.getLevel().getBlockEntity(receiverBlock).getCapability(CapabilityEnergy.ENERGY).orElse(null).receiveEnergy(this.cableEnergyStorage.getEnergyStored()/outputConnections.size(), false), false);
                }
            }
        }
    }

    @Override
    public void load(BlockState state, CompoundNBT tags) {
        super.load(state, tags);
        energyHandler.ifPresent(modEnergyStorage -> modEnergyStorage.deserializeNBT(tags.getCompound("energy")));
    }

    @Override
    public CompoundNBT save(CompoundNBT tags) {
        super.save(tags);
        energyHandler.ifPresent(modEnergyStorage -> tags.put("energy", modEnergyStorage.serializeNBT()));
        return tags;
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT tags = this.getUpdateTag();
        return new SUpdateTileEntityPacket(this.worldPosition, 1, tags);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tags = super.getUpdateTag();
        tags.putInt("energy", this.energy);
        return tags;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(!this.remove && cap == CapabilityEnergy.ENERGY) {
            return energyHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void setRemoved() {
        energyHandler.invalidate();
    }
}
