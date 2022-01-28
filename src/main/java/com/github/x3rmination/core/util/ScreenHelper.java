package com.github.x3rmination.core.util;

public final class ScreenHelper {

    ScreenHelper(){}

    public static boolean isBetween(double mouseX, double mouseY, int posX, int posY, int originX, int originY, int endX, int endY) {
        return mouseX >= posX + originX && mouseX <= posX + endX && mouseY >= posY + originY && mouseY <= posY + endY;
    }
}
