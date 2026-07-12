package com.lopew.blxckoutfx.client.config.widget;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public interface ControlWidget {
    int getWidth();

    int getHeight();

    void setBounds(int x, int y);

    void render(GuiGraphics graphics, Font font, int mouseX, int mouseY);

    default void renderOverlay(GuiGraphics graphics, Font font, int mouseX, int mouseY) {
    }

    boolean mouseClicked(double mouseX, double mouseY, int button);

    default boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        return false;
    }

    default void mouseReleased(double mouseX, double mouseY, int button) {
    }
}
