package com.github.x3rmination.client.color;

import com.github.x3rmination.X3TECH;
import com.github.x3rmination.common.items.MBlockItemBase;
import com.github.x3rmination.common.items.MItemBase;
import com.github.x3rmination.core.material.materialinit.MBlockInit;
import com.github.x3rmination.core.material.materialinit.MBlockItemInit;
import com.github.x3rmination.core.material.materialinit.MItemInit;
import com.github.x3rmination.registry.ModRegistration;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = X3TECH.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ColorLoader {

    private ColorLoader(){

    }

    @SubscribeEvent
    public static void loadColors(ColorHandlerEvent.Item event) {
        ItemColors itemColors = event.getItemColors();
        BlockColors blockColors = event.getBlockColors();

        ModRegistration.MATERIALS.getEntries().forEach(ingObj -> itemColors.register((stack, index) -> index > 0 ? -1 : ((MItemBase)stack.getItem()).getColor(stack, 0), ingObj.get()));
        ModRegistration.MATERIAL_BLOCK_ITEMS.getEntries().forEach(ingObj -> itemColors.register((stack, index) -> index > 0 ? -1 : ((MBlockItemBase)stack.getItem()).getColor(stack, 0), ingObj.get()));
        ModRegistration.MATERIAL_BLOCKS.getEntries().forEach(blockObj -> blockColors.register(new BlockColor(), blockObj.get()));

        MItemInit.MITEMS.getEntries().forEach(ingObj -> itemColors.register((stack, index) -> index > 0 ? -1 : ((MItemBase)stack.getItem()).getColor(stack, 0), ingObj.get()));
        MBlockItemInit.MBLOCKITEMS.getEntries().forEach(blockItemObj -> itemColors.register((stack, index) -> index > 0 ? -1 : ((MBlockItemBase)stack.getItem()).getColor(stack, 0), blockItemObj.get()));
        MBlockItemInit.MBLOCKOREITEMS.getEntries().forEach(blockOreItemObj -> itemColors.register((stack, index) -> index > 0 ? -1 : ((MBlockItemBase)stack.getItem()).getColor(stack, 0), blockOreItemObj.get()));

        MBlockInit.MBLOCKS.getEntries().forEach(blockObj -> blockColors.register(new BlockColor(), blockObj.get()));
        MBlockInit.MOREBLOCKS.getEntries().forEach(blockObj -> blockColors.register(new BlockColor(), blockObj.get()));

    }

}
