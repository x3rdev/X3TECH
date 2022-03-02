package com.github.x3rmination.common.blocks.tile_entities.archive.single_press;

import com.github.x3rmination.common.crafting.recipe.SinglePressRecipe;
import com.github.x3rmination.core.util.ModEnergyStorage;
import com.github.x3rmination.registry.RecipesInit;
import com.github.x3rmination.registry.TileEntityTypeInit;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nullable;

public class SinglePressTileEntity extends LockableTileEntity implements ISidedInventory, ITickableTileEntity {
    static int processTime;

    private NonNullList<ItemStack> items;
    private final LazyOptional<? extends IItemHandler>[] itemHandler;


    private int progress = 0;
    private int energy = 0;
    private static final int MAX_REDSTONE_FLUX = 10000;

    int defaultUse = 250;

    private final ModEnergyStorage singlePressEnergyStorage;
    private final LazyOptional<ModEnergyStorage> energyHandler;

    private final IIntArray fields = new IIntArray() {
        @Override
        public int get(int index) {
            switch(index) {
                case 0:
                    return progress;
                case 1:
                    return singlePressEnergyStorage.getEnergyStored();
                case 2:
                    return singlePressEnergyStorage.getMaxEnergyStored();
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            // Nah
        }

        @Override
        public int getCount() {
            return 3;
        }
    };

    public SinglePressTileEntity() {
        super(TileEntityTypeInit.SINGLE_PRESS.get());
        this.itemHandler = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        this.singlePressEnergyStorage = new ModEnergyStorage(this, 0, MAX_REDSTONE_FLUX, 100000, false, true);
        this.energyHandler = LazyOptional.of(() -> this.singlePressEnergyStorage);
    }

    void encodeExtraData(PacketBuffer buffer) {
        buffer.writeByte(fields.getCount());
    }

    @Override
    public void tick() {
        if(this.level == null || this.level.isClientSide) {
            return;
        }
        SinglePressRecipe recipe = getRecipe();
        if(recipe != null && useEnergy(defaultUse)) {
            doWork(recipe);
            this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(SinglePressBlock.ACTIVE, Boolean.TRUE), 3);
        } else {
            stopWork();
            this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(SinglePressBlock.ACTIVE, Boolean.FALSE), 3);
        }
    }

    @Nullable
    public SinglePressRecipe getRecipe() {
        if (this.level == null || getItem(0).isEmpty()) {
            return null;
        }
        return this.level.getRecipeManager().getRecipeFor(RecipesInit.SINGLE_PRESSING, this, this.level).orElse(null);
    }

    private ItemStack getWorkOutput(@Nullable SinglePressRecipe recipe) {
        if (recipe != null) {
            return recipe.assemble(this);
        }
        return ItemStack.EMPTY;
    }

    private void doWork(SinglePressRecipe recipe) {
        assert this.level != null;
        ItemStack current = getItem(1);
        ItemStack output = recipe.getResultItem();
        processTime = recipe.getProcessTime()/10 + 5;
        if(!current.isEmpty()) {
            int newCount = current.getCount() + output.getCount();
            if(!ItemStack.isSame(current, output) || newCount > output.getMaxStackSize()){
                stopWork();
                return;
            }
        }
        if(recipe == null) {
            return;
        }

        if(progress < processTime) {
            progress += 1;
        }

        if(progress >= processTime) {
            finishWork(current, output);
        }
    }

    public int getProcessTime(){
        return processTime;
    }

    private void stopWork() {
        progress = 0;
    }

    private void finishWork(ItemStack current, ItemStack output) {
        if(!current.isEmpty()){
            current.grow(output.getCount());
        } else {
            setItem(1, output);
        }
        progress = 0;
        this.removeItem(0, 1);
    }

    private boolean useEnergy(int amount) {
        if(singlePressEnergyStorage.getEnergyStored() < amount){
            return false;
        } else {
            singlePressEnergyStorage.setEnergy(singlePressEnergyStorage.getEnergyStored() - amount);
            return true;
        }
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return new int[]{0, 1};
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
        return index == 1;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.x3tech.single_press");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory inventory) {
        return new SinglePressContainer(id, inventory, this, this.fields);
    }

    @Override
    public int getContainerSize() {
        return 2;
    }

    @Override
    public boolean isEmpty() {
        for(int i = 0; i <= this.getContainerSize(); i++) {
            if(getItem(i).isEmpty()){
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int index) {
        return items.get(index);
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
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
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
                if (side == Direction.UP) {
                    return this.itemHandler[0].cast();
                } else if (side == Direction.DOWN) {
                    return this.itemHandler[1].cast();
                } else {
                    return this.itemHandler[2].cast();
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
