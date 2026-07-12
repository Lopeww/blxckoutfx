package com.lopew.blxckoutfx.client.render;

import com.lopew.blxckoutfx.client.integration.ae2.BlxckoutFXAe2Support;
import net.minecraft.util.ARGB;

public final class BlxckoutFXAe2TextColors {
    private static final int GRAY_TARGET_MIN = 170;
    private static final int GRAY_TARGET_MAX_OFFSET = 56;
    private static final int NAVY_TARGET = 0x6F82C8;
    private static final float NAVY_TARGET_LUMINANCE = 132.0F;
    private static final float NAVY_MIX_AMOUNT = 0.24F;
    private static final int BLACKOUT_TEXT_TARGET = 0xECECEC;
    private static final int BLACKOUT_ALERT_TEXT_TARGET = 0xFC0000;
    private static final int BLACKOUT_ALERT_SHADOW_TARGET = 0x3E0000;

    public static int adjustThemeTextColor(int argb) {
        if (!BlxckoutFXAe2Support.isBlxckoutThemeActive() || !BlxckoutFXShaders.isEnabled()) {
            return argb;
        }

        if (isAlertColor(argb)) {
            return argb;
        }

        if (BlxckoutFXShaders.isBlxckoutPresetActive()) {
            return adjustNavy(argb);
        }

        return adjustGray(argb);
    }

    public static int adjustBlackoutTextColor(int argb) {
        int alpha = ARGB.alpha(argb);
        int rgb = argb & 0xFFFFFF;

        if (alpha == 0 && rgb != 0) {
            alpha = 255;
        }

        if (isInventoryButtonTooltipColor(rgb)) {
            return ARGB.color(alpha, ARGB.red(rgb), ARGB.green(rgb), ARGB.blue(rgb));
        }

        if (isAlertColor(argb)) {
            int target = isDarkRedPass(rgb) ? BLACKOUT_ALERT_SHADOW_TARGET : BLACKOUT_ALERT_TEXT_TARGET;
            return ARGB.color(alpha, ARGB.red(target), ARGB.green(target), ARGB.blue(target));
        }

        if (rgb != 0 && getLuminance(argb) <= 64.0F) {
            return ARGB.color(alpha, ARGB.red(argb), ARGB.green(argb), ARGB.blue(argb));
        }

        return ARGB.color(
                alpha,
                ARGB.red(BLACKOUT_TEXT_TARGET),
                ARGB.green(BLACKOUT_TEXT_TARGET),
                ARGB.blue(BLACKOUT_TEXT_TARGET)
        );
    }

    private static boolean isAlertColor(int argb) {
        int red = ARGB.red(argb);
        int green = ARGB.green(argb);
        int blue = ARGB.blue(argb);
        return red >= 90 && red >= green + 28 && red >= blue + 28;
    }

    private static boolean isInventoryButtonTooltipColor(int rgb) {
        return rgb == 0xAAAAAA
                || rgb == 0xFFFFFF
                || rgb == 0xFFAA00
                || rgb == 0x616161;
    }

    private static int adjustGray(int argb) {
        if ((argb & 0xFFFFFF) == 0) {
            return argb;
        }

        int alpha = ARGB.alpha(argb);
        int red = ARGB.red(argb);
        int green = ARGB.green(argb);
        int blue = ARGB.blue(argb);

        float luminance = red * 0.299F + green * 0.587F + blue * 0.114F;
        int max = Math.max(red, Math.max(green, blue));
        int target = Math.min(255, Math.max(GRAY_TARGET_MIN, Math.max(Math.round(luminance), max + GRAY_TARGET_MAX_OFFSET)));
        return ARGB.color(alpha, target, target, target);
    }

    private static int adjustNavy(int argb) {
        int alpha = ARGB.alpha(argb);
        int red = ARGB.red(argb);
        int green = ARGB.green(argb);
        int blue = ARGB.blue(argb);

        if (!(blue >= red + 10 && blue >= green + 8 && blue >= 48)) {
            return argb;
        }

        float luminance = red * 0.299F + green * 0.587F + blue * 0.114F;
        if (luminance >= NAVY_TARGET_LUMINANCE) {
            return argb;
        }

        float scale = NAVY_TARGET_LUMINANCE / Math.max(1.0F, luminance);
        red = Math.min(255, Math.round(red * scale));
        green = Math.min(255, Math.round(green * scale));
        blue = Math.min(255, Math.round(blue * scale));

        red = mixChannel(red, ARGB.red(NAVY_TARGET));
        green = mixChannel(green, ARGB.green(NAVY_TARGET));
        blue = mixChannel(blue, ARGB.blue(NAVY_TARGET));

        return ARGB.color(alpha, red, green, blue);
    }

    private static int mixChannel(int from, int to) {
        return Math.round(from + (to - from) * NAVY_MIX_AMOUNT);
    }

    private static boolean isDarkRedPass(int rgb) {
        return getLuminance(rgb) <= 64.0F || ARGB.red(rgb) <= 96;
    }

    private static float getLuminance(int argb) {
        return ARGB.red(argb) * 0.299F + ARGB.green(argb) * 0.587F + ARGB.blue(argb) * 0.114F;
    }

    private BlxckoutFXAe2TextColors() {
    }
}
