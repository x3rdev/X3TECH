package com.github.x3rmination.common.blocks.tile_entities.cables.power_cable;

import com.github.x3rmination.core.util.block.CustomBlockProperties;
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
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("deprecation")
public class PowerCableBlock extends Block{

    public static final BooleanProperty NORTH = CustomBlockProperties.NORTH;
    public static final BooleanProperty EAST = CustomBlockProperties.EAST;
    public static final BooleanProperty SOUTH = CustomBlockProperties.SOUTH;
    public static final BooleanProperty WEST = CustomBlockProperties.WEST;
    public static final BooleanProperty UP = CustomBlockProperties.UP;
    public static final BooleanProperty DOWN = CustomBlockProperties.DOWN;

    //Doesn't necessarily have to be in minecraft's format
    public static final BooleanProperty HAS_BRAIN = CustomBlockProperties.HAS_BRAIN;

    private static PowerCableNetwork powerCableNetwork = null;

    public PowerCableBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(NORTH, false).setValue(EAST, false).setValue(SOUTH, false).setValue(WEST, false).setValue(UP, false).setValue(DOWN, false).setValue(HAS_BRAIN, false));
    }

    @Override
    public void onPlace(BlockState state, World world, BlockPos pos, BlockState pOldState, boolean pIsMoving) {
        List<BlockPos> l = getCableConnections(pos, world);
        if(l.isEmpty()) {
            powerCableNetwork = new PowerCableNetwork();
            powerCableNetwork.addCable(pos);
        } else {
            joinNetworks(l, world);
        }
        if(canConnectTo(world.getBlockState(pos.north()), world, pos.north()) && world.getBlockState(pos.north()).getBlock() != this) {
            if(world.getBlockEntity(pos.north()).getCapability(CapabilityEnergy.ENERGY).orElse(null).canReceive()) {
                powerCableNetwork.addOutputConnection(pos.north());
            }
            if(world.getBlockEntity(pos.north()).getCapability(CapabilityEnergy.ENERGY).orElse(null).canExtract()) {
                powerCableNetwork.addImportConnection(pos.north());
            }
        }
        if(canConnectTo(world.getBlockState(pos.east()), world, pos.east()) && world.getBlockState(pos.east()).getBlock() != this) {
            if(world.getBlockEntity(pos.east()).getCapability(CapabilityEnergy.ENERGY).orElse(null).canReceive()) {
                powerCableNetwork.addOutputConnection(pos.east());
            }
            if(world.getBlockEntity(pos.east()).getCapability(CapabilityEnergy.ENERGY).orElse(null).canExtract()) {
                powerCableNetwork.addImportConnection(pos.east());
            }
        }
        if(canConnectTo(world.getBlockState(pos.south()), world, pos.south()) && world.getBlockState(pos.south()).getBlock() != this) {
            if(world.getBlockEntity(pos.south()).getCapability(CapabilityEnergy.ENERGY).orElse(null).canReceive()) {
                powerCableNetwork.addOutputConnection(pos.south());
            }
            if(world.getBlockEntity(pos.south()).getCapability(CapabilityEnergy.ENERGY).orElse(null).canExtract()) {
                powerCableNetwork.addImportConnection(pos.south());
            }
        }
        if(canConnectTo(world.getBlockState(pos.west()), world, pos.west()) && world.getBlockState(pos.west()).getBlock() != this) {
            if(world.getBlockEntity(pos.west()).getCapability(CapabilityEnergy.ENERGY).orElse(null).canReceive()) {
                powerCableNetwork.addOutputConnection(pos.west());
            }
            if(world.getBlockEntity(pos.west()).getCapability(CapabilityEnergy.ENERGY).orElse(null).canExtract()) {
                powerCableNetwork.addImportConnection(pos.west());
            }
        }
        if(canConnectTo(world.getBlockState(pos.above()), world, pos.above()) && world.getBlockState(pos.above()).getBlock() != this) {
            if(world.getBlockEntity(pos.above()).getCapability(CapabilityEnergy.ENERGY).orElse(null).canReceive()) {
                powerCableNetwork.addOutputConnection(pos.above());
            }
            if(world.getBlockEntity(pos.above()).getCapability(CapabilityEnergy.ENERGY).orElse(null).canExtract()) {
                powerCableNetwork.addImportConnection(pos.above());
            }
        }
        if(canConnectTo(world.getBlockState(pos.below()), world, pos.below()) && world.getBlockState(pos.below()).getBlock() != this) {
            if(world.getBlockEntity(pos.below()).getCapability(CapabilityEnergy.ENERGY).orElse(null).canReceive()) {
                powerCableNetwork.addOutputConnection(pos.below());
            }
            if(world.getBlockEntity(pos.below()).getCapability(CapabilityEnergy.ENERGY).orElse(null).canExtract()) {
                powerCableNetwork.addImportConnection(pos.below());
            }
        }
    }

    @Override
    public void onRemove(BlockState pState, World pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        powerCableNetwork = null;
    }

    @Override
    public void onNeighborChange(BlockState state, IWorldReader level, BlockPos pos, BlockPos neighborPos) {
        World world = (World) level;
        BlockState neighborState = world.getBlockState(neighborPos);
        if(neighborState.hasTileEntity() && neighborState.getBlock() != this.getBlock() && Objects.requireNonNull(world.getBlockEntity(neighborPos)).getCapability(CapabilityEnergy.ENERGY).isPresent()) {
            TileEntity neighborTile = world.getBlockEntity(neighborPos);
            if(neighborTile.getCapability(CapabilityEnergy.ENERGY).orElse(null).canExtract() && !this.getPowerCableNetwork().getImportConnections().contains(neighborPos)) {
                this.getPowerCableNetwork().addImportConnection(neighborPos);
            }
            if(neighborTile.getCapability(CapabilityEnergy.ENERGY).orElse(null).canReceive() && !this.getPowerCableNetwork().getOutputConnections().contains(neighborPos)) {
                this.getPowerCableNetwork().addOutputConnection(neighborPos);
            }
        }
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
        return (Objects.requireNonNull(super.getStateForPlacement(context))).setValue(NORTH, canConnectTo(northBlockState, world, northBlockPos)).setValue(EAST, canConnectTo(eastBlockState, world, eastBlockPos)).setValue(SOUTH, canConnectTo(southBlockState, world, southBlockPos)).setValue(WEST, canConnectTo(westBlockState, world, westBlockPos)).setValue(UP, canConnectTo(upBlockState, world, upBlockPos)).setValue(DOWN, canConnectTo(downBlockState, world, downBlockPos)).setValue(HAS_BRAIN, getCableConnections(context.getClickedPos(), context.getLevel()).isEmpty() && !getNonCableConnections(context.getClickedPos(), context.getLevel()).isEmpty());
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
        return super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos).setValue(NORTH, canConnectTo(northBlockState, world, northBlockPos)).setValue(EAST, canConnectTo(eastBlockState, world, eastBlockPos)).setValue(SOUTH, canConnectTo(southBlockState, world, southBlockPos)).setValue(WEST, canConnectTo(westBlockState, world, westBlockPos)).setValue(UP, canConnectTo(upBlockState, world, upBlockPos)).setValue(DOWN, canConnectTo(downBlockState, world, downBlockPos)).setValue(HAS_BRAIN, getCableConnections(pCurrentPos, world).isEmpty() && !getNonCableConnections(pCurrentPos, world).isEmpty());
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
        assert tileEntity != null;
        return tileEntity.getCapability(CapabilityEnergy.ENERGY).isPresent();
    }

    private List<BlockPos> getConnections(BlockPos wirePos, BlockState wireState) {
        List<BlockPos> connections = new java.util.ArrayList<>();
        if(Boolean.TRUE.equals(wireState.getValue(NORTH))) {
            connections.add(wirePos.north());
        }
        if(Boolean.TRUE.equals(wireState.getValue(EAST))) {
            connections.add(wirePos.east());
        }
        if(Boolean.TRUE.equals(wireState.getValue(SOUTH))) {
            connections.add(wirePos.south());
        }
        if(Boolean.TRUE.equals(wireState.getValue(WEST))) {
            connections.add(wirePos.west());
        }
        if(Boolean.TRUE.equals(wireState.getValue(UP))) {
            connections.add(wirePos.above());
        }
        if(Boolean.TRUE.equals(wireState.getValue(DOWN))) {
            connections.add(wirePos.below());
        }
        return connections;
    }

    private List<BlockPos> getCableConnections(BlockPos wirePos, World world) {
        List<BlockPos> cableConnections = new java.util.ArrayList<>();
        TileEntity n = world.getBlockEntity(wirePos.north());
        TileEntity e = world.getBlockEntity(wirePos.east());
        TileEntity s = world.getBlockEntity(wirePos.south());
        TileEntity w = world.getBlockEntity(wirePos.west());
        TileEntity u = world.getBlockEntity(wirePos.above());
        TileEntity d = world.getBlockEntity(wirePos.below());

        if(n != null && n.getCapability(CapabilityEnergy.ENERGY).isPresent() && world.getBlockState(wirePos.north()).getBlock() == this.getBlock()) {
            cableConnections.add(wirePos.north());
        }
        if(e != null && e.getCapability(CapabilityEnergy.ENERGY).isPresent() && world.getBlockState(wirePos.east()).getBlock() == this.getBlock()) {
            cableConnections.add(wirePos.east());
        }
        if(s != null && s.getCapability(CapabilityEnergy.ENERGY).isPresent() && world.getBlockState(wirePos.south()).getBlock() == this.getBlock()) {
            cableConnections.add(wirePos.south());
        }
        if(w != null && w.getCapability(CapabilityEnergy.ENERGY).isPresent() && world.getBlockState(wirePos.west()).getBlock() == this.getBlock()) {
            cableConnections.add(wirePos.west());
        }
        if(u != null && u.getCapability(CapabilityEnergy.ENERGY).isPresent() && world.getBlockState(wirePos.above()).getBlock() == this.getBlock()) {
            cableConnections.add(wirePos.above());
        }
        if(d != null && d.getCapability(CapabilityEnergy.ENERGY).isPresent() && world.getBlockState(wirePos.below()).getBlock() == this.getBlock()) {
            cableConnections.add(wirePos.below());
        }
        return cableConnections;
    }

    private List<BlockPos> getNonCableConnections(BlockPos wirePos, World world) {
        List<BlockPos> nonCableConnections = new java.util.ArrayList<>();
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

    public PowerCableNetwork getPowerCableNetwork() {
        return powerCableNetwork;
    }

    public void setPowerCableNetwork(PowerCableNetwork network) {
        powerCableNetwork = network;
    }

    private void joinNetworks(List<BlockPos> cableConnectionsList, World world) {
        List<BlockPos> l = cableConnectionsList;
        PowerCableBlock p0;
        PowerCableBlock p1;
        while(l.size() > 1) {
            p0 = ((PowerCableBlock) world.getBlockState(l.get(0)).getBlock());
            p1 = ((PowerCableBlock) world.getBlockState(l.get(1)).getBlock());
            if(p0.getPowerCableNetwork().getNetworkSize() > p1.getPowerCableNetwork().getNetworkSize()) {
                l.remove(1);
            }
            if(p0.getPowerCableNetwork().getNetworkSize() < p1.getPowerCableNetwork().getNetworkSize()) {
                l.remove(0);
            } else {
                p0.getPowerCableNetwork().mergeNetworks(p1.getPowerCableNetwork(), world);
                l.remove(1);
            }
        }
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
