package com.github.x3rmination.registry.init;

import com.github.x3rmination.registry.ModRegistration;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;

public class BlockItemInit {

    public static final RegistryObject<BlockItem> POWERED_FURNACE = ModRegistration.BLOCK_ITEMS.register("powered_furnace",
            () -> new BlockItem(BlockInit.POWERED_FURNACE.get(), new Item.Properties().tab(ModRegistration.ModItemTab.instance)));
    public static final RegistryObject<BlockItem> POWERED_PULVERIZER = ModRegistration.BLOCK_ITEMS.register("powered_pulverizer",
            () -> new BlockItem(BlockInit.POWERED_PULVERIZER.get(), new Item.Properties().tab(ModRegistration.ModItemTab.instance)));
    public static final RegistryObject<BlockItem> SINGLE_PRESS = ModRegistration.BLOCK_ITEMS.register("single_press",
            () -> new BlockItem(BlockInit.SINGLE_PRESS.get(), new Item.Properties().tab(ModRegistration.ModItemTab.instance)));
    public static final RegistryObject<BlockItem> DOUBLE_PRESS = ModRegistration.BLOCK_ITEMS.register("double_press",
            () -> new BlockItem(BlockInit.DOUBLE_PRESS.get(), new Item.Properties().tab(ModRegistration.ModItemTab.instance)));
    public static final RegistryObject<BlockItem> COMBUSTION_GENERATOR = ModRegistration.BLOCK_ITEMS.register("combustion_generator",
            () -> new BlockItem(BlockInit.COMBUSTION_GENERATOR.get(), new Item.Properties().tab(ModRegistration.ModItemTab.instance)));
    public static final RegistryObject<BlockItem> MACHINE_FRAME = ModRegistration.BLOCK_ITEMS.register("machine_frame",
            () -> new BlockItem(BlockInit.MACHINE_FRAME.get(), new Item.Properties().tab(ModRegistration.ModItemTab.instance)));
    public static final RegistryObject<BlockItem> POWER_CABLE = ModRegistration.BLOCK_ITEMS.register("power_cable",
            () -> new BlockItem(BlockInit.POWER_CABLE.get(), new Item.Properties().tab(ModRegistration.ModItemTab.instance)));
    public static void register() {}
}
