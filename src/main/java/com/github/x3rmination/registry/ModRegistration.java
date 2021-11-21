package com.github.x3rmination.registry;

import com.github.x3rmination.X3TECH;
import com.github.x3rmination.registry.init.*;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModRegistration {

    private ModRegistration() {}

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, X3TECH.MOD_ID);
    public static final DeferredRegister<Item> BLOCK_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, X3TECH.MOD_ID);
    public static final DeferredRegister<Item> MATERIALS = DeferredRegister.create(ForgeRegistries.ITEMS, X3TECH.MOD_ID);
    public static final DeferredRegister<Item> MATERIAL_BLOCK_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, X3TECH.MOD_ID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, X3TECH.MOD_ID);
    public static final DeferredRegister<Block> MATERIAL_BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, X3TECH.MOD_ID);
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, X3TECH.MOD_ID);
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, X3TECH.MOD_ID);
    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, X3TECH.MOD_ID);

    public static void register() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register(modEventBus);
        BLOCK_ITEMS.register(modEventBus);
        MATERIAL_BLOCK_ITEMS.register(modEventBus);
        BLOCKS.register(modEventBus);
        CONTAINERS.register(modEventBus);
        TILE_ENTITIES.register(modEventBus);
        RECIPE_SERIALIZERS.register(modEventBus);

        BlockInit.register();
        BlockItemInit.register();
        ContainerTypeInit.register();
        ItemInit.register();
        RecipesInit.register();
        TileEntityTypeInit.register();
    }

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

    @Mod.EventBusSubscriber(modid = X3TECH.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Client {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            ContainerTypeInit.registerScreens(event);
        }
    }
}
