package com.github.x3rmination.common.blocks.tile_entities.zinc_copper_power_cell;

import com.github.x3rmination.core.util.ModEnergyStorage;
import com.github.x3rmination.registry.TileEntityTypeInit;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIntArray;
import net.minecraftforge.common.util.LazyOptional;

public class ZCPCTileEntity extends TileEntity {

    private final ModEnergyStorage energyStorage;
    private final LazyOptional<ModEnergyStorage> energyHandler;

    private static final int MAX_REDSTONE_FLUX = 1000;
    private static final int MAX_THROUGH = 1000;
    private int energy = 0;

    public ZCPCTileEntity() {
        super(TileEntityTypeInit.ZINC_COPPER_POWER_CELL.get());
        this.energyStorage = new ModEnergyStorage(this, 0, MAX_REDSTONE_FLUX, MAX_THROUGH, true, true);
        this.energyHandler = LazyOptional.of(() -> this.energyStorage);
    }

    private final IIntArray fields = new IIntArray() {
        @Override
        public int get(int index) {
            switch(index) {
                case 0:
                    return energyStorage.getEnergyStored();
                case 1:
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
            return 2;
        }
    };
}
