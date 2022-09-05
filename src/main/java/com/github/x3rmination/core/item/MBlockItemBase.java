package com.github.x3rmination.core.item;

import com.github.x3rmination.core.util.color.Color;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class MBlockItemBase extends net.minecraft.item.BlockItem{

    Color color;

    public MBlockItemBase(Block block, Properties properties, Color ingotColor) {
        super(block, properties);
        this.color = ingotColor;
    }


    public int getColor(ItemStack itemStack, int index) {
        return color.getRGB();
    }

}
