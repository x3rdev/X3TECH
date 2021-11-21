package com.github.x3rmination.common.crafting.recipe;

import com.github.x3rmination.registry.init.RecipesInit;
import com.google.gson.JsonObject;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.SingleItemRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class PoweredPulverizerRecipe extends SingleItemRecipe {

    public PoweredPulverizerRecipe(ResourceLocation recipeId, Ingredient input, ItemStack result) {
        super(RecipesInit.Types.POWERED_PULVERIZER_RECIPE, RecipesInit.Serializers.POWERED_PULVERIZER_RECIPE.get(), recipeId, "", input, result);
    }

    @Override
    public boolean matches(IInventory inventory, World world) {
        return this.ingredient.test(inventory.getItem(0));
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<PoweredPulverizerRecipe> {

        @Override
        public PoweredPulverizerRecipe fromJson(ResourceLocation recipeId, JsonObject jsonObject) {
            Ingredient ingredient = Ingredient.fromJson(jsonObject.get("ingredient"));
            ResourceLocation itemId = new ResourceLocation(JSONUtils.getAsString(jsonObject, "result"));
            int count = JSONUtils.getAsInt(jsonObject, "count", 1);

            ItemStack result = new ItemStack(ForgeRegistries.ITEMS.getValue(itemId), count);

            return new PoweredPulverizerRecipe(recipeId, ingredient, result);
        }

        @Nullable
        @Override
        public PoweredPulverizerRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            ItemStack result = buffer.readItem();
            return new PoweredPulverizerRecipe(recipeId, ingredient, result);
        }

        @Override
        public void toNetwork(PacketBuffer buffer, PoweredPulverizerRecipe recipe) {
            recipe.ingredient.toNetwork(buffer);
            buffer.writeItem(recipe.result);

        }
    }
}
