package com.lopew.blxckoutfx.client.config.widget;

import com.lopew.blxckoutfx.BlxckoutFX;
import com.lopew.blxckoutfx.client.config.BlxckoutFXConfigScreen;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class SidebarWidget {
    private static final int SEARCH_HEIGHT = 22;
    private static final int ITEM_HEIGHT = 24;
    private static final int GROUP_HEIGHT = 16;
    private static final int ICON_SLOT = 12;
    private static final int CATEGORY_ICON_SIZE = 10;
    private static final int FOOTER_ICON_SIZE = 24;
    private static final int FOOTER_ICON_DRAW_SIZE = 20;
    private static final ResourceLocation SEARCH_ICON = new ResourceLocation(BlxckoutFX.MOD_ID, "textures/gui/search_icon.png");
    private static final ResourceLocation DISCORD_ICON = new ResourceLocation(BlxckoutFX.MOD_ID, "textures/gui/discord.png");
    private static final ResourceLocation CURSEFORGE_ICON = new ResourceLocation(BlxckoutFX.MOD_ID, "textures/gui/curseforge.png");
    private static final ResourceLocation MODRINTH_ICON = new ResourceLocation(BlxckoutFX.MOD_ID, "textures/gui/modrinth.png");

    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private List<BlxckoutFXConfigScreen.Category> categories;
    private final Consumer<BlxckoutFXConfigScreen.Category> onSelected;
    private final Consumer<String> onSearchChanged;
    private final Runnable onDiscordPressed;
    private final Runnable onCurseforgePressed;
    private final Runnable onModrinthPressed;

    private BlxckoutFXConfigScreen.Category selected;
    private String search = "";
    private boolean searchFocused;
    private int searchSelectionStart;
    private int searchSelectionEnd;
    private int scroll;

    public SidebarWidget(
            int x,
            int y,
            int width,
            int height,
            List<BlxckoutFXConfigScreen.Category> categories,
            BlxckoutFXConfigScreen.Category selected,
            Consumer<BlxckoutFXConfigScreen.Category> onSelected,
            Consumer<String> onSearchChanged,
            Runnable onDiscordPressed,
            Runnable onCurseforgePressed,
            Runnable onModrinthPressed
    ) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.categories = new ArrayList<>(categories);
        this.selected = selected;
        this.onSelected = onSelected;
        this.onSearchChanged = onSearchChanged;
        this.onDiscordPressed = onDiscordPressed;
        this.onCurseforgePressed = onCurseforgePressed;
        this.onModrinthPressed = onModrinthPressed;
    }

    public void setSelected(BlxckoutFXConfigScreen.Category selected) {
        this.selected = selected;
    }

    public void setCategories(List<BlxckoutFXConfigScreen.Category> categories) {
        this.categories = new ArrayList<>(categories);
        if (!this.categories.contains(this.selected)) {
            this.selected = this.categories.isEmpty() ? null : this.categories.get(0);
        }
        this.scroll = 0;
    }

    public void render(GuiGraphics graphics, Font font, int mouseX, int mouseY) {
        int pad = 12;
        int profileY = this.y + 14;
        graphics.fill(this.x + pad, profileY, this.x + pad + 24, profileY + 24, 0xFF181818);
        graphics.fill(this.x + pad + 2, profileY + 2, this.x + pad + 22, profileY + 22, 0xFF090909);
        graphics.drawString(font, Component.literal("B"), this.x + pad + 9, profileY + 8, 0xFFCACACA, false);
        graphics.drawString(font, Component.literal("BlxckoutFX"), this.x + pad + 32, profileY + 3, BlxckoutFXConfigScreen.textColor(), false);
        graphics.drawString(font, Component.literal("by Lopew"), this.x + pad + 32, profileY + 15, BlxckoutFXConfigScreen.mutedTextColor(), false);

        int searchY = profileY + 40;
        int searchX = this.x + pad;
        int searchW = this.width - pad * 2;
        int searchColor = this.searchFocused ? 0xFF181818 : 0xFF101010;
        int searchBorderColor = this.searchFocused ? 0xFF6F6F6F : BlxckoutFXConfigScreen.borderColor();
        graphics.fill(searchX, searchY, searchX + searchW, searchY + SEARCH_HEIGHT, searchColor);
        graphics.fill(searchX, searchY, searchX + searchW, searchY + 1, searchBorderColor);
        graphics.fill(searchX, searchY + SEARCH_HEIGHT - 1, searchX + searchW, searchY + SEARCH_HEIGHT, searchBorderColor);
        graphics.fill(searchX, searchY, searchX + 1, searchY + SEARCH_HEIGHT, searchBorderColor);
        graphics.fill(searchX + searchW - 1, searchY, searchX + searchW, searchY + SEARCH_HEIGHT, searchBorderColor);
        if (this.searchFocused) {
            graphics.fill(searchX + 1, searchY + SEARCH_HEIGHT - 2, searchX + searchW - 1, searchY + SEARCH_HEIGHT - 1, 0xFF2F2F2F);
        }
        graphics.blit(SEARCH_ICON, searchX + 5, searchY + 5, 12, 12, 0.0F, 0.0F, 8, 8, 8, 8);
        boolean showPlaceholder = this.search.isEmpty() && !this.searchFocused;
        String visibleText = showPlaceholder ? Component.translatable("ui.blxckoutfx.config.search_placeholder").getString() : this.search;
        int queryColor = showPlaceholder ? BlxckoutFXConfigScreen.mutedTextColor() : BlxckoutFXConfigScreen.textColor();
        if (!visibleText.isEmpty()) {
            if (this.searchFocused && hasSelection()) {
                int textX = searchX + 20;
                int selectionStartX = textX + font.width(this.search.substring(0, this.searchSelectionStart));
                int selectionEndX = textX + font.width(this.search.substring(0, this.searchSelectionEnd));
                graphics.fill(selectionStartX - 1, searchY + 4, selectionEndX + 1, searchY + 16, 0xFF2F66D0);
            }
            graphics.drawString(font, visibleText, searchX + 20, searchY + 7, queryColor, false);
        }
        if (this.searchFocused && !hasSelection() && isCaretVisible()) {
            int caretX = searchX + 20 + font.width(visibleText) + 1;
            graphics.fill(caretX, searchY + 5, caretX + 1, searchY + 15, 0xFFE5E5E5);
        }

        int listTop = searchY + SEARCH_HEIGHT + 14;
        int listBottom = this.y + this.height - 48;
        graphics.enableScissor(this.x, listTop, this.x + this.width, listBottom);
        int rowY = listTop - this.scroll;
        String activeGroup = null;
        for (BlxckoutFXConfigScreen.Category category : this.categories) {
            String groupLabel = category.group().getString();
            if (!groupLabel.equals(activeGroup)) {
                activeGroup = groupLabel;
                if (rowY + GROUP_HEIGHT >= listTop && rowY <= listBottom) {
                    graphics.drawString(font, category.group(), this.x + 12, rowY + 4, BlxckoutFXConfigScreen.mutedTextColor(), false);
                }
                rowY += GROUP_HEIGHT;
            }
            if (rowY + ITEM_HEIGHT >= listTop && rowY <= listBottom) {
                renderCategory(graphics, font, mouseX, mouseY, category, this.x + 8, rowY, this.width - 16);
            }
            rowY += ITEM_HEIGHT + 4;
        }
        graphics.disableScissor();

        int footerY = this.y + this.height - 38;
        int footerGap = 8;
        int footerGroupWidth = FOOTER_ICON_SIZE * 3 + footerGap * 2;
        int footerX = this.x + (this.width - footerGroupWidth) / 2;
        renderFooterIcon(graphics, font, DISCORD_ICON, footerX, footerY, mouseX, mouseY, Component.translatable("ui.blxckoutfx.sidebar.tooltip.discord"));
        renderFooterIcon(graphics, font, CURSEFORGE_ICON, footerX + FOOTER_ICON_SIZE + footerGap, footerY, mouseX, mouseY, Component.translatable("ui.blxckoutfx.sidebar.tooltip.curseforge"));
        renderFooterIcon(graphics, font, MODRINTH_ICON, footerX + (FOOTER_ICON_SIZE + footerGap) * 2, footerY, mouseX, mouseY, Component.translatable("ui.blxckoutfx.sidebar.tooltip.modrinth"));
    }

    private void renderCategory(GuiGraphics graphics, Font font, int mouseX, int mouseY, BlxckoutFXConfigScreen.Category category, int rowX, int rowY, int rowW) {
        boolean isSelected = category == this.selected;
        boolean hover = inside(mouseX, mouseY, rowX, rowY, rowW, ITEM_HEIGHT);
        if (isSelected) {
            graphics.fill(rowX, rowY, rowX + rowW, rowY + ITEM_HEIGHT, 0xFF171717);
            graphics.fill(rowX, rowY, rowX + 2, rowY + ITEM_HEIGHT, 0xFF9D9D9D);
        } else if (hover) {
            graphics.fill(rowX, rowY, rowX + rowW, rowY + ITEM_HEIGHT, 0x88151515);
        }

        int iconX = rowX + 8;
        int iconY = rowY + 6;
        if (category.iconId != null && !category.iconId.isBlank()) {
            ResourceLocation iconTexture = new ResourceLocation(BlxckoutFX.MOD_ID, "textures/gui/" + category.iconId + ".png");
            graphics.blit(iconTexture, iconX + 1, iconY + 1, CATEGORY_ICON_SIZE, CATEGORY_ICON_SIZE, 0.0F, 0.0F, 8, 8, 8, 8);
        } else {
            graphics.fill(iconX, iconY, iconX + ICON_SLOT, iconY + ICON_SLOT, 0x33000000);
            graphics.fill(iconX, iconY, iconX + ICON_SLOT, iconY + 1, 0x662C2C2C);
            graphics.fill(iconX, iconY + ICON_SLOT - 1, iconX + ICON_SLOT, iconY + ICON_SLOT, 0x662C2C2C);
            graphics.fill(iconX, iconY, iconX + 1, iconY + ICON_SLOT, 0x662C2C2C);
            graphics.fill(iconX + ICON_SLOT - 1, iconY, iconX + ICON_SLOT, iconY + ICON_SLOT, 0x662C2C2C);
        }

        int color = isSelected ? BlxckoutFXConfigScreen.textColor() : 0xFFCFCFCF;
        graphics.drawString(font, category.title(), rowX + 22, rowY + 8, color, false);
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int pad = 12;
        int searchY = this.y + 54;
        boolean clickedSearch = inside(mouseX, mouseY, this.x + pad, searchY, this.width - pad * 2, SEARCH_HEIGHT);
        this.searchFocused = clickedSearch;
        if (clickedSearch) {
            collapseSelectionToEnd();
            return true;
        }
        clearSelection();

        int listTop = searchY + SEARCH_HEIGHT + 14;
        int listBottom = this.y + this.height - 48;
        int rowY = listTop - this.scroll;
        String activeGroup = null;
        for (BlxckoutFXConfigScreen.Category category : this.categories) {
            String groupLabel = category.group().getString();
            if (!groupLabel.equals(activeGroup)) {
                activeGroup = groupLabel;
                rowY += GROUP_HEIGHT;
            }
            if (inside(mouseX, mouseY, this.x + 8, rowY, this.width - 16, ITEM_HEIGHT) && rowY >= listTop - ITEM_HEIGHT && rowY <= listBottom) {
                this.selected = category;
                this.onSelected.accept(category);
                return true;
            }
            rowY += ITEM_HEIGHT + 4;
        }

        int footerY = this.y + this.height - 38;
        int footerGap = 8;
        int footerGroupWidth = FOOTER_ICON_SIZE * 3 + footerGap * 2;
        int footerX = this.x + (this.width - footerGroupWidth) / 2;
        if (inside(mouseX, mouseY, footerX, footerY, FOOTER_ICON_SIZE, FOOTER_ICON_SIZE)) {
            this.onDiscordPressed.run();
            return true;
        }
        if (inside(mouseX, mouseY, footerX + FOOTER_ICON_SIZE + footerGap, footerY, FOOTER_ICON_SIZE, FOOTER_ICON_SIZE)) {
            this.onCurseforgePressed.run();
            return true;
        }
        if (inside(mouseX, mouseY, footerX + (FOOTER_ICON_SIZE + footerGap) * 2, footerY, FOOTER_ICON_SIZE, FOOTER_ICON_SIZE)) {
            this.onModrinthPressed.run();
            return true;
        }

        return false;
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double scrollY) {
        int listTop = this.y + 90;
        int listBottom = this.y + this.height - 48;
        if (!inside(mouseX, mouseY, this.x, listTop, this.width, listBottom - listTop)) {
            return false;
        }

        int contentHeight = 0;
        String activeGroup = null;
        for (BlxckoutFXConfigScreen.Category category : this.categories) {
            String groupLabel = category.group().getString();
            if (!groupLabel.equals(activeGroup)) {
                activeGroup = groupLabel;
                contentHeight += GROUP_HEIGHT;
            }
            contentHeight += ITEM_HEIGHT + 4;
        }
        int maxScroll = Math.max(0, contentHeight - (listBottom - listTop));
        this.scroll = clamp(this.scroll - (int) (scrollY * 18), 0, maxScroll);
        return true;
    }

    public boolean keyPressed(int keyCode, int modifiers) {
        if (!this.searchFocused) {
            return false;
        }

        if (Screen.hasControlDown() && keyCode == GLFW.GLFW_KEY_A) {
            selectAllSearch();
            return true;
        }

        if (Screen.hasControlDown() && (keyCode == GLFW.GLFW_KEY_BACKSPACE || keyCode == GLFW.GLFW_KEY_DELETE)) {
            if (!this.search.isEmpty() || hasSelection()) {
                clearSearch();
            }
            return true;
        }

        if ((keyCode == GLFW.GLFW_KEY_BACKSPACE || keyCode == GLFW.GLFW_KEY_DELETE) && hasSelection()) {
            clearSearch();
            return true;
        }

        if (keyCode == GLFW.GLFW_KEY_BACKSPACE && !this.search.isEmpty()) {
            this.search = this.search.substring(0, this.search.length() - 1);
            this.scroll = 0;
            collapseSelectionToEnd();
            this.onSearchChanged.accept(this.search);
            return true;
        }

        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            this.searchFocused = false;
            clearSelection();
            return true;
        }
        return false;
    }

    public boolean charTyped(char codePoint) {
        if (!this.searchFocused || Character.isISOControl(codePoint)) {
            return false;
        }

        if (hasSelection()) {
            this.search = Character.toString(codePoint);
        } else {
            this.search += codePoint;
        }
        this.scroll = 0;
        collapseSelectionToEnd();
        this.onSearchChanged.accept(this.search);
        return true;
    }

    private void clearSearch() {
        this.search = "";
        this.scroll = 0;
        clearSelection();
        this.onSearchChanged.accept(this.search);
    }

    private void selectAllSearch() {
        this.searchSelectionStart = 0;
        this.searchSelectionEnd = this.search.length();
    }

    private void clearSelection() {
        this.searchSelectionStart = 0;
        this.searchSelectionEnd = 0;
    }

    private void collapseSelectionToEnd() {
        int end = this.search.length();
        this.searchSelectionStart = end;
        this.searchSelectionEnd = end;
    }

    private boolean hasSelection() {
        return this.searchSelectionStart != this.searchSelectionEnd;
    }

    private static boolean inside(double mouseX, double mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }

    private void renderFooterIcon(GuiGraphics graphics, Font font, ResourceLocation texture, int x, int y, int mouseX, int mouseY, Component tooltip) {
        boolean hover = inside(mouseX, mouseY, x, y, FOOTER_ICON_SIZE, FOOTER_ICON_SIZE);
        graphics.fill(x, y, x + FOOTER_ICON_SIZE, y + FOOTER_ICON_SIZE, hover ? 0xFF232323 : 0xFF111111);
        if (hover) {
            graphics.fill(x, y, x + FOOTER_ICON_SIZE, y + 1, 0xFF5A5A5A);
            graphics.fill(x, y, x + 1, y + FOOTER_ICON_SIZE, 0xFF5A5A5A);
            graphics.fill(x + FOOTER_ICON_SIZE - 1, y, x + FOOTER_ICON_SIZE, y + FOOTER_ICON_SIZE, 0xFF5A5A5A);
            graphics.fill(x, y + FOOTER_ICON_SIZE - 1, x + FOOTER_ICON_SIZE, y + FOOTER_ICON_SIZE, 0xFF5A5A5A);
        }
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        graphics.blit(texture, x + 2, y + 2, 0, 0, FOOTER_ICON_DRAW_SIZE, FOOTER_ICON_DRAW_SIZE, 20, 20);
        if (hover) {
            graphics.renderTooltip(font, tooltip, mouseX, mouseY);
        }
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(value, max));
    }

    private static boolean isCaretVisible() {
        return (System.currentTimeMillis() / 500L) % 2L == 0L;
    }
}
