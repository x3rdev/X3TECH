package com.github.x3rmination.data.recipes;

import com.github.x3rmination.X3TECH;
import com.github.x3rmination.core.material.materialinit.MBlockInit;
import com.github.x3rmination.core.material.materialinit.MItemInit;
import com.github.x3rmination.core.material.materialutil.MaterialBase;
import com.github.x3rmination.core.material.materialutil.MaterialRegistry;
import net.minecraft.data.*;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import java.util.function.Consumer;

public class RecipesProvider extends RecipeProvider {
    public RecipesProvider(DataGenerator generator){
        super(generator);
    }

    @Override
    public String getName() {
        return "X3TECH - Recipes";
    }

    @Override
    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {
        for (MaterialBase materialBase : MaterialRegistry.materialList) {

            ShapelessRecipeBuilder.shapeless(MItemInit.itemLibrary.get(materialBase.getName()+"_ingot"), 9)
                    .requires(MBlockInit.blockLibrary.get(materialBase.getName()+"_block"))
                    .unlockedBy("has_item", has(MItemInit.itemLibrary.get(materialBase.getName()+"_ingot")))
                    .save(consumer, materialBase.getName()+"_ingots_from_block");

            ShapelessRecipeBuilder.shapeless(MItemInit.itemLibrary.get(materialBase.getName()+"_nugget"), 9)
                    .requires(MItemInit.itemLibrary.get(materialBase.getName()+"_ingot"))
                    .unlockedBy("has_item", has(MItemInit.itemLibrary.get(materialBase.getName()+"_nugget")))
                    .save(consumer, materialBase.getName()+"_nugget_from_ingot");

            ShapedRecipeBuilder.shaped(MBlockInit.blockLibrary.get(materialBase.getName()+"_block"), 1)
                    .define('#', MItemInit.itemLibrary.get(materialBase.getName()+"_ingot"))
                    .pattern("###")
                    .pattern("###")
                    .pattern("###")
                    .unlockedBy("has_item", has(MItemInit.itemLibrary.get(materialBase.getName()+"_ingot")))
                    .save(consumer, materialBase.getName()+"_block_from_nuggets");

            ShapedRecipeBuilder.shaped(MItemInit.itemLibrary.get(materialBase.getName()+"_ingot"), 1)
                    .define('#', MItemInit.itemLibrary.get(materialBase.getName()+"_nugget"))
                    .pattern("###")
                    .pattern("###")
                    .pattern("###")
                    .unlockedBy("has_item", has(MItemInit.itemLibrary.get(materialBase.getName()+"_nugget")))
                    .save(consumer, materialBase.getName()+"_ingot_from_nuggets");

            if(materialBase.materialHasOre()) {
                CookingRecipeBuilder.smelting(Ingredient.of(MBlockInit.blockLibrary.get(materialBase.getName()+"_ore")), MItemInit.itemLibrary.get(materialBase.getName()+"_ingot"), 0.7f, 200)
                        .unlockedBy("has_item", has(MItemInit.itemLibrary.get(materialBase.getName()+"_ingot")))
                        .save(consumer, modId(materialBase.getName()+"_ingot_smelting"));
                CookingRecipeBuilder.blasting(Ingredient.of(MBlockInit.blockLibrary.get(materialBase.getName()+"_ore")), MItemInit.itemLibrary.get(materialBase.getName()+"_ingot"), 0.7f, 100)
                        .unlockedBy("has_item", has(MItemInit.itemLibrary.get(materialBase.getName()+"_ingot")))
                        .save(consumer, modId(materialBase.getName()+"_ingot_blasting"));
            }
        }
    }

    private static ResourceLocation modId(String path) {
        return new ResourceLocation(X3TECH.MOD_ID, path);
    }
}
