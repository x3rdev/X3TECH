package com.github.x3rmination.registry.init;

import com.github.x3rmination.common.crafting.recipe.DoublePressRecipe;
import com.github.x3rmination.common.crafting.recipe.PoweredPulverizerRecipe;
import com.github.x3rmination.registry.ModRegistration;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraftforge.fml.RegistryObject;

public class RecipesInit {

    public static IRecipeType<PoweredPulverizerRecipe> PULVERIZING;
    public static IRecipeType<DoublePressRecipe> DOUBLE_PRESSING;

    public static final RegistryObject<IRecipeSerializer<?>> PULVERIZING_SERIALIZER = ModRegistration.RECIPE_SERIALIZERS.register(
            "pulverizing", PoweredPulverizerRecipe.Serializer::new);
    public static final RegistryObject<IRecipeSerializer<?>> DOUBLE_PRESSING_SERIALIZER = ModRegistration.RECIPE_SERIALIZERS.register(
            "double_pressing", DoublePressRecipe.Serializer::new);

    private RecipesInit() {}

    public static void register() {
        PULVERIZING = IRecipeType.register("pulverizing");
        DOUBLE_PRESSING = IRecipeType.register("double_pressing");
    }
}
