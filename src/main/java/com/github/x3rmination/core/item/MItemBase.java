package com.github.x3rmination.core.item;

import com.github.x3rmination.core.util.color.Color;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

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
