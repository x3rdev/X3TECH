package com.github.x3rmination.common.blocks.tile_entities;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;

public abstract class MachineContainerBase extends Container {

    public MachineContainerBase(int id, PlayerInventory playerInventory, PacketBuffer buffer, TileEntity tileEntity, ContainerType<?> containerType) {
        this(id, playerInventory, (IInventory) tileEntity, new IntArray(buffer.readByte()), containerType );
    }

    public MachineContainerBase(int id, PlayerInventory playerInventory, IInventory inventory, IIntArray fields, ContainerType<?> containerType) {
        super(containerType, id);

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
}
