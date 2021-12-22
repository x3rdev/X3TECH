package com.github.x3rmination.core.util.cablenetworks;

import com.github.x3rmination.common.blocks.tile_entities.cables.power_cable.PowerCableBlock;
import com.github.x3rmination.common.blocks.tile_entities.cables.power_cable.PowerCableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class EnergyNetworkHelper {

    public void syncEnergyNetwork(World world) {
        List<PowerCableTileEntity> cableList = getTileEntityCables(world);
        ((PowerCableBlock) cableList.get(0).getBlockState().getBlock()).getPowerCableNetwork();
    }

    private List<PowerCableTileEntity> getTileEntityCables(World world) {
        List<TileEntity> blockEntityList = world.blockEntityList;
        List<PowerCableTileEntity> cableList = new ArrayList<>();
        for (TileEntity tileEntity : blockEntityList) {
            if (tileEntity instanceof PowerCableTileEntity) {
                cableList.add((PowerCableTileEntity) tileEntity);
            }
        }
        return cableList;
    }


}
