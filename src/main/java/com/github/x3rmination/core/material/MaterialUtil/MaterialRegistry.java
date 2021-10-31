package com.github.x3rmination.core.material.MaterialUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MaterialRegistry {

    public static final List<MaterialBase> materialList = new ArrayList<>();

    static {
        materialList.add(new MaterialBase("test1_material", new Color(200, 10, 44), 3, 1200, 3));
        materialList.add(new MaterialBase("test2_material", new Color(60, 100, 10), 5, 1, 2));
    }
}
