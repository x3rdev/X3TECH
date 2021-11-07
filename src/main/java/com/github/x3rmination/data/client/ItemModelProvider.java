package com.github.x3rmination.data.client;

import com.github.x3rmination.X3TECH;
import com.github.x3rmination.core.material.materialutil.MaterialBase;
import com.github.x3rmination.core.material.materialutil.MaterialRegistry;
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

        for (MaterialBase materialBase : MaterialRegistry.materialList) {
            MIngotBuilder(materialBase.getName());
            MNuggetBuilder(materialBase.getName());
            MBlockItemBuilder(materialBase.getName());
            if(materialBase.materialHasOre()) {
                MOreBlockItemBuilder(materialBase.getName());
            }
        }
    }

    private ItemModelBuilder MIngotBuilder(String name) {
        return getBuilder(name+"_ingot").parent(getExistingFile(mcLoc("item/generated"))).texture("layer0", X3TECH.MOD_ID + ":item/base_ingot");
    }
    private ItemModelBuilder MNuggetBuilder(String name) {
        return getBuilder(name+"_nugget").parent(getExistingFile(mcLoc("item/generated"))).texture("layer0", X3TECH.MOD_ID + ":item/base_nugget");
    }
    private ItemModelBuilder MBlockItemBuilder(String name) {
        return getBuilder(name + "_block").parent(getExistingFile(ResourceLocation.tryParse(X3TECH.MOD_ID + ":block/base_block"))).texture("layer0", X3TECH.MOD_ID + ":block/base_block");
    }
    private ItemModelBuilder MOreBlockItemBuilder(String name) {
        return getBuilder(name + "_ore").parent(getExistingFile(ResourceLocation.tryParse(X3TECH.MOD_ID + ":block/base_ore"))).texture("layer0", X3TECH.MOD_ID + ":block/base_ore");
    }

}
