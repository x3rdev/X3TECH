package com.github.x3rmination.common.blocks.tile_entities.cables.power_cable;

import com.github.x3rmination.core.util.block.CustomBlockProperties;
import net.minecraft.block.Block;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.state.BooleanProperty;

public class PowerCableBlock extends Block implements IWaterLoggable {

    public static final BooleanProperty NORTH = CustomBlockProperties.NORTH;
    public static final BooleanProperty EAST = CustomBlockProperties.EAST;
    public static final BooleanProperty SOUTH = CustomBlockProperties.SOUTH;
    public static final BooleanProperty WEST = CustomBlockProperties.WEST;
    public static final BooleanProperty UP = CustomBlockProperties.UP;
    public static final BooleanProperty DOWN = CustomBlockProperties.DOWN;

    public PowerCableBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(NORTH, false).setValue(EAST, false).setValue(SOUTH, false).setValue(WEST, false).setValue(UP, false).setValue(DOWN, false));
    }
}
