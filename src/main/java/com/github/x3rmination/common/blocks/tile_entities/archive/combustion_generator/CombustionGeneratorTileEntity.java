package com.github.x3rmination.common.blocks.tile_entities.archive.combustion_generator;

import com.github.x3rmination.core.util.ModEnergyStorage;
import com.github.x3rmination.registry.TileEntityTypeInit;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nullable;

public class CombustionGeneratorTileEntity extends LockableTileEntity implements ISidedInventory, ITickableTileEntity {

    static int processTime;

    private NonNullList<ItemStack> items;
    private final LazyOptional<? extends IItemHandler>[] itemHandler;

    private int progress = 0;
    private int energy = 0;
    private boolean working = false;
    private int lastBurnTime;
    private static final int MAX_REDSTONE_FLUX = 10000;

    private final ModEnergyStorage combustionGeneratorEnergyStorage;
    private final LazyOptional<ModEnergyStorage> energyHandler;

    private final IIntArray fields = new IIntArray() {
        @Override
        public int get(int index) {
            switch(index) {
                case 0:
                    return progress;
                case 1:
                    return combustionGeneratorEnergyStorage.getEnergyStored();
                case 2:
                    return combustionGeneratorEnergyStorage.getMaxEnergyStored();
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {
        }

        @Override
        public int getCount() {
            return 3;
        }
    };

    public CombustionGeneratorTileEntity() {
        super(TileEntityTypeInit.COMBUSTION_GENERATOR.get());
        this.itemHandler = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);
        this.items = NonNullList.withSize(1, ItemStack.EMPTY);
        this.combustionGeneratorEnergyStorage = new ModEnergyStorage(this, 0, MAX_REDSTONE_FLUX, 100000, true, false);
        this.energyHandler = LazyOptional.of(() -> this.combustionGeneratorEnergyStorage);
    }

    void encodeExtraData(PacketBuffer buffer) {
        buffer.writeByte(fields.getCount());
    }

    @Override
    public void tick() {
        if(this.level == null || this.level.isClientSide) {
            return;
        }
        int burnTime = ForgeHooks.getBurnTime(getItem(0), IRecipeType.SMELTING);
        if(burnTime > 0 && !working && (combustionGeneratorEnergyStorage.getEnergyStored() != combustionGeneratorEnergyStorage.getMaxEnergyStored())) {
            working = true;
            lastBurnTime = burnTime;
            doWork(burnTime);
            removeItem(0, 1);
            this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(CombustionGeneratorBlock.ACTIVE, Boolean.TRUE), 3);
        } else if(working) {
            doWork(lastBurnTime);
        } else {
            stopWork();
            this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(CombustionGeneratorBlock.ACTIVE, Boolean.FALSE), 3);
        }
        if(combustionGeneratorEnergyStorage.getEnergyStored() > 0) {
            Direction direction = this.getBlockState().getValue(CombustionGeneratorBlock.FACING).getOpposite();
            TileEntity tileEntity = this.level.getBlockEntity(getBlockPos().relative(direction, 1));
            if(tileEntity != null && !tileEntity.isRemoved() && tileEntity.getCapability(CapabilityEnergy.ENERGY).isPresent()) {
                // add all directions
                LazyOptional<IEnergyStorage> capabilityEnergy = tileEntity.getCapability(CapabilityEnergy.ENERGY);

                if(capabilityEnergy.orElse(null).canReceive()) {
                    int energyLoss = Math.min(combustionGeneratorEnergyStorage.getEnergyStored(), combustionGeneratorEnergyStorage.getMaxThrough());

                    combustionGeneratorEnergyStorage.extractEnergy(capabilityEnergy.orElse(null).receiveEnergy(energyLoss, false), false);
                }
            }
        }
    }

    private void doWork(int burnTime) {
        assert this.level != null;
        processTime = burnTime / 100;

        if(progress < processTime) {
            progress += 1;
            combustionGeneratorEnergyStorage.receiveEnergy(100, false);
        }

        if(progress >= processTime) {
            finishWork();
        }
    }

    public int getProcessTime() {
        return processTime;
    }

    private void stopWork() {
        progress = 0;
    }

    private void finishWork(){
        progress = 0;
        working = false;
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return new int[]{0};
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStack, @Nullable Direction direction) {
        if(index == 0) {
            return this.canPlaceItem(index, itemStack);
        } else {
            return false;
        }
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return index == 0;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.x3tech.combustion_generator");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory inventory) {
        return new CombustionGeneratorContainer(id, inventory, this, this.fields);
    }

    @Override
    public int getContainerSize() {
        return this.items.size();
    }

    @Override
    public boolean isEmpty() {
        return getItem(0).isEmpty();
    }

    @Override
    public ItemStack getItem(int index) {
        return this.items.get(index);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        return ItemStackHelper.removeItem(items, index, count);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return ItemStackHelper.takeItem(items, index);
    }

    @Override
    public void setItem(int index, ItemStack itemStack) {
        items.set(index, itemStack);
    }

    @Override
    public boolean stillValid(PlayerEntity playerEntity) {
        return this.level != null && this.level.getBlockEntity(this.worldPosition) == this && playerEntity.distanceToSqr(this.worldPosition.getX(),this.worldPosition.getY() + 0.5, this.worldPosition.getZ()) <= 64;
    }

    @Override
    public void clearContent() {
        items.clear();
    }

    @Override
    public void load(BlockState state, CompoundNBT tags) {
        super.load(state, tags);
        this.items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
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
            if (side != null && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
                return this.itemHandler[0].cast();
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
            handler.invalidate();
        }
        energyHandler.invalidate();
    }
}
