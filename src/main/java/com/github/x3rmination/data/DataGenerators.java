package com.github.x3rmination.data;

import com.github.x3rmination.X3TECH;
import com.github.x3rmination.data.client.BlockModelProvider;
import com.github.x3rmination.data.client.ItemModelProvider;
import com.github.x3rmination.data.recipes.RecipesProvider;
import com.github.x3rmination.data.tags.BlockTagsProvider;
import com.github.x3rmination.data.tags.ItemTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = X3TECH.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    private DataGenerators() {}

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();

        BlockTagsProvider blockTagsProvider = new BlockTagsProvider(generator, event.getExistingFileHelper());

        generator.addProvider(new ItemModelProvider(generator, fileHelper));
        generator.addProvider(new BlockModelProvider(generator, fileHelper));
        generator.addProvider(blockTagsProvider);
        generator.addProvider(new ItemTagsProvider(generator, blockTagsProvider, event.getExistingFileHelper()));
        generator.addProvider(new RecipesProvider(generator));

    }
}
