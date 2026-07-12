package com.lopew.blxckoutfx.client.gui;

import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import com.lopew.blxckoutfx.client.config.BlxckoutFXClientConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import com.lopew.blxckoutfx.BlxckoutFX;

public class InventoryButton extends Button {
    private static final double DRAG_THRESHOLD = 3.0D;
    private static final String ELLIPSIS = "...";

    private static final int CLASSIC_HEIGHT = 20;
    private static final int SHORT_HEIGHT = 20;
    private static final int CLASSIC_WIDTH = 95;
    private static final int SHORT_WIDTH = 20;
    private static final int GLYPH_CELL = 1;
    private static final int GLYPH_WIDTH = 8;
    private static final int GLYPH_HEIGHT = 8;
    private static final float CLASSIC_TEXT_SCALE = 1.12F;
    private static final ResourceLocation AE2_ICON = ResourceLocation.fromNamespaceAndPath(BlxckoutFX.MOD_ID, "textures/gui/ae2.png");
    private static final int AE2_ICON_SOURCE_SIZE = 1254;
    private static final int AE2_ICON_SIZE = 11;
    private static final int AE2_ICON_CROP_X = 126;
    private static final int AE2_ICON_CROP_Y = 45;
    private static final int AE2_ICON_CROP_SIZE = 1128;
    private static final int LOCK_ICON_SIZE = 12;
    private static final int CLASSIC_TEXT_RIGHT_PADDING = 6;
    private static final int CLASSIC_HOVER_LOCK_RESERVE = LOCK_ICON_SIZE + 7;

    enum Ae2Variant {
        NONE,
        CLASSIC_ICON_TEXT,
        SHORT_ICON_ONLY
    }

    private final BlxckoutFXClientConfig.InventoryButtonStyle style;
    private final Ae2Variant ae2Variant;
    private Component shortLabelSource = Component.empty();
    private boolean draggingButton;
    private boolean movedWhileDragging;
    private double dragStartMouseX;
    private double dragStartMouseY;
    private int dragStartX;
    private int dragStartY;
    private int screenWidth;
    private int screenHeight;
    private final int configuredX;
    private final int configuredY;
    private boolean configuredPositionPending = true;

    public InventoryButton(int x, int y, int screenWidth, int screenHeight, BlxckoutFXClientConfig.InventoryButtonStyle style, Ae2Variant ae2Variant, Component message, OnPress onPress) {
        super(x, y, widthFor(style), heightFor(style), message, onPress, DEFAULT_NARRATION);
        this.style = style == null ? BlxckoutFXClientConfig.InventoryButtonStyle.Classic : style;
        this.ae2Variant = ae2Variant == null ? Ae2Variant.NONE : ae2Variant;
        this.configuredX = x;
        this.configuredY = y;
        updateScreenBounds(screenWidth, screenHeight);
    }

    public static int widthFor(BlxckoutFXClientConfig.InventoryButtonStyle style) {
        return style == BlxckoutFXClientConfig.InventoryButtonStyle.Short ? SHORT_WIDTH : CLASSIC_WIDTH;
    }

    public static int heightFor(BlxckoutFXClientConfig.InventoryButtonStyle style) {
        return style == BlxckoutFXClientConfig.InventoryButtonStyle.Short ? SHORT_HEIGHT : CLASSIC_HEIGHT;
    }

    public void setShortLabelSource(Component shortLabelSource) {
        this.shortLabelSource = shortLabelSource == null ? Component.empty() : shortLabelSource;
    }

    public void clampToScreen(int screenWidth, int screenHeight) {
        int maxX = Math.max(0, screenWidth - this.width);
        int maxY = Math.max(0, screenHeight - this.height);
        int x = Math.max(0, Math.min(this.getX(), maxX));
        int y = Math.max(0, Math.min(this.getY(), maxY));

        this.setX(x);
        this.setY(y);
    }

    public void updateScreenBounds(int screenWidth, int screenHeight) {
        this.screenWidth = Math.max(0, screenWidth);
        this.screenHeight = Math.max(0, screenHeight);
    }

