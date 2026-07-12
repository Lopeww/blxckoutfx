package com.lopew.blxckoutfx.client.gui;

import com.lopew.blxckoutfx.BlxckoutFX;
import com.lopew.blxckoutfx.client.config.BlxckoutFXClientConfig;
import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderPipelines;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.Font;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class InventoryButton extends Button {
    private static final double DRAG_THRESHOLD = 3.0D;
    private static final String ELLIPSIS = "...";
    private static final int CLASSIC_WIDTH = 95;
    private static final int SHORT_WIDTH = 20;
    private static final int HEIGHT = 20;
    private static final float CLASSIC_TEXT_SCALE = 1.12F;
    private static final int CLASSIC_TEXT_RIGHT_PADDING = 6;
    private static final int CLASSIC_HOVER_LOCK_RESERVE = 19;
    private static final int GLYPH_CELL = 1;
    private static final int GLYPH_WIDTH = 8;
    private static final int GLYPH_HEIGHT = 8;
    private static final int LOCK_ICON_SIZE = 12;
    private static final Identifier AE2_ICON = Identifier.fromNamespaceAndPath(BlxckoutFX.MOD_ID, "textures/gui/ae2.png");
    private static final int AE2_ICON_SOURCE_SIZE = 1254;
    private static final int AE2_ICON_SIZE = 12;
    private static final int AE2_ICON_CROP_X = 126;
    private static final int AE2_ICON_CROP_Y = 45;
    private static final int AE2_ICON_CROP_SIZE = 1128;

    enum Ae2Variant {
        NONE,
        CLASSIC_ICON_TEXT,
        SHORT_ICON_ONLY
    }

    private final OnPress onPress;
    private final PositionChanged positionChanged;
    private final BlxckoutFXClientConfig.InventoryButtonStyle style;
    private final Ae2Variant ae2Variant;
    private int screenWidth;
    private int screenHeight;

    private Component shortLabelSource;
    private boolean draggingButton;
    private boolean movedWhileDragging;
    private double dragStartMouseX;
    private double dragStartMouseY;
    private int dragStartX;
    private int dragStartY;

    public InventoryButton(int x,
                           int y,
                           BlxckoutFXClientConfig.InventoryButtonStyle style,
                           Ae2Variant ae2Variant,
                           Component message,
                           OnPress onPress,
                           PositionChanged positionChanged,
                           int screenWidth,
                           int screenHeight) {
        super(x, y, widthFor(style), heightFor(style), message, onPress, DEFAULT_NARRATION);
        this.onPress = onPress;
        this.positionChanged = positionChanged;
        this.style = style == null ? BlxckoutFXClientConfig.InventoryButtonStyle.Classic : style;
        this.ae2Variant = ae2Variant == null ? Ae2Variant.NONE : ae2Variant;
        this.shortLabelSource = message == null ? Component.empty() : message;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    public static int widthFor(BlxckoutFXClientConfig.InventoryButtonStyle style) {
        return style == BlxckoutFXClientConfig.InventoryButtonStyle.Short ? SHORT_WIDTH : CLASSIC_WIDTH;
    }

    public static int heightFor(BlxckoutFXClientConfig.InventoryButtonStyle style) {
        return HEIGHT;
    }

    public void setPresentationLabel(Component label) {
        setShortLabelSource(label);
    }

    public void setShortLabelSource(Component label) {
        this.shortLabelSource = label == null ? Component.empty() : label;
        this.setMessage(this.shortLabelSource);
    }

    public void setScreenSize(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    public void activate() {
        this.setFocused(true);
        this.onPress.onPress(this);
    }

    @Override
    protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        BlxckoutFXRenderContext.enterButtonRender();
        BlxckoutFXRenderContext.enterInventoryButtonRender();
        BlxckoutFXRenderContext.enterBalancedCappedDarkening();
        try {
            this.extractDefaultSprite(graphics);
            if (this.style == BlxckoutFXClientConfig.InventoryButtonStyle.Short) {
                renderShortLabel(graphics);
            } else {
                renderClassicLabel(graphics);
            }

            if (this.isHovered() && this.style == BlxckoutFXClientConfig.InventoryButtonStyle.Classic) {
                renderLockIndicator(graphics, BlxckoutFXGUIHandler.isInventoryButtonLocked());
            }
        } finally {
            BlxckoutFXRenderContext.exitBalancedCappedDarkening();
            BlxckoutFXRenderContext.exitInventoryButtonRender();
            BlxckoutFXRenderContext.exitButtonRender();
        }
    }

    @Override
    public void onClick(MouseButtonEvent event, boolean doubleClick) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft == null || minecraft.gui.screen() == null) {
            return;
        }

        BlxckoutFXGUIHandler.onMouseClicked(minecraft.gui.screen(), event.x(), event.y(), event.button());
    }

    @Override
    protected void onDrag(MouseButtonEvent event, double dx, double dy) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft == null || minecraft.gui.screen() == null) {
            return;
        }

        BlxckoutFXGUIHandler.onMouseDragged(minecraft.gui.screen(), event.x(), event.y(), event.button(), dx, dy);
    }

    @Override
    public void onRelease(MouseButtonEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft == null || minecraft.gui.screen() == null) {
            return;
        }

        BlxckoutFXGUIHandler.onMouseReleased(minecraft.gui.screen(), event.x(), event.y(), event.button());
    }

    public void beginDrag(double mouseX, double mouseY) {
        if (BlxckoutFXGUIHandler.isInventoryButtonLocked() || !this.active || !this.visible || !this.isMouseOver(mouseX, mouseY)) {
            return;
        }

        this.draggingButton = true;
        this.movedWhileDragging = false;
        this.dragStartMouseX = mouseX;
        this.dragStartMouseY = mouseY;
        this.dragStartX = this.getX();
        this.dragStartY = this.getY();
        this.setFocused(true);
    }

    public void updateDrag(double mouseX, double mouseY) {
        if (BlxckoutFXGUIHandler.isInventoryButtonLocked() || !this.draggingButton) {
            return;
        }

        double totalDragX = mouseX - this.dragStartMouseX;
        double totalDragY = mouseY - this.dragStartMouseY;

        if (Math.abs(totalDragX) >= DRAG_THRESHOLD || Math.abs(totalDragY) >= DRAG_THRESHOLD) {
            this.movedWhileDragging = true;
        }

        if (!this.movedWhileDragging) {
            return;
        }

        this.setX(this.dragStartX + (int) Math.round(totalDragX));
        this.setY(this.dragStartY + (int) Math.round(totalDragY));
        this.clampToScreen(this.screenWidth, this.screenHeight);
    }

    public void finishDrag(double mouseX, double mouseY) {
        if (BlxckoutFXGUIHandler.isInventoryButtonLocked() || !this.draggingButton) {
            this.draggingButton = false;
            this.movedWhileDragging = false;
            return;
        }

        boolean moved = this.movedWhileDragging;
        this.draggingButton = false;
        this.movedWhileDragging = false;

        if (moved) {
            this.positionChanged.positionChanged(this);
        } else {
            activate();
        }
    }

    public boolean isDraggingButton() {
        return this.draggingButton;
    }

    public void cancelDrag() {
        this.draggingButton = false;
        this.movedWhileDragging = false;
    }

    public void clampToScreen(int screenWidth, int screenHeight) {
        int maxX = Math.max(0, screenWidth - this.width);
        int maxY = Math.max(0, screenHeight - this.height);
        this.setX(Math.max(0, Math.min(this.getX(), maxX)));
        this.setY(Math.max(0, Math.min(this.getY(), maxY)));
    }

    private void renderShortLabel(GuiGraphicsExtractor graphics) {
        if (this.ae2Variant == Ae2Variant.SHORT_ICON_ONLY) {
            renderAe2IconCentered(graphics);
            return;
        }

        String glyph = shortGlyphForMessage(this.shortLabelSource.getString());
        int textX = this.getX() + Math.max(0, (this.width - GLYPH_WIDTH) / 2);
        int textY = this.getY() + Math.max(0, (this.height - GLYPH_HEIGHT) / 2);
        int color = this.active ? 0xFFD4D4D4 : 0xFF8C8C8C;
        drawGlyph(graphics, glyph, textX, textY, color);
    }

    private void renderClassicLabel(GuiGraphicsExtractor graphics) {
        int fg = this.active ? 0xFFD8D8D8 : 0xFF8F8F8F;
        int startX = this.getX() + 5;
        int startY = this.getY() + Math.max(0, (this.height - 8) / 2);
        Font font = Minecraft.getInstance().font;
        int presetX;

        if (this.ae2Variant == Ae2Variant.CLASSIC_ICON_TEXT) {
            renderAe2Icon(graphics, startX, startY - 2);
            presetX = startX + AE2_ICON_SIZE + 3;
        } else {
            presetX = startX + 6 + 1 + 8 + 5;
            drawClassicFX(graphics, startX, startY, fg);
        }

        int rightPadding = this.isHovered() ? CLASSIC_HOVER_LOCK_RESERVE : CLASSIC_TEXT_RIGHT_PADDING;
        int labelWidth = Math.max(0, this.getX() + this.width - rightPadding - presetX);
        int availableWidth = Math.max(1, (int) Math.floor(labelWidth / CLASSIC_TEXT_SCALE));
        boolean scrolling = this.isHovered() && font.width(this.shortLabelSource) > availableWidth;
        if (scrolling) {
            graphics.enableScissor(
                    presetX,
                    this.getY() + 5,
                    presetX + labelWidth,
                    this.getY() + 7 + Math.max(1, (int) Math.ceil(font.lineHeight * CLASSIC_TEXT_SCALE))
            );
        }

        var pose = graphics.pose();
        pose.pushMatrix();
        pose.translate(presetX, this.getY() + 6);
        pose.scale(CLASSIC_TEXT_SCALE, CLASSIC_TEXT_SCALE);
        drawClassicLabelText(graphics, font, fg, labelWidth);
        pose.popMatrix();
        if (scrolling) {
            graphics.disableScissor();
        }
    }

    private void drawClassicLabelText(GuiGraphicsExtractor graphics, Font font, int color, int width) {
        if (width <= 0) {
            return;
        }

        int availableWidth = Math.max(1, (int) Math.floor(width / CLASSIC_TEXT_SCALE));
        int textWidth = font.width(this.shortLabelSource);
        if (!this.isHovered() || textWidth <= availableWidth) {
            graphics.text(font, ellipsize(font, this.shortLabelSource, availableWidth), 0, 0, color, false);
            return;
        }

        int cycle = Math.max(1, textWidth - availableWidth);
        int frame = (int) ((System.currentTimeMillis() / 70L) % (cycle * 2L + 24L));
        int offset;
        if (frame < 12) {
            offset = 0;
        } else if (frame < cycle + 12) {
            offset = frame - 12;
        } else {
            offset = Math.max(0, cycle - (frame - cycle - 12));
        }

        graphics.text(font, this.shortLabelSource, -offset, 0, color, false);
    }

    private static Component ellipsize(Font font, Component text, int width) {
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

    private static void drawClassicFX(GuiGraphicsExtractor graphics, int x, int y, int color) {
        drawClassicF(graphics, x, y, color);
        drawGlyph(graphics, "X", x + 7, y, color);
    }

    private static void drawClassicF(GuiGraphicsExtractor graphics, int x, int y, int color) {
        drawBitmapGlyph(graphics, x, y, color, new String[]{
                "XXXXXX",
                "XX    ",
                "XXXXX ",
                "XX    ",
                "XX    ",
                "XX    ",
                "XX    ",
                "XX    "
        });
    }

    private void renderAe2IconCentered(GuiGraphicsExtractor graphics) {
        int x = this.getX() + Math.max(0, Math.round((this.width - AE2_ICON_SIZE) / 2.0F));
        int y = this.getY() + Math.max(0, Math.round((this.height - AE2_ICON_SIZE) / 2.0F) - 1);
        renderAe2Icon(graphics, x, y);
    }

    private void renderAe2Icon(GuiGraphicsExtractor graphics, int x, int y) {
        BlxckoutFXRenderContext.enterAe2IconRender();
        BlxckoutFXRenderContext.suppressTextureDarkening();
        try {
            var pose = graphics.pose();
            pose.pushMatrix();
            pose.translate(x, y);
            pose.scale(AE2_ICON_SIZE / (float) AE2_ICON_CROP_SIZE, AE2_ICON_SIZE / (float) AE2_ICON_CROP_SIZE);
            graphics.blit(
                    BlxckoutFXRenderPipelines.CONTENT_ICON_TEXTURED,
                    AE2_ICON,
                    0,
                    0,
                    AE2_ICON_CROP_X,
                    AE2_ICON_CROP_Y,
                    AE2_ICON_CROP_SIZE,
                    AE2_ICON_CROP_SIZE,
                    AE2_ICON_SOURCE_SIZE,
                    AE2_ICON_SOURCE_SIZE,
                    AE2_ICON_SOURCE_SIZE,
                    AE2_ICON_SOURCE_SIZE
            );
            pose.popMatrix();
        } finally {
            BlxckoutFXRenderContext.allowTextureDarkening();
            BlxckoutFXRenderContext.exitAe2IconRender();
        }
    }

    private static String shortGlyphForMessage(String label) {
        String lower = label == null ? "" : label.toLowerCase();
        if (lower.contains("off")) {
            return "O";
        }
        if (lower.contains("soft")) {
            return "S";
        }
        if (lower.contains("dark")) {
            return "D";
        }
        if (lower.contains("blxckout") || lower.contains("blackout")) {
            return "X";
        }
        return "B";
    }

    private void renderLockIndicator(GuiGraphicsExtractor graphics, boolean locked) {
        int x = this.style == BlxckoutFXClientConfig.InventoryButtonStyle.Short
                ? this.getX() + (this.width - LOCK_ICON_SIZE) / 2
                : this.getX() + this.width - LOCK_ICON_SIZE - 5;
        int y = this.getY() + Math.max(0, (this.height - LOCK_ICON_SIZE) / 2 - 1);

        int outline = this.active ? 0xFFF1F1F1 : 0xFF9A9A9A;
        int body = this.active ? 0xFF262626 : 0xFF171717;
        int knob = this.active ? 0xFFF1F1F1 : 0xFFD0D0D0;
        int knobX = locked ? x + 6 : x + 2;

        graphics.fill(x + 2, y + 5, x + 10, y + 8, body);
        graphics.fill(x + 2, y + 4, x + 10, y + 5, outline);
        graphics.fill(x + 2, y + 8, x + 10, y + 9, outline);
        graphics.fill(x + 2, y + 4, x + 3, y + 9, outline);
        graphics.fill(x + 9, y + 4, x + 10, y + 9, outline);
        graphics.fill(knobX, y + 4, knobX + 4, y + 9, knob);
    }

    private static void drawGlyph(GuiGraphicsExtractor graphics, String glyph, int x, int y, int color) {
        switch (glyph) {
            case "O" -> drawBitmapGlyph(graphics, x, y, color, new String[]{
                    "  XXXX  ",
                    " XX  XX ",
                    "XX    XX",
                    "XX    XX",
                    "XX    XX",
                    "XX    XX",
                    " XX  XX ",
                    "  XXXX  "
            });
            case "S" -> drawBitmapGlyph(graphics, x, y, color, new String[]{
                    "  XXXX  ",
                    " XX  XX ",
                    " XX     ",
                    "  XXXX  ",
                    "     XX ",
                    " XX  XX ",
                    " XX  XX ",
                    "  XXXX  "
            });
            case "B" -> drawBitmapGlyph(graphics, x, y, color, new String[]{
                    " XXXXX  ",
                    " XX  XX ",
                    " XX  XX ",
                    " XXXXX  ",
                    " XX  XX ",
                    " XX  XX ",
                    " XX  XX ",
                    " XXXXX  "
            });
            case "D" -> drawBitmapGlyph(graphics, x, y, color, new String[]{
                    " XXXXX  ",
                    " XX  XX ",
                    " XX  XX ",
                    " XX  XX ",
                    " XX  XX ",
                    " XX  XX ",
                    " XX  XX ",
                    " XXXXX  "
            });
            case "X" -> drawBitmapGlyph(graphics, x, y, color, new String[]{
                    "XX    XX",
                    " XX  XX ",
                    "  XXXX  ",
                    "   XX   ",
                    "   XX   ",
                    "  XXXX  ",
                    " XX  XX ",
                    "XX    XX"
            });
            default -> drawBitmapGlyph(graphics, x, y, color, new String[]{
                    " XXXXX  ",
                    " XX  XX ",
                    " XX  XX ",
                    " XXXXX  ",
                    " XX  XX ",
                    " XX  XX ",
                    " XX  XX ",
                    " XXXXX  "
            });
        }
    }

    private static void drawBitmapGlyph(GuiGraphicsExtractor graphics, int x, int y, int color, String[] rows) {
        for (int row = 0; row < rows.length; row++) {
            String line = rows[row];
            for (int col = 0; col < line.length(); col++) {
                if (line.charAt(col) == 'X') {
                    int px = x + col * GLYPH_CELL;
                    int py = y + row * GLYPH_CELL;
                    graphics.fill(px, py, px + GLYPH_CELL, py + GLYPH_CELL, color);
                }
            }
        }
    }

    @FunctionalInterface
    public interface PositionChanged {
        void positionChanged(InventoryButton button);
    }
}
