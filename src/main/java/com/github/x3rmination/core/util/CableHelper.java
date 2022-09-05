package com.github.x3rmination.core.util;

import com.google.common.collect.ImmutableList;
import net.minecraft.util.Direction;

import java.util.List;

public final class CableHelper {

    private CableHelper() {

    }

    public static List<Direction> getDirectionList() {
        return ImmutableList.of(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.UP, Direction.DOWN);
    }
}
