package com.cft.utils;

public class IntColorUtils {
    public static final int BLACK  = fromRgb(0, 0, 0);
    public static final int WHITE  = fromRgb(255, 255, 255);

    public static final int RED    = fromRgb(255, 0, 0);
    public static final int BLUE   = fromRgb(0, 0, 255);
    public static final int GREEN  = fromRgb(0, 255, 0);

    public static final int YELLOW = fromRgb(255, 255, 0);
    public static final int ORANGE = fromRgb(255, 165, 0);

    public static final int PURPLE = fromRgb(128, 0, 128);
    public static final int PINK   = fromRgb(255, 192, 203);

    public static final int BROWN  = fromRgb(165, 42, 42);
    public static final int GRAY   = fromRgb(128, 128, 128);

    public static final int[] COMMON_COLORS = {
            BLACK, WHITE,
            RED, BLUE, GREEN,
            YELLOW, ORANGE,
            PURPLE, PINK,
            BROWN, GRAY
    };

    public static int fromArgb(int r, int g, int b, int a) {
        return ((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
    }

    public static int fromRgb(int r, int g, int b) {
        return fromArgb(r, g, b, 0xFF);
    }
}
