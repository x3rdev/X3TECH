package com.github.x3rmination.registry;

import com.github.x3rmination.X3TECH;
import com.github.x3rmination.common.blocks.tile_entities.archive.combustion_generator.CombustionGeneratorContainer;
import com.github.x3rmination.common.blocks.tile_entities.archive.combustion_generator.CombustionGeneratorScreen;
import com.github.x3rmination.common.blocks.tile_entities.archive.double_press.DoublePressContainer;
import com.github.x3rmination.common.blocks.tile_entities.archive.double_press.DoublePressScreen;
import com.github.x3rmination.common.blocks.tile_entities.archive.powered_furnace.PoweredFurnaceContainer;
import com.github.x3rmination.common.blocks.tile_entities.archive.powered_furnace.PoweredFurnaceScreen;
import com.github.x3rmination.common.blocks.tile_entities.archive.powered_pulverizer.PoweredPulverizerContainer;
import com.github.x3rmination.common.blocks.tile_entities.archive.powered_pulverizer.PoweredPulverizerScreen;
import com.github.x3rmination.common.blocks.tile_entities.archive.single_press.SinglePressContainer;
import com.github.x3rmination.common.blocks.tile_entities.archive.single_press.SinglePressScreen;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ContainerTypeInit {

    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, X3TECH.MOD_ID);

    public static final RegistryObject<ContainerType<PoweredFurnaceContainer>> POWERED_FURNACE = CONTAINERS.register("powered_furnace",
            () -> IForgeContainerType.create(PoweredFurnaceContainer::new));
    public static final RegistryObject<ContainerType<PoweredPulverizerContainer>> POWERED_PULVERIZER = CONTAINERS.register("powered_pulverizer",
            () -> IForgeContainerType.create(PoweredPulverizerContainer::new));
    public static final RegistryObject<ContainerType<SinglePressContainer>> SINGLE_PRESS = CONTAINERS.register("single_press",
            () -> IForgeContainerType.create(SinglePressContainer::new));
    public static final RegistryObject<ContainerType<DoublePressContainer>> DOUBLE_PRESS = CONTAINERS.register("double_press",
            () -> IForgeContainerType.create(DoublePressContainer::new));
    public static final RegistryObject<ContainerType<CombustionGeneratorContainer>> COMBUSTION_GENERATOR = CONTAINERS.register("combustion_generator",
            () -> IForgeContainerType.create(CombustionGeneratorContainer::new));

    @OnlyIn(Dist.CLIENT)
    public static void registerScreens(FMLClientSetupEvent event) {
        ScreenManager.register(POWERED_FURNACE.get(), PoweredFurnaceScreen::new);
        ScreenManager.register(POWERED_PULVERIZER.get(), PoweredPulverizerScreen::new);
        ScreenManager.register(SINGLE_PRESS.get(), SinglePressScreen::new);
        ScreenManager.register(DOUBLE_PRESS.get(), DoublePressScreen::new);
        ScreenManager.register(COMBUSTION_GENERATOR.get(), CombustionGeneratorScreen::new);
    }
}