    public void restoreConfiguredPosition() {
        if (!this.configuredPositionPending) {
            return;
        }

        this.setX(this.configuredX);
        this.setY(this.configuredY);
        clampToScreen(this.screenWidth, this.screenHeight);
        this.configuredPositionPending = false;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.renderWidget(graphics, mouseX, mouseY, partialTick);
        if (this.style == BlxckoutFXClientConfig.InventoryButtonStyle.Short) {
            renderShortLabel(graphics);
        } else {
            renderClassicLabel(graphics);
        }
        if (this.isHovered() && this.style == BlxckoutFXClientConfig.InventoryButtonStyle.Classic) {
            renderLockIndicator(graphics);
        }
    }

    private void renderShortLabel(GuiGraphics graphics) {
        if (this.ae2Variant == Ae2Variant.SHORT_ICON_ONLY) {
            renderAe2IconCentered(graphics);
            return;
        }

        String glyph = shortGlyphForMessage(this.shortLabelSource.getString());
        int textX = this.getX() + (this.width - GLYPH_WIDTH) / 2;
        int textY = this.getY() + (this.height - GLYPH_HEIGHT) / 2;
        int color = this.active ? 0xFFD4D4D4 : 0xFF8C8C8C;
        drawGlyph(graphics, glyph, textX, textY, color);
    }

    private static String shortGlyphForMessage(String label) {
        String lower = label == null ? "" : label.toLowerCase();
        if (lower.contains("soft")) return "S";
        if (lower.contains("balanced")) return "B";
        if (lower.contains("dark")) return "D";
        if (lower.contains("blxckout")) return "X";
        if (lower.contains("off")) return "O";
        return "B";
    }

