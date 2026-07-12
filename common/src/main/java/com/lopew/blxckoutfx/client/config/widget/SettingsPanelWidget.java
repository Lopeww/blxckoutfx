package com.lopew.blxckoutfx.client.config.widget;

import com.lopew.blxckoutfx.client.config.BlxckoutFXConfigScreen;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public final class SettingsPanelWidget {
    private static final int PAD = 18;
    private static final int HEADER_Y = 16;
    private static final int LIST_BOTTOM_PADDING = 8;
    private static final int BLOCK_GAP = 6;
    private static final int TEXT_GAP = 4;
    private static final int LIST_GAP = 10;

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

    public int getContentWidth() {
        return this.width - PAD * 2;
    }

    public void render(GuiGraphics graphics, Font font, int mouseX, int mouseY) {
        int innerWidth = this.width - PAD * 2;
        int headerY = this.y + HEADER_Y;

        if (this.warningVisible) {
            int warningHeight = renderWrappedBlock(
                    graphics,
                    font,
                    this.warningTitle,
                    this.warningDescription,
                    this.x + PAD,
                    headerY,
                    innerWidth,
                    0xFFFF5555,
                    BlxckoutFXConfigScreen.mutedTextColor()
            );
            graphics.fill(this.x + PAD, headerY + warningHeight + 2, this.x + this.width - PAD, headerY + warningHeight + 3, 0x66C94040);
            headerY += warningHeight + BLOCK_GAP + 3;
        }

        int titleHeight = ConfigTextLayout.drawWrapped(graphics, font, this.title, this.x + PAD, headerY, innerWidth, BlxckoutFXConfigScreen.textColor());
        int descriptionY = headerY + titleHeight + TEXT_GAP;
        int descriptionHeight = ConfigTextLayout.drawWrapped(graphics, font, this.description, this.x + PAD, descriptionY, innerWidth, BlxckoutFXConfigScreen.mutedTextColor());
        int dividerY = descriptionY + descriptionHeight + BLOCK_GAP;
        drawDashedDivider(graphics, this.x + PAD, this.x + this.width - PAD, dividerY);

        int listTop = dividerY + LIST_GAP;
        int listBottom = this.y + this.height - LIST_BOTTOM_PADDING;
        int listX = this.x + PAD;
        int listW = this.width - PAD * 2;

        graphics.enableScissor(listX, listTop, listX + listW, listBottom);
        int rowY = listTop - this.scroll;
        for (SettingRow row : this.rows) {
            int rowH = row.getHeight();
            if (rowY + rowH >= listTop && rowY <= listBottom) {
                row.render(graphics, font, mouseX, mouseY, listX, rowY, listW);
            }
            rowY += rowH;
        }
        for (SettingRow row : this.rows) {
            row.renderOverlay(graphics, font, mouseX, mouseY);
        }
        graphics.disableScissor();

        if (this.warningVisible) {
            graphics.fill(listX, listTop, listX + listW, listBottom, 0x55080808);
            graphics.fill(listX, listTop, listX + listW, listTop + 1, 0x77FF5555);
        }

        int contentHeight = getContentHeight();
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
        if (this.warningVisible) {
            return false;
        }
        int listTop = calculateListTop();
        int listBottom = this.y + this.height - LIST_BOTTOM_PADDING;
        if (!inside(mouseX, mouseY, this.x, listTop, this.width, listBottom - listTop)) {
            return false;
        }

        int rowY = listTop - this.scroll;
        int listX = this.x + PAD;
        int listW = this.width - PAD * 2;
        for (SettingRow row : this.rows) {
            if (row.mouseClicked(mouseX, mouseY, button, listX, rowY, listW)) {
                return true;
            }
            rowY += row.getHeight();
        }
        return true;
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (this.warningVisible) {
            return false;
        }
        for (SettingRow row : this.rows) {
            if (row.mouseDragged(mouseX, mouseY, button, dragX, dragY)) {
                return true;
            }
        }
        return false;
    }

    public void mouseReleased(double mouseX, double mouseY, int button) {
        if (this.warningVisible) {
            return;
        }
        for (SettingRow row : this.rows) {
            row.mouseReleased(mouseX, mouseY, button);
        }
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double scrollY) {
        if (this.warningVisible) {
            return false;
        }
        int listTop = calculateListTop();
        int listBottom = this.y + this.height - LIST_BOTTOM_PADDING;
        if (!inside(mouseX, mouseY, this.x, listTop, this.width, listBottom - listTop)) {
            return false;
        }

        int maxScroll = Math.max(0, getContentHeight() - (listBottom - listTop));
        this.scroll = clamp(this.scroll - (int) (scrollY * 22), 0, maxScroll);
        return true;
    }

    private int getContentHeight() {
        int contentHeight = 0;
        for (SettingRow row : this.rows) {
            contentHeight += row.getHeight();
        }
        return contentHeight;
    }

    private static boolean inside(double mouseX, double mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(value, max));
    }

    private static void drawDashedDivider(GuiGraphics graphics, int x1, int x2, int y) {
        for (int x = x1; x < x2; x += 8) {
            graphics.fill(x, y, Math.min(x + 4, x2), y + 1, 0x66373737);
        }
    }

    private int renderWrappedBlock(GuiGraphics graphics, Font font, Component title, Component description, int x, int y, int width, int titleColor, int descriptionColor) {
        int titleHeight = ConfigTextLayout.drawWrapped(graphics, font, title, x, y, width, titleColor);
        int descriptionY = y + titleHeight + TEXT_GAP;
        int descriptionHeight = ConfigTextLayout.drawWrapped(graphics, font, description, x, descriptionY, width, descriptionColor);
        return titleHeight + (titleHeight > 0 && descriptionHeight > 0 ? TEXT_GAP : 0) + descriptionHeight;
    }

    private int calculateListTop() {
        Font font = net.minecraft.client.Minecraft.getInstance().font;
        int headerY = this.y + HEADER_Y;
        int innerWidth = this.width - PAD * 2;
        if (this.warningVisible) {
            int warningHeight = measureBlockHeight(font, this.warningTitle, this.warningDescription, innerWidth);
            headerY += warningHeight + BLOCK_GAP + 3;
        }
        int titleHeight = ConfigTextLayout.wrappedHeight(font, this.title, innerWidth);
        int descriptionY = headerY + titleHeight + TEXT_GAP;
        int descriptionHeight = ConfigTextLayout.wrappedHeight(font, this.description, innerWidth);
        int dividerY = descriptionY + descriptionHeight + BLOCK_GAP;
        return dividerY + LIST_GAP;
    }

    private int measureBlockHeight(Font font, Component title, Component description, int width) {
        int titleHeight = ConfigTextLayout.wrappedHeight(font, title, width);
        int descriptionHeight = ConfigTextLayout.wrappedHeight(font, description, width);
        return titleHeight + (titleHeight > 0 && descriptionHeight > 0 ? TEXT_GAP : 0) + descriptionHeight;
    }
}
