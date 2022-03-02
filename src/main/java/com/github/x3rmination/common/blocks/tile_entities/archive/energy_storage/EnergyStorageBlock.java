package com.github.x3rmination.common.blocks.tile_entities.archive.energy_storage;

import net.minecraft.block.Block;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.state.DirectionProperty;
import net.minecraft.util.Direction;

public class EnergyStorageBlock extends Block {

    public static final DirectionProperty FACING = HorizontalBlock.FACING;

    public EnergyStorageBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }
}
