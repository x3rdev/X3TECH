package com.github.x3rmination.common.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.awt.*;

public class MItemBase extends Item {

    Color color;

    public MItemBase(Properties properties, Color ingotColor) {
        super(properties);
        this.color = ingotColor;
    }

    public int getColor(ItemStack stack, int index) {
        return color.getRGB();
    }
}
