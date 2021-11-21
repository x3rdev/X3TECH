package com.github.x3rmination.registry.init;

import com.github.x3rmination.X3TECH;
import com.github.x3rmination.common.crafting.recipe.PoweredPulverizerRecipe;
import com.github.x3rmination.registry.ModRegistration;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraftforge.fml.RegistryObject;

public class RecipesInit {
    public static class Types {
        public static final IRecipeType<PoweredPulverizerRecipe> POWERED_PULVERIZER_RECIPE = IRecipeType.register(X3TECH.MOD_ID + "powered_pulverizer");

        private Types() {}
    }

    public static class Serializers {
        public static final RegistryObject<IRecipeSerializer<PoweredPulverizerRecipe>> POWERED_PULVERIZER_RECIPE = ModRegistration.RECIPE_SERIALIZERS.register(
                "powered_pulverizer", PoweredPulverizerRecipe.Serializer::new);

        private Serializers() {}
    }
    private RecipesInit() {}
    public static void register() {}
}
