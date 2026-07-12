package com.lopew.blxckoutfx.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.Locale;
import java.util.function.BooleanSupplier;

final class InventoryPresetButton extends GuiButton {
    private static final double DRAG_THRESHOLD = 3.0D;
    private static final int CLASSIC_WIDTH = 95;
    private static final int SHORT_WIDTH = 20;
    private static final int BUTTON_HEIGHT = 20;
    private static final float CLASSIC_TEXT_SCALE = 1.12F;
    private static final int CLASSIC_TEXT_RIGHT_PADDING = 6;
    private static final int CLASSIC_HOVER_LOCK_RESERVE = 19;
    private static final int GLYPH_WIDTH = 8;
    private static final int GLYPH_HEIGHT = 8;
    private static final int LOCK_ICON_SIZE = 12;
    private static final ResourceLocation AE2_ICON = new ResourceLocation("blxckoutfx", "textures/gui/ae2.png");
    private static final int AE2_ICON_SOURCE_SIZE = 1254;
    private static final int AE2_ICON_SIZE = 11;
    private static final int AE2_ICON_CROP_X = 126;
    private static final int AE2_ICON_CROP_Y = 45;
    private static final int AE2_ICON_CROP_SIZE = 1128;

    private BlxckoutFXClientConfig.InventoryButtonStyle style;
    private final boolean ae2Screen;
    private final Runnable pressAction;
    private final PositionChanged positionChanged;
    private final BooleanSupplier locked;
    private int screenWidth;
    private int screenHeight;
    private String labelSource = "";
    private boolean dragging;
    private boolean movedWhileDragging;
    private double dragStartMouseX;
    private double dragStartMouseY;
    private int dragStartX;
    private int dragStartY;

