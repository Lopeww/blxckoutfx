package com.lopew.blxckoutfx.client.config.widget;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public final class ConfigTextLayout {
    private static final String ELLIPSIS = "...";

    private ConfigTextLayout() {
    }

    public static int lineHeight(Font font) {
        return font.lineHeight;
    }

    public static int controlTextY(Font font, int topY, int height) {
        return topY + Math.max(0, (height - lineHeight(font) + 1) / 2);
    }

    public static List<FormattedCharSequence> wrap(Font font, Component text, int width) {
        if (text == null || width <= 0) {
            return List.of();
        }

        return font.split(text, width);
    }

    public static int wrappedHeight(Font font, Component text, int width) {
        return wrap(font, text, width).size() * lineHeight(font);
    }

    public static void drawWrapped(GuiGraphicsExtractor graphics, Font font, Component text, int x, int y, int width, int color) {
        int lineY = y;
        for (FormattedCharSequence line : wrap(font, text, width)) {
            graphics.text(font, line, x, lineY, color, false);
            lineY += lineHeight(font);
        }
    }

    public static Component ellipsize(Font font, Component text, int width) {
        if (text == null || width <= 0) {
            return Component.empty();
        }

        String raw = text.getString();
        if (font.width(raw) <= width) {
            return text;
        }

        int ellipsisWidth = font.width(ELLIPSIS);
        if (ellipsisWidth >= width) {
            return Component.literal(font.plainSubstrByWidth(ELLIPSIS, width));
        }

        String trimmed = font.plainSubstrByWidth(raw, width - ellipsisWidth);
        return Component.literal(trimmed + ELLIPSIS);
    }

    public static void drawClippedOrMarquee(
            GuiGraphicsExtractor graphics,
            Font font,
            Component text,
            int x,
            int y,
            int width,
            int color,
            boolean marquee
    ) {
        if (text == null || width <= 0) {
            return;
        }

        if (!marquee || font.width(text) <= width) {
            graphics.text(font, ellipsize(font, text, width), x, y, color, false);
            return;
        }

        int textWidth = font.width(text);
        int cycle = Math.max(1, textWidth - width);
        int frame = (int) ((System.currentTimeMillis() / 70L) % (cycle * 2L + 24L));
        int offset;
        if (frame < 12) {
            offset = 0;
        } else if (frame < cycle + 12) {
            offset = frame - 12;
        } else {
            offset = Math.max(0, cycle - (frame - cycle - 12));
        }

        graphics.enableScissor(x, y - 1, x + width, y + font.lineHeight + 1);
        graphics.text(font, text, x - offset, y, color, false);
        graphics.disableScissor();
    }
}
