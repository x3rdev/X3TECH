package com.github.x3rmination.registry.init;

import com.github.x3rmination.X3TECH;
import com.github.x3rmination.common.items.MItemBase;
import com.github.x3rmination.registry.ModRegistration;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.RegistryObject;

import java.awt.*;

public class ItemInit {

    //Items
    public static final RegistryObject<Item> GINGOT = ModRegistration.ITEMS.register("gingot",
            () -> new MItemBase(new Item.Properties().tab(ModItemTab.instance), new Color(255, 255, 255)));

    public static class ModItemTab extends ItemGroup {
        public static final ModItemTab instance = new ModItemTab(ItemGroup.TABS.length, X3TECH.MOD_ID);
        private ModItemTab(int index, String tabName) {
            super(index, tabName);
            this.hasSearchBar();
        }

        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ItemInit.GINGOT.get());
        }
    }

    static void register() {}
}
