package com.github.x3rmination.core.material.materialinit;

import com.github.x3rmination.X3TECH;
import com.github.x3rmination.common.items.MItemBase;
import com.github.x3rmination.core.material.materialutil.MaterialBase;
import com.github.x3rmination.core.material.materialutil.MaterialRegistry;
import com.github.x3rmination.registry.ModRegistration;
import net.minecraft.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public class MItemInit {

    public static final DeferredRegister<Item> MITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, X3TECH.MOD_ID);

    public static final Map<String, MItemBase> itemLibrary = new HashMap<>();

    public void registerMItems() {
        for (MaterialBase materialBase : MaterialRegistry.materialList) {
            MItemBase mIngotItemBase = new MItemBase(new Item.Properties().tab(ModRegistration.ModItemTab.instance), materialBase.getColor());
            MItemBase mNuggetItemBase = new MItemBase(new Item.Properties().tab(ModRegistration.ModItemTab.instance), materialBase.getColor());
            MItemBase mDustItemBase = new MItemBase(new Item.Properties().tab(ModRegistration.ModItemTab.instance), materialBase.getColor());
            itemLibrary.put(materialBase.getName().concat("_ingot"), mIngotItemBase);
            MITEMS.register(materialBase.getName().concat("_ingot"),
                    () -> mIngotItemBase);
            itemLibrary.put(materialBase.getName().concat("_nugget"), mNuggetItemBase);
            MITEMS.register(materialBase.getName().concat("_nugget"),
                    () -> mNuggetItemBase);
            itemLibrary.put(materialBase.getName().concat("_dust"), mDustItemBase);
            MITEMS.register(materialBase.getName().concat("_dust"),
                    () -> mDustItemBase);
        }
    }
}
