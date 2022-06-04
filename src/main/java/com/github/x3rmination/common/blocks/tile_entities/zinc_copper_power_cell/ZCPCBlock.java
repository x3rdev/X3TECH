package com.github.x3rmination.common.blocks.tile_entities.zinc_copper_power_cell;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class ZCPCBlock extends Block {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public ZCPCBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.SOUTH));
    }


    public BlockState getStateForPlacement(BlockItemUseContext pContext) {
        return (super.getStateForPlacement(pContext)).setValue(FACING, pContext.getNearestLookingDirection().getOpposite());
    }


    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new ZCPCTileEntity();
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    public void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }


}
