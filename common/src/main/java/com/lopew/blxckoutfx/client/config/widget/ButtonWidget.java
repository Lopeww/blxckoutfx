package com.lopew.blxckoutfx.client.config.widget;

import com.lopew.blxckoutfx.client.config.BlxckoutFXConfigScreen;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public final class ButtonWidget implements ControlWidget {
    private final int width;
    private final int height;
    private final Component label;
    private final Runnable onPress;
    private int x;
    private int y;

    public ButtonWidget(int x, int y, int width, int height, Component label, Runnable onPress) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.label = label;
        this.onPress = onPress;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public void setBounds(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void render(GuiGraphics graphics, Font font, int mouseX, int mouseY) {
        boolean hover = inside(mouseX, mouseY);
        int bg = hover ? 0xFF1B1B1B : 0xFF111111;
        graphics.fill(this.x, this.y, this.x + this.width, this.y + this.height, bg);
        graphics.fill(this.x, this.y, this.x + this.width, this.y + 1, BlxckoutFXConfigScreen.borderColor());
        graphics.fill(this.x, this.y + this.height - 1, this.x + this.width, this.y + this.height, BlxckoutFXConfigScreen.borderColor());
        graphics.fill(this.x, this.y, this.x + 1, this.y + this.height, BlxckoutFXConfigScreen.borderColor());
        graphics.fill(this.x + this.width - 1, this.y, this.x + this.width, this.y + this.height, BlxckoutFXConfigScreen.borderColor());
        Component label = ConfigTextLayout.ellipsize(font, this.label, this.width - 8);
        graphics.drawCenteredString(font, label, this.x + this.width / 2, ConfigTextLayout.controlTextY(font, this.y, this.height), 0xFFE5E5E5);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0 || !inside(mouseX, mouseY)) {
            return false;
        }

        this.onPress.run();
        return true;
    }

    private boolean inside(double mouseX, double mouseY) {
        return mouseX >= this.x && mouseX < this.x + this.width && mouseY >= this.y && mouseY < this.y + this.height;
    }
}
