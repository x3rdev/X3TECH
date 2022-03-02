package com.github.x3rmination.common.blocks.tile_entities.archive.single_press;

import com.github.x3rmination.core.util.CustomBlockProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

@SuppressWarnings("deprecation")
public class SinglePressBlock extends Block {

    public static final DirectionProperty FACING = HorizontalBlock.FACING;
    public static final BooleanProperty ACTIVE = CustomBlockProperties.ACTIVE;

    public SinglePressBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(ACTIVE, Boolean.FALSE));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader blockReader) {
        return new SinglePressTileEntity();
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos blockPos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult blockRayTraceResult) {
        if(world.isClientSide){
            return ActionResultType.SUCCESS;
        }
        this.interactWith(world, blockPos, playerEntity);
        return ActionResultType.CONSUME;
    }

    private void interactWith(World world, BlockPos blockPos, PlayerEntity playerEntity){
        TileEntity tileEntity = world.getBlockEntity(blockPos);
        if(tileEntity instanceof SinglePressTileEntity && playerEntity instanceof ServerPlayerEntity) {
            SinglePressTileEntity pfe = (SinglePressTileEntity) tileEntity;
            NetworkHooks.openGui((ServerPlayerEntity) playerEntity, pfe, pfe::encodeExtraData);
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public void onRemove(BlockState blockState, World world, BlockPos blockPos, BlockState newState, boolean isMoving) {
        if(!blockState.is(newState.getBlock())){
            TileEntity tileEntity = world.getBlockEntity(blockPos);
            if(tileEntity instanceof IInventory){
                InventoryHelper.dropContents(world, blockPos, (IInventory) tileEntity);
                world.updateNeighbourForOutputSignal(blockPos, this);
            }
            super.onRemove(blockState, world, blockPos, newState, isMoving);
        }
    }

    @Override
    public BlockState rotate(BlockState blockState, Rotation rotation) {
        return blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.rotate(mirror.getRotation(blockState.getValue(FACING))).setValue(ACTIVE, false);
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(FACING);
        stateBuilder.add(ACTIVE);
    }
}