    private void renderClassicLabel(GuiGraphics graphics) {
        int fg = this.active ? 0xFFD8D8D8 : 0xFF8F8F8F;
        int startX = this.getX() + 5;
        int startY = this.getY() + Math.max(0, (this.height - 8) / 2);
        var font = Minecraft.getInstance().font;
        int presetX;

        if (this.ae2Variant == Ae2Variant.CLASSIC_ICON_TEXT) {
            renderAe2Icon(graphics, startX, startY - 2);
            presetX = startX + AE2_ICON_SIZE + 4;
        } else {
            int textGap = 5;
            presetX = startX + 6 + 1 + 8 + textGap;
            drawClassicF(graphics, startX, startY, fg);
            drawGlyph(graphics, "X", startX + 7, startY, fg);
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

        PoseStack pose = graphics.pose();
        pose.pushPose();
        pose.translate(presetX, this.getY() + 6, 0);
        pose.scale(CLASSIC_TEXT_SCALE, CLASSIC_TEXT_SCALE, 1f);
        drawClassicLabelText(graphics, font, fg, labelWidth);
        pose.popPose();

        if (scrolling) {
            graphics.disableScissor();
        }
    }

    private void drawClassicLabelText(GuiGraphics graphics, net.minecraft.client.gui.Font font, int color, int width) {
        if (width <= 0) {
            return;
        }

        int availableWidth = Math.max(1, (int) Math.floor(width / CLASSIC_TEXT_SCALE));
        int textWidth = font.width(this.shortLabelSource);
        if (!this.isHovered() || textWidth <= availableWidth) {
            graphics.drawString(font, ellipsize(font, this.shortLabelSource, availableWidth), 0, 0, color, false);
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

        graphics.drawString(font, this.shortLabelSource, -offset, 0, color, false);
    }

    private static Component ellipsize(net.minecraft.client.gui.Font font, Component text, int width) {
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

    private void renderAe2IconCentered(GuiGraphics graphics) {
        int x = this.getX() + Math.max(0, Math.round((this.width - AE2_ICON_SIZE) / 2.0F));
        int y = this.getY() + Math.max(0, Math.round((this.height - AE2_ICON_SIZE) / 2.0F) - 1);
        renderAe2Icon(graphics, x, y);
    }

    private void renderAe2Icon(GuiGraphics graphics, int x, int y) {
        BlxckoutFXRenderContext.enterAe2IconRender();
        BlxckoutFXRenderContext.suppressTextureDarkening();
        try {
            PoseStack pose = graphics.pose();
            pose.pushPose();
            pose.translate(x, y, 0);
            pose.scale(AE2_ICON_SIZE / (float) AE2_ICON_CROP_SIZE, AE2_ICON_SIZE / (float) AE2_ICON_CROP_SIZE, 1f);
            graphics.blit(AE2_ICON, 0, 0, AE2_ICON_CROP_X, AE2_ICON_CROP_Y, AE2_ICON_CROP_SIZE, AE2_ICON_CROP_SIZE, AE2_ICON_SOURCE_SIZE, AE2_ICON_SOURCE_SIZE);
            pose.popPose();
        } finally {
            BlxckoutFXRenderContext.allowTextureDarkening();
            BlxckoutFXRenderContext.exitAe2IconRender();
        }
    }

    private void renderLockIndicator(GuiGraphics graphics) {
        int x;
        if (this.style == BlxckoutFXClientConfig.InventoryButtonStyle.Short) {
            x = this.getX() + (this.width - LOCK_ICON_SIZE) / 2;
        } else {
            x = this.getX() + this.width - LOCK_ICON_SIZE - 5;
        }
        int y = this.getY() + Math.max(0, (this.height - LOCK_ICON_SIZE) / 2 - 1);
        drawLockIcon(graphics, x, y, BlxckoutFXGUIHandler.isInventoryButtonLocked());
    }

    private void drawLockIcon(GuiGraphics graphics, int x, int y, boolean locked) {
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

    private static void drawClassicF(GuiGraphics graphics, int x, int y, int color) {
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

    private static void drawGlyph(GuiGraphics graphics, String glyph, int x, int y, int color) {
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

    private static void drawBitmapGlyph(GuiGraphics graphics, int x, int y, int color, String[] rows) {
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

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!this.active || !this.visible || button != 0 || !this.isMouseOver(mouseX, mouseY)) {
            return false;
        }

        if (BlxckoutFXGUIHandler.isInventoryButtonLocked()) {
            this.setFocused(true);
            this.onPress();
            return true;
        }

        beginDrag(mouseX, mouseY);
        return true;
    }

    private void beginDrag(double mouseX, double mouseY) {
        this.draggingButton = true;
        this.movedWhileDragging = false;
        this.dragStartMouseX = mouseX;
        this.dragStartMouseY = mouseY;
        this.dragStartX = this.getX();
        this.dragStartY = this.getY();
        this.setFocused(true);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (BlxckoutFXGUIHandler.isInventoryButtonLocked()) {
            return false;
        }

        if (!this.draggingButton || button != 0) {
            return false;
        }

        updateDrag(mouseX, mouseY);
        return true;
    }

    private void updateDrag(double mouseX, double mouseY) {
        double totalDragX = mouseX - this.dragStartMouseX;
        double totalDragY = mouseY - this.dragStartMouseY;

        if (Math.abs(totalDragX) >= DRAG_THRESHOLD || Math.abs(totalDragY) >= DRAG_THRESHOLD) {
            this.movedWhileDragging = true;
        }

        if (this.movedWhileDragging) {
            this.setX(this.dragStartX + (int) Math.round(totalDragX));
            this.setY(this.dragStartY + (int) Math.round(totalDragY));
            clampToScreen(this.screenWidth, this.screenHeight);
        }
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (BlxckoutFXGUIHandler.isInventoryButtonLocked()) {
            this.draggingButton = false;
            this.movedWhileDragging = false;
            return false;
        }

        if (!this.draggingButton || button != 0) {
            return false;
        }

        finishDrag();
        return true;
    }

    private void finishDrag() {
        this.draggingButton = false;

        if (this.movedWhileDragging) {
            savePosition();
            return;
        }

        this.onPress();
    }

    public void tickDrag(double mouseX, double mouseY, boolean leftDown) {
        if (BlxckoutFXGUIHandler.isInventoryButtonLocked()) {
            this.draggingButton = false;
            this.movedWhileDragging = false;
            return;
        }

        if (!this.draggingButton) {
            return;
        }

        if (leftDown) {
            updateDrag(mouseX, mouseY);
            return;
        }

        finishDrag();
    }

    private void savePosition() {
        BlxckoutFXClientConfig.load().setInventoryButtonPosition(this.getX(), this.getY());
    }

    public String describePresetLabel() {
        return this.shortLabelSource.getString();
    }
}
