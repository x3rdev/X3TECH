package com.github.x3rmination.core.material.materialinit;

import com.github.x3rmination.X3TECH;
import com.github.x3rmination.common.items.MBlockItemBase;
import com.github.x3rmination.core.material.materialutil.MaterialBase;
import com.github.x3rmination.core.material.materialutil.MaterialRegistry;
import com.github.x3rmination.registry.ItemInit;
import net.minecraft.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public class MBlockItemInit {

    public static final DeferredRegister<Item> MBLOCKITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, X3TECH.MOD_ID);
    public static final DeferredRegister<Item> MBLOCKOREITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, X3TECH.MOD_ID);

    public static final Map<String, MBlockItemBase> blockItemLibrary = new HashMap<>();

    public void registerMItems() {

        for (MaterialBase materialBase : MaterialRegistry.materialList) {
            MBlockItemBase mBlockItemBase = new MBlockItemBase(MBlockInit.blockLibrary.get(materialBase.getName().concat("_block")), new Item.Properties().tab(ItemInit.ModItemTab.instance), materialBase.getColor());
            blockItemLibrary.put(materialBase.getName().concat("_block"), mBlockItemBase);
            MBLOCKITEMS.register(materialBase.getName().concat("_block"),
                    () -> mBlockItemBase);
            if(materialBase.materialHasOre()) {
                MBlockItemBase mOreBlockItemBase = new MBlockItemBase(MBlockInit.blockLibrary.get(materialBase.getName().concat("_ore")), new Item.Properties().tab(ItemInit.ModItemTab.instance), materialBase.getColor());
                blockItemLibrary.put(materialBase.getName().concat("_ore"), mOreBlockItemBase);
                MBLOCKOREITEMS.register(materialBase.getName().concat("_ore"),
                        () -> mOreBlockItemBase);
            }
        }
    }
}
