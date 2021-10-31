package com.github.x3rmination.init;

import com.github.x3rmination.X3TECH;
import com.github.x3rmination.common.items.MBlockItemBase;
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
    public static final DeferredRegister<Item> MATERIALS = DeferredRegister.create(ForgeRegistries.ITEMS, X3TECH.MOD_ID);
    public static final DeferredRegister<Item> BLOCK_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, X3TECH.MOD_ID);

    //Items
    public static final RegistryObject<Item> TEST_INGOT = MATERIALS.register("test_ingot",
            () -> new MItemBase(new Item.Properties().tab(ModItemTab.instance), new Color(0, 250, 0)));
    public static final RegistryObject<Item> GINGOT = MATERIALS.register("gingot",
            () -> new MItemBase(new Item.Properties().tab(ModItemTab.instance), new Color(255, 255, 255)));
    //BlockItems
    public static final RegistryObject<net.minecraft.item.BlockItem> TEST_BLOCK = BLOCK_ITEMS.register("test_block",
            () -> new MBlockItemBase(BlockInit.TEST_BLOCK.get(), new Item.Properties().tab(ModItemTab.instance), new Color(255, 0, 0)));

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
}
