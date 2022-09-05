package com.github.x3rmination.core.util;

import net.minecraft.util.Direction;

import java.util.ArrayList;
import java.util.List;

public final class CableHelper {

    private static final List<Direction> DIRECTIONS = new ArrayList<>();

    private CableHelper() {

    }

    public static List<Direction> getDirectionList() {
        if(DIRECTIONS.isEmpty()) {
            DIRECTIONS.add(Direction.NORTH);
            DIRECTIONS.add(Direction.EAST);
            DIRECTIONS.add(Direction.SOUTH);
            DIRECTIONS.add(Direction.WEST);
            DIRECTIONS.add(Direction.UP);
            DIRECTIONS.add(Direction.DOWN);
        }
        return DIRECTIONS;
    }
}
