package com.github.x3rmination.core.material.materialutil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MaterialRegistry {

    public static final List<MaterialBase> materialList = new ArrayList<>();

    static {
        materialList.add(new MaterialBase("copper", new Color(237, 126, 57), 3, 1200, 2, true));
        materialList.add(new MaterialBase("tin", new Color(190, 204, 209), 3, 1, 2, true));
        materialList.add(new MaterialBase("bronze", new Color(230, 155, 101), 3, 1, 2, false));
        materialList.add(new MaterialBase("cobalt", new Color(66, 78, 245), 3,1, 2, true));
    }
}
