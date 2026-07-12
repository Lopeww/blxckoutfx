package com.lopew.blxckoutfx.client.render;

import net.minecraft.util.ARGB;

public final class BlxckoutFXColorUtil {
    public static int darkenColor(int color, float factor) {
        float clampedFactor = Math.max(1.0F, factor);
        float scale = 1.0F / clampedFactor;

        int alpha = ARGB.alpha(color);
        int red = Math.round(ARGB.red(color) * scale);
        int green = Math.round(ARGB.green(color) * scale);
        int blue = Math.round(ARGB.blue(color) * scale);

        return ARGB.color(alpha, red, green, blue);
    }

    private BlxckoutFXColorUtil() {
    }
}
