package com.github.x3rmination.registry.init;

import com.github.x3rmination.common.blocks.powered_furnace.PoweredFurnaceTileEntity;
import com.github.x3rmination.common.blocks.powered_pulverizer.PoweredPulverizerTileEntity;
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

    public static void register() {
    }


    private static <T extends TileEntity> RegistryObject<TileEntityType<T>> register(String name, Supplier<T> factory, RegistryObject<? extends Block> block){
        return ModRegistration.TILE_ENTITIES.register(name, () -> {
            return TileEntityType.Builder.of(factory, block.get()).build(null);
        });
    }

}
