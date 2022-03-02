package com.github.x3rmination.core.util;

import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;

import java.util.ArrayList;
import java.util.List;

public class CustomBlockProperties {

    public static List<String> DirectionList = new ArrayList<>();

    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty EAST = BooleanProperty.create("east");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty WEST = BooleanProperty.create("west");
    public static final BooleanProperty UP = BooleanProperty.create("up");
    public static final BooleanProperty DOWN = BooleanProperty.create("down");
    public static final BooleanProperty HAS_BRAIN = BooleanProperty.create("has_brain");
    public static final IntegerProperty ITEM_NORTH = IntegerProperty.create("item_north", 1, 3);
    public static final IntegerProperty ITEM_EAST = IntegerProperty.create("item_east", 1, 3);
    public static final IntegerProperty ITEM_SOUTH = IntegerProperty.create("item_south", 1, 3);
    public static final IntegerProperty ITEM_WEST = IntegerProperty.create("item_west", 1, 3);
    public static final IntegerProperty ITEM_UP = IntegerProperty.create("item_up", 1, 3);
    public static final IntegerProperty ITEM_DOWN = IntegerProperty.create("item_down", 1, 3);

    public static List<String> getDirList(){
        if(DirectionList.isEmpty()) {
            DirectionList.add("north");
            DirectionList.add("east");
            DirectionList.add("south");
            DirectionList.add("west");
            DirectionList.add("up");
            DirectionList.add("down");
        }
        return DirectionList;
    }
}
