package com.lopew.blxckoutfx.client;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class InventoryButton extends Button {
    public InventoryButton(int x, int y, int width, int height, Component message, OnPress onPress) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
    }

    @Override
    protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        this.extractDefaultSprite(graphics);
        this.extractDefaultLabel(graphics.textRendererForWidget(this, GuiGraphicsExtractor.HoveredTextEffects.NONE));
    }

    public void clampToScreen(int screenWidth, int screenHeight) {
        int maxX = Math.max(0, screenWidth - this.width);
        int maxY = Math.max(0, screenHeight - this.height);
        int x = Math.max(0, Math.min(this.getX(), maxX));
        int y = Math.max(0, Math.min(this.getY(), maxY));

        this.setX(x);
        this.setY(y);
    }
}
