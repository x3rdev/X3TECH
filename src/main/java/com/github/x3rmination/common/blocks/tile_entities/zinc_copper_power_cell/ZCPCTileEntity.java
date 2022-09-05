package com.github.x3rmination.common.blocks.tile_entities.zinc_copper_power_cell;

import com.github.x3rmination.core.util.ModEnergyStorage;
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

public class ZCPCTileEntity extends TileEntity implements ITickableTileEntity {

    private final ModEnergyStorage energyStorage;
    private final LazyOptional<ModEnergyStorage> energyHandler;

    private static final int MAX_REDSTONE_FLUX = 15000;
    private static final int MAX_THROUGH = 1000;
    private int energy = 0;

    public ZCPCTileEntity() {
        super(TileEntityTypeInit.ZINC_COPPER_POWER_CELL.get());
        this.energyStorage = new ModEnergyStorage(this, 0, MAX_REDSTONE_FLUX, MAX_THROUGH, true, true);
        this.energyHandler = LazyOptional.of(() -> this.energyStorage);
    }

    @Override
    public void tick() {
        if(this.level == null || this.level.isClientSide) {
            return;
        }
        BlockPos pos = this.getBlockPos().relative(this.getBlockState().getValue(ZCPCBlock.FACING).getOpposite(), 1);
//        if(EnergyHelper.isValidEnergyReceiver(this.level, pos, EnergyHelper.isPosAdjacent(this.getBlockPos(), pos, this.level))) {
//            EnergyHelper.transferEnergy(this, level.getBlockEntity(pos), MAX_THROUGH, MAX_THROUGH);
//        }
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
        Direction direction = getBlockState().getValue(ZCPCBlock.FACING);
        if (!this.remove && cap == CapabilityEnergy.ENERGY && (side == null || side == direction || side == direction.getOpposite())) {
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
