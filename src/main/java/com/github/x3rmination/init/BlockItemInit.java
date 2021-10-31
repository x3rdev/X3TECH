package com.github.x3rmination.init;

import com.github.x3rmination.X3TECH;
import com.github.x3rmination.common.items.MBlockItemBase;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.awt.*;

public class BlockItemInit {

    public static final DeferredRegister<Item> MATERIAL_BLOCK_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, X3TECH.MOD_ID);

    public static final RegistryObject<BlockItem> TEST_BLOCK = MATERIAL_BLOCK_ITEMS.register("test_block",
            () -> new MBlockItemBase(BlockInit.TEST_BLOCK.get(), new Item.Properties().tab(ItemInit.ModItemTab.instance), new Color(255, 0, 0)));
    public static final RegistryObject<BlockItem> TEST_ORE = MATERIAL_BLOCK_ITEMS.register("test_ore",
            () -> new MBlockItemBase(BlockInit.TEST_ORE.get(), new Item.Properties().tab(ItemInit.ModItemTab.instance), new Color(40, 100, 123)));

}
