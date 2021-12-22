package com.github.x3rmination.common.items;

import com.github.x3rmination.common.blocks.tile_entities.cables.power_cable.PowerCableBlock;
import com.github.x3rmination.common.blocks.tile_entities.cables.power_cable.PowerCableNetwork;
import com.github.x3rmination.registry.init.BlockInit;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;

public class PipeDebugger extends Item {
    public PipeDebugger(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public ActionResultType useOn(ItemUseContext pContext) {
        Block b = pContext.getLevel().getBlockState(pContext.getClickedPos()).getBlock();
        if(b.getBlock() == BlockInit.POWER_CABLE.get().getBlock()) {
            PowerCableNetwork p = ((PowerCableBlock)b).getPowerCableNetwork();
            if(p != null && !pContext.getLevel().isClientSide()) {
                System.out.println(String.valueOf(p));
                System.out.println("cableList: " +p.getCableList());
                System.out.println("importConnections: " + p.getImportConnections());
                System.out.println("outputConnections: " + p.getOutputConnections());
            }
        }
        return ActionResultType.SUCCESS;
    }
}
