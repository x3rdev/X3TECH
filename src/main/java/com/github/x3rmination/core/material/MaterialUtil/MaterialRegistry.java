package com.github.x3rmination.core.material.MaterialUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MaterialRegistry {

    public static final List<MaterialBase> materialList = new ArrayList<>();

    static {
        materialList.add(new MaterialBase("test1_material", new Color(200, 10, 44)));
        materialList.add(new MaterialBase("test2_material", new Color(60, 100, 10)));
        materialList.add(new MaterialBase("test3_material", new Color(50, 40, 200)));
        materialList.add(new MaterialBase("test4_material", new Color(41, 40, 60)));
        materialList.add(new MaterialBase("test5_material", new Color(50, 40, 10)));
        materialList.add(new MaterialBase("test6_material", new Color(100, 40, 200)));
        materialList.add(new MaterialBase("test7_material", new Color(0, 140, 22)));
        materialList.add(new MaterialBase("test8_material", new Color(160, 50, 120)));
        materialList.add(new MaterialBase("test9_material", new Color(60, 40, 11)));
        materialList.add(new MaterialBase("test10_material", new Color(230, 40, 200)));
        materialList.add(new MaterialBase("test11_material", new Color(10, 150, 10)));
        materialList.add(new MaterialBase("test12_material", new Color(50, 40, 138)));
        materialList.add(new MaterialBase("test13_material", new Color(29, 100, 233)));
        materialList.add(new MaterialBase("test14_material", new Color(127, 230, 100)));
        materialList.add(new MaterialBase("test15_material", new Color(50, 54, 30)));

    }
}
