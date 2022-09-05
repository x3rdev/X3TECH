package com.github.x3rmination.common.blocks.tile_entities.cables.base.fluid_pipe;

import com.github.x3rmination.core.util.CableHelper;
import com.github.x3rmination.core.util.FluidHelper;
import com.github.x3rmination.core.util.ModFluidStorage;
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
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class FluidPipeTileEntity extends TileEntity implements ITickableTileEntity {

    private final ModFluidStorage pipeFluidStorage;
    private final LazyOptional<ModFluidStorage> fluidHandler;

    private static final int CAPACITY = 1000;
    private static final int MAX_THROUGH = 1000;

    public FluidPipeTileEntity() {
        super(TileEntityTypeInit.FLUID_PIPE.get());
        this.pipeFluidStorage = new ModFluidStorage(this, 1, CAPACITY, MAX_THROUGH);
        this.fluidHandler = LazyOptional.of(() -> this.pipeFluidStorage);
    }

    @Override
    public void tick() {
        if(level == null || level.isClientSide) {
            return;
        }
//        new Thread(() -> {
        if(this.pipeFluidStorage.getFluidAmount() > 0) {
            List<BlockPos> iteratedCables = Collections.synchronizedList(new LinkedList<>());
            List<BlockPos> workingList = Collections.synchronizedList(new LinkedList<>());
            FluidPipeBlock fluidPipeBlock = (FluidPipeBlock) this.getBlockState().getBlock();
            List<BlockPos> nonPipeConnectionList = fluidPipeBlock.getNonCableConnectionsCanInput(this.getBlockPos(), this.level);
            List<BlockPos> pipeConnectionList = fluidPipeBlock.getCableConnections(this.getBlockPos(), this.level);
            iteratedCables.clear();
            if(!nonPipeConnectionList.isEmpty()) {
                FluidHelper.transferFluid(this, this.level.getBlockEntity(nonPipeConnectionList.get(0)), MAX_THROUGH, 0);
                workingList.clear();
            } else if(!pipeConnectionList.isEmpty()) {
                workingList.add(this.getBlockPos());
                while (!workingList.isEmpty()) {
                    for (BlockPos blockPos : workingList) {
                        List<BlockPos> possibleNeighbors = getNeighbors(blockPos);
                        for (BlockPos pos : possibleNeighbors) {
                            if (isValidEndpoint(pos, this.level)) {
                                FluidHelper.transferFluid(this, this.level.getBlockEntity(pos), MAX_THROUGH, 0);
                                iteratedCables.clear();
                                break;
                            }
                            if (isValidPipe(pos, this.level, iteratedCables) && !workingList.contains(pos)) {
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
                }
            }
    }

    private boolean isValidEndpoint(BlockPos pos, World level) {
        Block block = level.getBlockState(pos).getBlock();
        TileEntity tileEntity = level.getBlockEntity(pos);
        if(tileEntity != null && !tileEntity.isRemoved() && !(block instanceof FluidPipeBlock)) {
            return FluidHelper.isValidRecOrExt(level, pos, FluidHelper.isPosAdjacent(this.getBlockPos(), pos, level));
        }
        return false;
    }

    private boolean isValidPipe(BlockPos pos, World level, List<BlockPos> iteratedCables) {
        Block block = level.getBlockState(pos).getBlock();
        return block instanceof FluidPipeBlock && !iteratedCables.contains(pos);
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
        pipeFluidStorage.readFromNBT(tags);
    }

    @Override
    public CompoundNBT save(CompoundNBT tags) {
        super.save(tags);
        pipeFluidStorage.writeToNBT(tags);
        return tags;
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT tags = this.getUpdateTag();
        return new SUpdateTileEntityPacket(this.worldPosition, 1, tags);
    }

//    @Override
//    public CompoundNBT getUpdateTag() {
//        CompoundNBT tags = super.getUpdateTag();
//        tags.putInt("energy", this.energy);
//        return tags;
//    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return fluidHandler.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        fluidHandler.invalidate();
    }


}
