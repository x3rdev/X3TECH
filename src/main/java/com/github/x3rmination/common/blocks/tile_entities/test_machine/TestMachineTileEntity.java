package com.github.x3rmination.common.blocks.tile_entities.test_machine;

import com.github.x3rmination.common.blocks.tile_entities.MachineTileEntityBase;
import com.github.x3rmination.registry.TileEntityTypeInit;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public class TestMachineTileEntity extends MachineTileEntityBase {

    public TestMachineTileEntity() {
        super(TileEntityTypeInit.TEST_MACHINE.get(),
                2,
                new int[0],
                new int[1],
                1,
                100000,
                2300,
                true,
                true);
    }

    private final IIntArray fields = new IIntArray() {
        @Override
        public int get(int index) {
            switch(index) {
                case 0:
                    return progress;
                case 1:
                    return energyStorage.getEnergyStored();
                case 2:
                    return energyStorage.getMaxEnergyStored();
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

    @Override
    public void encodeExtraData(PacketBuffer buffer) {
        buffer.writeByte(fields.getCount());
    }

    @Override
    public boolean canPlaceItemThroughFace(int pIndex, ItemStack pItemStack, @Nullable Direction pDirection) {
        return true;
    }

    @Override
    public boolean canTakeItemThroughFace(int pIndex, ItemStack pStack, Direction pDirection) {
        return true;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.x3tech.test_machine");
    }

    @Override
    protected Container createMenu(int pId, PlayerInventory pPlayer) {
        return null;
    }

    @Override
    public void tick() {

    }
}
