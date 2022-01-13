package com.github.x3rmination.registry;

import com.github.x3rmination.X3TECH;
import com.github.x3rmination.common.blocks.tile_entities.cables.item_cable.CableDebuggerItem;
import com.github.x3rmination.common.items.MItemBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.awt.*;

public class ItemInit {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, X3TECH.MOD_ID);

    //Items
    public static final RegistryObject<Item> GINGOT = ITEMS.register("gingot",
            () -> new MItemBase(new Item.Properties().tab(ModItemTab.instance), new Color(255, 255, 255)));

    public static final RegistryObject<Item> DEBUGGER = ITEMS.register("cable_debugger",
            () -> new CableDebuggerItem(new Item.Properties().tab(ModItemTab.instance)));

    public static class ModItemTab extends ItemGroup {
        public static final ModItemTab instance = new ModItemTab(ItemGroup.TABS.length, X3TECH.MOD_ID);
        private ModItemTab(int index, String tabName) {
            super(index, tabName);
            this.hasSearchBar();
        }

        @Override
        public ItemStack makeIcon() {
            return new ItemStack(GINGOT.get());
        }
    }
}
