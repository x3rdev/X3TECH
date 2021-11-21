package com.github.x3rmination.common.blocks.powered_furnace;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

@SuppressWarnings("deprecation")
public class PoweredFurnaceBlock extends Block {

    public static final DirectionProperty FACING = HorizontalBlock.FACING;

    public PoweredFurnaceBlock(Properties properties) {
        super(properties);
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
        return blockState.rotate(mirror.getRotation(blockState.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(FACING);
    }
}