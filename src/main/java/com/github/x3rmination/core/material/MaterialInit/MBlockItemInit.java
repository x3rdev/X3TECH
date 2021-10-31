package com.github.x3rmination.core.material.MaterialInit;

import com.github.x3rmination.X3TECH;
import com.github.x3rmination.common.items.MBlockItemBase;
import com.github.x3rmination.core.material.MaterialUtil.MaterialBase;
import com.github.x3rmination.core.material.MaterialUtil.MaterialRegistry;
import com.github.x3rmination.init.ItemInit;
import net.minecraft.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MBlockItemInit {

    public static final DeferredRegister<Item> MBLOCKITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, X3TECH.MOD_ID);
    public static final DeferredRegister<Item> MBLOCKOREITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, X3TECH.MOD_ID);


    public void registerMItems() {

        for (MaterialBase materialBase : MaterialRegistry.materialList) {
            MBLOCKITEMS.register(materialBase.getName().concat("_block"),
                    () -> new MBlockItemBase(MBlockInit.blocksLibrary.get(materialBase.getName()), new Item.Properties().tab(ItemInit.ModItemTab.instance), materialBase.getColor()));
            if(materialBase.materialHasOre()) {
                MBLOCKOREITEMS.register(materialBase.getName().concat("_ore"),
                        () -> new MBlockItemBase(MBlockInit.blocksLibrary.get(materialBase.getName().concat("_ore")), new Item.Properties().tab(ItemInit.ModItemTab.instance), materialBase.getColor()));
            }
        }
    }
}
