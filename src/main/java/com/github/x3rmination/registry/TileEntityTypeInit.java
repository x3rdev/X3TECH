package com.github.x3rmination.registry;

import com.github.x3rmination.X3TECH;
import com.github.x3rmination.common.blocks.tile_entities.archive.combustion_generator.CombustionGeneratorTileEntity;
import com.github.x3rmination.common.blocks.tile_entities.archive.double_press.DoublePressTileEntity;
import com.github.x3rmination.common.blocks.tile_entities.archive.powered_furnace.PoweredFurnaceTileEntity;
import com.github.x3rmination.common.blocks.tile_entities.archive.powered_pulverizer.PoweredPulverizerTileEntity;
import com.github.x3rmination.common.blocks.tile_entities.archive.single_press.SinglePressTileEntity;
import com.github.x3rmination.common.blocks.tile_entities.cables.base.fluid_pipe.FluidPipeTileEntity;
import com.github.x3rmination.common.blocks.tile_entities.cables.base.power_cable.PowerCableTileEntity;
import com.github.x3rmination.common.blocks.tile_entities.zinc_copper_power_cell.ZCPCTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileEntityTypeInit {

    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, X3TECH.MOD_ID);

    public static final RegistryObject<TileEntityType<PoweredFurnaceTileEntity>> POWERED_FURNACE = TILE_ENTITIES.register("powered_furnace",
            () -> TileEntityType.Builder.of(PoweredFurnaceTileEntity::new,
                    BlockInit.POWERED_FURNACE.get()).build(null));
    public static final RegistryObject<TileEntityType<PoweredPulverizerTileEntity>> POWERED_PULVERIZER = TILE_ENTITIES.register("powered_pulverizer",
            () -> TileEntityType.Builder.of(PoweredPulverizerTileEntity::new,
                    BlockInit.POWERED_PULVERIZER.get()).build(null));
    public static final RegistryObject<TileEntityType<SinglePressTileEntity>> SINGLE_PRESS = TILE_ENTITIES.register("single_press",
            () -> TileEntityType.Builder.of(SinglePressTileEntity::new,
                    BlockInit.SINGLE_PRESS.get()).build(null));
    public static final RegistryObject<TileEntityType<DoublePressTileEntity>> DOUBLE_PRESS = TILE_ENTITIES.register("double_press",
            () -> TileEntityType.Builder.of(DoublePressTileEntity::new,
                    BlockInit.DOUBLE_PRESS.get()).build(null));
    public static final RegistryObject<TileEntityType<CombustionGeneratorTileEntity>> COMBUSTION_GENERATOR = TILE_ENTITIES.register("combustion_generator",
            () -> TileEntityType.Builder.of(CombustionGeneratorTileEntity::new,
                    BlockInit.COMBUSTION_GENERATOR.get()).build(null));
    public static final RegistryObject<TileEntityType<PowerCableTileEntity>> POWER_CABLE = TILE_ENTITIES.register("power_cable",
            () -> TileEntityType.Builder.of(PowerCableTileEntity::new,
                    BlockInit.POWER_CABLE.get()).build(null));
    public static final RegistryObject<TileEntityType<FluidPipeTileEntity>> FLUID_PIPE = TILE_ENTITIES.register("fluid_pipe",
            () -> TileEntityType.Builder.of(FluidPipeTileEntity::new,
                    BlockInit.FLUID_PIPE.get()).build(null));
    public static final RegistryObject<TileEntityType<ZCPCTileEntity>> ZINC_COPPER_POWER_CELL = TILE_ENTITIES.register("zinc_copper_power_cell",
            () -> TileEntityType.Builder.of(ZCPCTileEntity::new,
                    BlockInit.ZINC_COPPER_POWER_CELL.get()).build(null));
}
