package com.github.x3rmination.core.material.MaterialUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MaterialRegistry {

    public static final List<MaterialBase> materialList = new ArrayList<>();

    static {
        materialList.add(new MaterialBase("copper", new Color(237, 126, 57), 3, 1200, 3, true));
        materialList.add(new MaterialBase("tin", new Color(190, 204, 209), 5, 1, 2, true));
        materialList.add(new MaterialBase("bronze", new Color(230, 155, 101), 5, 1, 2, false));
    }
}
