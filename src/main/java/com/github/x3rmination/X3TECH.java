package com.github.x3rmination;

import com.github.x3rmination.client.color.ColorLoader;
import com.github.x3rmination.common.network.ModPacketHandler;
import com.github.x3rmination.core.material.materialinit.MBlockInit;
import com.github.x3rmination.core.material.materialinit.MBlockItemInit;
import com.github.x3rmination.core.material.materialinit.MItemInit;
import com.github.x3rmination.data.tags.ModTags;
import com.github.x3rmination.registry.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("x3tech")
public class X3TECH {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "x3tech";
    public static final String MINECRAFT_ID = "minecraft";

    public X3TECH() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::doClientStuff);
        modEventBus.addListener(MBlockInit::renderTypeSet);
        modEventBus.addListener(ColorLoader::loadColors);

        // Generic Items
        ItemInit.ITEMS.register(modEventBus);
        MItemInit.MITEMS.register(modEventBus);
        new MItemInit().registerMItems();
        // Blocks
        BlockInit.BLOCKS.register(modEventBus);
        MBlockInit.MBLOCKS.register(modEventBus);
        MBlockInit.MOREBLOCKS.register(modEventBus);
        new MBlockInit().registerMItems();
        // Block Items
        BlockItemInit.BLOCK_ITEMS.register(modEventBus);
        MBlockItemInit.MBLOCKITEMS.register(modEventBus);
        MBlockItemInit.MBLOCKOREITEMS.register(modEventBus);
        new MBlockItemInit().registerMItems();
        // Tags
        new ModTags.Blocks().createBlockTags();
        new ModTags.Items().createItemTags();
        // Tile Entities
        TileEntityTypeInit.TILE_ENTITIES.register(modEventBus);
        // Containers
        ContainerTypeInit.CONTAINERS.register(modEventBus);
        // Recipes
        RecipesInit.RECIPE_SERIALIZERS.register(modEventBus);
        RecipesInit.register();

        MinecraftForge.EVENT_BUS.register(this);

    }

    private void setup(final FMLCommonSetupEvent event) {
        ModPacketHandler.register();
    }

    private void doClientStuff(FMLClientSetupEvent event) {

    }

    @SubscribeEvent
    public void setupClient(FMLClientSetupEvent event) {
    }

    @Mod.EventBusSubscriber(modid = X3TECH.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Client {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            ContainerTypeInit.registerScreens(event);
        }
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {

    }
}
