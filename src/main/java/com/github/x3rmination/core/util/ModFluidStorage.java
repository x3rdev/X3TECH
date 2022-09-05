package com.github.x3rmination.core.util;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public class ModFluidStorage implements IFluidHandler, IFluidTank {

    protected Predicate<FluidStack> validator;
    protected FluidStack fluid = FluidStack.EMPTY;

    private final int tanks;
    private final int capacity;
    private final int maxThrough;

    public ModFluidStorage(TileEntity tileEntity, int tanks, int capacity, int maxThrough) {

        this.tanks = tanks;
        this.capacity = capacity;
        this.maxThrough = maxThrough;

        this.validator = (v -> true);
    }

    @Override
    public int getTanks() {
        return this.tanks;
    }

    @Nonnull
    @Override
    public FluidStack getFluidInTank(int tank) {
        return this.fluid;
    }

    @Override
    public int getTankCapacity(int tank) {
        return this.capacity;
    }

    @Nonnull
    @Override
    public FluidStack getFluid() {
        return this.fluid;
    }

    @Override
    public int getFluidAmount() {
        return this.fluid.getAmount();
    }

    @Override
    public int getCapacity() {
        return this.capacity;
    }

    public int getMaxThrough() {
        return this.maxThrough;
    }

    public boolean isFluidValid(@Nonnull FluidStack stack) {
        return this.isFluidValid(0, stack);
    }

    @Override
    public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
        return this.validator.test(this.getFluidInTank(tank));
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        if (resource.isEmpty() || !isFluidValid(resource)) {
            return 0;
        }
        if (action.simulate()) {
            if (fluid.isEmpty())
            {
                return Math.min(capacity, resource.getAmount());
            }
            if (!fluid.isFluidEqual(resource))
            {
                return 0;
            }
            return Math.min(capacity - fluid.getAmount(), resource.getAmount());
        }
        if (fluid.isEmpty()) {
            fluid = new FluidStack(resource, Math.min(capacity, resource.getAmount()));
            return fluid.getAmount();
        }
        if (!fluid.isFluidEqual(resource)) {
            return 0;
        }
        int filled = capacity - fluid.getAmount();

        if (resource.getAmount() < filled) {
            fluid.grow(resource.getAmount());
            filled = resource.getAmount();
        } else {
            fluid.setAmount(capacity);
        }
        return filled;
    }

    @Nonnull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        if (resource.isEmpty() || !resource.isFluidEqual(fluid)) {
            return FluidStack.EMPTY;
        }
        return drain(resource.getAmount(), action);
    }

    @Nonnull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        int drained = maxDrain;
        if (fluid.getAmount() < drained) {
            drained = fluid.getAmount();
        }
        FluidStack stack = new FluidStack(fluid, drained);
        if (action.execute() && drained > 0) {
            fluid.shrink(drained);
        }
        return stack;
    }

    public ModFluidStorage readFromNBT(CompoundNBT nbt) {
        FluidStack fluid = FluidStack.loadFluidStackFromNBT(nbt);
        setFluid(fluid);
        return this;
    }

    public CompoundNBT writeToNBT(CompoundNBT nbt) {
        fluid.writeToNBT(nbt);
        return nbt;
    }

    public void setFluid(FluidStack stack) {
        this.fluid = stack;
    }

    public boolean isEmpty()
    {
        return this.fluid.isEmpty();
    }

    public int getSpace()
    {
        return Math.max(0, this.capacity - this.fluid.getAmount());
    }
}
