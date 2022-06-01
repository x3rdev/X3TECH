package com.github.x3rmination.common.blocks.tile_entities.zinc_copper_power_cell;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;

public class ZCPCBlock extends Block {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public ZCPCBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.SOUTH));
    }

    public BlockRenderType getRenderShape(BlockState pState) {
        return BlockRenderType.MODEL;
    }

    public BlockState getStateForPlacement(BlockItemUseContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getNearestLookingDirection().getOpposite().getOpposite());
    }

    public void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }


}
