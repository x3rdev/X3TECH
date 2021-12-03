package com.github.x3rmination.data.loot_table;

import com.github.x3rmination.core.material.materialinit.MBlockInit;
import com.github.x3rmination.core.material.materialutil.MaterialBase;
import com.github.x3rmination.core.material.materialutil.MaterialRegistry;
import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ModBlockLootTables extends net.minecraft.data.loot.BlockLootTables {
    @Override
    protected void addTables() {
        for (MaterialBase materialBase : MaterialRegistry.materialList) {
            dropSelf(MBlockInit.blockLibrary.get(materialBase.getName() + "_block"));
            if(materialBase.materialHasOre()) {
                dropSelf(MBlockInit.blockLibrary.get(materialBase.getName() + "_ore"));
            }
        }
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return Stream.concat(MBlockInit.MOREBLOCKS.getEntries().stream(), MBlockInit.MBLOCKS.getEntries().stream()).map(RegistryObject::get).collect(Collectors.toList());
    }
}
