package com.github.x3rmination.core.material.materialinit;

import com.github.x3rmination.X3TECH;
import com.github.x3rmination.common.blocks.MBlockBase;
import com.github.x3rmination.common.blocks.MOreBlockBase;
import com.github.x3rmination.core.material.materialutil.MaterialBase;
import com.github.x3rmination.core.material.materialutil.MaterialRegistry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public class MBlockInit {

    public static final DeferredRegister<Block> MBLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, X3TECH.MOD_ID);
    public static final DeferredRegister<Block> MOREBLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, X3TECH.MOD_ID);

    public static final Map<String, MBlockBase> blockLibrary = new HashMap<>();

    public void registerMItems() {

        for (MaterialBase materialBase : MaterialRegistry.materialList) {
            MBlockBase mBlockBase = new MBlockBase(AbstractBlock.Properties.of(Material.HEAVY_METAL).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops().strength(materialBase.getMiningTime(), materialBase.getExplosionResistance()).harvestLevel(materialBase.getMiningLevel()), materialBase.getColor());
            blockLibrary.put(materialBase.getName() + "_block", mBlockBase);
            MBLOCKS.register(materialBase.getName() + "_block",
                    () -> mBlockBase);
            if(materialBase.materialHasOre()) {
                MOreBlockBase mOreBlockBase = new MOreBlockBase(AbstractBlock.Properties.of(Material.STONE).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops().harvestLevel(materialBase.getMiningLevel()).strength(materialBase.getMiningTime()), materialBase.getColor());
                blockLibrary.put(materialBase.getName() + "_ore", mOreBlockBase);
                MOREBLOCKS.register(materialBase.getName() + "_ore",
                        () -> mOreBlockBase);
            }
        }
    }

    public static void renderTypeSet(final FMLClientSetupEvent event) {
        for (MaterialBase materialBase : MaterialRegistry.materialList) {
            if(materialBase.materialHasOre()) {
                RenderTypeLookup.setRenderLayer(MBlockInit.blockLibrary.get(materialBase.getName()+"_ore"), RenderType.cutout());
            }
        }
    }
}
