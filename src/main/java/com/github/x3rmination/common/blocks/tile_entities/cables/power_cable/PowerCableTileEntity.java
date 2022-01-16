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
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PowerCableTileEntity extends TileEntity implements ITickableTileEntity {

    private final ModEnergyStorage cableEnergyStorage;
    private final LazyOptional<ModEnergyStorage> energyHandler;

    private static final int MAX_REDSTONE_FLUX = 1000;
    private static final int MAX_THROUGH = 1000;
    private int energy = 0;

    private boolean foundCable = false;
    private List<BlockPos> iteratedCables = new ArrayList<>();

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
        if(level == null || level.isClientSide) {
            return;
        }

        if(this.cableEnergyStorage.getEnergyStored() > 0) {
            PowerCableBlock powerCableBlock = (PowerCableBlock) this.getBlockState().getBlock();
            List<BlockPos> nonCableConnectionList = powerCableBlock.getNonCableConnectionsCanInput(this.getBlockPos(), this.level);
            List<BlockPos> cableConnectionList = powerCableBlock.getCableConnections(this.getBlockPos(), this.level);
            if(!nonCableConnectionList.isEmpty()) {
                extractEnergy(nonCableConnectionList.get(0));
                iteratedCables.clear();
            } else if(!cableConnectionList.isEmpty()) {
                extractEnergy(getNextTargetBlock(this.getBlockPos(), powerCableBlock));
                iteratedCables.clear();
            }
        }
    }

    private BlockPos getNextTargetBlock(BlockPos currentCable, PowerCableBlock powerCableBlock) {
        assert level != null;
        iteratedCables.clear();
        iteratedCables.add(currentCable);
        List<BlockPos> possibleTargets = new ArrayList<>();
        while(possibleTargets.isEmpty()) {
            System.out.println(""+getAdjacent(iteratedCables.get(iteratedCables.size()-1)).get(0).getPos());
            System.out.println(""+getAdjacent(iteratedCables.get(iteratedCables.size()-1)).get(0).getIsCable());
        }
        //Check Contents of possibleTargets before returning 1 value
        return possibleTargets.get(0);
    }

    private List<CableConnectionDataHolder> getAdjacent(BlockPos initialCable) {
        assert level != null;
        level.getBlockState(initialCable).getBlock();
        List<BlockPos> neighborList = getNeighbours(initialCable, this.level);
        List<CableConnectionDataHolder> outputList= new ArrayList<>();
        for(BlockPos blockPos : neighborList) {
            if (!iteratedCables.contains(blockPos)) {
                if (level.getBlockState(blockPos).getBlock() instanceof PowerCableBlock) {
                    outputList.add(new CableConnectionDataHolder(blockPos, true));
                } else {
                    outputList.add(new CableConnectionDataHolder(blockPos, false));
                }
            }
        }
        return outputList;
    }

    private List<BlockPos> getNeighbours(BlockPos pos, World level) {
        List<Direction> directionList = new PowerCableHelper().getDirectionList();
        List<BlockPos> outputList = new ArrayList<>();
        for (Direction direction : directionList) {
            BlockState selectedBlock = level.getBlockState(pos.relative(direction));
            if (selectedBlock.hasTileEntity() && level.getBlockEntity(pos.relative(direction)).getCapability(CapabilityEnergy.ENERGY).isPresent() && level.getBlockEntity(pos.relative(direction)).getCapability(CapabilityEnergy.ENERGY).orElse(null).canReceive()) {
                outputList.add(pos.relative(direction));
            }
        }
        return outputList;
    }

    public final class CableConnectionDataHolder {
        private final BlockPos pos;
        private final boolean isCable;
        public CableConnectionDataHolder(BlockPos pos, boolean isCable) {
            this.pos = pos;
            this.isCable = isCable;
        }

        public BlockPos getPos() {
            return pos;
        }

        public boolean getIsCable() {
            return isCable;
        }
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
