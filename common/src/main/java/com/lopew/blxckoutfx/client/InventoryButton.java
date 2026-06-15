package com.lopew.blxckoutfx.client;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class InventoryButton extends Button {
    private static final double DRAG_THRESHOLD = 3.0D;

    private final int screenWidth;
    private final int screenHeight;

    private boolean draggingButton;
    private boolean movedWhileDragging;
    private double dragStartMouseX;
    private double dragStartMouseY;
    private int dragStartX;
    private int dragStartY;

    public InventoryButton(int x, int y, int width, int height, int screenWidth, int screenHeight, Component message, OnPress onPress) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    public void clampToScreen(int screenWidth, int screenHeight) {
        int maxX = Math.max(0, screenWidth - this.width);
        int maxY = Math.max(0, screenHeight - this.height);
        int x = Math.max(0, Math.min(this.getX(), maxX));
        int y = Math.max(0, Math.min(this.getY(), maxY));

        this.setX(x);
        this.setY(y);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!this.active || !this.visible || button != 0 || !this.isMouseOver(mouseX, mouseY)) {
            return false;
        }

        beginDrag(mouseX, mouseY);
        return true;
    }

    public void beginDrag(double mouseX, double mouseY) {
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
        if (!this.draggingButton || button != 0) {
            return false;
        }

        updateDrag(mouseX, mouseY);
        return true;
    }

    public void updateDrag(double mouseX, double mouseY) {
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
        if (!this.draggingButton || button != 0) {
            return false;
        }

        finishDrag();
        return true;
    }

    public void finishDrag() {
        this.draggingButton = false;

        if (this.movedWhileDragging) {
            savePosition();
            return;
        }

        this.onPress();
    }

    public boolean isDraggingButton() {
        return this.draggingButton;
    }

    private void savePosition() {
        int bottomOffset = Math.max(0, this.screenHeight - this.height - this.getY());
        BlxckoutFXClientConfig.load().setInventoryButtonOffset(this.getX(), bottomOffset);
    }
}
