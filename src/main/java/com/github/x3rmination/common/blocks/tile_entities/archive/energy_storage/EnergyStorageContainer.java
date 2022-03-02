package com.github.x3rmination.common.blocks.tile_entities.archive.energy_storage;

import com.github.x3rmination.registry.ContainerTypeInit;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;

public class EnergyStorageContainer extends Container {
    private final IInventory inventory;
    private IIntArray fields;
    protected int energy;
    protected int capacity;
    protected int maxReceive;
    protected int maxExtract;

    public EnergyStorageContainer(int id, PlayerInventory playerInventory, PacketBuffer buffer) {
        this(id, playerInventory, new EnergyStorageTileEntity(), new IntArray(buffer.readByte()));
    }

    public EnergyStorageContainer(int id, PlayerInventory playerInventory, IInventory inventory, IIntArray fields) {
        super(ContainerTypeInit.DOUBLE_PRESS.get(), id);
        this.inventory = inventory;
        this.fields = fields;

        this.energy = 0;
        this.capacity = 10000;
        this.maxReceive = 10000;
        this.maxExtract = 10000;

//        this.addSlot(new Slot(this.inventory, 0, 44, 27));
//        this.addSlot(new Slot(this.inventory, 1, 44, 47));
//        this.addSlot(new Slot(this.inventory, 2, 104, 36) {
//            @Override
//            public boolean mayPlace(ItemStack stack) {
//                return false;
//            }
//        });
//        this.addDataSlots(fields);

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

    @Override
    public boolean stillValid(PlayerEntity pPlayer) {
        return false;
    }
}
