package com.github.x3rmination.core.material.MaterialUtil;

import java.awt.*;

public class MaterialBase {

    private Color color;
    private String name;

    public MaterialBase(String name, Color color){
        this.color = color;
        this.name = name;
    }

    public Color getColor() {
        return this.color;
    }

    public String getName() {
        return this.name;
    }
}
