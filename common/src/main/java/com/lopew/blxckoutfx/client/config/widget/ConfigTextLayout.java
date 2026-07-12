package com.lopew.blxckoutfx.client.config.widget;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public final class ConfigTextLayout {
    private static final int LINE_SPACING = 2;
    private static final String ELLIPSIS = "...";

    private ConfigTextLayout() {
    }

    public static int wrappedHeight(Font font, Component text, int width) {
        if (text == null || width <= 0) {
            return 0;
        }

        List<?> lines = font.split(text, width);
        if (lines.isEmpty()) {
            return 0;
        }

        return lines.size() * font.lineHeight + Math.max(0, lines.size() - 1) * LINE_SPACING;
    }

    public static int drawWrapped(GuiGraphics graphics, Font font, Component text, int x, int y, int width, int color) {
        if (text == null || width <= 0) {
            return 0;
        }

        List<FormattedCharSequence> lines = font.split(text, width);
        int drawnHeight = 0;
        for (FormattedCharSequence line : lines) {
            graphics.drawString(font, line, x, y + drawnHeight, color, false);
            drawnHeight += font.lineHeight + LINE_SPACING;
        }

        return lines.isEmpty() ? 0 : drawnHeight - LINE_SPACING;
    }

    public static Component ellipsize(Font font, Component text, int width) {
        if (text == null) {
            return Component.empty();
        }
        if (width <= 0) {
            return Component.literal(ELLIPSIS);
        }

        String value = text.getString();
        if (font.width(value) <= width) {
            return text;
        }

        int clippedWidth = Math.max(0, width - font.width(ELLIPSIS));
        String clipped = font.plainSubstrByWidth(value, clippedWidth);
        return Component.literal(clipped + ELLIPSIS);
    }

    public static void drawClippedOrMarquee(
            GuiGraphics graphics,
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
            graphics.drawString(font, ellipsize(font, text, width), x, y, color, false);
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
        graphics.drawString(font, text, x - offset, y, color, false);
        graphics.disableScissor();
    }
}
