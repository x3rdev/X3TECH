package com.github.x3rmination.common.blocks.tile_entities.cables.base.power_cable;

import com.github.x3rmination.core.util.CableHelper;
import com.github.x3rmination.core.util.EnergyHelper;
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class PowerCableTileEntity extends TileEntity implements ITickableTileEntity {

    private final ModEnergyStorage cableEnergyStorage;
    private final LazyOptional<ModEnergyStorage> energyHandler;
    private final int maxRedstoneFlux;
    private final int maxThrough;
    private int energy = 0;

    public PowerCableTileEntity() {
        this(1000, 1000);
    }

    public PowerCableTileEntity(int maxRedstoneFlux, int maxThrough) {
        super(TileEntityTypeInit.POWER_CABLE.get());
        this.maxRedstoneFlux = maxRedstoneFlux;
        this.maxThrough = maxThrough;
        this.cableEnergyStorage = new ModEnergyStorage(this, 0, maxRedstoneFlux, maxThrough, true, true);
        this.energyHandler = LazyOptional.of(() -> this.cableEnergyStorage);

    }

    @Override
    public void tick() {
        if(level == null || level.isClientSide) {
            return;
        }

        if(this.cableEnergyStorage.getEnergyStored() > 0) {
            List<BlockPos> iteratedCables = Collections.synchronizedList(new LinkedList<>());
            List<BlockPos> workingList = Collections.synchronizedList(new LinkedList<>());
            PowerCableBlock powerCableBlock = (PowerCableBlock) this.getBlockState().getBlock();
            List<BlockPos> nonCableConnectionList = powerCableBlock.getNonCableConnectionsCanInput(this.getBlockPos(), this.level);
            List<BlockPos> cableConnectionList = powerCableBlock.getCableConnections(this.getBlockPos(), this.level);
            iteratedCables.clear();

            if(!nonCableConnectionList.isEmpty()) {
//                extractEnergy(nonCableConnectionList.get(0));
                EnergyHelper.transferEnergy(this, this.level.getBlockEntity(nonCableConnectionList.get(0)), this.energy, this.maxThrough);
                workingList.clear();

            } else if(!cableConnectionList.isEmpty()) {
                workingList.add(this.getBlockPos());
                while (!workingList.isEmpty()) {
                    for (BlockPos blockPos : workingList) {
                        List<BlockPos> possibleNeighbors = getNeighbors(blockPos);
                        for (BlockPos pos : possibleNeighbors) {
                            if (isValidCable(pos, this.level, iteratedCables) && !workingList.contains(pos)) {
                                workingList.add(pos);
                            } else if (EnergyHelper.isValidEnergyReceiver(this.level, pos, EnergyHelper.isPosAdjacent(this.getBlockPos(), pos, this.level))) {
                                EnergyHelper.transferAsOther(this.level.getBlockEntity(iteratedCables.get(iteratedCables.size() - 1)), this, this.level.getBlockEntity(pos), this.energy, maxThrough);
                                iteratedCables.clear();
                                break;
                            }
                        }
                        if (!iteratedCables.contains(blockPos)) {
                            iteratedCables.add(blockPos);
                        }
                        workingList.removeAll(iteratedCables);
                        iteratedCables.addAll(workingList);
                    }
                }
            }
        }
    }

    private boolean isValidCable(BlockPos pos, World level, List<BlockPos> iteratedCables) {
        Block block = level.getBlockState(pos).getBlock();
        return block instanceof PowerCableBlock && !iteratedCables.contains(pos);
    }

    private List<BlockPos> getNeighbors(BlockPos pos) {
        List<BlockPos> resultList = new ArrayList<>();
        for(Direction direction : CableHelper.getDirectionList()) {
            resultList.add(pos.relative(direction));
        }
        return resultList;
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
