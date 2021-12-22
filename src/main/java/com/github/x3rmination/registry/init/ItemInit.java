package com.github.x3rmination.registry.init;

import com.github.x3rmination.common.items.MItemBase;
import com.github.x3rmination.common.items.PipeDebugger;
import com.github.x3rmination.registry.ModRegistration;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;

import java.awt.*;

public class ItemInit {

    //Items
    public static final RegistryObject<Item> GINGOT = ModRegistration.ITEMS.register("gingot",
            () -> new MItemBase(new Item.Properties().tab(ModRegistration.ModItemTab.instance), new Color(255, 255, 255)));
    public static final RegistryObject<Item> PIPE_DEBUGGER = ModRegistration.ITEMS.register("cable_debugger",
            () -> new PipeDebugger(new Item.Properties().tab(ModRegistration.ModItemTab.instance)));
    public static void register() {}
}
