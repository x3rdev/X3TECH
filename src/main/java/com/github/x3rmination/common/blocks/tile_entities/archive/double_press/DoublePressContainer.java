package com.github.x3rmination.common.blocks.tile_entities.archive.double_press;

import com.github.x3rmination.registry.ContainerTypeInit;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;

public class DoublePressContainer extends Container {
    private final IInventory inventory;
    private IIntArray fields;
    protected int energy;
    protected int capacity;
    protected int maxReceive;
    protected int maxExtract;

    public DoublePressContainer(int id, PlayerInventory playerInventory, PacketBuffer buffer) {
        this(id, playerInventory, new DoublePressTileEntity(), new IntArray(buffer.readByte()));
    }

    public DoublePressContainer(int id, PlayerInventory playerInventory, IInventory inventory, IIntArray fields) {
        super(ContainerTypeInit.DOUBLE_PRESS.get(), id);
        this.inventory = inventory;
        this.fields = fields;

        this.energy = 0;
        this.capacity = 10000;
        this.maxReceive = 10000;
        this.maxExtract = 0;

        this.addSlot(new Slot(this.inventory, 0, 44, 27));
        this.addSlot(new Slot(this.inventory, 1, 44, 47));
        this.addSlot(new Slot(this.inventory, 2, 104, 36) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });
        this.addDataSlots(fields);

        // Player Inventory
        for(int y = 0; y < 3; y++) {
            for(int x = 0; x < 9; x++) {
                int index = x + 9 + y * 9;
                int posX = 8 + x * 18;
                int posY = 84 + y * 18;
                this.addSlot(new Slot(playerInventory, index, posX, posY));
            }
        }
        for (int x = 0; x < 9; x++) {
            int index = x;
            int posX = 8 + x * 18;
            int posY = 142;
            this.addSlot(new Slot(playerInventory, index, posX, posY));
        }
    }

    public int getProgressArrowScale() {
        int progress = fields.get(0);
        if(progress > 0){
            return progress * 24 / new DoublePressTileEntity().getProcessTime();
        }
        return 0;
    }
    public int getRFMeterScale(){
        float s = this.getRf()*49/this.getMaxRf();
        return (49 - (Math.round(s)));
    }
    public int getRf(){
        return fields.get(1);
    }
    public int getMaxRf(){
        return fields.get(2);
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return this.inventory.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            final int inventorySize = 2;
            final int playerInventoryEnd = inventorySize + 27;
            final int playerHotbarEnd = playerInventoryEnd + 9;

            if (index == 1) {
                if (!this.moveItemStackTo(itemstack1, inventorySize, playerHotbarEnd, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            } else if (index != 0) {
                if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, inventorySize, playerHotbarEnd, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }
}
