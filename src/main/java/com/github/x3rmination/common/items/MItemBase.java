package com.github.x3rmination.common.items;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.awt.*;

public class MItemBase extends Item implements IItemColor {

    Color color;

    public MItemBase(Properties properties, Color ingotColor) {
        super(properties);
        this.color = ingotColor;
    }

    @Override
    public int getColor(ItemStack stack, int index) {
        return color.getRGB();
    }
}
