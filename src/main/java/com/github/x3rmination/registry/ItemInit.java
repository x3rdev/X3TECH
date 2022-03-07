package com.github.x3rmination.registry;

import com.github.x3rmination.X3TECH;
import com.github.x3rmination.common.items.MItemBase;
import com.github.x3rmination.core.item.IngredientItemBase;
import net.minecraft.item.BucketItem;
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
    public static final RegistryObject<Item> TEST_INGOT = ITEMS.register("test_ingot",
            () -> new MItemBase(new Item.Properties().tab(ModItemTab.instance), new Color(255, 255, 255)));
    public static final RegistryObject<Item> ENGINE_PISTON = ITEMS.register("engine_piston",
            () -> new IngredientItemBase(new Item.Properties().tab(ModItemTab.instance)));
    public static final RegistryObject<Item> CRUDE_GRAPHITE = ITEMS.register("crude_graphite",
            () -> new IngredientItemBase(new Item.Properties().tab(ModItemTab.instance)));
    public static final RegistryObject<Item> COAL_TAR_CREOSOTE_BUCKET = ITEMS.register("coal_tar_creosote_bucket",
            () -> new BucketItem(() -> FluidInit.COAL_TAR_CREOSOTE_FLUID.get(), new Item.Properties().tab(ModItemTab.instance).stacksTo(1)));

    public static class ModItemTab extends ItemGroup {
        public static final ModItemTab instance = new ModItemTab(ItemGroup.TABS.length, X3TECH.MOD_ID);
        private ModItemTab(int index, String tabName) {
            super(index, tabName);
            this.hasSearchBar();
        }

        @Override
        public ItemStack makeIcon() {
            return new ItemStack(TEST_INGOT.get());
        }
    }
}
