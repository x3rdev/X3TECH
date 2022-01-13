package com.github.x3rmination.common.blocks.tile_entities.cables.item_cable;

import com.github.x3rmination.common.blocks.tile_entities.cables.power_cable.PowerCableBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraftforge.energy.CapabilityEnergy;

public class CableDebuggerItem extends Item {
    public CableDebuggerItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public ActionResultType useOn(ItemUseContext pContext) {
        if(pContext.getLevel().getBlockState(pContext.getClickedPos()).getBlock() instanceof PowerCableBlock) {
            BlockState cableBlockState = pContext.getLevel().getBlockState(pContext.getClickedPos());
            Block cableBlock = cableBlockState.getBlock();
            System.out.println("Has Tile Entity: "+cableBlock.hasTileEntity(cableBlockState));
            if(pContext.getLevel().getBlockEntity(pContext.getClickedPos()) != null) {
                System.out.println("Energy: " + pContext.getLevel().getBlockEntity(pContext.getClickedPos()).getCapability(CapabilityEnergy.ENERGY).orElse(null).getEnergyStored());
            }
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.FAIL;
    }
}
