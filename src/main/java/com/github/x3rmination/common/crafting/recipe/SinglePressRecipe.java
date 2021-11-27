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
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class SinglePressRecipe extends BaseRecipe {
    private final Ingredient ingredient;
    private final ItemStack result;
    private final int count;
    private final int processTime;

    public SinglePressRecipe(ResourceLocation id, Ingredient ingredient, ItemStack result, int count, int processTime) {
        super(RecipesInit.SINGLE_PRESSING, RecipesInit.SINGLE_PRESSING_SERIALIZER.get(), id, ingredient, result);
        this.ingredient = ingredient;
        this.count = count;
        this.result = result;
        this.processTime = processTime;
    }

    public int getProcessTime() {
        return processTime;
    }

    public int getCount(){
        return count;
    }


    @Override
    public boolean matches(IInventory inventory, World world) {
        return this.ingredient.test(inventory.getItem(0));
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();
        nonnulllist.add(this.ingredient);
        return nonnulllist;
    }

    @Override
    public ItemStack getResultItem() {
        return new ItemStack(result.getItem(), count);
    }

    public static class Serializer<T extends SinglePressRecipe> extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<SinglePressRecipe> {

        @Override
        public SinglePressRecipe fromJson(ResourceLocation recipeId, JsonObject jsonObject) {
            Ingredient ingredient = Ingredient.fromJson(jsonObject.get("ingredient"));
            ResourceLocation itemId = new ResourceLocation(JSONUtils.getAsString(jsonObject, "result"));
            int count = JSONUtils.getAsInt(jsonObject, "count", 1);
            int processTime = JSONUtils.getAsInt(jsonObject, "time", 15);
            ItemStack result = new ItemStack(ForgeRegistries.ITEMS.getValue(itemId), count);
            return new SinglePressRecipe(itemId, ingredient, result, count, processTime);
        }

        @Nullable
        @Override
        public SinglePressRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            ItemStack result = buffer.readItem();
            int count = buffer.readInt();
            int processTime = buffer.readInt();
            return new SinglePressRecipe(recipeId, ingredient, result, count, processTime);
        }

        @Override
        public void toNetwork(PacketBuffer buffer, SinglePressRecipe recipe) {
            recipe.ingredient.toNetwork(buffer);
            buffer.writeItem(recipe.result);
            buffer.writeInt(recipe.processTime);
            buffer.writeInt(recipe.count);
        }
    }
}