    InventoryPresetButton(int id,
                          int x,
                          int y,
                          BlxckoutFXClientConfig.InventoryButtonStyle style,
                          boolean ae2Screen,
                          Runnable pressAction,
                          PositionChanged positionChanged,
                          BooleanSupplier locked,
                          int screenWidth,
                          int screenHeight) {
        super(id, x, y, widthFor(style), heightFor(style), "");
        this.style = style == null ? BlxckoutFXClientConfig.InventoryButtonStyle.Classic : style;
        this.ae2Screen = ae2Screen;
        this.pressAction = pressAction;
        this.positionChanged = positionChanged;
        this.locked = locked;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    static int widthFor(BlxckoutFXClientConfig.InventoryButtonStyle style) {
        return style == BlxckoutFXClientConfig.InventoryButtonStyle.Short ? SHORT_WIDTH : CLASSIC_WIDTH;
    }

    static int heightFor(BlxckoutFXClientConfig.InventoryButtonStyle style) {
        return BUTTON_HEIGHT;
    }

    void setStyle(BlxckoutFXClientConfig.InventoryButtonStyle style) {
        this.style = style == null ? BlxckoutFXClientConfig.InventoryButtonStyle.Classic : style;
        this.width = widthFor(this.style);
        this.height = heightFor(this.style);
    }

    void setLabelSource(String labelSource) {
        this.labelSource = labelSource == null ? "" : labelSource;
    }

    void setScreenSize(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    boolean isMouseInside(int mouseX, int mouseY) {
        return mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
    }

    void beginDrag(int mouseX, int mouseY) {
        if (isLocked() || !this.enabled || !this.visible || !isMouseInside(mouseX, mouseY)) {
            return;
        }

        this.dragging = true;
        this.movedWhileDragging = false;
        this.dragStartMouseX = mouseX;
        this.dragStartMouseY = mouseY;
        this.dragStartX = this.x;
        this.dragStartY = this.y;
    }

    boolean isDragging() {
        return this.dragging;
    }

    void updateDrag(int mouseX, int mouseY) {
        if (!this.dragging || isLocked()) {
            return;
        }

        double totalDragX = mouseX - this.dragStartMouseX;
        double totalDragY = mouseY - this.dragStartMouseY;
        if (Math.abs(totalDragX) >= DRAG_THRESHOLD || Math.abs(totalDragY) >= DRAG_THRESHOLD) {
            this.movedWhileDragging = true;
        }

        if (this.movedWhileDragging) {
            this.x = clamp(this.dragStartX + (int) Math.round(totalDragX), 0, Math.max(0, this.screenWidth - this.width));
            this.y = clamp(this.dragStartY + (int) Math.round(totalDragY), 0, Math.max(0, this.screenHeight - this.height));
        }
    }

    void finishDrag() {
        if (!this.dragging) {
            return;
        }

        this.dragging = false;
        if (isLocked()) {
            this.movedWhileDragging = false;
            return;
        }

        if (this.movedWhileDragging) {
            this.movedWhileDragging = false;
            if (this.positionChanged != null) {
                this.positionChanged.positionChanged(this);
            }
            return;
        }

        activate();
    }

    void cancelDrag() {
        this.dragging = false;
        this.movedWhileDragging = false;
    }

    void activate() {
        if (this.pressAction != null) {
            this.pressAction.run();
        }
    }

    void clampToScreen(int screenWidth, int screenHeight) {
        this.x = clamp(this.x, 0, Math.max(0, screenWidth - this.width));
        this.y = clamp(this.y, 0, Math.max(0, screenHeight - this.height));
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (!this.visible) {
            return;
        }

        String originalLabel = this.displayString;
        this.displayString = "";
        super.drawButton(mc, mouseX, mouseY, partialTicks);
        this.displayString = originalLabel;

        if (this.style == BlxckoutFXClientConfig.InventoryButtonStyle.Short) {
            renderShortLabel();
        } else {
            renderClassicLabel(mc);
        }

        if (this.hovered && this.style == BlxckoutFXClientConfig.InventoryButtonStyle.Classic) {
            renderLockIndicator();
        }
    }

    private void renderShortLabel() {
        int textX = this.x + (this.width - GLYPH_WIDTH) / 2;
        int textY = this.y + (this.height - GLYPH_HEIGHT) / 2;
        int color = this.enabled ? 0xFFD4D4D4 : 0xFF8C8C8C;
        BlxckoutFXRenderContext.suppressTextureDarkening();
        try {
            drawGlyph(shortGlyphForLabel(this.labelSource), textX, textY, color);
        } finally {
            BlxckoutFXRenderContext.allowTextureDarkening();
        }
    }

    private void renderClassicLabel(Minecraft mc) {
        int color = this.enabled ? 0xFFD8D8D8 : 0xFF8F8F8F;
        int startX = this.x + 5;
        int startY = this.y + Math.max(0, (this.height - GLYPH_HEIGHT) / 2);
        int presetX;

        if (this.ae2Screen) {
            renderAe2Icon(mc, startX, startY - 2);
            presetX = startX + AE2_ICON_SIZE + 4;
        } else {
            drawClassicF(startX, startY, color);
            drawGlyph("X", startX + 7, startY, color);
            presetX = startX + 6 + 1 + GLYPH_WIDTH + 5;
        }

        int rightPadding = this.hovered ? CLASSIC_HOVER_LOCK_RESERVE : CLASSIC_TEXT_RIGHT_PADDING;
        int labelWidth = Math.max(0, this.x + this.width - rightPadding - presetX);
        if (labelWidth <= 0) {
            return;
        }

        FontRenderer font = mc.fontRenderer;
        int availableWidth = Math.max(1, (int) Math.floor(labelWidth / CLASSIC_TEXT_SCALE));
        int textWidth = font.getStringWidth(this.labelSource);
        boolean scrolling = this.hovered && textWidth > availableWidth;
        if (scrolling) {
            enableScissor(mc, presetX, this.y + 5, labelWidth, 2 + (int) Math.ceil(font.FONT_HEIGHT * CLASSIC_TEXT_SCALE));
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(presetX, this.y + 6, 0.0F);
        GlStateManager.scale(CLASSIC_TEXT_SCALE, CLASSIC_TEXT_SCALE, 1.0F);
        if (!scrolling) {
            font.drawString(ellipsize(font, this.labelSource, availableWidth), 0, 0, color, false);
        } else {
            font.drawString(this.labelSource, -marqueeOffset(textWidth, availableWidth), 0, color, false);
        }
        GlStateManager.popMatrix();

        if (scrolling) {
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
        }
    }

    private void renderAe2Icon(Minecraft mc, int x, int y) {
        BlxckoutFXRenderContext.suppressTextureDarkening();
        try {
            mc.getTextureManager().bindTexture(AE2_ICON);
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, 0.0F);
            float scale = AE2_ICON_SIZE / (float) AE2_ICON_CROP_SIZE;
            GlStateManager.scale(scale, scale, 1.0F);
            drawModalRectWithCustomSizedTexture(0, 0, AE2_ICON_CROP_X, AE2_ICON_CROP_Y, AE2_ICON_CROP_SIZE, AE2_ICON_CROP_SIZE, AE2_ICON_SOURCE_SIZE, AE2_ICON_SOURCE_SIZE);
            GlStateManager.popMatrix();
        } finally {
            BlxckoutFXRenderContext.allowTextureDarkening();
        }
    }

    private void renderLockIndicator() {
        int x = this.x + this.width - LOCK_ICON_SIZE - 5;
        int y = this.y + Math.max(0, (this.height - LOCK_ICON_SIZE) / 2 - 1);
        int outline = this.enabled ? 0xFFF1F1F1 : 0xFF9A9A9A;
        int body = this.enabled ? 0xFF262626 : 0xFF171717;
        int knob = this.enabled ? 0xFFF1F1F1 : 0xFFD0D0D0;
        int knobX = isLocked() ? x + 6 : x + 2;

        drawRect(x + 2, y + 5, x + 10, y + 8, body);
        drawRect(x + 2, y + 4, x + 10, y + 5, outline);
        drawRect(x + 2, y + 8, x + 10, y + 9, outline);
        drawRect(x + 2, y + 4, x + 3, y + 9, outline);
        drawRect(x + 9, y + 4, x + 10, y + 9, outline);
        drawRect(knobX, y + 4, knobX + 4, y + 9, knob);
    }

    private static String shortGlyphForLabel(String label) {
        String lower = label == null ? "" : label.toLowerCase(Locale.ROOT);
        if (lower.contains("soft")) return "S";
        if (lower.contains("balanced")) return "B";
        if (lower.contains("dark")) return "D";
        if (lower.contains("blxckout")) return "X";
        if (lower.contains("off")) return "O";
        return "B";
    }

    private static String ellipsize(FontRenderer font, String text, int width) {
        if (text == null || width <= 0) {
            return "";
        }
        if (font.getStringWidth(text) <= width) {
            return text;
        }
        int ellipsisWidth = font.getStringWidth("...");
        if (ellipsisWidth >= width) {
            return font.trimStringToWidth("...", width);
        }
        return font.trimStringToWidth(text, width - ellipsisWidth) + "...";
    }

    private static int marqueeOffset(int textWidth, int availableWidth) {
        int cycle = Math.max(1, textWidth - availableWidth);
        int frame = (int) ((System.currentTimeMillis() / 70L) % (cycle * 2L + 24L));
        if (frame < 12) {
            return 0;
        }
        if (frame < cycle + 12) {
            return frame - 12;
        }
        return Math.max(0, cycle - (frame - cycle - 12));
    }

    private static void enableScissor(Minecraft mc, int x, int y, int width, int height) {
        ScaledResolution resolution = new ScaledResolution(mc);
        int scale = resolution.getScaleFactor();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(
                x * scale,
                mc.displayHeight - (y + height) * scale,
                width * scale,
                height * scale
        );
    }

    private static void drawClassicF(int x, int y, int color) {
        drawBitmapGlyph(x, y, color, new String[]{
                "XXXXXX", "XX    ", "XXXXX ", "XX    ",
                "XX    ", "XX    ", "XX    ", "XX    "
        });
    }

    private static void drawGlyph(String glyph, int x, int y, int color) {
        String[] rows;
        if ("O".equals(glyph)) {
            rows = new String[]{"  XXXX  ", " XX  XX ", "XX    XX", "XX    XX", "XX    XX", "XX    XX", " XX  XX ", "  XXXX  "};
        } else if ("S".equals(glyph)) {
            rows = new String[]{"  XXXX  ", " XX  XX ", " XX     ", "  XXXX  ", "     XX ", " XX  XX ", " XX  XX ", "  XXXX  "};
        } else if ("D".equals(glyph)) {
            rows = new String[]{" XXXXX  ", " XX  XX ", " XX  XX ", " XX  XX ", " XX  XX ", " XX  XX ", " XX  XX ", " XXXXX  "};
        } else if ("X".equals(glyph)) {
            rows = new String[]{"XX    XX", " XX  XX ", "  XXXX  ", "   XX   ", "   XX   ", "  XXXX  ", " XX  XX ", "XX    XX"};
        } else {
            rows = new String[]{" XXXXX  ", " XX  XX ", " XX  XX ", " XXXXX  ", " XX  XX ", " XX  XX ", " XX  XX ", " XXXXX  "};
        }
        drawBitmapGlyph(x, y, color, rows);
    }

    private static void drawBitmapGlyph(int x, int y, int color, String[] rows) {
        for (int row = 0; row < rows.length; row++) {
            String line = rows[row];
            for (int col = 0; col < line.length(); col++) {
                if (line.charAt(col) == 'X') {
                    drawRect(x + col, y + row, x + col + 1, y + row + 1, color);
                }
            }
        }
    }

    private boolean isLocked() {
        return this.locked != null && this.locked.getAsBoolean();
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(value, max));
    }

    @FunctionalInterface
    interface PositionChanged {
        void positionChanged(InventoryPresetButton button);
    }
}
