package com.lopew.blxckoutfx.client.config.widget;

import com.lopew.blxckoutfx.client.config.BlxckoutFXConfigScreen;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public final class SettingsPanelWidget {
    private static final int PAD = 18;
    private static final int HEADER_Y = 16;
    private static final int LIST_BOTTOM_PADDING = 8;
    private static final int HEADER_GAP = 8;
    private static final int TITLE_DESC_GAP = 3;
    private static final int WARNING_PAD = 8;
    private static final int WARNING_GAP = 6;

    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private Component title = Component.empty();
    private Component description = Component.empty();
    private Component warningTitle = Component.empty();
    private Component warningDescription = Component.empty();
    private boolean warningVisible;
    private List<SettingRow> rows = List.of();
    private int scroll;

    public SettingsPanelWidget(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void setCategory(Component title, Component description, List<SettingRow> rows) {
        this.title = title == null ? Component.empty() : title;
        this.description = description == null ? Component.empty() : description;
        this.warningVisible = false;
        this.warningTitle = Component.empty();
        this.warningDescription = Component.empty();
        this.rows = new ArrayList<>(rows);
        this.scroll = 0;
    }

    public void setCategoryWithWarning(Component title, Component description, Component warningTitle, Component warningDescription, List<SettingRow> rows) {
        this.title = title == null ? Component.empty() : title;
        this.description = description == null ? Component.empty() : description;
        this.warningVisible = true;
        this.warningTitle = warningTitle == null ? Component.empty() : warningTitle;
        this.warningDescription = warningDescription == null ? Component.empty() : warningDescription;
        this.rows = new ArrayList<>(rows);
        this.scroll = 0;
    }

    public void render(GuiGraphicsExtractor graphics, Font font, int mouseX, int mouseY) {
        PanelLayout layout = layout(font);
        int headerY = this.y + HEADER_Y;
        if (this.warningVisible) {
            int warningTextW = layout.headerWidth;
            int warningHeight = layout.warningHeight;
            graphics.fill(this.x + PAD, headerY, this.x + this.width - PAD, headerY + warningHeight, 0x66101010);
            graphics.fill(this.x + PAD, headerY, this.x + this.width - PAD, headerY + 1, 0x66C94040);
            ConfigTextLayout.drawWrapped(graphics, font, this.warningTitle, this.x + PAD, headerY + WARNING_PAD, warningTextW, 0xFFFF5555);
            int warningDescY = headerY + WARNING_PAD + ConfigTextLayout.wrappedHeight(font, this.warningTitle, warningTextW) + WARNING_GAP;
            ConfigTextLayout.drawWrapped(graphics, font, this.warningDescription, this.x + PAD, warningDescY, warningTextW, BlxckoutFXConfigScreen.mutedTextColor());
            headerY += warningHeight + HEADER_GAP;
        }

        ConfigTextLayout.drawWrapped(graphics, font, this.title, this.x + PAD, headerY, layout.headerWidth, BlxckoutFXConfigScreen.textColor());
        int titleHeight = ConfigTextLayout.wrappedHeight(font, this.title, layout.headerWidth);
        int descriptionY = headerY + titleHeight + TITLE_DESC_GAP;
        ConfigTextLayout.drawWrapped(graphics, font, this.description, this.x + PAD, descriptionY, layout.headerWidth, BlxckoutFXConfigScreen.mutedTextColor());
        int descriptionHeight = ConfigTextLayout.wrappedHeight(font, this.description, layout.headerWidth);
        int dividerY = descriptionY + descriptionHeight + HEADER_GAP;
        drawDashedDivider(graphics, this.x + PAD, this.x + this.width - PAD, dividerY);

        int listTop = layout.listTop;
        int listBottom = this.y + this.height - LIST_BOTTOM_PADDING;
        int listX = this.x + PAD;
        int listW = layout.listWidth;

        graphics.enableScissor(listX, listTop, listX + listW, listBottom);
        int rowY = listTop - this.scroll;
        for (SettingRow row : this.rows) {
            int rowH = row.getHeight(font, listW);
            if (rowY + rowH >= listTop && rowY <= listBottom) {
                row.render(graphics, font, mouseX, mouseY, listX, rowY, listW);
            }
            rowY += rowH;
        }
        graphics.disableScissor();

        for (SettingRow row : this.rows) {
            row.renderOverlay(graphics, font, mouseX, mouseY);
        }

        if (this.warningVisible) {
            graphics.fill(listX, listTop, listX + listW, listBottom, 0x55080808);
            graphics.fill(listX, listTop, listX + listW, listTop + 1, 0x77FF5555);
        }

        int contentHeight = getContentHeight(font, listW);
        int viewportHeight = listBottom - listTop;
        if (contentHeight > viewportHeight) {
            int barX = this.x + this.width - 10;
            int barY = listTop;
            int barH = viewportHeight;
            int thumbH = Math.max(18, barH * viewportHeight / contentHeight);
            int thumbY = barY + this.scroll * (barH - thumbH) / Math.max(1, contentHeight - viewportHeight);
            graphics.fill(barX, barY, barX + 2, barY + barH, 0x550B0B0B);
            graphics.fill(barX, thumbY, barX + 2, thumbY + thumbH, 0xFF6A6A6A);
        }
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        PanelLayout layout = layout(net.minecraft.client.Minecraft.getInstance().font);
        int listTop = layout.listTop;
        int listBottom = this.y + this.height - LIST_BOTTOM_PADDING;
        if (!inside(mouseX, mouseY, this.x, listTop, this.width, listBottom - listTop)) {
            return false;
        }

        int rowY = listTop - this.scroll;
        int listX = this.x + PAD;
        int listW = layout.listWidth;
        for (SettingRow row : this.rows) {
            int rowH = row.getHeight(net.minecraft.client.Minecraft.getInstance().font, listW);
            if (row.mouseClicked(mouseX, mouseY, button, listX, rowY, listW)) {
                return true;
            }
            rowY += rowH;
        }
        return true;
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        for (SettingRow row : this.rows) {
            if (row.mouseDragged(mouseX, mouseY, button, dragX, dragY)) {
                return true;
            }
        }
        return false;
    }

    public void mouseReleased(double mouseX, double mouseY, int button) {
        for (SettingRow row : this.rows) {
            row.mouseReleased(mouseX, mouseY, button);
        }
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double scrollY) {
        PanelLayout layout = layout(net.minecraft.client.Minecraft.getInstance().font);
        int listTop = layout.listTop;
        int listBottom = this.y + this.height - LIST_BOTTOM_PADDING;
        if (!inside(mouseX, mouseY, this.x, listTop, this.width, listBottom - listTop)) {
            return false;
        }

        int maxScroll = Math.max(0, getContentHeight(net.minecraft.client.Minecraft.getInstance().font, layout.listWidth) - (listBottom - listTop));
        this.scroll = clamp(this.scroll - (int) (scrollY * 22), 0, maxScroll);
        return true;
    }

    private int getContentHeight(Font font, int listWidth) {
        int contentHeight = 0;
        for (SettingRow row : this.rows) {
            contentHeight += row.getHeight(font, listWidth);
        }
        return contentHeight;
    }

    private PanelLayout layout(Font font) {
        int headerWidth = this.width - PAD * 2;
        int warningHeight = 0;
        if (this.warningVisible) {
            int warningTextHeight = ConfigTextLayout.wrappedHeight(font, this.warningTitle, headerWidth);
            int warningDescHeight = ConfigTextLayout.wrappedHeight(font, this.warningDescription, headerWidth);
            warningHeight = WARNING_PAD + warningTextHeight + (warningDescHeight > 0 ? WARNING_GAP : 0) + warningDescHeight + WARNING_PAD;
        }

        int headerStartY = HEADER_Y + warningHeight + (this.warningVisible ? HEADER_GAP : 0);
        int titleHeight = ConfigTextLayout.wrappedHeight(font, this.title, headerWidth);
        int descriptionHeight = ConfigTextLayout.wrappedHeight(font, this.description, headerWidth);
        int dividerY = this.y + headerStartY + titleHeight + (descriptionHeight > 0 ? TITLE_DESC_GAP : 0) + descriptionHeight + HEADER_GAP;
        int listTop = Math.min(dividerY + HEADER_GAP, this.y + this.height - LIST_BOTTOM_PADDING - 1);
        int listWidth = headerWidth;
        return new PanelLayout(headerWidth, warningHeight, dividerY, listTop, listWidth);
    }

    private static boolean inside(double mouseX, double mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(value, max));
    }

    private static void drawDashedDivider(GuiGraphicsExtractor graphics, int x1, int x2, int y) {
        for (int x = x1; x < x2; x += 8) {
            graphics.fill(x, y, Math.min(x + 4, x2), y + 1, 0x66373737);
        }
    }

    private record PanelLayout(int headerWidth, int warningHeight, int dividerY, int listTop, int listWidth) {
    }
}
