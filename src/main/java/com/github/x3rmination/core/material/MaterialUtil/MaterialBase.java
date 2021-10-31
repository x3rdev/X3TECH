package com.github.x3rmination.core.material.MaterialUtil;

import java.awt.*;

public class MaterialBase {

    private final Color color;
    private final String name;
    private final int explosionResistance;
    private final int miningResistance;
    private final int miningLevel;

    public MaterialBase(String name, Color color, int miningResistance, int explosionResistance, int miningLevel){
        this.color = color;
        this.name = name;
        this.explosionResistance = explosionResistance;
        this.miningResistance = miningResistance;
        this.miningLevel = miningLevel;
    }

    public Color getColor() {
        return this.color;
    }

    public String getName() {
        return this.name;
    }

    public int getExplosionResistance() {
        return this.explosionResistance;
    }

    public int getMiningLevel() {
        return this.miningLevel;
    }

    public int getMiningResistance() {
        return miningResistance;
    }
}
