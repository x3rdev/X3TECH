package com.github.x3rmination.core.util.block;

import net.minecraft.state.BooleanProperty;

public class CustomBlockProperties {
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty EAST = BooleanProperty.create("east");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty WEST = BooleanProperty.create("west");
    public static final BooleanProperty UP = BooleanProperty.create("up");
    public static final BooleanProperty DOWN = BooleanProperty.create("down");
}
