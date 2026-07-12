package com.lopew.blxckoutfx.client.render;

public final class BlxckoutFXTextColors {
    private static final int DARK_NEUTRAL_MAX_CHANNEL = 120;
    private static final int DARK_NEUTRAL_MAX_SATURATION = 28;
    private static final int READABLE_BRIGHTNESS = 170;
    private static final int READABLE_LIFT = 90;

    public static int adjustGeneralTextColor(int color) {
        int a = (color >>> 24) & 0xFF;
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;

        if (!isDarkNeutral(r, g, b)) {
            return color;
        }

        int maxChannel = Math.max(r, Math.max(g, b));
        int target = Math.min(255, Math.max(READABLE_BRIGHTNESS, maxChannel + READABLE_LIFT));
        int lift = target - maxChannel;

        int newR = Math.min(255, r + lift);
        int newG = Math.min(255, g + lift);
        int newB = Math.min(255, b + lift);

        return (a << 24) | (newR << 16) | (newG << 8) | newB;
    }

    private static boolean isDarkNeutral(int r, int g, int b) {
        int maxChannel = Math.max(r, Math.max(g, b));
        int minChannel = Math.min(r, Math.min(g, b));
        return maxChannel <= DARK_NEUTRAL_MAX_CHANNEL && (maxChannel - minChannel) <= DARK_NEUTRAL_MAX_SATURATION;
    }

    private BlxckoutFXTextColors() {
    }
}
