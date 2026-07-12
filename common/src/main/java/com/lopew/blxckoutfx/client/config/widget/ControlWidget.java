package com.lopew.blxckoutfx.client.config.widget;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public interface ControlWidget {
    int getWidth();

    int getHeight();

    default int getPreferredWidth(Font font, int maxWidth) {
        return Math.min(getWidth(), Math.max(0, maxWidth));
    }

    default int getPreferredHeight(Font font, int width) {
        return getHeight();
    }

    void setBounds(int x, int y);

    void render(GuiGraphicsExtractor graphics, Font font, int mouseX, int mouseY);

    default void renderOverlay(GuiGraphicsExtractor graphics, Font font, int mouseX, int mouseY) {
    }

    boolean mouseClicked(double mouseX, double mouseY, int button);

    default boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        return false;
    }

    default void mouseReleased(double mouseX, double mouseY, int button) {
    }
}
