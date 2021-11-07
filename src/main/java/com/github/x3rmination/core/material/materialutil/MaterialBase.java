package com.github.x3rmination.core.material.materialutil;

import java.awt.*;

public class MaterialBase {

    private final Color color;
    private final String name;
    private final int explosionResistance;
    private final int miningResistance;
    private final int miningLevel;
    private final boolean hasOre;

    public MaterialBase(String name, Color color, int miningResistance, int explosionResistance, int miningLevel, boolean hasOre){
        this.color = color;
        this.name = name;
        this.explosionResistance = explosionResistance;
        this.miningResistance = miningResistance;
        this.miningLevel = miningLevel;
        this.hasOre = hasOre;
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
        return this.miningResistance;
    }

    public boolean materialHasOre() {
        return this.hasOre;
    }
}
