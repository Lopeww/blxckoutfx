package com.lopew.blxckoutfx.client;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

public class InventoryButton extends Button {
    private final PositionChanged positionChanged;
    private final int screenWidth;
    private final int screenHeight;

    private boolean pressed;
    private boolean dragged;
    private double dragRemainderX;
    private double dragRemainderY;

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
        this.pressed = true;
        this.dragged = false;
        this.dragRemainderX = 0.0D;
        this.dragRemainderY = 0.0D;
    }

    @Override
    protected void onDrag(MouseButtonEvent event, double dx, double dy) {
        if (!this.pressed) {
            return;
        }

        this.dragRemainderX += dx;
        this.dragRemainderY += dy;

        int moveX = wholePixels(this.dragRemainderX);
        int moveY = wholePixels(this.dragRemainderY);
        if (moveX == 0 && moveY == 0) {
            return;
        }

        this.dragRemainderX -= moveX;
        this.dragRemainderY -= moveY;

        int previousX = this.getX();
        int previousY = this.getY();

        this.setX(previousX + moveX);
        this.setY(previousY + moveY);
        this.clampToScreen(this.screenWidth, this.screenHeight);

        this.dragged = this.dragged || this.getX() != previousX || this.getY() != previousY;
    }

    @Override
    public void onRelease(MouseButtonEvent event) {
        boolean wasPressed = this.pressed;
        boolean wasDragged = this.dragged;
        this.pressed = false;
        this.dragged = false;
        this.dragRemainderX = 0.0D;
        this.dragRemainderY = 0.0D;

        if (!wasPressed) {
            return;
        }

        if (wasDragged) {
            this.positionChanged.positionChanged(this);
            return;
        }

        if (this.isMouseOver(event.x(), event.y())) {
            this.onPress(event);
        }
    }

    public void clampToScreen(int screenWidth, int screenHeight) {
        int maxX = Math.max(0, screenWidth - this.width);
        int maxY = Math.max(0, screenHeight - this.height);
        int x = Math.max(0, Math.min(this.getX(), maxX));
        int y = Math.max(0, Math.min(this.getY(), maxY));

        this.setX(x);
        this.setY(y);
    }

    private static int wholePixels(double value) {
        return value > 0.0D ? (int) Math.floor(value) : (int) Math.ceil(value);
    }

    @FunctionalInterface
    public interface PositionChanged {
        void positionChanged(InventoryButton button);
    }
}
