package com.github.x3rmination.init;

import com.github.x3rmination.X3TECH;
import com.github.x3rmination.common.blocks.MBlockBase;
import com.github.x3rmination.common.blocks.MOreBlockBase;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.awt.*;

public class BlockInit {

    private BlockInit(){
    }

    public static final DeferredRegister<Block> MATERIAL_BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, X3TECH.MOD_ID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, X3TECH.MOD_ID);

    public static final RegistryObject<Block> TEST_BLOCK = MATERIAL_BLOCKS.register("test_block",
            () -> new MBlockBase(AbstractBlock.Properties.of(Material.HEAVY_METAL).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops(), new Color(255, 0, 0)));
    public static final RegistryObject<Block> TEST_ORE = MATERIAL_BLOCKS.register("test_ore",
            () -> new MOreBlockBase(AbstractBlock.Properties.of(Material.STONE).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops().harvestLevel(1), new Color(100, 180, 20)));
}
