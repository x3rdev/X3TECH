package com.github.x3rmination.data.client;

import com.github.x3rmination.X3TECH;
import com.github.x3rmination.core.material.MaterialUtil.MaterialBase;
import com.github.x3rmination.core.material.MaterialUtil.MaterialRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ItemModelProvider extends net.minecraftforge.client.model.generators.ItemModelProvider {

    public ItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, X3TECH.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
//        withExistingParent("test1_material_block", modLoc("blocks/base_block"));

//        ModelFile itemGenerated = getExistingFile(mcLoc("item/generated"));

        for (MaterialBase materialBase : MaterialRegistry.materialList) {
            MIngotBuilder(materialBase.getName());
            MBlockItemBuilder(materialBase.getName());
        }
    }

    private ItemModelBuilder MIngotBuilder(String name) {
        return getBuilder(name).parent(getExistingFile(mcLoc("item/generated"))).texture("layer0", X3TECH.MOD_ID + ":item/base_ingot");
    }
    private ItemModelBuilder MBlockItemBuilder(String name) {
        return getBuilder(name+"_block").parent(getExistingFile(ResourceLocation.tryParse(X3TECH.MOD_ID + ":block/base_block"))).texture("layer0", X3TECH.MOD_ID + ":block/base_block");
    }
}
