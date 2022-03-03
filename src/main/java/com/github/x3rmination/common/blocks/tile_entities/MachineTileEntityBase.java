package com.github.x3rmination.common.blocks.tile_entities;

import com.github.x3rmination.core.util.ModEnergyStorage;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nullable;

public abstract class MachineTileEntityBase extends LockableTileEntity implements ISidedInventory, ITickableTileEntity {

    /*
    Class which should streamline the process and reduce the work required for generic tile entities
     */

    // Item Fields
    protected NonNullList<ItemStack> items;
    protected final LazyOptional<? extends IItemHandler>[] itemHandler;
    protected final int[] inputSlotIndex;
    protected final int[] outputSlotIndex;
    protected final int containerSize;

    // Energy Fields
    protected final ModEnergyStorage energyStorage;
    protected final LazyOptional<ModEnergyStorage> energyHandler;
    protected final int processTime;
    protected int progress = 0;
    protected int energy = 0;

    protected MachineTileEntityBase(TileEntityType<?> tileEntityType, int containerSize, int[] inputSlotIndex, int[] outputSlotIndex, int processTime, int capacity, int maxThrough, boolean canExtract, boolean canReceive) {
        super(tileEntityType);
        this.containerSize = containerSize;
        this.inputSlotIndex = inputSlotIndex;
        this.outputSlotIndex = outputSlotIndex;
        this.items = NonNullList.withSize(this.containerSize,ItemStack.EMPTY);
        this.itemHandler = SidedInvWrapper.create(this, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.EAST, Direction.UP, Direction.DOWN);

        this.processTime = processTime;
        this.energyStorage = new ModEnergyStorage(this, 0, capacity, maxThrough, canExtract, canReceive);
        this.energyHandler = LazyOptional.of(() -> this.energyStorage);
    }

    public abstract void encodeExtraData(PacketBuffer buffer);

    @Override
    public int[] getSlotsForFace(Direction pSide) {
        return inputSlotIndex;
    }

