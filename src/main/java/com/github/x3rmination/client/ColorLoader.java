package com.github.x3rmination.client;

import com.github.x3rmination.X3TECH;
import com.github.x3rmination.common.items.MBlockItemBase;
import com.github.x3rmination.common.items.MItemBase;
import com.github.x3rmination.core.material.MaterialInit.MBlockInit;
import com.github.x3rmination.core.material.MaterialInit.MBlockItemInit;
import com.github.x3rmination.core.material.MaterialInit.MItemInit;
import com.github.x3rmination.init.BlockInit;
import com.github.x3rmination.init.ItemInit;
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

        ItemInit.MATERIALS.getEntries().forEach(ingObj -> itemColors.register((stack, index) -> index > 0 ? -1 : ((MItemBase)stack.getItem()).getColor(stack, 0), ingObj.get()));
        ItemInit.BLOCK_ITEMS.getEntries().forEach(ingObj -> itemColors.register((stack, index) -> index > 0 ? -1 : ((MBlockItemBase)stack.getItem()).getColor(stack, 0), ingObj.get()));
        BlockInit.MATERIAL_BLOCKS.getEntries().forEach(blockObj -> blockColors.register(new BlockColor(), blockObj.get()));

        MItemInit.MITEMS.getEntries().forEach(ingObj -> itemColors.register((stack, index) -> index > 0 ? -1 : ((MItemBase)stack.getItem()).getColor(stack, 0), ingObj.get()));
        MBlockItemInit.MBLOCKITEMS.getEntries().forEach(ingObj -> itemColors.register((stack, index) -> index > 0 ? -1 : ((MBlockItemBase)stack.getItem()).getColor(stack, 0), ingObj.get()));
        MBlockInit.MBLOCKS.getEntries().forEach(blockObj -> blockColors.register(new BlockColor(), blockObj.get()));

    }

}
