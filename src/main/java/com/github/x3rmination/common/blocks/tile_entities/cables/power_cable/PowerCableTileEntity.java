package com.github.x3rmination.common.blocks.tile_entities.cables.power_cable;

import com.github.x3rmination.core.util.ModEnergyStorage;
import com.github.x3rmination.registry.TileEntityTypeInit;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

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
        if(level == null || level.isClientSide) {
            return;
        }
//        new Thread(() -> {
            if(this.cableEnergyStorage.getEnergyStored() > 0) {
            List<BlockPos> iteratedCables = Collections.synchronizedList(new LinkedList<>());
            List<BlockPos> workingList = Collections.synchronizedList(new LinkedList<>());
            PowerCableBlock powerCableBlock = (PowerCableBlock) this.getBlockState().getBlock();
            List<BlockPos> nonCableConnectionList = powerCableBlock.getNonCableConnectionsCanInput(this.getBlockPos(), this.level);
            List<BlockPos> cableConnectionList = powerCableBlock.getCableConnections(this.getBlockPos(), this.level);
            iteratedCables.clear();
            if(!nonCableConnectionList.isEmpty()) {
                extractEnergy(nonCableConnectionList.get(0));
                workingList.clear();
            } else if(!cableConnectionList.isEmpty()) {
                workingList.add(this.getBlockPos());
                while (!workingList.isEmpty()) {
                    for (BlockPos blockPos : workingList) {
                        List<BlockPos> possibleNeighbors = getNeighbors(blockPos);
                        for (BlockPos pos : possibleNeighbors) {
                            if (isValidEndpoint(pos, this.level)) {
                                extractEnergy(pos);
                                iteratedCables.clear();
                                break;
                            }
                            if (isValidCable(pos, this.level, iteratedCables) && !workingList.contains(pos)) {
                                workingList.add(pos);
                            }
                        }
                        if (!iteratedCables.contains(blockPos)) {
                            iteratedCables.add(blockPos);
                        }
                        workingList.removeAll(iteratedCables);
                        iteratedCables.addAll(workingList);
                    }
                }
//                    Thread.currentThread().interrupt();
                }
            }
//        }).start();
    }

    private boolean isValidEndpoint(BlockPos pos, World level) {
        Block block = level.getBlockState(pos).getBlock();
        TileEntity tileEntity = level.getBlockEntity(pos);
        if(tileEntity != null && !tileEntity.isRemoved() && !(block instanceof PowerCableBlock)) {
            LazyOptional<IEnergyStorage> cap = tileEntity.getCapability(CapabilityEnergy.ENERGY);
            if(cap.isPresent() && cap.orElse(null).canReceive() && cap.orElse(null).getMaxEnergyStored() != cap.orElse(null).getEnergyStored()) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidCable(BlockPos pos, World level, List<BlockPos> iteratedCables) {
        Block block = level.getBlockState(pos).getBlock();
        return block instanceof PowerCableBlock && !iteratedCables.contains(pos);
    }

    private List<BlockPos> getNeighbors(BlockPos pos) {
        List<BlockPos> resultList = new ArrayList<>();
        for(Direction direction : PowerCableHelper.getDirectionList()) {
            resultList.add(pos.relative(direction));
        }
        return resultList;
    }

    private void extractEnergy(BlockPos pos) {
        if(this.level!=null && this.level.getBlockEntity(pos) != null && Objects.requireNonNull(this.level.getBlockEntity(pos)).getCapability(CapabilityEnergy.ENERGY).isPresent()) {
            int energyLoss = Math.min(this.cableEnergyStorage.getEnergyStored(), this.cableEnergyStorage.getMaxThrough());
            this.cableEnergyStorage.extractEnergy( this.level.getBlockEntity(pos).getCapability(CapabilityEnergy.ENERGY).orElse(null).receiveEnergy(energyLoss, false), false);
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
