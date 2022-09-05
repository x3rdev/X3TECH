package com.github.x3rmination.common.blocks.tile_entities.archive.powered_furnace;

import com.github.x3rmination.core.util.ModEnergyStorage;
import com.github.x3rmination.registry.TileEntityTypeInit;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.util.ActionResultType;
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

public class PoweredFurnaceTileEntity extends LockableTileEntity implements ISidedInventory, ITickableTileEntity {

    static int processTime;

    private NonNullList<ItemStack> items;
    private final ModEnergyStorage poweredFurnaceEnergyStorage;

    private LazyOptional<? extends IItemHandler>[] itemHandler;
    private LazyOptional<ModEnergyStorage> energyHandler;

    private int progress = 0;
    private int energy = 0;
    private int item_north = 3;
    private int item_east = 3;
    private int item_south = 3;
    private int item_west = 3;
    private int item_up = 3;
    private int item_down = 3;
    private static final int MAX_REDSTONE_FLUX = 10000;
    private static final int INPUT_SLOT_INDEX = 0;
    private static final int OUTPUT_SLOT_INDEX = 1;

    int defaultUse = 100;

    private final IIntArray fields = new IIntArray() {
        @Override
        public int get(int index) {
            switch(index) {
                case 0:
                    return progress;
                case 1:
                    return poweredFurnaceEnergyStorage.getEnergyStored();
                case 2:
                    return poweredFurnaceEnergyStorage.getMaxEnergyStored();
                case 3:
                    return item_north;
                case 4:
                    return item_east;
                case 5:
                    return item_south;
                case 6:
                    return item_west;
                case 7:
                    return item_up;
                case 8:
                    return item_down;
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {
        }

        @Override
        public int getCount() {
            return 9;
        }
    };

    public PoweredFurnaceTileEntity() {
        super(TileEntityTypeInit.POWERED_FURNACE.get());
        this.itemHandler = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);
        this.items = NonNullList.withSize(2, ItemStack.EMPTY);
        this.poweredFurnaceEnergyStorage = new ModEnergyStorage(this, 0, MAX_REDSTONE_FLUX, 100000, false, true);
        this.energyHandler = LazyOptional.of(() -> this.poweredFurnaceEnergyStorage);
    }

    void encodeExtraData(PacketBuffer buffer) {
        buffer.writeByte(fields.getCount());
    }

    @Override
    public void tick() {
        if(this.level == null || this.level.isClientSide) {
            return;
        }
        FurnaceRecipe recipe = getRecipe();
        if(!getItem(OUTPUT_SLOT_INDEX).isEmpty()) {
            autoEject();
        }
        if(recipe != null && useEnergy(defaultUse * (recipe.getCookingTime()/10 + 5))) {
            doWork(recipe);
            this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(PoweredFurnaceBlock.ACTIVE, Boolean.TRUE), 3);
        } else {
            stopWork();
            this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(PoweredFurnaceBlock.ACTIVE, Boolean.FALSE), 3);
        }
    }

    @Nullable
    public FurnaceRecipe getRecipe() {
        if (this.level == null || getItem(INPUT_SLOT_INDEX).isEmpty()) {
            return null;
        }
        return this.level.getRecipeManager().getRecipeFor(IRecipeType.SMELTING, this, this.level).orElse(null);
    }

    private ItemStack getWorkOutput(@Nullable FurnaceRecipe recipe) {
        if (recipe != null) {
            return recipe.assemble(this);
        }
        return ItemStack.EMPTY;
    }

    private void doWork(FurnaceRecipe recipe) {
        assert this.level != null;
        ItemStack current = getItem(OUTPUT_SLOT_INDEX);
        ItemStack output = getWorkOutput(recipe);
        processTime = recipe.getCookingTime()/10 + 5;
        if(!current.isEmpty()) {
            int newCount = current.getCount() + output.getCount();
            if(!ItemStack.isSame(current, output) || newCount > output.getMaxStackSize()){
                stopWork();
                return;
            }
        }

        if(progress < processTime) {
            progress += 1;
        }

        if(progress >= processTime) {
            finishWork(recipe, current, output);
        }
    }

    private void autoEject(){
        if(item_north == 1 && eject(Direction.NORTH) == ActionResultType.SUCCESS) {
            return;
        }
        if(item_east == 1 && eject(Direction.EAST) == ActionResultType.SUCCESS) {
            return;
        }
        if(item_south == 1 && eject(Direction.SOUTH) == ActionResultType.SUCCESS) {
            return;
        }
        if(item_west == 1 && eject(Direction.WEST) == ActionResultType.SUCCESS) {
            return;
        }
        if(item_up == 1 && eject(Direction.UP) == ActionResultType.SUCCESS) {
            return;
        }
        if(item_down == 1 && eject(Direction.DOWN) == ActionResultType.SUCCESS) {
            return;
        }
    }

    private ActionResultType eject(Direction dir){
        IInventory inventory = checkForContainer(dir);
        if(inventory != null) {
            for (int i = 0; i < inventory.getContainerSize(); i++) {
                ItemStack targetedItemStack = inventory.getItem(i);
                ItemStack currentItemStack = this.getItem(OUTPUT_SLOT_INDEX);
                if(targetedItemStack.isEmpty()) {
                    inventory.setItem(i, currentItemStack.copy());
                    setItem(OUTPUT_SLOT_INDEX, ItemStack.EMPTY);
                    return ActionResultType.SUCCESS;
                } else if (targetedItemStack.getItem().equals(currentItemStack.getItem()) && targetedItemStack.getMaxStackSize() > targetedItemStack.getCount()) {
                    if(targetedItemStack.getCount() + currentItemStack.getCount() <= targetedItemStack.getMaxStackSize()) {
                        inventory.getItem(i).setCount(targetedItemStack.getCount() + currentItemStack.getCount());
                        setItem(OUTPUT_SLOT_INDEX, ItemStack.EMPTY);
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

    private IInventory checkForContainer(Direction side){
        assert this.level != null;
        if(this.level.getBlockEntity(this.getBlockPos().relative(side, 1)) instanceof IInventory) {
            return ((IInventory) this.level.getBlockEntity(this.getBlockPos().relative(side, 1)));
        }
        return null;
    }

    public int getProcessTime(){
        return processTime;
    }

    private void stopWork() {
        progress = 0;
    }

    private void finishWork(FurnaceRecipe recipe, ItemStack current, ItemStack output) {
        energy -= 10;
        if(!current.isEmpty()){
            current.grow(output.getCount());
        } else {
            setItem(OUTPUT_SLOT_INDEX, output);
        }
        progress = 0;
        this.removeItem(INPUT_SLOT_INDEX, 1);
    }

    private boolean useEnergy(int amount) {
        if(poweredFurnaceEnergyStorage.getEnergyStored() < amount){
            return false;
        } else {
            poweredFurnaceEnergyStorage.setEnergy(poweredFurnaceEnergyStorage.getEnergyStored() - amount);
            return true;
        }
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return new int[]{0, 1};
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStack, @Nullable Direction direction) {
        if(index == INPUT_SLOT_INDEX) {
            return this.canPlaceItem(index, itemStack);
        } else {
            return false;
        }
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return index == OUTPUT_SLOT_INDEX;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.x3tech.powered_furnace");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory inventory) {
        return new PoweredFurnaceContainer(id, inventory, this, this.fields);
    }

    @Override
    public int getContainerSize() {
        return 2;
    }

    @Override
    public boolean isEmpty() {
        return getItem(INPUT_SLOT_INDEX).isEmpty() && getItem(OUTPUT_SLOT_INDEX).isEmpty();
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

    public void setItemNorth(byte b){
        item_north = b;
    }

    public void setItemEast(byte b){
        item_east = b;
    }

    public void setItemSouth(byte b){
        item_south = b;
    }

    public void setItemWest(byte b){
        item_west = b;
    }

    public void setItemUp(byte b){
        item_up = b;
    }

    public void setItemDown(byte b){
        item_down = b;
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
        this.items = NonNullList.withSize(2, ItemStack.EMPTY);
        this.item_north = tags.getByte("item_north");
        this.item_east = tags.getByte("item_east");
        this.item_south = tags.getByte("item_south");
        this.item_west = tags.getByte("item_west");
        this.item_up = tags.getByte("item_up");
        this.item_down = tags.getByte("item_down");
        this.progress = tags.getInt("progress");
        ItemStackHelper.loadAllItems(tags, this.items);
        energyHandler.ifPresent(modEnergyStorage -> modEnergyStorage.deserializeNBT(tags.getCompound("energy")));
    }


    @Override
    public CompoundNBT save(CompoundNBT tags) {
        super.save(tags);
        ItemStackHelper.saveAllItems(tags, this.items);
        tags.putInt("progress", this.progress);
        tags.putInt("item_north", this.item_north);
        tags.putInt("item_east", this.item_east);
        tags.putInt("item_south", this.item_south);
        tags.putInt("item_west", this.item_west);
        tags.putInt("item_up", this.item_up);
        tags.putInt("item_down", this.item_down);
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
        tags.putInt("item_north", this.item_north);
        tags.putInt("item_east", this.item_east);
        tags.putInt("item_south", this.item_south);
        tags.putInt("item_west", this.item_west);
        tags.putInt("item_up", this.item_up);
        tags.putInt("item_down", this.item_down);
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
        energyHandler.invalidate();
        for (LazyOptional<? extends IItemHandler> handler : this.itemHandler) {
            handler.invalidate();
        }
    }
}
