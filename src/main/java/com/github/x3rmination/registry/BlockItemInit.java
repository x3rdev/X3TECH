package com.github.x3rmination.registry;

import com.github.x3rmination.X3TECH;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockItemInit {

    public static final DeferredRegister<Item> BLOCK_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, X3TECH.MOD_ID);

    public static final RegistryObject<BlockItem> MACHINE_FRAME = BLOCK_ITEMS.register("machine_frame",
            () -> new BlockItem(BlockInit.MACHINE_FRAME.get(), new Item.Properties().tab(ItemInit.ModItemTab.instance)));

    public static final RegistryObject<BlockItem> POWERED_FURNACE = BLOCK_ITEMS.register("powered_furnace",
            () -> new BlockItem(BlockInit.POWERED_FURNACE.get(), new Item.Properties().tab(ItemInit.ModItemTab.instance)));
    public static final RegistryObject<BlockItem> POWERED_PULVERIZER = BLOCK_ITEMS.register("powered_pulverizer",
            () -> new BlockItem(BlockInit.POWERED_PULVERIZER.get(), new Item.Properties().tab(ItemInit.ModItemTab.instance)));
    public static final RegistryObject<BlockItem> SINGLE_PRESS = BLOCK_ITEMS.register("single_press",
            () -> new BlockItem(BlockInit.SINGLE_PRESS.get(), new Item.Properties().tab(ItemInit.ModItemTab.instance)));
    public static final RegistryObject<BlockItem> DOUBLE_PRESS = BLOCK_ITEMS.register("double_press",
            () -> new BlockItem(BlockInit.DOUBLE_PRESS.get(), new Item.Properties().tab(ItemInit.ModItemTab.instance)));
    public static final RegistryObject<BlockItem> COMBUSTION_GENERATOR = BLOCK_ITEMS.register("combustion_generator",
            () -> new BlockItem(BlockInit.COMBUSTION_GENERATOR.get(), new Item.Properties().tab(ItemInit.ModItemTab.instance)));
    public static final RegistryObject<BlockItem> POWER_CABLE = BLOCK_ITEMS.register("power_cable",
            () -> new BlockItem(BlockInit.POWER_CABLE.get(), new Item.Properties().tab(ItemInit.ModItemTab.instance)));
    public static final RegistryObject<BlockItem> FLUID_PIPE = BLOCK_ITEMS.register("fluid_pipe",
            () -> new BlockItem(BlockInit.FLUID_PIPE.get(), new Item.Properties().tab(ItemInit.ModItemTab.instance)));
    public static final RegistryObject<BlockItem> ZINC_COPPER_POWER_CELL = BLOCK_ITEMS.register("zinc_copper_power_cell",
            () -> new BlockItem(BlockInit.ZINC_COPPER_POWER_CELL.get(), new Item.Properties().tab(ItemInit.ModItemTab.instance)));
}
