package com.github.x3rmination.registry;

import com.github.x3rmination.X3TECH;
import com.github.x3rmination.common.blocks.tile_entities.cables.power_cable.PowerCableTileEntity;
import com.github.x3rmination.common.blocks.tile_entities.combustion_generator.CombustionGeneratorTileEntity;
import com.github.x3rmination.common.blocks.tile_entities.double_press.DoublePressTileEntity;
import com.github.x3rmination.common.blocks.tile_entities.energy_storage.EnergyStorageTileEntity;
import com.github.x3rmination.common.blocks.tile_entities.powered_furnace.PoweredFurnaceTileEntity;
import com.github.x3rmination.common.blocks.tile_entities.powered_pulverizer.PoweredPulverizerTileEntity;
import com.github.x3rmination.common.blocks.tile_entities.single_press.SinglePressTileEntity;
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
    public static final RegistryObject<TileEntityType<EnergyStorageTileEntity>> ENERGY_STORAGE = TILE_ENTITIES.register("power_cable",
            () -> TileEntityType.Builder.of(EnergyStorageTileEntity::new,
                    BlockInit.POWER_CABLE.get()).build(null));

}
