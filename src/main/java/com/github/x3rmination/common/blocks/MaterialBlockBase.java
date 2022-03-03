package com.github.x3rmination.common.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;

import javax.annotation.Nullable;
import java.awt.*;

public class MaterialBlockBase extends Block {

    Color color;

    public MaterialBlockBase(AbstractBlock.Properties properties, Color ingotColor) {
        super(properties);
        this.color = ingotColor;
    }

    public int getColor(BlockState blockState, @Nullable IBlockDisplayReader displayReader, @Nullable BlockPos blockPos, int index) {
        return color.getRGB();
    }
}
