package com.lopew.blxckoutfx.client;

import net.minecraft.util.FastColor;

public final class BlxckoutFXTextColors {
    private static final int DEFAULT_READABLE_TEXT_BRIGHTNESS = 170;
    private static final int PATCHOULI_READABLE_TEXT_BRIGHTNESS = 188;
    private static final int DARK_NEUTRAL_MAX_THRESHOLD = 120;
    private static final int DARK_NEUTRAL_SATURATION_THRESHOLD = 28;
    private static final float DARK_COLOR_MIN_LUMINANCE = 118.0F;
    private static final int PATCHOULI_SOFT_ACCENT_SATURATION_THRESHOLD = 64;
    private static final float PATCHOULI_SOFT_ACCENT_MIN_LUMINANCE = 88.0F;
    private static final float PATCHOULI_SOFT_ACCENT_TARGET_LUMINANCE = 72.0F;
    private static final int PATCHOULI_SOFT_GREEN_ACCENT = 0x2F7D32;
    private static final int PATCHOULI_SOFT_PURPLE_ACCENT = 0x7E4A9E;

    public static int adjustGeneralTextColor(int color, boolean forceOpaque, boolean usePatchouliBrightness) {
        if (!isDarkNeutral(color)) {
            return color;
        }

        int max = getMaxChannel(color);
        int readableBrightness = usePatchouliBrightness ? PATCHOULI_READABLE_TEXT_BRIGHTNESS : DEFAULT_READABLE_TEXT_BRIGHTNESS;
        int lifted = Math.min(255, Math.max(readableBrightness, max + 90));
        return FastColor.ARGB32.color(getEffectiveAlpha(color, forceOpaque), lifted, lifted, lifted);
    }

    public static int adjustPatchouliBookTextColor(int color) {
        if (!BlxckoutFXShaders.isEnabled()) {
            return color;
        }

        int rgb = color & 0xFFFFFF;

        if (BlxckoutFXShaders.isSoftPresetActive()) {
            return adjustPatchouliSoftAccentColor(rgb);
        }

        if (isDarkNeutral(rgb)) {
            return PATCHOULI_READABLE_TEXT_BRIGHTNESS << 16
                    | PATCHOULI_READABLE_TEXT_BRIGHTNESS << 8
                    | PATCHOULI_READABLE_TEXT_BRIGHTNESS;
        }

        float luminance = getLuminance(rgb);
        int max = getMaxChannel(rgb);

        if (max == 0 || luminance >= DARK_COLOR_MIN_LUMINANCE) {
            return color;
        }

        float scale = Math.min(255.0F / max, DARK_COLOR_MIN_LUMINANCE / Math.max(1.0F, luminance));
        int red = Math.min(255, Math.round(FastColor.ARGB32.red(rgb) * scale));
        int green = Math.min(255, Math.round(FastColor.ARGB32.green(rgb) * scale));
        int blue = Math.min(255, Math.round(FastColor.ARGB32.blue(rgb) * scale));

        if (getLuminance(red, green, blue) < DARK_COLOR_MIN_LUMINANCE) {
            return mixTowardWhite(red, green, blue, DARK_COLOR_MIN_LUMINANCE);
        }

        return red << 16 | green << 8 | blue;
    }

    private static int adjustPatchouliSoftAccentColor(int color) {
        int red = FastColor.ARGB32.red(color);
        int green = FastColor.ARGB32.green(color);
        int blue = FastColor.ARGB32.blue(color);
        int max = Math.max(red, Math.max(green, blue));
        int min = Math.min(red, Math.min(green, blue));

        if (max - min < PATCHOULI_SOFT_ACCENT_SATURATION_THRESHOLD) {
            return color;
        }

        float luminance = getLuminance(red, green, blue);

        if (luminance < PATCHOULI_SOFT_ACCENT_MIN_LUMINANCE) {
            return color;
        }

        if (isGreenAccent(red, green, blue)) {
            return mixColor(color, PATCHOULI_SOFT_GREEN_ACCENT, 0.72F);
        }

        if (isPinkAccent(red, green, blue)) {
            return mixColor(color, PATCHOULI_SOFT_PURPLE_ACCENT, 0.72F);
        }

        float scale = PATCHOULI_SOFT_ACCENT_TARGET_LUMINANCE / Math.max(1.0F, luminance);
        return Math.round(red * scale) << 16
                | Math.round(green * scale) << 8
                | Math.round(blue * scale);
    }

    private static boolean isGreenAccent(int red, int green, int blue) {
        return green >= red + 32 && green >= blue + 32;
    }

    private static boolean isPinkAccent(int red, int green, int blue) {
        return red >= green + 32 && blue >= green + 16 && red >= 120 && blue >= 80;
    }

    private static int mixColor(int color, int targetColor, float amount) {
        int red = mixChannel(FastColor.ARGB32.red(color), FastColor.ARGB32.red(targetColor), amount);
        int green = mixChannel(FastColor.ARGB32.green(color), FastColor.ARGB32.green(targetColor), amount);
        int blue = mixChannel(FastColor.ARGB32.blue(color), FastColor.ARGB32.blue(targetColor), amount);
        return red << 16 | green << 8 | blue;
    }

    private static int mixChannel(int from, int to, float amount) {
        return Math.round(from + (to - from) * amount);
    }

    private static boolean isDarkNeutral(int color) {
        int max = getMaxChannel(color);
        int min = Math.min(FastColor.ARGB32.red(color), Math.min(FastColor.ARGB32.green(color), FastColor.ARGB32.blue(color)));
        int saturation = max - min;
        return max <= DARK_NEUTRAL_MAX_THRESHOLD && saturation <= DARK_NEUTRAL_SATURATION_THRESHOLD;
    }

    private static int getMaxChannel(int color) {
        return Math.max(FastColor.ARGB32.red(color), Math.max(FastColor.ARGB32.green(color), FastColor.ARGB32.blue(color)));
    }

    private static float getLuminance(int color) {
        return getLuminance(FastColor.ARGB32.red(color), FastColor.ARGB32.green(color), FastColor.ARGB32.blue(color));
    }

    private static float getLuminance(int red, int green, int blue) {
        return red * 0.299F + green * 0.587F + blue * 0.114F;
    }

    private static int mixTowardWhite(int red, int green, int blue, float targetLuminance) {
        float luminance = getLuminance(red, green, blue);
        float amount = (targetLuminance - luminance) / Math.max(1.0F, 255.0F - luminance);
        amount = Math.max(0.0F, Math.min(1.0F, amount));

        int mixedRed = mixChannelTowardWhite(red, amount);
        int mixedGreen = mixChannelTowardWhite(green, amount);
        int mixedBlue = mixChannelTowardWhite(blue, amount);
        return mixedRed << 16 | mixedGreen << 8 | mixedBlue;
    }

    private static int mixChannelTowardWhite(int channel, float amount) {
        return Math.min(255, Math.round(channel + (255 - channel) * amount));
    }

    private static int getEffectiveAlpha(int color, boolean forceOpaque) {
        int alpha = FastColor.ARGB32.alpha(color);

        if (alpha == 0 && (forceOpaque || (color & 0xFFFFFF) != 0)) {
            return 255;
        }

        return alpha;
    }

    private BlxckoutFXTextColors() {
    }
}
