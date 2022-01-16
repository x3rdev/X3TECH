package com.github.x3rmination.common.blocks.tile_entities.cables.power_cable;

import net.minecraft.util.Direction;

import java.util.ArrayList;
import java.util.List;

public final class PowerCableHelper {

    public static final List<Direction> directionList = new ArrayList<>();

    public PowerCableHelper(){}

    public List<Direction> getDirectionList() {
        if(directionList.isEmpty()) {
            directionList.add(Direction.NORTH);
            directionList.add(Direction.EAST);
            directionList.add(Direction.SOUTH);
            directionList.add(Direction.WEST);
            directionList.add(Direction.UP);
            directionList.add(Direction.DOWN);
        }
        return directionList;
    }
}
