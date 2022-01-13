package com.github.x3rmination.registry;

import com.github.x3rmination.X3TECH;
import com.github.x3rmination.common.crafting.recipe.DoublePressRecipe;
import com.github.x3rmination.common.crafting.recipe.PoweredPulverizerRecipe;
import com.github.x3rmination.common.crafting.recipe.SinglePressRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RecipesInit {

    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, X3TECH.MOD_ID);

    public static IRecipeType<PoweredPulverizerRecipe> PULVERIZING;
    public static IRecipeType<SinglePressRecipe> SINGLE_PRESSING;
    public static IRecipeType<DoublePressRecipe> DOUBLE_PRESSING;

    public static final RegistryObject<IRecipeSerializer<?>> PULVERIZING_SERIALIZER = RECIPE_SERIALIZERS.register(
            "pulverizing", PoweredPulverizerRecipe.Serializer::new);
    public static final RegistryObject<IRecipeSerializer<?>> SINGLE_PRESSING_SERIALIZER = RECIPE_SERIALIZERS.register(
            "single_pressing", SinglePressRecipe.Serializer::new);
    public static final RegistryObject<IRecipeSerializer<?>> DOUBLE_PRESSING_SERIALIZER = RECIPE_SERIALIZERS.register(
            "double_pressing", DoublePressRecipe.Serializer::new);

    public static void register() {
        PULVERIZING = IRecipeType.register("pulverizing");
        SINGLE_PRESSING = IRecipeType.register("single_pressing");
        DOUBLE_PRESSING = IRecipeType.register("double_pressing");
    }
}
