package com.github.x3rmination.core.util.color;

import com.github.x3rmination.common.blocks.MaterialBlockBase;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;

import javax.annotation.Nullable;

public class BlockColor implements IBlockColor {
    @Override
    public int getColor(BlockState blockState, @Nullable IBlockDisplayReader displayReader, @Nullable BlockPos blockPos, int index) {
        return ((MaterialBlockBase)blockState.getBlock()).getColor(blockState, displayReader, blockPos, index);
    }
}
