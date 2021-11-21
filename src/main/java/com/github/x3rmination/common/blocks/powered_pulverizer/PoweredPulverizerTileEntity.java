package com.github.x3rmination.common.blocks.powered_pulverizer;

import com.github.x3rmination.registry.init.TileEntityTypeInit;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
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
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nullable;

public class PoweredPulverizerTileEntity extends LockableTileEntity implements ISidedInventory, ITickableTileEntity {

    static final int PROCESS_TIME = 10;

    private NonNullList<ItemStack> items;
    private final LazyOptional<? extends IItemHandler>[] handlers;

    private int progress = 0;

    private final IIntArray fields = new IIntArray() {
        @Override
        public int get(int index) {
            if (index == 0) {
                return progress;
            }
            return 0;
        }

        @Override
        public void set(int index, int value) {
            if (index == 0) {
                progress = value;
            }
        }

        @Override
        public int getCount() {
            return 1;
        }
    };

    public PoweredPulverizerTileEntity() {
        super(TileEntityTypeInit.POWERED_PULVERIZER.get());
        this.handlers = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);
        this.items = NonNullList.withSize(2, ItemStack.EMPTY);
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
        if(recipe != null) {
            doWork(recipe);
        } else {
            stopWork();
        }
    }

    @Nullable
    public FurnaceRecipe getRecipe() {
        if (this.level == null || getItem(0).isEmpty()) {
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

        ItemStack current = getItem(1);
        ItemStack output = getWorkOutput(recipe);

        if(!current.isEmpty()) {
            int newCount = current.getCount() + output.getCount();

            if(!ItemStack.isSame(current, output) || newCount > output.getMaxStackSize()){
                stopWork();
                return;
            }
        }

        if(progress < PROCESS_TIME) {
            progress += 1;
        }

        if(progress >= PROCESS_TIME) {
            finishWork(recipe, current, output);
        }
    }

    private void stopWork() {
        progress = 0;
    }

    private void finishWork(FurnaceRecipe recipe, ItemStack current, ItemStack output) {
        if(!current.isEmpty()){
            current.grow(output.getCount());
        } else {
            setItem(1, output);
        }

        progress = 0;
        this.removeItem(0, 1);
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return new int[]{0, 1};
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStack, @Nullable Direction direction) {
        return this.canPlaceItem(index, itemStack);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return index == 1;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.x3tech.powered_pulverizer");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory inventory) {
        return new PoweredPulverizerContainer(id, inventory, this, this.fields);
    }

    @Override
    public int getContainerSize() {
        return 2;
    }

    @Override
    public boolean isEmpty() {
        return getItem(0).isEmpty() && getItem(1).isEmpty();
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
        return
                this.level != null && this.level.getBlockEntity(this.worldPosition) == this && playerEntity.distanceToSqr(this.worldPosition.getX(),this.worldPosition.getY() + 0.5, this.worldPosition.getZ()) <= 64;
    }

    @Override
    public void clearContent() {
        items.clear();
    }

    @Override
    public void load(BlockState state, CompoundNBT tags) {
        super.load(state, tags);
        this.items = NonNullList.withSize(2, ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(tags, this.items);

        this.progress = tags.getInt("Progress");
    }

    @Override
    public CompoundNBT save(CompoundNBT tags) {
        super.save(tags);
        ItemStackHelper.saveAllItems(tags, this.items);
        tags.putInt("Progress", this.progress);
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
        tags.putInt("Progress", this.progress);
        return tags;
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if(!this.remove && side != null && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if(side == Direction.UP){
                return this.handlers[0].cast();
            } else if(side == Direction.DOWN) {
                return this.handlers[1].cast();
            } else {
                return this.handlers[2].cast();
            }
        } else {
            return super.getCapability(cap, side);
        }
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        for (LazyOptional<? extends IItemHandler> handler : this.handlers) {
            handler.invalidate();
        }
    }
}
