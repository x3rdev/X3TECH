package com.github.x3rmination.common.blocks.tile_entities.powered_furnace;

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
import net.minecraft.state.IntegerProperty;
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
public class PoweredFurnaceBlock extends Block {

    public static final DirectionProperty FACING = HorizontalBlock.FACING;
    public static final BooleanProperty ACTIVE = CustomBlockProperties.ACTIVE;
    public static final IntegerProperty ITEM_NORTH = CustomBlockProperties.ITEM_NORTH;
    public static final IntegerProperty ITEM_EAST = CustomBlockProperties.ITEM_EAST;
    public static final IntegerProperty ITEM_SOUTH = CustomBlockProperties.ITEM_SOUTH;
    public static final IntegerProperty ITEM_WEST = CustomBlockProperties.ITEM_WEST;
    public static final IntegerProperty ITEM_UP = CustomBlockProperties.ITEM_UP;
    public static final IntegerProperty ITEM_DOWN = CustomBlockProperties.ITEM_DOWN;

    public PoweredFurnaceBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(ACTIVE, Boolean.FALSE)
                .setValue(ITEM_NORTH, 3)
                .setValue(ITEM_EAST, 3)
                .setValue(ITEM_SOUTH, 3)
                .setValue(ITEM_WEST, 3)
                .setValue(ITEM_UP, 3)
                .setValue(ITEM_DOWN, 3));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader blockReader) {
        return new PoweredFurnaceTileEntity();
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
        if(tileEntity instanceof PoweredFurnaceTileEntity && playerEntity instanceof ServerPlayerEntity) {
            PoweredFurnaceTileEntity pfe = (PoweredFurnaceTileEntity) tileEntity;
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
        stateBuilder.add(ITEM_NORTH);
        stateBuilder.add(ITEM_EAST);
        stateBuilder.add(ITEM_SOUTH);
        stateBuilder.add(ITEM_WEST);
        stateBuilder.add(ITEM_UP);
        stateBuilder.add(ITEM_DOWN);
    }
}
