package com.github.x3rmination.core.material.materialutil;

import java.awt.*;

public class MaterialBase {

    private final Color color;
    private final String name;
    private final int explosionResistance;
    private final int miningTime;
    private final int miningLevel;
    private final boolean hasOre;
    // Info storage class
    public MaterialBase(String name, Color color, int miningTime, int explosionResistance, int miningLevel, boolean hasOre){
        this.color = color;
        this.name = name;
        this.explosionResistance = explosionResistance;
        this.miningTime = miningTime;
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

    public int getMiningTime() {
        return this.miningTime;
    }

    public boolean materialHasOre() {
        return this.hasOre;
    }
}
