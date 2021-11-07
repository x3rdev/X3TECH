package com.github.x3rmination.data.tags;

import com.github.x3rmination.X3TECH;
import com.github.x3rmination.core.material.materialinit.MBlockInit;
import com.github.x3rmination.core.material.materialutil.MaterialBase;
import com.github.x3rmination.core.material.materialutil.MaterialRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockTagsProvider extends net.minecraft.data.BlockTagsProvider {
    public BlockTagsProvider(DataGenerator gen, ExistingFileHelper existingFileHelper) {
        super(gen, X3TECH.MOD_ID, existingFileHelper);
    }


    @Override
    public void addTags() {
        for (MaterialBase materialBase : MaterialRegistry.materialList) {
            tag(ModTags.blockTagLibrary.get(materialBase.getName() + "_block")).add(MBlockInit.blockLibrary.get(materialBase.getName()));
            if(materialBase.materialHasOre()) {
                tag(ModTags.blockTagLibrary.get(materialBase.getName() + "_ore")).add(MBlockInit.blockLibrary.get(materialBase.getName() + "_ore"));
            }
        }
    }
}
