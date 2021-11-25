package com.github.x3rmination.registry.init;

import com.github.x3rmination.common.blocks.tile_entities.double_press.DoublePressContainer;
import com.github.x3rmination.common.blocks.tile_entities.double_press.DoublePressScreen;
import com.github.x3rmination.common.blocks.tile_entities.powered_furnace.PoweredFurnaceContainer;
import com.github.x3rmination.common.blocks.tile_entities.powered_furnace.PoweredFurnaceScreen;
import com.github.x3rmination.common.blocks.tile_entities.powered_pulverizer.PoweredPulverizerContainer;
import com.github.x3rmination.common.blocks.tile_entities.powered_pulverizer.PoweredPulverizerScreen;
import com.github.x3rmination.registry.ModRegistration;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.network.IContainerFactory;

public class ContainerTypeInit {

    public static final RegistryObject<ContainerType<PoweredFurnaceContainer>> POWERED_FURNACE = register("powered_furnace", PoweredFurnaceContainer::new);
    public static final RegistryObject<ContainerType<PoweredPulverizerContainer>> POWERED_PULVERIZER = register("powered_pulverizer", PoweredPulverizerContainer::new);
    public static final RegistryObject<ContainerType<DoublePressContainer>> DOUBLE_PRESS = register("double_press", DoublePressContainer::new);


    public static void register() {}

    @OnlyIn(Dist.CLIENT)
    public static void registerScreens(FMLClientSetupEvent event) {
        ScreenManager.register(POWERED_FURNACE.get(), PoweredFurnaceScreen::new);
        ScreenManager.register(POWERED_PULVERIZER.get(), PoweredPulverizerScreen::new);
        ScreenManager.register(DOUBLE_PRESS.get(), DoublePressScreen::new);
    }

    private static <T extends Container> RegistryObject<ContainerType<T>> register(String name, IContainerFactory<T> factory) {
        return ModRegistration.CONTAINERS.register(name, () -> IForgeContainerType.create(factory));
    }
}