    @Override
    public int getContainerSize() {
        return this.containerSize;
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < getContainerSize() - 1; i++) {
            if(!getItem(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int pIndex) {
        return items.get(pIndex);
    }

    @Override
    public ItemStack removeItem(int pIndex, int pCount) {
        return ItemStackHelper.removeItem(items, pIndex, pCount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int pIndex) {
        return ItemStackHelper.takeItem(items, pIndex);
    }

    @Override
    public void setItem(int pIndex, ItemStack pStack) {
        items.set(pIndex, pStack);
    }

    @Override
    public boolean stillValid(PlayerEntity playerEntity) {
        return this.level != null && this.level.getBlockEntity(this.worldPosition) == this && playerEntity.distanceToSqr(this.worldPosition.getX(),this.worldPosition.getY() + 0.5, this.worldPosition.getZ()) <= 64;
    }

    @Override
    public void clearContent() {
        items.clear();
    }

    public int getProcessTime(){
        return processTime;
    }

    private void stopWork() {
        progress = 0;
    }

    public boolean useEnergy(int amount) {
        if(energyStorage.getEnergyStored() < amount){
            return false;
        } else {
            energyStorage.setEnergy(energyStorage.getEnergyStored() - amount);
            return true;
        }
    }

    public ModEnergyStorage getEnergyStorage() {
        return this.energyStorage;
    }

    private void autoEject(){
        if(getBlockState().getValue(MachineBlockBase.ITEM_NORTH).equals(1) && eject(Direction.NORTH) == ActionResultType.SUCCESS) {
            return;
        }
        if(getBlockState().getValue(MachineBlockBase.ITEM_EAST).equals(1) && eject(Direction.EAST) == ActionResultType.SUCCESS) {
            return;
        }
        if(getBlockState().getValue(MachineBlockBase.ITEM_SOUTH).equals(1) && eject(Direction.SOUTH) == ActionResultType.SUCCESS) {
            return;
        }
        if(getBlockState().getValue(MachineBlockBase.ITEM_WEST).equals(1) && eject(Direction.WEST) == ActionResultType.SUCCESS) {
            return;
        }
        if(getBlockState().getValue(MachineBlockBase.ITEM_UP).equals(1) && eject(Direction.UP) == ActionResultType.SUCCESS) {
            return;
        }
        if(getBlockState().getValue(MachineBlockBase.ITEM_DOWN).equals(1) && eject(Direction.DOWN) == ActionResultType.SUCCESS) {
            return;
        }
    }
    private ActionResultType eject(Direction dir){
        IInventory inventory = checkForContainer(dir);
        int slotIndex = getOutputSlotIndex();
        if(inventory != null && slotIndex != -1) {
            for (int i = 0; i < inventory.getContainerSize(); i++) {
                ItemStack targetedItemStack = inventory.getItem(i);
                ItemStack currentItemStack = this.getItem(slotIndex);
                if(targetedItemStack.isEmpty()) {
                    inventory.setItem(i, currentItemStack.copy());
                    setItem(slotIndex, ItemStack.EMPTY);
                    return ActionResultType.SUCCESS;
                } else if (targetedItemStack.getItem().equals(currentItemStack.getItem()) && targetedItemStack.getMaxStackSize() > targetedItemStack.getCount()) {
                    if(targetedItemStack.getCount() + currentItemStack.getCount() <= targetedItemStack.getMaxStackSize()) {
                        inventory.getItem(i).setCount(targetedItemStack.getCount() + currentItemStack.getCount());
                        setItem(slotIndex, ItemStack.EMPTY);
                        return ActionResultType.SUCCESS;
                    }
                    if(targetedItemStack.getCount() + currentItemStack.getCount() > targetedItemStack.getMaxStackSize()) {
                        int diff = Math.abs(targetedItemStack.getMaxStackSize() - (targetedItemStack.getCount() + currentItemStack.getCount()));
                        inventory.getItem(i).setCount(targetedItemStack.getMaxStackSize());
                        currentItemStack.setCount(currentItemStack.getCount() - diff);
                        return ActionResultType.SUCCESS;
                    }
                }
            }
        }
        return ActionResultType.FAIL;
    }
    private int getOutputSlotIndex(){
        for (Integer outputIndex : outputSlotIndex) {
            if(!this.getItem(outputIndex).isEmpty()) {
                return outputIndex;
            }
        }
        return -1;
    }
    private IInventory checkForContainer(Direction side){
        assert this.level != null;
        if(this.level.getBlockEntity(this.getBlockPos().relative(side, 1)) instanceof IInventory) {
            return ((IInventory) this.level.getBlockEntity(this.getBlockPos().relative(side, 1)));
        }
        return null;
    }

    @Override
    public void load(BlockState state, CompoundNBT tags) {
        super.load(state, tags);
        this.items = NonNullList.withSize(this.items.size(), ItemStack.EMPTY);
        this.progress = tags.getInt("progress");
        ItemStackHelper.loadAllItems(tags, this.items);
        energyHandler.ifPresent(modEnergyStorage -> modEnergyStorage.deserializeNBT(tags.getCompound("energy")));
    }


    @Override
    public CompoundNBT save(CompoundNBT tags) {
        super.save(tags);
        ItemStackHelper.saveAllItems(tags, this.items);
        tags.putInt("progress", this.progress);
        energyHandler.ifPresent(modEnergyStorage -> tags.put("energy", modEnergyStorage.serializeNBT()));
        return tags;
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT tags = this.getUpdateTag();
        ItemStackHelper.saveAllItems(tags, this.items);
        return new SUpdateTileEntityPacket(this.worldPosition, 1, tags);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tags = super.getUpdateTag();
        tags.putInt("progress", this.progress);
        tags.putInt("energy", this.energy);
        return tags;
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (!this.remove) {
            if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
                if(side == Direction.NORTH) {
                    return this.itemHandler[0].cast();
                }
                if(side == Direction.EAST) {
                    return this.itemHandler[1].cast();
                }
                if(side == Direction.SOUTH) {
                    return this.itemHandler[2].cast();
                }
                if(side == Direction.WEST) {
                    return this.itemHandler[3].cast();
                }
                if(side == Direction.UP) {
                    return this.itemHandler[4].cast();
                }
                if(side == Direction.DOWN) {
                    return this.itemHandler[5].cast();
                }
            }
            if (cap == CapabilityEnergy.ENERGY) {
                return energyHandler.cast();
            }
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        for (LazyOptional<? extends IItemHandler> handler : this.itemHandler) {
            energyHandler.invalidate();
            handler.invalidate();
        }
    }


}
