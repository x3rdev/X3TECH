package com.github.x3rmination.data;

import com.github.x3rmination.X3TECH;
import com.github.x3rmination.data.client.BlockModelProvider;
import com.github.x3rmination.data.client.ItemModelProvider;
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
        DataGenerator generators = event.getGenerator();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();

        generators.addProvider(new ItemModelProvider(generators, fileHelper));
        generators.addProvider(new BlockModelProvider(generators, fileHelper));
    }
}
