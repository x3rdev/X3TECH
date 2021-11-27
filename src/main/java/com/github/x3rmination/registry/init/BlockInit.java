package com.github.x3rmination.registry.init;

import com.github.x3rmination.common.blocks.tile_entities.double_press.DoublePressBlock;
import com.github.x3rmination.common.blocks.tile_entities.powered_furnace.PoweredFurnaceBlock;
import com.github.x3rmination.common.blocks.tile_entities.powered_pulverizer.PoweredPulverizerBlock;
import com.github.x3rmination.common.blocks.tile_entities.single_press.SinglePressBlock;
import com.github.x3rmination.registry.ModRegistration;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.RegistryObject;

public class BlockInit {

    public static final RegistryObject<PoweredFurnaceBlock> POWERED_FURNACE = ModRegistration.BLOCKS.register("powered_furnace",
            () -> new PoweredFurnaceBlock(AbstractBlock.Properties.of(Material.HEAVY_METAL)
                    .strength(4, 20)
                    .sound(SoundType.METAL)
                    .noOcclusion()));
    public static final RegistryObject<PoweredPulverizerBlock> POWERED_PULVERIZER = ModRegistration.BLOCKS.register("powered_pulverizer",
            () -> new PoweredPulverizerBlock(AbstractBlock.Properties.of(Material.HEAVY_METAL)
                    .strength(4, 20)
                    .sound(SoundType.METAL)
                    .noOcclusion()));
    public static final RegistryObject<SinglePressBlock> SINGLE_PRESS = ModRegistration.BLOCKS.register("single_press",
            () -> new SinglePressBlock(AbstractBlock.Properties.of(Material.HEAVY_METAL)
                    .strength(4, 20)
                    .sound(SoundType.METAL)
                    .noOcclusion()));
    public static final RegistryObject<DoublePressBlock> DOUBLE_PRESS = ModRegistration.BLOCKS.register("double_press",
            () -> new DoublePressBlock(AbstractBlock.Properties.of(Material.HEAVY_METAL)
                    .strength(4, 20)
                    .sound(SoundType.METAL)
                    .noOcclusion()));
    public static final RegistryObject<Block> MACHINE_FRAME = ModRegistration.BLOCKS.register("machine_frame",
            () -> new Block(AbstractBlock.Properties.of(Material.HEAVY_METAL)
                    .strength(4, 20)
                    .sound(SoundType.METAL)));
    public static void register() {}

}
