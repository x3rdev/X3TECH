package com.github.x3rmination.common.blocks.tile_entities.cables.base.fluid_pipe;

import com.github.x3rmination.core.util.CableHelper;
import com.github.x3rmination.core.util.CustomBlockProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("deprecation")
public class FluidPipeBlock extends Block {

    public static final BooleanProperty NORTH = CustomBlockProperties.NORTH;
    public static final BooleanProperty EAST = CustomBlockProperties.EAST;
    public static final BooleanProperty SOUTH = CustomBlockProperties.SOUTH;
    public static final BooleanProperty WEST = CustomBlockProperties.WEST;
    public static final BooleanProperty UP = CustomBlockProperties.UP;
    public static final BooleanProperty DOWN = CustomBlockProperties.DOWN;
    public static final BooleanProperty HAS_BRAIN = CustomBlockProperties.HAS_BRAIN;

    public FluidPipeBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(NORTH, false).setValue(EAST, false).setValue(SOUTH, false).setValue(WEST, false).setValue(UP, false).setValue(DOWN, false).setValue(HAS_BRAIN, false));
    }

    @Override
    public void onNeighborChange(BlockState state, IWorldReader level, BlockPos pos, BlockPos neighborPos) {
        hasTileEntity(state);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new FluidPipeTileEntity();
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockPos blockPos = context.getClickedPos();
        World world = context.getLevel();
        BlockPos northBlockPos = blockPos.north();
        BlockPos eastBlockPos = blockPos.east();
        BlockPos southBlockPos = blockPos.south();
        BlockPos westBlockPos = blockPos.west();
        BlockPos upBlockPos = blockPos.above();
        BlockPos downBlockPos = blockPos.below();
        BlockState northBlockState = world.getBlockState(northBlockPos);
        BlockState eastBlockState = world.getBlockState(eastBlockPos);
        BlockState southBlockState = world.getBlockState(southBlockPos);
        BlockState westBlockState = world.getBlockState(westBlockPos);
        BlockState upBlockState = world.getBlockState(upBlockPos);
        BlockState downBlockState = world.getBlockState(downBlockPos);
        return (super.getStateForPlacement(context)).setValue(NORTH, canConnectTo(northBlockState, world, northBlockPos, Direction.NORTH)).setValue(EAST, canConnectTo(eastBlockState, world, eastBlockPos, Direction.EAST)).setValue(SOUTH, canConnectTo(southBlockState, world, southBlockPos, Direction.SOUTH)).setValue(WEST, canConnectTo(westBlockState, world, westBlockPos, Direction.WEST)).setValue(UP, canConnectTo(upBlockState, world, upBlockPos, Direction.UP)).setValue(DOWN, canConnectTo(downBlockState, world, downBlockPos, Direction.DOWN)).setValue(HAS_BRAIN, !getNonCableConnections(context.getClickedPos(), context.getLevel()).isEmpty());
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, IWorld pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        World world = (World) pLevel;
        BlockPos northBlockPos = pCurrentPos.north();
        BlockPos eastBlockPos = pCurrentPos.east();
        BlockPos southBlockPos = pCurrentPos.south();
        BlockPos westBlockPos = pCurrentPos.west();
        BlockPos upBlockPos = pCurrentPos.above();
        BlockPos downBlockPos = pCurrentPos.below();
        BlockState northBlockState = world.getBlockState(northBlockPos);
        BlockState eastBlockState = world.getBlockState(eastBlockPos);
        BlockState southBlockState = world.getBlockState(southBlockPos);
        BlockState westBlockState = world.getBlockState(westBlockPos);
        BlockState upBlockState = world.getBlockState(upBlockPos);
        BlockState downBlockState = world.getBlockState(downBlockPos);
        return super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos).setValue(NORTH, canConnectTo(northBlockState, world, northBlockPos, Direction.NORTH)).setValue(EAST, canConnectTo(eastBlockState, world, eastBlockPos, Direction.EAST)).setValue(SOUTH, canConnectTo(southBlockState, world, southBlockPos, Direction.SOUTH)).setValue(WEST, canConnectTo(westBlockState, world, westBlockPos, Direction.WEST)).setValue(UP, canConnectTo(upBlockState, world, upBlockPos, Direction.UP)).setValue(DOWN, canConnectTo(downBlockState, world, downBlockPos, Direction.DOWN)).setValue(HAS_BRAIN, !getNonCableConnections(pCurrentPos, world).isEmpty());
    }

    @Override
    public VoxelShape getCollisionShape(BlockState pState, IBlockReader pLevel, BlockPos pPos, ISelectionContext pContext) {
        return constructVoxelShape(pState);
    }

    @Override
    public VoxelShape getShape(BlockState pState, IBlockReader pLevel, BlockPos pPos, ISelectionContext pContext) {
        return constructVoxelShape(pState);
    }

    private VoxelShape constructVoxelShape(BlockState state) {
        VoxelShape box = Block.box(5.5,5.5,5.5, 10.5, 10.5, 10.5);
        VoxelShape box1 = VoxelShapes.empty();
        VoxelShape box2 = VoxelShapes.empty();
        VoxelShape box3 = VoxelShapes.empty();
        VoxelShape box4 = VoxelShapes.empty();
        VoxelShape box5 = VoxelShapes.empty();
        VoxelShape box6 = VoxelShapes.empty();
        if(Boolean.TRUE.equals(state.getValue(NORTH))) {
            box1 = Block.box(5.5,5.5,5.5,10.5,10.5,5.5);
        }
        if(Boolean.TRUE.equals(state.getValue(EAST))) {
            box2 = Block.box(10.5,5.5,5.5,16,10.5,10.5);
        }
        if(Boolean.TRUE.equals(state.getValue(SOUTH))) {
            box3 = Block.box(5.5,5.5,10.5,10.5,10.5,16);
        }
        if(Boolean.TRUE.equals(state.getValue(WEST))) {
            box4 = Block.box(0,5.5,5.5,5.5,10.5,10.5);
        }
        if(Boolean.TRUE.equals(state.getValue(UP))) {
            box5 = Block.box(5.5,10.5,5.5,10.5,16,10.5);
        }
        if(Boolean.TRUE.equals(state.getValue(DOWN))) {
            box6 = Block.box(5.5,5.5,5.5,10.5,5.5,10.5);
        }
        return VoxelShapes.or(box, box1, box2, box3, box4, box5, box6);
    }

    private boolean canConnectTo(BlockState blockState, World world, BlockPos pos, Direction direction) {
        Block block = blockState.getBlock();
        if(block.getBlock() instanceof FluidPipeBlock) {
            return true;
        }
        if(!block.hasTileEntity(blockState)){
            return false;
        }
        TileEntity tileEntity = world.getBlockEntity(pos);
        if(tileEntity != null) {
            return tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, direction.getOpposite()).isPresent();
        }
        return false;
    }

    public List<BlockPos> getCableConnections(BlockPos wirePos, World world) {
        List<BlockPos> cableConnections = new java.util.ArrayList<>();
        for(Direction direction : CableHelper.getDirectionList()) {
            if(world.getBlockState(wirePos.relative(direction, 1)).getBlock() instanceof FluidPipeBlock) {
                cableConnections.add(wirePos.relative(direction, 1));
            }
        }
        return cableConnections;
    }

    public List<BlockPos> getNonCableConnections(BlockPos wirePos, World world) {
        List<BlockPos> nonCableConnections = new java.util.ArrayList<>(Collections.emptyList());
        for(Direction direction : CableHelper.getDirectionList()) {
            TileEntity tile = world.getBlockEntity(wirePos.relative(direction, 1));
            if(tile != null && tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).isPresent() && !(world.getBlockState(wirePos.relative(direction, 1)).getBlock() instanceof FluidPipeBlock)) {
                nonCableConnections.add(wirePos.relative(direction, 1));
            }
        }

        return nonCableConnections;
    }

    public List<BlockPos> getNonCableConnectionsCanInput(BlockPos wirePos, World world) {
        List<BlockPos> nonCableConnections = new java.util.ArrayList<>(Collections.emptyList());
        for(Direction direction : CableHelper.getDirectionList()) {
            TileEntity tile = world.getBlockEntity(wirePos.relative(direction, 1));
            if(tile != null) {
                LazyOptional<IFluidHandler> cap = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
                if (cap.isPresent() && !(world.getBlockState(wirePos.relative(direction, 1)).getBlock() instanceof FluidPipeBlock)) {
                    nonCableConnections.add(wirePos.relative(direction, 1));
                }
            }
        }

        return nonCableConnections;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return state.getValue(HAS_BRAIN);
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(NORTH);
        stateBuilder.add(EAST);
        stateBuilder.add(SOUTH);
        stateBuilder.add(WEST);
        stateBuilder.add(UP);
        stateBuilder.add(DOWN);
        stateBuilder.add(HAS_BRAIN);
    }


}
