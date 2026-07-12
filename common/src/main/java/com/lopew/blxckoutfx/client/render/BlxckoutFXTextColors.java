package com.lopew.blxckoutfx.client.render;

import net.minecraft.util.ARGB;

public final class BlxckoutFXTextColors {
    private static final int READABLE_TEXT_BRIGHTNESS = 184;

    public static int adjustGeneralTextColor(int color, boolean forceOpaque) {
        if (!isDarkNeutral(color)) {
            return color;
        }

        int alpha = ARGB.alpha(color);
        if (alpha == 0 && (forceOpaque || (color & 0xFFFFFF) != 0)) {
            alpha = 255;
        }

        return ARGB.color(alpha, READABLE_TEXT_BRIGHTNESS, READABLE_TEXT_BRIGHTNESS, READABLE_TEXT_BRIGHTNESS);
    }

    private static boolean isDarkNeutral(int color) {
        int red = color >> 16 & 0xFF;
        int green = color >> 8 & 0xFF;
        int blue = color & 0xFF;
        int max = Math.max(red, Math.max(green, blue));
        int min = Math.min(red, Math.min(green, blue));

        return max <= 96 && max - min <= 10;
    }

    private BlxckoutFXTextColors() {
    }
}
