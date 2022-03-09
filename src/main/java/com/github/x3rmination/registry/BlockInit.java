package com.github.x3rmination.registry;

import com.github.x3rmination.X3TECH;
import com.github.x3rmination.common.blocks.tile_entities.archive.combustion_generator.CombustionGeneratorBlock;
import com.github.x3rmination.common.blocks.tile_entities.archive.double_press.DoublePressBlock;
import com.github.x3rmination.common.blocks.tile_entities.archive.powered_pulverizer.PoweredPulverizerBlock;
import com.github.x3rmination.common.blocks.tile_entities.archive.single_press.SinglePressBlock;
import com.github.x3rmination.common.blocks.tile_entities.cables.power_cable.PowerCableBlock;
import com.github.x3rmination.common.blocks.tile_entities.powered_furnace.PoweredFurnaceBlock;
import com.github.x3rmination.common.blocks.tile_entities.test_machine.TestMachineBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockInit {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, X3TECH.MOD_ID);

    public static final RegistryObject<PoweredFurnaceBlock> POWERED_FURNACE = BLOCKS.register("powered_furnace",
            () -> new PoweredFurnaceBlock(AbstractBlock.Properties.of(Material.HEAVY_METAL)
                    .strength(4, 20)
                    .sound(SoundType.METAL)
                    .noOcclusion()));
    public static final RegistryObject<PoweredPulverizerBlock> POWERED_PULVERIZER = BLOCKS.register("powered_pulverizer",
            () -> new PoweredPulverizerBlock(AbstractBlock.Properties.of(Material.HEAVY_METAL)
                    .strength(4, 20)
                    .sound(SoundType.METAL)
                    .noOcclusion()));
    public static final RegistryObject<SinglePressBlock> SINGLE_PRESS = BLOCKS.register("single_press",
            () -> new SinglePressBlock(AbstractBlock.Properties.of(Material.HEAVY_METAL)
                    .strength(4, 20)
                    .sound(SoundType.METAL)
                    .noOcclusion()));
    public static final RegistryObject<DoublePressBlock> DOUBLE_PRESS = BLOCKS.register("double_press",
            () -> new DoublePressBlock(AbstractBlock.Properties.of(Material.HEAVY_METAL)
                    .strength(4, 20)
                    .sound(SoundType.METAL)
                    .noOcclusion()));
    public static final RegistryObject<CombustionGeneratorBlock> COMBUSTION_GENERATOR = BLOCKS.register("combustion_generator",
            () -> new CombustionGeneratorBlock(AbstractBlock.Properties.of(Material.HEAVY_METAL)
                    .strength(4, 20)
                    .sound(SoundType.METAL)
                    .noOcclusion()));
    public static final RegistryObject<Block> POWER_CABLE = BLOCKS.register("power_cable",
            () -> new PowerCableBlock(AbstractBlock.Properties.of(Material.METAL)
                    .strength(1, 10)
                    .sound(SoundType.METAL)
                    .noOcclusion()));

    public static final RegistryObject<Block> TEST_MACHINE = BLOCKS.register("test_block",
            () -> new TestMachineBlock(AbstractBlock.Properties.of(Material.METAL)
                    .strength(1, 10)
                    .sound(SoundType.METAL)));

    //Generic Blocks
    public static final RegistryObject<Block> MACHINE_FRAME = BLOCKS.register("machine_frame",
            () -> new Block(AbstractBlock.Properties.of(Material.HEAVY_METAL)
                    .strength(4, 20)
                    .sound(SoundType.METAL)));
    public static final RegistryObject<Block> ZINC_COPPER_POWER_CELL = BLOCKS.register("zinc_copper_power_cell",
            () -> new Block(AbstractBlock.Properties.of(Material.WOOD)
                    .noOcclusion()
                    .strength(4, 20)
                    .lightLevel(value -> 1)
                    .sound(SoundType.WOOD)));

    public static void renderTypeSet(final FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(ZINC_COPPER_POWER_CELL.get(), RenderType.cutout());

        // Fluids
        RenderTypeLookup.setRenderLayer(FluidInit.CREOSOTE_FLUID.get(), RenderType.translucent());
        RenderTypeLookup.setRenderLayer(FluidInit.CREOSOTE_BLOCK.get(), RenderType.translucent());
        RenderTypeLookup.setRenderLayer(FluidInit.CREOSOTE_FLOWING.get(), RenderType.translucent());
    }
}
