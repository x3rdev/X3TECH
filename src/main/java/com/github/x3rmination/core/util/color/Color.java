package com.github.x3rmination.core.util.color;

public class Color {
    private final int r;
    private final int g;
    private final int b;
    private final int a;

    public Color(int red, int green, int blue, int opacity) {
        this.r = red;
        this.g = green;
        this.b = blue;
        this.a = opacity;
    }

    public Color(int red, int green, int blue) {
        this(red, green, blue, 100);
    }

    public int getR() {
        return r;
    }

    public int getG() {
        return g;
    }

    public int getB() {
        return b;
    }

    public int getA() {
        return a;
    }

    public int getRGB() {
        //RGB stuff
        return ((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8)  | ((b & 0xFF));
    }
}
