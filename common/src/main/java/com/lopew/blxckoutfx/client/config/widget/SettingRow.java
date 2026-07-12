package com.lopew.blxckoutfx.client.config.widget;

import com.lopew.blxckoutfx.client.config.BlxckoutFXConfigScreen;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;

public final class SettingRow {
    private static final int MIN_ROW_HEIGHT = 50;
    private static final int SECTION_HEIGHT = 22;
    private static final int TEXT_RIGHT_PAD = 32;
    private static final int TEXT_TOP_PAD = 10;
    private static final int TEXT_BETWEEN_PAD = 3;
    private static final int TEXT_BOTTOM_PAD = 10;

    private final Component title;
    private final Component description;
    private final ControlWidget control;
    private final boolean section;
    private final boolean controlOnly;
    private final boolean controlOnlyLeftAligned;
    private final int rowHeight;

    public SettingRow(Component title, Component description, ControlWidget control) {
        this(title, description, control, MIN_ROW_HEIGHT, false, false, false);
    }

    public SettingRow(Component title, Component description, ControlWidget control, int rowHeight) {
        this(title, description, control, rowHeight, false, false, false);
    }

    private SettingRow(Component title, Component description, ControlWidget control, int rowHeight, boolean section, boolean controlOnly, boolean controlOnlyLeftAligned) {
        this.title = title;
        this.description = description;
        this.control = control;
        this.rowHeight = rowHeight;
        this.section = section;
        this.controlOnly = controlOnly;
        this.controlOnlyLeftAligned = controlOnlyLeftAligned;
    }

    private SettingRow(Component title) {
        this(title, Component.empty(), null, SECTION_HEIGHT, true, false, false);
    }

    public static SettingRow section(Component title) {
        return new SettingRow(title);
    }

    public static SettingRow controlOnly(ControlWidget control, int rowHeight) {
        return new SettingRow(Component.empty(), Component.empty(), control, rowHeight, false, true, false);
    }

    public static SettingRow controlOnlyLeftAligned(ControlWidget control, int rowHeight) {
        return new SettingRow(Component.empty(), Component.empty(), control, rowHeight, false, true, true);
    }

    public int getHeight(Font font, int width) {
        if (this.section) {
            return SECTION_HEIGHT;
        }
        if (this.controlOnly) {
            return this.rowHeight;
        }

        return layout(font, width).rowHeight;
    }

    public void render(GuiGraphicsExtractor graphics, Font font, int mouseX, int mouseY, int x, int y, int width) {
        if (this.section) {
            graphics.text(font, ConfigTextLayout.ellipsize(font, this.title, width), x, y + 7, 0xFFC9C9C9, false);
            return;
        }

        Layout layout = layout(font, width);
        boolean hover = inside(mouseX, mouseY, x, y, width, layout.rowHeight);
        if (hover) {
            graphics.fill(x - 4, y, x + width + 4, y + layout.rowHeight, 0x18181818);
        }

        if (this.controlOnly) {
            Layout controlLayout = new Layout(this.control == null ? 0 : Math.min(this.control.getWidth(), width), width, this.rowHeight);
            if (this.control != null) {
                int controlY = y + (controlLayout.rowHeight - this.control.getPreferredHeight(font, controlLayout.controlWidth)) / 2;
                int controlX = this.controlOnlyLeftAligned ? x : x + width - controlLayout.controlWidth;
                this.control.setBounds(controlX, controlY);
                this.control.render(graphics, font, mouseX, mouseY);
            }
            drawDashedDivider(graphics, x, x + width, y + controlLayout.rowHeight - 1);
            return;
        }

        graphics.text(font, ConfigTextLayout.ellipsize(font, this.title, layout.textWidth), x, y + TEXT_TOP_PAD, BlxckoutFXConfigScreen.textColor(), false);
        ConfigTextLayout.drawWrapped(graphics, font, this.description, x, y + TEXT_TOP_PAD + font.lineHeight + TEXT_BETWEEN_PAD, layout.textWidth, BlxckoutFXConfigScreen.mutedTextColor());

        if (this.control != null) {
            int controlY = y + (layout.rowHeight - this.control.getPreferredHeight(font, layout.controlWidth)) / 2;
            this.control.setBounds(x + width - layout.controlWidth, controlY);
            this.control.render(graphics, font, mouseX, mouseY);
        }

        drawDashedDivider(graphics, x, x + width, y + layout.rowHeight - 1);
    }

    public void renderOverlay(GuiGraphicsExtractor graphics, Font font, int mouseX, int mouseY) {
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

    private static void drawDashedDivider(GuiGraphicsExtractor graphics, int x1, int x2, int y) {
        for (int x = x1; x < x2; x += 8) {
            graphics.fill(x, y, Math.min(x + 4, x2), y + 1, 0x66404040);
        }
    }

    private Layout layout(Font font, int width) {
        if (this.section) {
            return new Layout(0, width, SECTION_HEIGHT);
        }
        if (this.controlOnly) {
            int controlWidth = this.control == null ? 0 : Math.min(this.control.getPreferredWidth(font, width), width);
            return new Layout(controlWidth, width, this.rowHeight);
        }

        int controlMaxWidth = this.control == null ? 0 : Math.max(0, width - 120);
        int controlWidth = this.control == null ? 0 : this.control.getPreferredWidth(font, controlMaxWidth);
        int textWidth = Math.max(0, width - controlWidth - TEXT_RIGHT_PAD);
        int titleHeight = font.lineHeight;
        int descriptionHeight = ConfigTextLayout.wrappedHeight(font, this.description, textWidth);
        int textBlockHeight = titleHeight + (descriptionHeight > 0 ? TEXT_BETWEEN_PAD + descriptionHeight : 0);
        int controlHeight = this.control == null ? 0 : this.control.getPreferredHeight(font, controlWidth);
        int rowHeight = Math.max(MIN_ROW_HEIGHT, Math.max(textBlockHeight + TEXT_TOP_PAD + TEXT_BOTTOM_PAD, controlHeight + 10));
        return new Layout(controlWidth, textWidth, rowHeight);
    }

    private record Layout(int controlWidth, int textWidth, int rowHeight) {
    }
}
