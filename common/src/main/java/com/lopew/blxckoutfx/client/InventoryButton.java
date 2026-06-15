package com.lopew.blxckoutfx.client;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

public class InventoryButton extends Button {
    private static final double DRAG_THRESHOLD = 3.0D;

    private final OnPress onPress;
    private final PositionChanged positionChanged;
    private final int screenWidth;
    private final int screenHeight;

    private boolean draggingButton;
    private boolean movedWhileDragging;
    private double dragStartMouseX;
    private double dragStartMouseY;
    private int dragStartX;
    private int dragStartY;

    public InventoryButton(int x,
                           int y,
                           int width,
                           int height,
                           Component message,
                           OnPress onPress,
                           PositionChanged positionChanged,
                           int screenWidth,
                           int screenHeight) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
        this.onPress = onPress;
        this.positionChanged = positionChanged;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    @Override
    protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        this.extractDefaultSprite(graphics);
        this.extractDefaultLabel(graphics.textRendererForWidget(this, GuiGraphicsExtractor.HoveredTextEffects.NONE));
    }

    @Override
    public void onClick(MouseButtonEvent event, boolean doubleClick) {
        beginDrag(event.x(), event.y());
    }

    @Override
    protected void onDrag(MouseButtonEvent event, double dx, double dy) {
        if (!this.draggingButton) {
            return;
        }

        updateDrag(event.x(), event.y());
    }

    @Override
    public void onRelease(MouseButtonEvent event) {
        finishDrag(event.x(), event.y());
    }

    public void beginDrag(double mouseX, double mouseY) {
        if (!this.active || !this.visible || !this.isMouseOver(mouseX, mouseY)) {
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
        if (!this.draggingButton) {
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
        if (!this.draggingButton) {
            return;
        }

        boolean moved = this.movedWhileDragging;
        this.draggingButton = false;
        this.movedWhileDragging = false;

        if (moved) {
            this.positionChanged.positionChanged(this);
        } else if (this.isMouseOver(mouseX, mouseY)) {
            this.onPress.onPress(this);
        }
    }

    public boolean isDraggingButton() {
        return this.draggingButton;
    }

    public void clampToScreen(int screenWidth, int screenHeight) {
        int maxX = Math.max(0, screenWidth - this.width);
        int maxY = Math.max(0, screenHeight - this.height);
        int x = Math.max(0, Math.min(this.getX(), maxX));
        int y = Math.max(0, Math.min(this.getY(), maxY));

        this.setX(x);
        this.setY(y);
    }

    @FunctionalInterface
    public interface PositionChanged {
        void positionChanged(InventoryButton button);
    }
}
