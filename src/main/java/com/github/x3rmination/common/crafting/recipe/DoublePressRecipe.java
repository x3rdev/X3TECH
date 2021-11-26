package com.github.x3rmination.common.crafting.recipe;

import com.github.x3rmination.common.crafting.recipe.util.BaseRecipe;
import com.github.x3rmination.registry.init.RecipesInit;
import com.google.gson.JsonObject;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class DoublePressRecipe extends BaseRecipe {

    private final Ingredient inputTop;
    private final ItemStack inputBottom;
    private final ItemStack result;
    private final int processTime;

    public DoublePressRecipe(ResourceLocation id, Ingredient inputTop, ItemStack inputBottom, ItemStack result, int processTime) {
        super(id, RecipesInit.DOUBLE_PRESSING_SERIALIZER.get(), RecipesInit.DOUBLE_PRESSING, result);
        this.inputBottom = inputBottom;
        this.inputTop = inputTop;
        this.result = result.copy();
        this.processTime = processTime;
    }

    public int getProcessTime() {
        return processTime;
    }

    public Ingredient getInputTop() {
        return this.inputTop;
    }

    public ItemStack getInputBottom() {
        return this.inputBottom;
    }

    public ItemStack getResult() {
        return this.result;
    }

    @Override
    public boolean matches(IInventory pInv, World pLevel) {
        return super.matches(pInv, pLevel);
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<DoublePressRecipe> {

        @Override
        public DoublePressRecipe fromJson(ResourceLocation recipeId, JsonObject jsonObject) {
            ResourceLocation itemId = new ResourceLocation(JSONUtils.getAsString(jsonObject, "result"));
            Ingredient ingredientTop = Ingredient.fromJson(jsonObject.get("ingredient_top"));
            ItemStack ingredientBottom = new ItemStack(JSONUtils.getAsItem(jsonObject, "ingredient_bottom"));
            int count = JSONUtils.getAsInt(jsonObject, "count", 1);
            int processTime = JSONUtils.getAsInt(jsonObject, "time", 15);
            ItemStack result = new ItemStack(ForgeRegistries.ITEMS.getValue(itemId), count);

            return new DoublePressRecipe(itemId, ingredientTop, ingredientBottom, result, processTime);
        }

        @Nullable
        @Override
        public DoublePressRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
            Ingredient ingredientTop = Ingredient.fromNetwork(buffer);
            ItemStack ingredientBottom = buffer.readItem();
            ItemStack result = buffer.readItem();
            int processTime = buffer.readInt();
            return new DoublePressRecipe(recipeId, ingredientTop, ingredientBottom, result, processTime);
        }

        @Override
        public void toNetwork(PacketBuffer buffer, DoublePressRecipe recipe) {
            recipe.getInputTop().toNetwork(buffer);
            buffer.writeItem(recipe.inputBottom);
            buffer.writeItem(recipe.result);
            buffer.writeInt(recipe.processTime);
        }
    }
}
