package com.github.x3rmination.data.tags;

import com.github.x3rmination.X3TECH;
import com.github.x3rmination.core.material.materialinit.MItemInit;
import com.github.x3rmination.core.material.materialutil.MaterialBase;
import com.github.x3rmination.core.material.materialutil.MaterialRegistry;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ItemTagsProvider extends net.minecraft.data.ItemTagsProvider {
    public ItemTagsProvider(DataGenerator gen, BlockTagsProvider blockTagProvider, ExistingFileHelper existingFileHelper) {
        super(gen, blockTagProvider, X3TECH.MOD_ID, existingFileHelper);
    }

    @Override
    public void addTags() {
        for (MaterialBase materialBase : MaterialRegistry.materialList) {
            copy(ModTags.blockTagLibrary.get(materialBase.getName() + "_block"), ModTags.itemTagLibrary.get(materialBase.getName() + "_block"));
            if(materialBase.materialHasOre()){
                copy(ModTags.blockTagLibrary.get(materialBase.getName() + "_ore"), ModTags.itemTagLibrary.get(materialBase.getName() + "_ore"));
            }
            tag(ModTags.itemTagLibrary.get(materialBase.getName() + "_ingot")).add(MItemInit.itemLibrary.get(materialBase.getName() + "_ingot"));
            tag(ModTags.itemTagLibrary.get(materialBase.getName() + "_nugget")).add(MItemInit.itemLibrary.get(materialBase.getName() + "_nugget"));
            tag(ModTags.itemTagLibrary.get(materialBase.getName() + "_plate")).add(MItemInit.itemLibrary.get(materialBase.getName() + "_plate"));
        }
    }
}
