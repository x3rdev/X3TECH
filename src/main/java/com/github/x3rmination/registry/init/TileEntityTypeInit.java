package com.github.x3rmination.registry.init;

import com.github.x3rmination.common.blocks.tile_entities.cables.power_cable.PowerCableTileEntity;
import com.github.x3rmination.common.blocks.tile_entities.combustion_generator.CombustionGeneratorTileEntity;
import com.github.x3rmination.common.blocks.tile_entities.double_press.DoublePressTileEntity;
import com.github.x3rmination.common.blocks.tile_entities.powered_furnace.PoweredFurnaceTileEntity;
import com.github.x3rmination.common.blocks.tile_entities.powered_pulverizer.PoweredPulverizerTileEntity;
import com.github.x3rmination.common.blocks.tile_entities.single_press.SinglePressTileEntity;
import com.github.x3rmination.registry.ModRegistration;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public class TileEntityTypeInit {

    public static final RegistryObject<TileEntityType<PoweredFurnaceTileEntity>> POWERED_FURNACE = register(
            "powered_furnace",
            PoweredFurnaceTileEntity::new,
            BlockInit.POWERED_FURNACE);
    public static final RegistryObject<TileEntityType<PoweredPulverizerTileEntity>> POWERED_PULVERIZER = register(
            "powered_pulverizer",
            PoweredPulverizerTileEntity::new,
            BlockInit.POWERED_PULVERIZER);
    public static final RegistryObject<TileEntityType<SinglePressTileEntity>> SINGLE_PRESS = register(
            "single_press",
            SinglePressTileEntity::new,
            BlockInit.SINGLE_PRESS);
    public static final RegistryObject<TileEntityType<DoublePressTileEntity>> DOUBLE_PRESS = register(
            "double_press",
            DoublePressTileEntity::new,
            BlockInit.DOUBLE_PRESS);
    public static final RegistryObject<TileEntityType<CombustionGeneratorTileEntity>> COMBUSTION_GENERATOR = register(
            "combustion_generator",
            CombustionGeneratorTileEntity::new,
            BlockInit.COMBUSTION_GENERATOR);
    public static final RegistryObject<TileEntityType<PowerCableTileEntity>> POWER_CABLE = register(
            "power_cable",
            PowerCableTileEntity::new,
            BlockInit.POWER_CABLE);

    public static void register() {
    }

    private static <T extends TileEntity> RegistryObject<TileEntityType<T>> register(String name, Supplier<T> factory, RegistryObject<? extends Block> block){
        return ModRegistration.TILE_ENTITIES.register(name, () -> {
            return TileEntityType.Builder.of(factory, block.get()).build(null);
        });
    }

}
