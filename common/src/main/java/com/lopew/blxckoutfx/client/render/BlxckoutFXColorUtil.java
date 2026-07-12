package com.lopew.blxckoutfx.client.render;

public final class BlxckoutFXColorUtil {
    public static int darkenColor(int color, float factor) {
        float scale = 1.0F / Math.max(1.0F, factor);

        int alpha = color & 0xFF000000;
        int red = Math.round(((color >> 16) & 0xFF) * scale);
        int green = Math.round(((color >> 8) & 0xFF) * scale);
        int blue = Math.round((color & 0xFF) * scale);

        return alpha | (red << 16) | (green << 8) | blue;
    }

    private BlxckoutFXColorUtil() {
    }
}
