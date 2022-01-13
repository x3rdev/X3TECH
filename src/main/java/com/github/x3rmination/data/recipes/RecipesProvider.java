package com.github.x3rmination.data.recipes;

import com.github.x3rmination.X3TECH;
import com.github.x3rmination.core.material.materialinit.MBlockInit;
import com.github.x3rmination.core.material.materialinit.MItemInit;
import com.github.x3rmination.core.material.materialutil.MaterialBase;
import com.github.x3rmination.core.material.materialutil.MaterialRegistry;
import com.github.x3rmination.registry.RecipesInit;
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
            new SingleItemRecipeBuilder(RecipesInit.PULVERIZING_SERIALIZER.get(), Ingredient.of(MItemInit.itemLibrary.get(materialBase.getName()+"_ingot")), MItemInit.itemLibrary.get(materialBase.getName()+"_dust"), 1)
                    .unlocks("has_item", has(MItemInit.itemLibrary.get(materialBase.getName()+"_ingot")))
                    .save(consumer, materialBase.getName()+"_dust_from_ingot");
            new SingleItemRecipeBuilder(RecipesInit.SINGLE_PRESSING_SERIALIZER.get(), Ingredient.of(MItemInit.itemLibrary.get(materialBase.getName()+"_ingot")), MItemInit.itemLibrary.get(materialBase.getName()+"_plate"), 1)
                    .unlocks("has_item", has(MItemInit.itemLibrary.get(materialBase.getName()+"_ingot")))
                    .save(consumer, materialBase.getName()+"_plate_from_ingot");
            CookingRecipeBuilder.smelting(Ingredient.of(MItemInit.itemLibrary.get(materialBase.getName()+"_dust")), MItemInit.itemLibrary.get(materialBase.getName()+"_ingot"), 0.7f, 200)
                    .unlockedBy("has_item", has(MItemInit.itemLibrary.get(materialBase.getName()+"_dust")))
                    .save(consumer, materialBase.getName() + "_ingot_from_dust");
            if(materialBase.materialHasOre()) {
                new SingleItemRecipeBuilder(RecipesInit.PULVERIZING_SERIALIZER.get(), Ingredient.of(MBlockInit.blockLibrary.get(materialBase.getName() + "_ore")), MItemInit.itemLibrary.get(materialBase.getName() + "_dust"), 2)
                        .unlocks("has_item", has(MBlockInit.blockLibrary.get(materialBase.getName()+"_ore")))
                        .save(consumer, materialBase.getName() + "_dust_from_ore");
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
