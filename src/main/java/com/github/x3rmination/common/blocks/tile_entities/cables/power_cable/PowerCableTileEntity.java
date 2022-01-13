package com.github.x3rmination.common.blocks.tile_entities.cables.power_cable;

import com.github.x3rmination.core.util.energy.ModEnergyStorage;
import com.github.x3rmination.registry.TileEntityTypeInit;
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
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class PowerCableTileEntity extends TileEntity implements ITickableTileEntity {

    private final ModEnergyStorage cableEnergyStorage;
    private final LazyOptional<ModEnergyStorage> energyHandler;

    private static final int MAX_REDSTONE_FLUX = 1000;
    private static final int MAX_THROUGH = 1000;
    private int energy = 0;

    private boolean foundCable = false;
    private Set<BlockPos> iteratedCables = Collections.EMPTY_SET;

    public PowerCableTileEntity() {
        super(TileEntityTypeInit.POWER_CABLE.get());
        this.cableEnergyStorage = new ModEnergyStorage(this, 0, MAX_REDSTONE_FLUX, MAX_THROUGH, true, true);
        this.energyHandler = LazyOptional.of(() -> this.cableEnergyStorage);
    }

    @Override
    public void tick() {
//        new Thread(() -> {
//            //try threading later
//        }).start();
        if(this.level == null || this.level.isClientSide) {
            return;
        }
        if(this.cableEnergyStorage.getEnergyStored() > 0) {
            PowerCableBlock powerCableBlock = (PowerCableBlock) this.getBlockState().getBlock();
            List<BlockPos> nonCableConnectionList = powerCableBlock.getNonCableConnections(this.getBlockPos(), this.level);
            List<BlockPos> cableConnectionList = powerCableBlock.getCableConnections(this.getBlockPos(), this.level);
            if(!nonCableConnectionList.isEmpty()) {
                for (BlockPos blockPos : nonCableConnectionList) {
                    if (this.cableEnergyStorage.getEnergyStored() > 0 && Objects.requireNonNull(level.getBlockEntity(blockPos)).getCapability(CapabilityEnergy.ENERGY).orElse(null).canReceive()) {
                        extractEnergy(blockPos);
                    }
                }

            } else if(!cableConnectionList.isEmpty()) {
                foundCable = false;
                BlockPos nextDest = getNextDestination(this.getBlockPos(), null).get(0);
                iteratedCables.add(this.getBlockPos());
                if(nextDest != null) {
                    if(level.getBlockEntity(nextDest).getCapability(CapabilityEnergy.ENERGY).orElse(null).canReceive()) {
                        extractEnergy(nextDest);
                    }
                    foundCable = true;
                    iteratedCables.clear();
                }
            }
        }
    }

    private List<BlockPos> getNextDestination(BlockPos previousPos, Direction direction) {
        if(iteratedCables.contains(previousPos) || foundCable) {
            iteratedCables.clear();
            return null;
        }
        assert this.level != null;
        PowerCableBlock thisBlock = (PowerCableBlock) this.level.getBlockState(previousPos).getBlock();
        List<BlockPos> nonCableConnectionList = thisBlock.getNonCableConnections(previousPos, this.level);
        List<BlockPos> cableConnectionList = thisBlock.getCableConnections(previousPos, this.level);
        if(!nonCableConnectionList.isEmpty()) {
            return nonCableConnectionList;
        } else if(!cableConnectionList.isEmpty()) {
            for (BlockPos nextPos : cableConnectionList) {
                System.out.println(""+cableConnectionList);
                Direction relativeDirection = relativeDirection(previousPos, nextPos);
                if (direction == null || direction != (relativeDirection != null ? relativeDirection.getOpposite() : null)) {
                    getNextDestination(nextPos, relativeDirection);
                }
            }
        } else {
            return cableConnectionList;
        }
        iteratedCables.clear();
        return null;
    }

    private Direction relativeDirection(BlockPos pos1, BlockPos pos2) {
        if(pos1.north() == pos2) {
            return Direction.NORTH;
        }
        if(pos1.east() == pos2) {
            return Direction.EAST;
        }
        if(pos1.south() == pos2) {
            return Direction.SOUTH;
        }
        if(pos1.west() == pos2) {
            return Direction.WEST;
        }
        if(pos1.above() == pos2) {
            return Direction.UP;
        }
        if(pos1.below() == pos2) {
            return Direction.DOWN;
        }
        return null;
    }

    private void extractEnergy(BlockPos pos) {
        if(this.level!=null && this.level.getBlockEntity(pos).getCapability(CapabilityEnergy.ENERGY).isPresent()) {
            this.cableEnergyStorage.extractEnergy(this.level.getBlockEntity(pos).getCapability(CapabilityEnergy.ENERGY).orElse(null).receiveEnergy(MAX_THROUGH, false), false);
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
        super.setRemoved();
        energyHandler.invalidate();
    }
}
