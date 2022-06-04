package com.github.x3rmination.common.blocks.tile_entities.cables.power_cable;

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
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("deprecation")
public class PowerCableBlock extends Block{

    public static final BooleanProperty NORTH = CustomBlockProperties.NORTH;
    public static final BooleanProperty EAST = CustomBlockProperties.EAST;
    public static final BooleanProperty SOUTH = CustomBlockProperties.SOUTH;
    public static final BooleanProperty WEST = CustomBlockProperties.WEST;
    public static final BooleanProperty UP = CustomBlockProperties.UP;
    public static final BooleanProperty DOWN = CustomBlockProperties.DOWN;
    public static final BooleanProperty HAS_BRAIN = CustomBlockProperties.HAS_BRAIN;

    public PowerCableBlock(Properties properties) {
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
        return new PowerCableTileEntity();
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
        return (super.getStateForPlacement(context)).setValue(NORTH, canConnectTo(northBlockState, world, northBlockPos)).setValue(EAST, canConnectTo(eastBlockState, world, eastBlockPos)).setValue(SOUTH, canConnectTo(southBlockState, world, southBlockPos)).setValue(WEST, canConnectTo(westBlockState, world, westBlockPos)).setValue(UP, canConnectTo(upBlockState, world, upBlockPos)).setValue(DOWN, canConnectTo(downBlockState, world, downBlockPos)).setValue(HAS_BRAIN, !getNonCableConnections(context.getClickedPos(), context.getLevel()).isEmpty());
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
        return super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos).setValue(NORTH, canConnectTo(northBlockState, world, northBlockPos)).setValue(EAST, canConnectTo(eastBlockState, world, eastBlockPos)).setValue(SOUTH, canConnectTo(southBlockState, world, southBlockPos)).setValue(WEST, canConnectTo(westBlockState, world, westBlockPos)).setValue(UP, canConnectTo(upBlockState, world, upBlockPos)).setValue(DOWN, canConnectTo(downBlockState, world, downBlockPos)).setValue(HAS_BRAIN, !getNonCableConnections(pCurrentPos, world).isEmpty());
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
        VoxelShape box = Block.box(6,6,6, 10, 10, 10);
        VoxelShape box1 = VoxelShapes.empty();
        VoxelShape box2 = VoxelShapes.empty();
        VoxelShape box3 = VoxelShapes.empty();
        VoxelShape box4 = VoxelShapes.empty();
        VoxelShape box5 = VoxelShapes.empty();
        VoxelShape box6 = VoxelShapes.empty();
        if(Boolean.TRUE.equals(state.getValue(NORTH))) {
            box1 = Block.box(6,6,0,10,10,6);
        }
        if(Boolean.TRUE.equals(state.getValue(EAST))) {
            box2 = Block.box(10,6,6,16,10,10);
        }
        if(Boolean.TRUE.equals(state.getValue(SOUTH))) {
            box3 = Block.box(6,6,10,10,10,16);
        }
        if(Boolean.TRUE.equals(state.getValue(WEST))) {
            box4 = Block.box(0,6,6,6,10,10);
        }
        if(Boolean.TRUE.equals(state.getValue(UP))) {
            box5 = Block.box(6,10,6,10,16,10);
        }
        if(Boolean.TRUE.equals(state.getValue(DOWN))) {
            box6 = Block.box(6,0,6,10,6,10);
        }
        return VoxelShapes.or(box, box1, box2, box3, box4, box5, box6);
    }

    private boolean canConnectTo(BlockState blockState, World world, BlockPos pos) {
        Block block = blockState.getBlock();
        if(block.getBlock() instanceof PowerCableBlock) {
            return true;
        }
        if(!block.hasTileEntity(blockState)){
            return false;
        }
        TileEntity tileEntity = world.getBlockEntity(pos);
        if(tileEntity != null) {
            return tileEntity.getCapability(CapabilityEnergy.ENERGY).isPresent();
        }
        return false;
    }

    public List<BlockPos> getCableConnections(BlockPos wirePos, World world) {
        List<BlockPos> cableConnections = new java.util.ArrayList<>();
        if(world.getBlockState(wirePos.north()).getBlock() == this.getBlock()) {
            cableConnections.add(wirePos.north());
        }
        if(world.getBlockState(wirePos.east()).getBlock() == this.getBlock()) {
            cableConnections.add(wirePos.east());
        }
        if(world.getBlockState(wirePos.south()).getBlock() == this.getBlock()) {
            cableConnections.add(wirePos.south());
        }
        if(world.getBlockState(wirePos.west()).getBlock() == this.getBlock()) {
            cableConnections.add(wirePos.west());
        }
        if(world.getBlockState(wirePos.above()).getBlock() == this.getBlock()) {
            cableConnections.add(wirePos.above());
        }
        if(world.getBlockState(wirePos.below()).getBlock() == this.getBlock()) {
            cableConnections.add(wirePos.below());
        }
        return cableConnections;
    }

    public List<BlockPos> getNonCableConnections(BlockPos wirePos, World world) {
        List<BlockPos> nonCableConnections = new java.util.ArrayList<>(Collections.emptyList());
        TileEntity n = world.getBlockEntity(wirePos.north());
        TileEntity e = world.getBlockEntity(wirePos.east());
        TileEntity s = world.getBlockEntity(wirePos.south());
        TileEntity w = world.getBlockEntity(wirePos.west());
        TileEntity u = world.getBlockEntity(wirePos.above());
        TileEntity d = world.getBlockEntity(wirePos.below());

        if(n != null && n.getCapability(CapabilityEnergy.ENERGY).isPresent() && world.getBlockState(wirePos.north()).getBlock() != this.getBlock()) {
            nonCableConnections.add(wirePos.north());
        }
        if(e != null && e.getCapability(CapabilityEnergy.ENERGY).isPresent() && world.getBlockState(wirePos.east()).getBlock() != this.getBlock()) {
            nonCableConnections.add(wirePos.east());
        }
        if(s != null && s.getCapability(CapabilityEnergy.ENERGY).isPresent() && world.getBlockState(wirePos.south()).getBlock() != this.getBlock()) {
            nonCableConnections.add(wirePos.south());
        }
        if(w != null && w.getCapability(CapabilityEnergy.ENERGY).isPresent() && world.getBlockState(wirePos.west()).getBlock() != this.getBlock()) {
            nonCableConnections.add(wirePos.west());
        }
        if(u != null && u.getCapability(CapabilityEnergy.ENERGY).isPresent() && world.getBlockState(wirePos.above()).getBlock() != this.getBlock()) {
            nonCableConnections.add(wirePos.above());
        }
        if(d != null && d.getCapability(CapabilityEnergy.ENERGY).isPresent() && world.getBlockState(wirePos.below()).getBlock() != this.getBlock()) {
            nonCableConnections.add(wirePos.below());
        }

        return nonCableConnections;
    }

    public List<BlockPos> getNonCableConnectionsCanInput(BlockPos wirePos, World world) {
        List<BlockPos> nonCableConnections = new java.util.ArrayList<>(Collections.emptyList());
        TileEntity n = world.getBlockEntity(wirePos.north());
        TileEntity e = world.getBlockEntity(wirePos.east());
        TileEntity s = world.getBlockEntity(wirePos.south());
        TileEntity w = world.getBlockEntity(wirePos.west());
        TileEntity u = world.getBlockEntity(wirePos.above());
        TileEntity d = world.getBlockEntity(wirePos.below());
        if(n != null) {
            LazyOptional<IEnergyStorage> nCap = n.getCapability(CapabilityEnergy.ENERGY);
            if (nCap.isPresent() && world.getBlockState(wirePos.north()).getBlock() != this.getBlock() && nCap.orElse(null).canReceive() && nCap.orElse(null).getMaxEnergyStored() != nCap.orElse(null).getEnergyStored()) {
                nonCableConnections.add(wirePos.north());
            }
        }
        if(e != null) {
            LazyOptional<IEnergyStorage> eCap = e.getCapability(CapabilityEnergy.ENERGY);
            if (eCap.isPresent() && world.getBlockState(wirePos.east()).getBlock() != this.getBlock() && eCap.orElse(null).canReceive() && eCap.orElse(null).getMaxEnergyStored() != eCap.orElse(null).getEnergyStored()) {
                nonCableConnections.add(wirePos.east());
            }
        }
        if(s != null) {
            LazyOptional<IEnergyStorage> sCap = s.getCapability(CapabilityEnergy.ENERGY);
            if (sCap.isPresent() && world.getBlockState(wirePos.south()).getBlock() != this.getBlock() && sCap.orElse(null).canReceive() && sCap.orElse(null).getMaxEnergyStored() != sCap.orElse(null).getEnergyStored()) {
                nonCableConnections.add(wirePos.south());
            }
        }
        if(w != null) {
            LazyOptional<IEnergyStorage> wCap = w.getCapability(CapabilityEnergy.ENERGY);
            if (wCap.isPresent() && world.getBlockState(wirePos.west()).getBlock() != this.getBlock() && wCap.orElse(null).canReceive() && wCap.orElse(null).getMaxEnergyStored() != wCap.orElse(null).getEnergyStored()) {
                nonCableConnections.add(wirePos.west());
            }
        }
        if(u != null) {
            LazyOptional<IEnergyStorage> uCap = u.getCapability(CapabilityEnergy.ENERGY);
            if (uCap.isPresent() && world.getBlockState(wirePos.above()).getBlock() != this.getBlock() && uCap.orElse(null).canReceive() && uCap.orElse(null).getMaxEnergyStored() != uCap.orElse(null).getEnergyStored()) {
                nonCableConnections.add(wirePos.above());
            }
        }
        if(d != null) {
            LazyOptional<IEnergyStorage> dCap = d.getCapability(CapabilityEnergy.ENERGY);
            if (dCap.isPresent() && world.getBlockState(wirePos.below()).getBlock() != this.getBlock() && dCap.orElse(null).canReceive() && dCap.orElse(null).getMaxEnergyStored() != dCap.orElse(null).getEnergyStored()) {
                nonCableConnections.add(wirePos.below());
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
