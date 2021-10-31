package com.github.x3rmination.core.material.MaterialInit;

import com.github.x3rmination.X3TECH;
import com.github.x3rmination.common.items.MItemBase;
import com.github.x3rmination.core.material.MaterialUtil.MaterialBase;
import com.github.x3rmination.core.material.MaterialUtil.MaterialRegistry;
import com.github.x3rmination.init.ItemInit;
import net.minecraft.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MItemInit {

    public static final DeferredRegister<Item> MITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, X3TECH.MOD_ID);

    public void registerMItems() {
        for (MaterialBase materialBase : MaterialRegistry.materialList) {
            MITEMS.register(materialBase.getName(),
                    () -> new MItemBase(new Item.Properties().tab(ItemInit.ModItemTab.instance), materialBase.getColor()));
            MITEMS.register(materialBase.getName() + "_nugget",
                    () -> new MItemBase(new Item.Properties().tab(ItemInit.ModItemTab.instance), materialBase.getColor()));
        }
    }
}
