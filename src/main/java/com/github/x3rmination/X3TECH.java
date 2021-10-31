package com.github.x3rmination;

import com.github.x3rmination.client.ColorLoader;
import com.github.x3rmination.core.material.MaterialInit.MBlockInit;
import com.github.x3rmination.core.material.MaterialInit.MBlockItemInit;
import com.github.x3rmination.core.material.MaterialInit.MItemInit;
import com.github.x3rmination.init.BlockInit;
import com.github.x3rmination.init.BlockItemInit;
import com.github.x3rmination.init.ItemInit;
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
public class X3TECH
{
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "x3tech";
    public static final String MINECRAFT_ID = "minecraft";

    public X3TECH() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::setup);
        eventBus.addListener(this::doClientStuff);
        eventBus.addListener(MBlockInit::renderTypeSet);
        eventBus.addListener(ColorLoader::loadColors);

        ItemInit.ITEMS.register(eventBus);
        ItemInit.MATERIALS.register(eventBus);
        BlockItemInit.MATERIAL_BLOCK_ITEMS.register(eventBus);
        BlockInit.BLOCKS.register(eventBus);
        BlockInit.MATERIAL_BLOCKS.register(eventBus);

        MItemInit.MITEMS.register(eventBus);
        new MItemInit().registerMItems();
        MBlockInit.MBLOCKS.register(eventBus);
        MBlockInit.MOREBLOCKS.register(eventBus);
        new MBlockInit().registerMItems();
        MBlockItemInit.MBLOCKITEMS.register(eventBus);
        MBlockItemInit.MBLOCKOREITEMS.register(eventBus);
        new MBlockItemInit().registerMItems();

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
    }

    @SubscribeEvent
    public void setupClient(final FMLClientSetupEvent event) {}

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
    }
}
