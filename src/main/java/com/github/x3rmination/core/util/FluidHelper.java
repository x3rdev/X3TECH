package com.github.x3rmination.core.util;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class FluidHelper {

    FluidHelper(){}

    public static boolean isValidFluidReceiver(@Nonnull World level, BlockPos pos, @Nullable Direction direction) {
        TileEntity tile = level.getBlockEntity(pos);
        if(tile != null) {
            LazyOptional<IFluidHandler> fluidHandler = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, direction);
            // Checks for valid fluid done when transferring the liquid
            return tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, direction).isPresent();
        }
        return false;
    }
    public static boolean isValidFluidExtractor(@Nonnull World level, BlockPos pos, @Nullable Direction direction) {
        // For now it just checks if cap is present
        return isValidFluidReceiver(level, pos, direction);
    }

    public static boolean isValidRecOrExt(@Nonnull World level, BlockPos pos, @Nullable Direction direction) {
        return isValidFluidReceiver(level, pos, direction) || isValidFluidExtractor(level, pos, direction);
    }

    public static boolean transferFluid(TileEntity tileEntitySender, TileEntity tileEntityReceiver, int maxThrough, int tank) {
        Direction direction = isPosAdjacent(tileEntitySender.getBlockPos(), tileEntityReceiver.getBlockPos(), tileEntityReceiver.getLevel());
        if(direction == null || !isValidFluidReceiver(tileEntityReceiver.getLevel(), tileEntityReceiver.getBlockPos(), direction.getOpposite())) return false;
        IFluidHandler fluidHandlerS = tileEntitySender.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, direction).orElse(null);
        IFluidHandler fluidHandlerR = tileEntityReceiver.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, direction.getOpposite()).orElse(null);
        int rTank = -1;
        for (int i = 0; i < fluidHandlerR.getTanks(); i++) {
            if(fluidHandlerR.fill(fluidHandlerS.getFluidInTank(tank), IFluidHandler.FluidAction.SIMULATE) > 0) {
                rTank = i;
                break;
            }
        }

        int fluidLoss = Math.min(fluidHandlerS.getFluidInTank(rTank).getAmount(), maxThrough);
        fluidHandlerR.fill(new FluidStack(fluidHandlerS.getFluidInTank(tank).getFluid(), fluidLoss), IFluidHandler.FluidAction.EXECUTE);
        fluidHandlerS.drain(fluidLoss, IFluidHandler.FluidAction.EXECUTE);
        return true;
    }

    @Nullable
    public static Direction isPosAdjacent(BlockPos pos1, BlockPos pos2, World level) {
        for(Direction direction : CableHelper.getDirectionList()) {
            if(pos1.relative(direction) == pos2) {
                return direction;
            }
        }
        return null;
    }
}
