package com.lopew.blxckoutfx.client.config.widget;

import com.lopew.blxckoutfx.client.config.BlxckoutFXConfigScreen;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public final class SettingRow {
    private static final int ROW_HEIGHT = 50;
    private static final int SECTION_HEIGHT = 22;

    private final Component title;
    private final Component description;
    private final ControlWidget control;
    private final boolean section;
    private final boolean controlOnly;
    private final int rowHeight;

    public SettingRow(Component title, Component description, ControlWidget control) {
        this(title, description, control, ROW_HEIGHT, false, false);
    }

    public SettingRow(Component title, Component description, ControlWidget control, int rowHeight) {
        this(title, description, control, rowHeight, false, false);
    }

    private SettingRow(Component title, Component description, ControlWidget control, int rowHeight, boolean section, boolean controlOnly) {
        this.title = title;
        this.description = description;
        this.control = control;
        this.rowHeight = rowHeight;
        this.section = section;
        this.controlOnly = controlOnly;
    }

    private SettingRow(Component title) {
        this(title, Component.empty(), null, SECTION_HEIGHT, true, false);
    }

    public static SettingRow section(Component title) {
        return new SettingRow(title);
    }

    public static SettingRow controlOnly(ControlWidget control, int rowHeight) {
        return new SettingRow(Component.empty(), Component.empty(), control, rowHeight, false, true);
    }

    public int getHeight() {
        return this.rowHeight;
    }

    public void render(GuiGraphics graphics, Font font, int mouseX, int mouseY, int x, int y, int width) {
        if (this.section) {
            graphics.drawString(font, ConfigTextLayout.ellipsize(font, this.title, width), x, y + 7, 0xFFC9C9C9, false);
            return;
        }

        boolean hover = inside(mouseX, mouseY, x, y, width, this.rowHeight);
        if (hover) {
            graphics.fill(x - 4, y, x + width + 4, y + this.rowHeight, 0x18181818);
        }

        if (this.controlOnly) {
            if (this.control != null) {
                this.control.setBounds(x, y + 4);
                this.control.render(graphics, font, mouseX, mouseY);
            }
            drawDashedDivider(graphics, x, x + width, y + this.rowHeight - 1);
            return;
        }

        int controlW = this.control == null ? 0 : this.control.getWidth();
        int textW = width - controlW - 32;
        graphics.drawString(font, ConfigTextLayout.ellipsize(font, this.title, textW), x, y + 10, BlxckoutFXConfigScreen.textColor(), false);
        ConfigTextLayout.drawWrapped(graphics, font, this.description, x, y + 24, textW, BlxckoutFXConfigScreen.mutedTextColor());

        if (this.control != null) {
            int controlX = x + width - controlW;
            int controlY = y + (this.rowHeight - this.control.getHeight()) / 2;
            this.control.setBounds(controlX, controlY);
            this.control.render(graphics, font, mouseX, mouseY);
        }

        drawDashedDivider(graphics, x, x + width, y + this.rowHeight - 1);
    }

    public void renderOverlay(GuiGraphics graphics, Font font, int mouseX, int mouseY) {
        if (this.control != null) {
            this.control.renderOverlay(graphics, font, mouseX, mouseY);
        }
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button, int x, int y, int width) {
        if (this.section) {
            return false;
        }

        return this.control != null && this.control.mouseClicked(mouseX, mouseY, button);
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        return this.control != null && this.control.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    public void mouseReleased(double mouseX, double mouseY, int button) {
        if (this.control != null) {
            this.control.mouseReleased(mouseX, mouseY, button);
        }
    }

    private static boolean inside(double mouseX, double mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }

    private static void drawDashedDivider(GuiGraphics graphics, int x1, int x2, int y) {
        for (int x = x1; x < x2; x += 8) {
            graphics.fill(x, y, Math.min(x + 4, x2), y + 1, 0x66404040);
        }
    }
}
