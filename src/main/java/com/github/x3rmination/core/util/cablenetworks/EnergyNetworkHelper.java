package com.github.x3rmination.core.util.cablenetworks;

import com.github.x3rmination.X3TECH;
import com.github.x3rmination.common.blocks.tile_entities.cables.power_cable.PowerCableBlock;
import com.github.x3rmination.common.blocks.tile_entities.cables.power_cable.PowerCableNetwork;
import com.github.x3rmination.common.blocks.tile_entities.cables.power_cable.PowerCableTileEntity;
import com.github.x3rmination.registry.init.BlockInit;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid= X3TECH.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EnergyNetworkHelper {

    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load event) {
        System.out.println("Doing cable network setup");
        World world = (World) event.getWorld();
        syncEnergyNetwork(world, getTileEntityCables(world));
        System.out.println("Finished network setup");
    }

    private static List<PowerCableTileEntity> getTileEntityCables(World world) {
        List<TileEntity> blockEntityList = world.blockEntityList;
        List<PowerCableTileEntity> cableList = new ArrayList<>();
        for (TileEntity tileEntity : blockEntityList) {
            if (tileEntity instanceof PowerCableTileEntity) {
                cableList.add((PowerCableTileEntity) tileEntity);
            }
        }
        return cableList;
    }

    private static void syncEnergyNetwork(World world, List<PowerCableTileEntity> cableList) {
        List<BlockPos> networkedCables = new ArrayList<>();
        for (PowerCableTileEntity powerCableTileEntity : cableList) {
            if(!networkedCables.contains(powerCableTileEntity.getBlockPos())) {
                List<BlockPos> networkPosList = pathFind(world, powerCableTileEntity.getBlockPos(), null);
                PowerCableNetwork net = new PowerCableNetwork();
                for(BlockPos pos : networkPosList) {
                    System.out.println("syncEnergyNetworks");
                    ((PowerCableBlock) world.getBlockState(pos).getBlock()).setPowerCableNetwork(net);
                }
                networkedCables.addAll(networkPosList);
            }
        }
//        return networkedCables;
    }

    private static List<BlockPos> pathFind(World world, BlockPos pos, Direction previousCableDirection) {
        List<BlockPos> allCableList = new ArrayList<>();
        if(world.getBlockState(pos.north()).getBlock().is(BlockInit.POWER_CABLE.get())) {
            if(previousCableDirection != Direction.NORTH.getOpposite()) {
                allCableList.addAll(pathFind(world, pos.north(), Direction.NORTH));
            }
        }
        if(world.getBlockState(pos.east()).getBlock().is(BlockInit.POWER_CABLE.get())) {
            if(previousCableDirection != Direction.EAST.getOpposite()) {
                allCableList.addAll(pathFind(world, pos.north(), Direction.EAST));
            }
        }
        if(world.getBlockState(pos.south()).getBlock().is(BlockInit.POWER_CABLE.get())) {
            if(previousCableDirection != Direction.SOUTH.getOpposite()) {
                allCableList.addAll(pathFind(world, pos.north(), Direction.SOUTH));
            }
        }
        if(world.getBlockState(pos.west()).getBlock().is(BlockInit.POWER_CABLE.get())) {
            if(previousCableDirection != Direction.WEST.getOpposite()) {
                allCableList.addAll(pathFind(world, pos.north(), Direction.WEST));
            }
        }
        if(world.getBlockState(pos.above()).getBlock().is(BlockInit.POWER_CABLE.get())) {
            if(previousCableDirection != Direction.UP.getOpposite()) {
                allCableList.addAll(pathFind(world, pos.north(), Direction.UP));
            }
        }
        if(world.getBlockState(pos.below()).getBlock().is(BlockInit.POWER_CABLE.get())) {
            if(previousCableDirection != Direction.DOWN.getOpposite()) {
                allCableList.addAll(pathFind(world, pos.north(), Direction.DOWN));
            }
        }
        allCableList.add(pos);
        return allCableList;
    }
}
