package com.github.x3rmination.common.items;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;

import java.awt.*;

public class MBlockItemBase extends net.minecraft.item.BlockItem implements IItemColor {

    Color color;

    public MBlockItemBase(Block block, Properties properties, Color ingotColor) {
        super(block, properties);
        this.color = ingotColor;
    }

    @Override
    public int getColor(ItemStack itemStack, int index) {
        return color.getRGB();
    }

}
