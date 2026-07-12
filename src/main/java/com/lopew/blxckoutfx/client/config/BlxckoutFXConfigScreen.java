package com.lopew.blxckoutfx.client.config;

import com.lopew.blxckoutfx.client.BlxckoutFXClientConfig;
import com.lopew.blxckoutfx.client.BlxckoutFXI18n;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public final class BlxckoutFXConfigScreen extends GuiScreen {
    private static final int PANEL_COLOR = 0xF0060606;
    private static final int SIDEBAR_COLOR = 0xF00A0A0A;
    private static final int CONTENT_COLOR = 0xE0101010;
    private static final int BORDER_COLOR = 0xFF232323;
    private static final int TEXT_COLOR = 0xFFF1F1F1;
    private static final int MUTED_TEXT_COLOR = 0xFF8E8E8E;
    private static final int ACCENT_COLOR = 0xFF4A4A4A;
    private static final String MODID = "blxckoutfx";
    private static final String GUI_TEXTURE_ROOT = "textures/gui/";
    private static final String SEARCH_ICON = GUI_TEXTURE_ROOT + "search_icon.png";
    private static final String DISCORD_ICON = GUI_TEXTURE_ROOT + "discord.png";
    private static final String CURSEFORGE_ICON = GUI_TEXTURE_ROOT + "curseforge.png";
    private static final String MODRINTH_ICON = GUI_TEXTURE_ROOT + "modrinth.png";

    private static final int DONE_ID = 1;
    private static final int CANCEL_ID = 2;
    private static final int CATEGORY_BASE_ID = 100;
    private static final int ACTION_BASE_ID = 200;

    private static final int ACTION_DARK_MODE = ACTION_BASE_ID + 1;
    private static final int ACTION_SELECTED_PRESET = ACTION_BASE_ID + 2;
    private static final int ACTION_PRESET = ACTION_BASE_ID + 3;
    private static final int ACTION_INVENTORY_BUTTON_ENABLED = ACTION_BASE_ID + 4;
    private static final int ACTION_INVENTORY_BUTTON_LOCKED = ACTION_BASE_ID + 5;
    private static final int ACTION_INVENTORY_BUTTON_PRESET = ACTION_BASE_ID + 6;
    private static final int ACTION_INVENTORY_BUTTON_STYLE = ACTION_BASE_ID + 7;

    private static final int ROW_HEIGHT = 50;
    private static final int SECTION_HEIGHT = 22;
    private static final int BUTTON_WIDTH = 82;
    private static final int DROPDOWN_WIDTH = 110;
    private static final int INVENTORY_DROPDOWN_WIDTH = 95;
    private static final int TOGGLE_RESERVED_WIDTH = 58;
    private static final int BUTTON_RESERVED_WIDTH = 114;
    private static final int DROPDOWN_RESERVED_WIDTH = 142;
    private static final int INVENTORY_DROPDOWN_RESERVED_WIDTH = 127;
    private static final int PREVIEW_WIDTH = 320;
    private static final int PREVIEW_HEIGHT = 138;
    private static final int PREVIEW_TAB_WIDTH = 60;
    private static final int PREVIEW_TAB_HEIGHT = 16;
    private static final int PREVIEW_BUTTON_MIN_WIDTH = 78;
    private static final int PREVIEW_BUTTON_MAX_WIDTH = 112;
    private static final int CONTENT_PAD = 18;
    private static final int CONTENT_HEADER_Y = 16;
    private static final int CONTENT_TEXT_GAP = 4;
    private static final int CONTENT_BLOCK_GAP = 6;
    private static final int CONTENT_LIST_GAP = 10;
    private static final int SEARCH_HEIGHT = 22;
    private static final int SIDEBAR_GROUP_HEIGHT = 16;
    private static final int SIDEBAR_ITEM_HEIGHT = 24;
    private static final int SIDEBAR_ITEM_GAP = 4;
    private static final int FOOTER_ICON_SIZE = 24;
    private static final Map<String, ResourceLocation> GUI_TEXTURE_CACHE = new HashMap<>();

    private final GuiScreen parent;
    private final List<ConfigControl> controls = new ArrayList<>();
    private final List<SidebarGroupLabel> sidebarGroupLabels = new ArrayList<>();

    private GuiTextField searchField;
    private Category selectedCategory = Category.OVERVIEW;
    private boolean darkModeEnabled;
    private boolean inventoryButtonEnabled;
    private boolean inventoryButtonLocked;
    private int presetIndex;
    private BlxckoutFXClientConfig.InventoryButtonPreset inventoryButtonPreset;
    private BlxckoutFXClientConfig.InventoryButtonStyle inventoryButtonStyle;
    private PreviewTab selectedPreviewTab = PreviewTab.GUI;

    private int rootX;
    private int rootY;
    private int rootW;
    private int rootH;
    private int sidebarW;

    public BlxckoutFXConfigScreen(GuiScreen parent) {
        this.parent = parent;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        loadLocalValues();
        calculateLayout();
        rebuildButtons();
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    private void loadLocalValues() {
        BlxckoutFXClientConfig.normalize();
        this.selectedCategory = Category.fromConfigValue(BlxckoutFXClientConfig.getLastConfigCategory());
        this.darkModeEnabled = BlxckoutFXClientConfig.isDarkModeEnabled();
        this.inventoryButtonEnabled = BlxckoutFXClientConfig.isInventoryButtonEnabled();
        this.inventoryButtonLocked = BlxckoutFXClientConfig.isInventoryButtonLocked();
        this.presetIndex = clampPresetIndex(BlxckoutFXClientConfig.getPresetIndex());
        this.inventoryButtonPreset = BlxckoutFXClientConfig.getInventoryButtonPreset();
        this.inventoryButtonStyle = BlxckoutFXClientConfig.getInventoryButtonStyle();
    }

    private void calculateLayout() {
        this.rootW = Math.min(810, Math.max(300, this.width - 88));
        this.rootH = Math.min(468, Math.max(190, this.height - 76));
        this.rootX = (this.width - this.rootW) / 2;
        this.rootY = (this.height - this.rootH) / 2;
        this.sidebarW = Math.min(212, Math.max(188, this.rootW / 4));
    }

    private void rebuildButtons() {
        this.buttonList.clear();
        this.controls.clear();
        this.sidebarGroupLabels.clear();

        int searchX = this.rootX + 12;
        int searchY = this.rootY + 54;
        String oldSearch = this.searchField == null ? "" : this.searchField.getText();
        boolean focused = this.searchField != null && this.searchField.isFocused();
        this.searchField = new GuiTextField(0, this.fontRenderer, searchX + 20, searchY + 7, this.sidebarW - 48, 12);
        this.searchField.setMaxStringLength(32);
        this.searchField.setEnableBackgroundDrawing(false);
        this.searchField.setTextColor(0xFFE5E5E5);
        this.searchField.setDisabledTextColour(0xFF777777);
        this.searchField.setText(oldSearch);
        this.searchField.setFocused(focused);

        int rowY = this.rootY + 90;
        int categoryIndex = 0;
        String activeGroup = null;
        for (Category category : Category.values()) {
            String groupLabel = translate(category.groupKey);
            if (!groupLabel.equals(activeGroup)) {
                activeGroup = groupLabel;
                this.sidebarGroupLabels.add(new SidebarGroupLabel(this.rootX + 12, rowY + 4, groupLabel));
                rowY += SIDEBAR_GROUP_HEIGHT;
            }
            String title = translate(category.titleKey);
            GuiButton button = new CategoryButton(CATEGORY_BASE_ID + categoryIndex, this.rootX + 8, rowY, this.sidebarW - 16, SIDEBAR_ITEM_HEIGHT, title, category);
            button.enabled = category != this.selectedCategory;
            this.buttonList.add(button);
            rowY += SIDEBAR_ITEM_HEIGHT + SIDEBAR_ITEM_GAP;
            categoryIndex++;
        }

        if (hasSearchQuery(oldSearch)) {
            addSearchControls(oldSearch);
        } else {
            addCategoryControls();
        }

        int buttonY = this.rootY + this.rootH - 23;
        this.buttonList.add(new FlatButton(CANCEL_ID, this.rootX + this.rootW - 146, buttonY, 64, 16, translate("ui.blxckoutfx.config.cancel")));
        this.buttonList.add(new FlatButton(DONE_ID, this.rootX + this.rootW - 76, buttonY, 64, 16, translate("ui.blxckoutfx.config.done")));
    }

    private void addCategoryControls() {
        int contentRight = this.rootX + this.rootW - 18;
        int rowY = contentListTop() + SECTION_HEIGHT;

        switch (this.selectedCategory) {
            case OVERVIEW:
                this.controls.add(new ToggleControl(
                        contentRight - 26,
                        rowY + (ROW_HEIGHT - 12) / 2,
                        () -> this.darkModeEnabled,
                        value -> {
                            this.darkModeEnabled = value;
                            if (!value) {
                                this.presetIndex = 0;
                            } else if (this.presetIndex == 0) {
                                this.presetIndex = 2;
                            }
                            rebuildButtons();
                        }
                ));
                rowY += ROW_HEIGHT;
                this.controls.add(new ButtonControl(
                        contentRight - BUTTON_WIDTH,
                        rowY + (ROW_HEIGHT - 16) / 2,
                        BUTTON_WIDTH,
                        16,
                        () -> presetLabel(this.presetIndex),
                        () -> {
                            this.selectedCategory = Category.PRESETS;
                            rebuildButtons();
                        }
                ));
                break;
            case PRESETS:
                this.controls.add(new DropdownControl(
                        contentRight - DROPDOWN_WIDTH,
                        rowY + (ROW_HEIGHT - 16) / 2 - 1,
                        DROPDOWN_WIDTH,
                        presetOptions(),
                        () -> this.presetIndex,
                        index -> {
                            this.presetIndex = clampPresetIndex(index);
                            this.darkModeEnabled = this.presetIndex != 0;
                            rebuildButtons();
                        }
                ));
                break;
            case INVENTORY_BUTTON:
                this.controls.add(new ToggleControl(
                        contentRight - 26,
                        rowY + (ROW_HEIGHT - 12) / 2,
                        () -> this.inventoryButtonEnabled,
                        value -> {
                            this.inventoryButtonEnabled = value;
                            rebuildButtons();
                        }
                ));
                rowY += ROW_HEIGHT;
                this.controls.add(new ToggleControl(
                        contentRight - 26,
                        rowY + (ROW_HEIGHT - 12) / 2,
                        () -> this.inventoryButtonLocked,
                        value -> {
                            this.inventoryButtonLocked = value;
                            rebuildButtons();
                        }
                ));
                rowY += ROW_HEIGHT;
                this.controls.add(new DropdownControl(
                        contentRight - INVENTORY_DROPDOWN_WIDTH,
                        rowY + (ROW_HEIGHT - 16) / 2,
                        INVENTORY_DROPDOWN_WIDTH,
                        inventoryButtonPresetOptions(),
                        () -> this.inventoryButtonPreset == null ? 0 : this.inventoryButtonPreset.ordinal(),
                        index -> {
                            BlxckoutFXClientConfig.InventoryButtonPreset[] values = BlxckoutFXClientConfig.InventoryButtonPreset.values();
                            this.inventoryButtonPreset = values[Math.max(0, Math.min(index, values.length - 1))];
                            rebuildButtons();
                        }
                ));
                rowY += ROW_HEIGHT;
                this.controls.add(new DropdownControl(
                        contentRight - INVENTORY_DROPDOWN_WIDTH,
                        rowY + (ROW_HEIGHT - 16) / 2,
                        INVENTORY_DROPDOWN_WIDTH,
                        inventoryButtonStyleOptions(),
                        () -> this.inventoryButtonStyle == BlxckoutFXClientConfig.InventoryButtonStyle.Short ? 1 : 0,
                        index -> {
                            this.inventoryButtonStyle = index == 1
                                    ? BlxckoutFXClientConfig.InventoryButtonStyle.Short
                                    : BlxckoutFXClientConfig.InventoryButtonStyle.Classic;
                            rebuildButtons();
                        }
                ));
                break;
            case AE2:
            case ABOUT:
                break;
        }
    }

    private void addSearchControls(String query) {
        int contentRight = this.rootX + this.rootW - CONTENT_PAD;
        int rowY = contentListTop();
        for (SearchGroup group : buildSearchGroups(query)) {
            rowY += SECTION_HEIGHT;
            for (SearchRowSpec spec : group.rows) {
                if (spec.controlFactory != null) {
                    this.controls.add(spec.controlFactory.create(contentRight, rowY));
                }
                rowY += ROW_HEIGHT;
            }
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button == null) {
            return;
        }

        if (button.id == DONE_ID) {
            saveAndClose();
            return;
        }
        if (button.id == CANCEL_ID) {
            this.mc.displayGuiScreen(this.parent);
            return;
        }
        if (button.id >= CATEGORY_BASE_ID && button.id < CATEGORY_BASE_ID + Category.values().length) {
            this.selectedCategory = Category.values()[button.id - CATEGORY_BASE_ID];
            rebuildButtons();
            return;
        }

        handleAction(button.id);
    }

    private void handleAction(int id) {
        if (id == ACTION_DARK_MODE) {
            this.darkModeEnabled = !this.darkModeEnabled;
            if (!this.darkModeEnabled) {
                this.presetIndex = 0;
            } else if (this.presetIndex == 0) {
                this.presetIndex = 2;
            }
        } else if (id == ACTION_SELECTED_PRESET) {
            this.selectedCategory = Category.PRESETS;
        } else if (id == ACTION_PRESET) {
            this.presetIndex = (this.presetIndex + 1) % 5;
            this.darkModeEnabled = this.presetIndex != 0;
        } else if (id == ACTION_INVENTORY_BUTTON_ENABLED) {
            this.inventoryButtonEnabled = !this.inventoryButtonEnabled;
        } else if (id == ACTION_INVENTORY_BUTTON_LOCKED) {
            this.inventoryButtonLocked = !this.inventoryButtonLocked;
        } else if (id == ACTION_INVENTORY_BUTTON_PRESET) {
            this.inventoryButtonPreset = nextInventoryButtonPreset(this.inventoryButtonPreset);
        } else if (id == ACTION_INVENTORY_BUTTON_STYLE) {
            this.inventoryButtonStyle = this.inventoryButtonStyle == BlxckoutFXClientConfig.InventoryButtonStyle.Short
                    ? BlxckoutFXClientConfig.InventoryButtonStyle.Classic
                    : BlxckoutFXClientConfig.InventoryButtonStyle.Short;
        }
        rebuildButtons();
    }

    private void saveAndClose() {
        BlxckoutFXClientConfig.setLastConfigCategory(this.selectedCategory.name());
        BlxckoutFXClientConfig.setPresetIndex(this.presetIndex);
        BlxckoutFXClientConfig.setDarkModeEnabled(this.darkModeEnabled);
        BlxckoutFXClientConfig.setInventoryButtonEnabled(this.inventoryButtonEnabled);
        BlxckoutFXClientConfig.setInventoryButtonLocked(this.inventoryButtonLocked);
        BlxckoutFXClientConfig.setInventoryButtonPreset(this.inventoryButtonPreset);
        BlxckoutFXClientConfig.setInventoryButtonStyle(this.inventoryButtonStyle);
        this.mc.displayGuiScreen(this.parent);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        drawRect(0, 0, this.width, this.height, 0xCC000000);
        drawPanel();
        drawSidebar(mouseX, mouseY);
        drawContent(mouseX, mouseY);
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderControls(mouseX, mouseY);
        this.searchField.drawTextBox();
        drawSearchPlaceholder();
        renderControlOverlays(mouseX, mouseY);
    }

    private void renderControls(int mouseX, int mouseY) {
        for (ConfigControl control : this.controls) {
            control.render(this.mc, this.fontRenderer, mouseX, mouseY);
        }
    }

    private void renderControlOverlays(int mouseX, int mouseY) {
        for (ConfigControl control : this.controls) {
            control.renderOverlay(this.mc, this.fontRenderer, mouseX, mouseY);
        }
    }

    private void drawPanel() {
        drawRect(this.rootX, this.rootY, this.rootX + this.rootW, this.rootY + this.rootH, PANEL_COLOR);
        drawRect(this.rootX, this.rootY, this.rootX + this.rootW, this.rootY + 1, BORDER_COLOR);
        drawRect(this.rootX, this.rootY + this.rootH - 1, this.rootX + this.rootW, this.rootY + this.rootH, BORDER_COLOR);
        drawRect(this.rootX, this.rootY, this.rootX + 1, this.rootY + this.rootH, BORDER_COLOR);
        drawRect(this.rootX + this.rootW - 1, this.rootY, this.rootX + this.rootW, this.rootY + this.rootH, BORDER_COLOR);
        drawRect(this.rootX, this.rootY, this.rootX + this.sidebarW, this.rootY + this.rootH, SIDEBAR_COLOR);
        drawRect(this.rootX + this.sidebarW, this.rootY, this.rootX + this.rootW, this.rootY + this.rootH - 30, CONTENT_COLOR);
        drawRect(this.rootX + this.sidebarW - 1, this.rootY, this.rootX + this.sidebarW, this.rootY + this.rootH, BORDER_COLOR);
        drawRect(this.rootX + this.sidebarW, this.rootY + this.rootH - 30, this.rootX + this.rootW, this.rootY + this.rootH - 29, BORDER_COLOR);
    }

    private void drawSidebar(int mouseX, int mouseY) {
        int profileY = this.rootY + 14;
        int pad = 12;
        drawRect(this.rootX + pad, profileY, this.rootX + pad + 24, profileY + 24, 0xFF181818);
        drawRect(this.rootX + pad + 2, profileY + 2, this.rootX + pad + 22, profileY + 22, 0xFF090909);
        drawString(this.fontRenderer, "B", this.rootX + pad + 9, profileY + 8, 0xFFCACACA);
        drawString(this.fontRenderer, "BlxckoutFX", this.rootX + pad + 32, profileY + 3, TEXT_COLOR);
        drawString(this.fontRenderer, "by Lopew", this.rootX + pad + 32, profileY + 15, MUTED_TEXT_COLOR);

        int searchBoxX = this.rootX + pad;
        int searchBoxY = profileY + 40;
        int searchBoxW = this.sidebarW - pad * 2;
        boolean searchFocused = this.searchField.isFocused();
        int searchColor = searchFocused ? 0xFF181818 : 0xFF101010;
        int searchBorderColor = searchFocused ? 0xFF6F6F6F : BORDER_COLOR;
        drawRect(searchBoxX, searchBoxY, searchBoxX + searchBoxW, searchBoxY + SEARCH_HEIGHT, searchColor);
        drawRect(searchBoxX, searchBoxY, searchBoxX + searchBoxW, searchBoxY + 1, searchBorderColor);
        drawRect(searchBoxX, searchBoxY + SEARCH_HEIGHT - 1, searchBoxX + searchBoxW, searchBoxY + SEARCH_HEIGHT, searchBorderColor);
        drawRect(searchBoxX, searchBoxY, searchBoxX + 1, searchBoxY + SEARCH_HEIGHT, searchBorderColor);
        drawRect(searchBoxX + searchBoxW - 1, searchBoxY, searchBoxX + searchBoxW, searchBoxY + SEARCH_HEIGHT, searchBorderColor);
        if (searchFocused) {
            drawRect(searchBoxX + 1, searchBoxY + SEARCH_HEIGHT - 2, searchBoxX + searchBoxW - 1, searchBoxY + SEARCH_HEIGHT - 1, 0xFF2F2F2F);
        }
        drawTexture(SEARCH_ICON, searchBoxX + 5, searchBoxY + 5, 12, 12, 8, 8);

        for (SidebarGroupLabel label : this.sidebarGroupLabels) {
            drawString(this.fontRenderer, label.text, label.x, label.y, MUTED_TEXT_COLOR);
        }

        int footerY = this.rootY + this.rootH - 38;
        int footerGap = 8;
        int footerGroupWidth = FOOTER_ICON_SIZE * 3 + footerGap * 2;
        int footerX = this.rootX + (this.sidebarW - footerGroupWidth) / 2;
        drawFooterIcon(DISCORD_ICON, footerX, footerY, mouseX, mouseY);
        drawFooterIcon(CURSEFORGE_ICON, footerX + FOOTER_ICON_SIZE + footerGap, footerY, mouseX, mouseY);
        drawFooterIcon(MODRINTH_ICON, footerX + (FOOTER_ICON_SIZE + footerGap) * 2, footerY, mouseX, mouseY);
    }

    private void drawSearchPlaceholder() {
        if (this.searchField.getText().isEmpty() && !this.searchField.isFocused()) {
            drawString(this.fontRenderer, translate("ui.blxckoutfx.config.search_placeholder"), this.searchField.x, this.searchField.y, MUTED_TEXT_COLOR);
        }
    }

    private void drawFooterIcon(String texturePath, int x, int y, int mouseX, int mouseY) {
        boolean hover = inside(mouseX, mouseY, x, y, FOOTER_ICON_SIZE, FOOTER_ICON_SIZE);
        drawRect(x, y, x + FOOTER_ICON_SIZE, y + FOOTER_ICON_SIZE, hover ? 0xFF232323 : 0xFF111111);
        if (hover) {
            drawBorder(x, y, FOOTER_ICON_SIZE, FOOTER_ICON_SIZE, 0xFF5A5A5A);
        }
        drawTexture(texturePath, x + 2, y + 2, 20, 20, 20, 20);
    }

    private void drawContent(int mouseX, int mouseY) {
        int x = this.rootX + this.sidebarW + CONTENT_PAD;
        int width = this.rootW - this.sidebarW - CONTENT_PAD * 2;
        int headerY = this.rootY + CONTENT_HEADER_Y;

        String title = hasSearchQuery(this.searchField.getText())
                ? translate("ui.blxckoutfx.config.search.results.title")
                : translate(this.selectedCategory.titleKey);
        String description = hasSearchQuery(this.searchField.getText())
                ? searchResultsDescription(this.searchField.getText())
                : translate(this.selectedCategory.descriptionKey);

        int titleHeight = drawWrapped(this.fontRenderer, title, x, headerY, width, TEXT_COLOR);
        int descriptionY = headerY + titleHeight + CONTENT_TEXT_GAP;
        int descriptionHeight = drawWrapped(this.fontRenderer, description, x, descriptionY, width, MUTED_TEXT_COLOR);
        int dividerY = descriptionY + descriptionHeight + CONTENT_BLOCK_GAP;
        drawDashedDivider(x, x + width, dividerY);

        if (hasSearchQuery(this.searchField.getText())) {
            drawSearchResults(x, dividerY + CONTENT_LIST_GAP, width, this.searchField.getText());
        } else {
            drawRowsForSelectedCategory(x, dividerY + CONTENT_LIST_GAP, width);
        }
    }

    private void drawSearchResults(int x, int y, int width, String query) {
        List<SearchGroup> groups = buildSearchGroups(query);
        if (groups.isEmpty()) {
            drawSection(x, y, translate("ui.blxckoutfx.config.search.results.section"));
            y += SECTION_HEIGHT;
            drawInfoBlock(x, y, width,
                    translate("ui.blxckoutfx.config.search.no_matches.title"),
                    BlxckoutFXI18n.format("ui.blxckoutfx.config.search.no_matches.description", query.trim()));
            return;
        }

        for (SearchGroup group : groups) {
            drawSection(x, y, sectionTitle(group.category));
            y += SECTION_HEIGHT;
            for (SearchRowSpec spec : group.rows) {
                drawRow(x, y, width, translate(spec.titleKey), translate(spec.descriptionKey), spec.reservedControlWidth);
                y += ROW_HEIGHT;
            }
        }
    }

    private void drawRowsForSelectedCategory(int x, int y, int width) {
        switch (this.selectedCategory) {
            case OVERVIEW:
                drawSection(x, y, translate("ui.blxckoutfx.config.section.general"));
                y += SECTION_HEIGHT;
                drawRow(x, y, width, translate("ui.blxckoutfx.config.setting.enable_dark_mode.title"), translate("ui.blxckoutfx.config.setting.enable_dark_mode.description"), TOGGLE_RESERVED_WIDTH);
                y += ROW_HEIGHT;
                drawRow(x, y, width, translate("ui.blxckoutfx.config.setting.selected_preset.title"), translate("ui.blxckoutfx.config.setting.selected_preset.description"), BUTTON_RESERVED_WIDTH);
                break;
            case PRESETS:
                drawSection(x, y, translate("ui.blxckoutfx.config.section.presets"));
                y += SECTION_HEIGHT;
                drawRow(x, y, width, translate("ui.blxckoutfx.config.setting.preset.title"), translate("ui.blxckoutfx.config.setting.preset.description"), DROPDOWN_RESERVED_WIDTH);
                y += ROW_HEIGHT;
                drawSection(x, y, translate("ui.blxckoutfx.config.section.preview"));
                y += SECTION_HEIGHT;
                drawPresetPreview(x, y, width);
                break;
            case INVENTORY_BUTTON:
                drawSection(x, y, translate("ui.blxckoutfx.config.section.inventory_button"));
                y += SECTION_HEIGHT;
                drawRow(x, y, width, translate("ui.blxckoutfx.config.setting.inventory_button_enabled.title"), translate("ui.blxckoutfx.config.setting.inventory_button_enabled.description"), TOGGLE_RESERVED_WIDTH);
                y += ROW_HEIGHT;
                drawRow(x, y, width, translate("ui.blxckoutfx.config.setting.button_lock.title"), translate("ui.blxckoutfx.config.setting.button_lock.description"), TOGGLE_RESERVED_WIDTH);
                y += ROW_HEIGHT;
                drawRow(x, y, width, translate("ui.blxckoutfx.config.setting.position_preset.title"), translate("ui.blxckoutfx.config.setting.position_preset.description"), INVENTORY_DROPDOWN_RESERVED_WIDTH);
                y += ROW_HEIGHT;
                drawRow(x, y, width, translate("ui.blxckoutfx.config.setting.button_style.title"), translate("ui.blxckoutfx.config.setting.button_style.description"), INVENTORY_DROPDOWN_RESERVED_WIDTH);
                break;
            case AE2:
                drawSection(x, y, translate("ui.blxckoutfx.config.section.ae2"));
                y += SECTION_HEIGHT;
                drawWarning(x, y, width);
                break;
            case ABOUT:
                drawSection(x, y, translate("ui.blxckoutfx.config.section.about"));
                y += SECTION_HEIGHT;
                y = drawInfoBlock(x, y, width, translate("ui.blxckoutfx.config.setting.purpose.title"), translate("ui.blxckoutfx.config.setting.purpose.description"));
                y = drawInfoBlock(x, y, width, translate("ui.blxckoutfx.config.setting.shader_driven.title"), translate("ui.blxckoutfx.config.setting.shader_driven.description"));
                y = drawInfoBlock(x, y, width, translate("ui.blxckoutfx.config.setting.scope.title"), translate("ui.blxckoutfx.config.setting.scope.description"));
                drawInfoBlock(x, y, width, translate("ui.blxckoutfx.config.setting.support.title"), translate("ui.blxckoutfx.config.setting.support.description"));
                break;
        }
    }

    private void drawSection(int x, int y, String title) {
        drawString(this.fontRenderer, title, x, y + 7, 0xFFC9C9C9);
    }

    private void drawRow(int x, int y, int width, String title, String description, int reservedControlWidth) {
        int textWidth = Math.max(1, width - reservedControlWidth - 28);
        drawString(this.fontRenderer, ellipsize(title, textWidth), x, y + 10, TEXT_COLOR);
        drawWrapped(this.fontRenderer, description, x, y + 24, textWidth, MUTED_TEXT_COLOR);
        drawDashedDivider(x, x + width, y + ROW_HEIGHT - 1);
    }

    private int drawInfoBlock(int x, int y, int width, String title, String description) {
        drawString(this.fontRenderer, ellipsize(title, width), x, y + 10, TEXT_COLOR);
        int height = drawWrapped(this.fontRenderer, description, x, y + 24, width, MUTED_TEXT_COLOR);
        int nextY = y + Math.max(ROW_HEIGHT, 34 + height);
        drawDashedDivider(x, x + width, nextY - 1);
        return nextY;
    }

    private void drawWarning(int x, int y, int width) {
        drawRect(x, y, x + width, y + 54, 0x551A0808);
        drawRect(x, y, x + width, y + 1, 0x99FF5555);
        drawString(this.fontRenderer, translate("ui.blxckoutfx.config.warning.unsupported.title"), x + 10, y + 9, 0xFFFF5555);
        drawWrapped(this.fontRenderer, translate("ui.blxckoutfx.config.warning.unsupported.description"), x + 10, y + 23, width - 20, MUTED_TEXT_COLOR);
    }

    private void drawPresetPreview(int x, int y, int width) {
        PreviewPalette palette = PreviewPalette.forPreset(this.presetIndex);
        int panelY = y + PREVIEW_TAB_HEIGHT - 1;
        int panelHeight = PREVIEW_HEIGHT - PREVIEW_TAB_HEIGHT + 1;

        for (int i = 0; i < PreviewTab.values().length; i++) {
            PreviewTab tab = PreviewTab.values()[i];
            int tabX = x + i * (PREVIEW_TAB_WIDTH + 4);
            boolean selected = tab == this.selectedPreviewTab;
            int bg = selected ? 0xFF161616 : 0xFF101010;
            drawRect(tabX, y, tabX + PREVIEW_TAB_WIDTH, y + PREVIEW_TAB_HEIGHT, bg);
            drawBorder(tabX, y, PREVIEW_TAB_WIDTH, PREVIEW_TAB_HEIGHT, selected ? 0xFF585858 : BORDER_COLOR);
            if (!selected) {
                drawRect(tabX + 1, y + PREVIEW_TAB_HEIGHT - 1, tabX + PREVIEW_TAB_WIDTH - 1, y + PREVIEW_TAB_HEIGHT, 0xFF101010);
            }
            drawCenteredString(this.fontRenderer, ellipsize(this.fontRenderer, translate(tab.labelKey), PREVIEW_TAB_WIDTH - 6), tabX + PREVIEW_TAB_WIDTH / 2, y + 4, selected ? TEXT_COLOR : 0xFFB7B7B7);
        }

        drawRect(x, panelY, x + PREVIEW_WIDTH, panelY + panelHeight, palette.surface);
        drawBorder(x, panelY, PREVIEW_WIDTH, panelHeight, BORDER_COLOR);

        if (this.selectedPreviewTab == PreviewTab.GUI) {
            drawPresetGuiPreview(x + 12, panelY + 12, palette);
        } else {
            drawPresetButtonPreview(x + 12, panelY + 12, palette);
        }
    }

    private void drawPresetGuiPreview(int x, int y, PreviewPalette palette) {
        drawString(this.fontRenderer, translate("ui.blxckoutfx.config.preview.gui.container"), x, y, 0xFFD7D7D7);
        int frameX = x;
        int frameY = y + 12;
        drawRect(frameX, frameY, frameX + 134, frameY + 76, palette.panel);
        drawBorder(frameX, frameY, 134, 76, palette.border);
        drawRect(frameX + 8, frameY + 16, frameX + 126, frameY + 24, palette.inset);
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 4; col++) {
                int slotX = frameX + 10 + col * 22;
                int slotY = frameY + 32 + row * 22;
                drawRect(slotX, slotY, slotX + 18, slotY + 18, palette.slot);
                drawBorder(slotX, slotY, 18, 18, palette.slotBorder);
            }
        }

        int infoX = frameX + 146;
        int infoWidth = 146;
        drawString(this.fontRenderer, palette.label, infoX, frameY + 8, 0xFFE4E4E4);
        int headlineHeight = drawWrapped(this.fontRenderer, palette.guiHeadline, infoX, frameY + 24, infoWidth, 0xFFA9A9A9);
        drawWrapped(this.fontRenderer, palette.guiText, infoX, frameY + 24 + headlineHeight + 8, infoWidth, 0xFF9A9A9A);
    }

    private void drawPresetButtonPreview(int x, int y, PreviewPalette palette) {
        drawString(this.fontRenderer, translate("ui.blxckoutfx.config.preview.buttons.container"), x, y, 0xFFD7D7D7);
        int buttonY = y + 20;
        drawPreviewButtonSample(x, buttonY, dynamicPreviewButtonWidth(translate("ui.blxckoutfx.config.preview.buttons.apply")), 16, translate("ui.blxckoutfx.config.preview.buttons.apply"), palette.buttonPrimary, palette.border);
        drawPreviewButtonSample(x, buttonY + 24, dynamicPreviewButtonWidth(translate("ui.blxckoutfx.config.preview.buttons.cancel")), 16, translate("ui.blxckoutfx.config.preview.buttons.cancel"), palette.buttonSecondary, palette.border);
        drawPreviewButtonSample(x, buttonY + 48, dynamicPreviewButtonWidth(translate("ui.blxckoutfx.config.preview.buttons.inventory")), 16, translate("ui.blxckoutfx.config.preview.buttons.inventory"), palette.buttonPrimary, palette.border);

        int infoX = x + PREVIEW_BUTTON_MAX_WIDTH + 14;
        int infoWidth = PREVIEW_WIDTH - (infoX - x) - 28;
        drawString(this.fontRenderer, palette.label, infoX, buttonY + 4, 0xFFE4E4E4);
        int headlineHeight = drawWrapped(this.fontRenderer, palette.buttonHeadline, infoX, buttonY + 16, infoWidth, 0xFFA9A9A9);
        drawWrapped(this.fontRenderer, palette.buttonText, infoX, buttonY + 16 + headlineHeight + 8, infoWidth, 0xFF9A9A9A);
    }

    private void drawPreviewButtonSample(int x, int y, int width, int height, String label, int fill, int border) {
        drawRect(x, y, x + width, y + height, fill);
        drawBorder(x, y, width, height, border);
        drawCenteredString(this.fontRenderer, ellipsize(this.fontRenderer, label, width - 8), x + width / 2, y + 4, 0xFFE9E9E9);
    }

    private int dynamicPreviewButtonWidth(String label) {
        return Math.min(PREVIEW_BUTTON_MAX_WIDTH, Math.max(PREVIEW_BUTTON_MIN_WIDTH, this.fontRenderer.getStringWidth(label) + 12));
    }

    private PreviewBounds previewBounds() {
        int x = this.rootX + this.sidebarW + CONTENT_PAD;
        int y = contentListTop() + SECTION_HEIGHT + ROW_HEIGHT + SECTION_HEIGHT;
        return new PreviewBounds(x, y);
    }

    private int drawWrapped(FontRenderer font, String text, int x, int y, int width, int color) {
        String safeText = text == null ? "" : text;
        if (width <= 8) {
            drawString(font, ellipsize(font, safeText, Math.max(1, width)), x, y, color);
            return font.FONT_HEIGHT;
        }
        List<String> lines = font.listFormattedStringToWidth(safeText, width);
        int drawn = 0;
        for (String line : lines) {
            drawString(font, line, x, y + drawn, color);
            drawn += font.FONT_HEIGHT + 2;
        }
        return lines.isEmpty() ? 0 : drawn - 2;
    }

    private void drawDashedDivider(int x1, int x2, int y) {
        for (int x = x1; x < x2; x += 8) {
            drawRect(x, y, Math.min(x + 4, x2), y + 1, 0x66373737);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mouseButton == 0 && insideSearchBox(mouseX, mouseY)) {
            this.searchField.setFocused(true);
        }
        this.searchField.mouseClicked(mouseX, mouseY, mouseButton);
        for (int i = this.controls.size() - 1; i >= 0; i--) {
            if (this.controls.get(i).mouseClicked(mouseX, mouseY, mouseButton)) {
                return;
            }
        }
        if (mouseButton == 0 && handlePresetPreviewClick(mouseX, mouseY)) {
            return;
        }
        if (mouseButton == 0 && handleFooterClick(mouseX, mouseY)) {
            return;
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    private boolean handleFooterClick(int mouseX, int mouseY) {
        int footerY = this.rootY + this.rootH - 38;
        int footerGap = 8;
        int footerGroupWidth = FOOTER_ICON_SIZE * 3 + footerGap * 2;
        int footerX = this.rootX + (this.sidebarW - footerGroupWidth) / 2;
        if (inside(mouseX, mouseY, footerX, footerY, FOOTER_ICON_SIZE, FOOTER_ICON_SIZE)) {
            openUrl("https://discord.com/invite/rUD6tynDaK");
            return true;
        }
        if (inside(mouseX, mouseY, footerX + FOOTER_ICON_SIZE + footerGap, footerY, FOOTER_ICON_SIZE, FOOTER_ICON_SIZE)) {
            openUrl("https://www.curseforge.com/minecraft/mc-mods/blxckoutfx");
            return true;
        }
        if (inside(mouseX, mouseY, footerX + (FOOTER_ICON_SIZE + footerGap) * 2, footerY, FOOTER_ICON_SIZE, FOOTER_ICON_SIZE)) {
            openUrl("https://modrinth.com/mod/blxckoutfx");
            return true;
        }
        return false;
    }

    private boolean handlePresetPreviewClick(int mouseX, int mouseY) {
        if (this.selectedCategory != Category.PRESETS || hasSearchQuery(this.searchField == null ? "" : this.searchField.getText())) {
            return false;
        }

        PreviewBounds bounds = previewBounds();
        for (int i = 0; i < PreviewTab.values().length; i++) {
            int tabX = bounds.x + i * (PREVIEW_TAB_WIDTH + 4);
            if (inside(mouseX, mouseY, tabX, bounds.y, PREVIEW_TAB_WIDTH, PREVIEW_TAB_HEIGHT)) {
                this.selectedPreviewTab = PreviewTab.values()[i];
                return true;
            }
        }

        return inside(mouseX, mouseY, bounds.x, bounds.y, PREVIEW_WIDTH, PREVIEW_HEIGHT);
    }

    private void openUrl(String url) {
        try {
            Method method = GuiScreen.class.getDeclaredMethod("openWebLink", URI.class);
            method.setAccessible(true);
            method.invoke(this, new URI(url));
        } catch (Exception ignored) {
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            this.mc.displayGuiScreen(this.parent);
            return;
        }

        String before = this.searchField.getText();
        if (this.searchField.textboxKeyTyped(typedChar, keyCode)) {
            if (!before.equals(this.searchField.getText())) {
                rebuildButtons();
                this.searchField.setFocused(true);
            }
            return;
        }

        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void updateScreen() {
        if (this.searchField != null) {
            this.searchField.updateCursorCounter();
        }
    }

    private int contentListTop() {
        int x = this.rootX + this.sidebarW + CONTENT_PAD;
        int width = this.rootW - this.sidebarW - CONTENT_PAD * 2;
        int headerY = this.rootY + CONTENT_HEADER_Y;
        String title = hasSearchQuery(this.searchField == null ? "" : this.searchField.getText())
                ? translate("ui.blxckoutfx.config.search.results.title")
                : translate(this.selectedCategory.titleKey);
        String description = hasSearchQuery(this.searchField == null ? "" : this.searchField.getText())
                ? searchResultsDescription(this.searchField == null ? "" : this.searchField.getText())
                : translate(this.selectedCategory.descriptionKey);
        int titleHeight = wrappedHeight(this.fontRenderer, title, width);
        int descriptionY = headerY + titleHeight + CONTENT_TEXT_GAP;
        int descriptionHeight = wrappedHeight(this.fontRenderer, description, width);
        int dividerY = descriptionY + descriptionHeight + CONTENT_BLOCK_GAP;
        return dividerY + CONTENT_LIST_GAP;
    }

    private boolean hasSearchQuery(String query) {
        return query != null && !query.trim().isEmpty();
    }

    private String searchResultsDescription(String query) {
        int matchCount = 0;
        for (SearchGroup group : buildSearchGroups(query)) {
            matchCount += group.rows.size();
        }
        return BlxckoutFXI18n.format("ui.blxckoutfx.config.search.results.description", matchCount, query.trim());
    }

    private boolean insideSearchBox(int mouseX, int mouseY) {
        int searchBoxX = this.rootX + 12;
        int searchBoxY = this.rootY + 54;
        int searchBoxW = this.sidebarW - 24;
        return inside(mouseX, mouseY, searchBoxX, searchBoxY, searchBoxW, SEARCH_HEIGHT);
    }

    private int wrappedHeight(FontRenderer font, String text, int width) {
        String safeText = text == null ? "" : text;
        if (width <= 8) {
            return font.FONT_HEIGHT;
        }
        List<String> lines = font.listFormattedStringToWidth(safeText, width);
        if (lines.isEmpty()) {
            return 0;
        }
        return lines.size() * (font.FONT_HEIGHT + 2) - 2;
    }

    private boolean matchesSearch(Category category, String query) {
        if (query == null || query.trim().isEmpty()) {
            return true;
        }
        String needle = query.toLowerCase(Locale.ROOT);
        return translate(category.titleKey).toLowerCase(Locale.ROOT).contains(needle)
                || translate(category.descriptionKey).toLowerCase(Locale.ROOT).contains(needle)
                || translate(category.groupKey).toLowerCase(Locale.ROOT).contains(needle);
    }

    private List<SearchGroup> buildSearchGroups(String query) {
        List<SearchGroup> groups = new ArrayList<>();
        if (!hasSearchQuery(query)) {
            return groups;
        }

        String needle = query.trim().toLowerCase(Locale.ROOT);
        for (Category category : Category.values()) {
            boolean categoryMatches = translate(category.titleKey).toLowerCase(Locale.ROOT).contains(needle)
                    || translate(category.descriptionKey).toLowerCase(Locale.ROOT).contains(needle)
                    || translate(category.groupKey).toLowerCase(Locale.ROOT).contains(needle)
                    || sectionTitle(category).toLowerCase(Locale.ROOT).contains(needle);
            List<SearchRowSpec> matches = new ArrayList<>();
            for (SearchRowSpec spec : buildSearchSpecsForCategory(category)) {
                if (categoryMatches || spec.matches(needle)) {
                    matches.add(spec);
                }
            }
            if (!matches.isEmpty()) {
                groups.add(new SearchGroup(category, matches));
            }
        }
        return groups;
    }

    private List<SearchRowSpec> buildSearchSpecsForCategory(Category category) {
        List<SearchRowSpec> specs = new ArrayList<>();
        switch (category) {
            case OVERVIEW:
                specs.add(new SearchRowSpec(
                        "ui.blxckoutfx.config.setting.enable_dark_mode.title",
                        "ui.blxckoutfx.config.setting.enable_dark_mode.description",
                        TOGGLE_RESERVED_WIDTH,
                        (contentRight, rowY) -> new ToggleControl(
                                contentRight - 26,
                                rowY + (ROW_HEIGHT - 12) / 2,
                                () -> this.darkModeEnabled,
                                value -> {
                                    this.darkModeEnabled = value;
                                    if (!value) {
                                        this.presetIndex = 0;
                                    } else if (this.presetIndex == 0) {
                                        this.presetIndex = 2;
                                    }
                                    rebuildButtons();
                                }
                        )
                ));
                specs.add(new SearchRowSpec(
                        "ui.blxckoutfx.config.setting.selected_preset.title",
                        "ui.blxckoutfx.config.setting.selected_preset.description",
                        BUTTON_RESERVED_WIDTH,
                        (contentRight, rowY) -> new ButtonControl(
                                contentRight - BUTTON_WIDTH,
                                rowY + (ROW_HEIGHT - 16) / 2,
                                BUTTON_WIDTH,
                                16,
                                () -> presetLabel(this.presetIndex),
                                () -> {
                                    this.selectedCategory = Category.PRESETS;
                                    this.searchField.setText("");
                                    rebuildButtons();
                                }
                        )
                ));
                break;
            case PRESETS:
                specs.add(new SearchRowSpec(
                        "ui.blxckoutfx.config.setting.preset.title",
                        "ui.blxckoutfx.config.setting.preset.description",
                        DROPDOWN_RESERVED_WIDTH,
                        (contentRight, rowY) -> new DropdownControl(
                                contentRight - DROPDOWN_WIDTH,
                                rowY + (ROW_HEIGHT - 16) / 2,
                                DROPDOWN_WIDTH,
                                presetOptions(),
                                () -> this.presetIndex,
                                index -> {
                                    this.presetIndex = clampPresetIndex(index);
                                    this.darkModeEnabled = this.presetIndex != 0;
                                    rebuildButtons();
                                }
                        )
                ));
                break;
            case INVENTORY_BUTTON:
                specs.add(new SearchRowSpec(
                        "ui.blxckoutfx.config.setting.inventory_button_enabled.title",
                        "ui.blxckoutfx.config.setting.inventory_button_enabled.description",
                        TOGGLE_RESERVED_WIDTH,
                        (contentRight, rowY) -> new ToggleControl(
                                contentRight - 26,
                                rowY + (ROW_HEIGHT - 12) / 2,
                                () -> this.inventoryButtonEnabled,
                                value -> {
                                    this.inventoryButtonEnabled = value;
                                    rebuildButtons();
                                }
                        )
                ));
                specs.add(new SearchRowSpec(
                        "ui.blxckoutfx.config.setting.button_lock.title",
                        "ui.blxckoutfx.config.setting.button_lock.description",
                        TOGGLE_RESERVED_WIDTH,
                        (contentRight, rowY) -> new ToggleControl(
                                contentRight - 26,
                                rowY + (ROW_HEIGHT - 12) / 2,
                                () -> this.inventoryButtonLocked,
                                value -> {
                                    this.inventoryButtonLocked = value;
                                    rebuildButtons();
                                }
                        )
                ));
                specs.add(new SearchRowSpec(
                        "ui.blxckoutfx.config.setting.position_preset.title",
                        "ui.blxckoutfx.config.setting.position_preset.description",
                        INVENTORY_DROPDOWN_RESERVED_WIDTH,
                        (contentRight, rowY) -> new DropdownControl(
                                contentRight - INVENTORY_DROPDOWN_WIDTH,
                                rowY + (ROW_HEIGHT - 16) / 2,
                                INVENTORY_DROPDOWN_WIDTH,
                                inventoryButtonPresetOptions(),
                                () -> this.inventoryButtonPreset == null ? 0 : this.inventoryButtonPreset.ordinal(),
                                index -> {
                                    BlxckoutFXClientConfig.InventoryButtonPreset[] values = BlxckoutFXClientConfig.InventoryButtonPreset.values();
                                    this.inventoryButtonPreset = values[Math.max(0, Math.min(index, values.length - 1))];
                                    rebuildButtons();
                                }
                        )
                ));
                specs.add(new SearchRowSpec(
                        "ui.blxckoutfx.config.setting.button_style.title",
                        "ui.blxckoutfx.config.setting.button_style.description",
                        INVENTORY_DROPDOWN_RESERVED_WIDTH,
                        (contentRight, rowY) -> new DropdownControl(
                                contentRight - INVENTORY_DROPDOWN_WIDTH,
                                rowY + (ROW_HEIGHT - 16) / 2,
                                INVENTORY_DROPDOWN_WIDTH,
                                inventoryButtonStyleOptions(),
                                () -> this.inventoryButtonStyle == BlxckoutFXClientConfig.InventoryButtonStyle.Short ? 1 : 0,
                                index -> {
                                    this.inventoryButtonStyle = index == 1
                                            ? BlxckoutFXClientConfig.InventoryButtonStyle.Short
                                            : BlxckoutFXClientConfig.InventoryButtonStyle.Classic;
                                    rebuildButtons();
                                }
                        )
                ));
                break;
            case ABOUT:
                specs.add(new SearchRowSpec("ui.blxckoutfx.config.setting.purpose.title", "ui.blxckoutfx.config.setting.purpose.description", 0, null));
                specs.add(new SearchRowSpec("ui.blxckoutfx.config.setting.shader_driven.title", "ui.blxckoutfx.config.setting.shader_driven.description", 0, null));
                specs.add(new SearchRowSpec("ui.blxckoutfx.config.setting.scope.title", "ui.blxckoutfx.config.setting.scope.description", 0, null));
                specs.add(new SearchRowSpec("ui.blxckoutfx.config.setting.support.title", "ui.blxckoutfx.config.setting.support.description", 0, null));
                break;
            case AE2:
                specs.add(new SearchRowSpec("ui.blxckoutfx.config.warning.unsupported.title", "ui.blxckoutfx.config.warning.unsupported.description", 0, null));
                break;
        }
        return specs;
    }

    private static BlxckoutFXClientConfig.InventoryButtonPreset nextInventoryButtonPreset(BlxckoutFXClientConfig.InventoryButtonPreset preset) {
        BlxckoutFXClientConfig.InventoryButtonPreset[] values = BlxckoutFXClientConfig.InventoryButtonPreset.values();
        int index = preset == null ? 0 : preset.ordinal();
        return values[(index + 1) % values.length];
    }

    private static List<String> presetOptions() {
        return Arrays.asList(
                translate("preset.blxckoutfx.off"),
                translate("preset.blxckoutfx.soft"),
                translate("preset.blxckoutfx.balanced"),
                translate("preset.blxckoutfx.dark"),
                translate("preset.blxckoutfx.blxckout")
        );
    }

    private static List<String> inventoryButtonPresetOptions() {
        return Arrays.asList(
                translate("ui.blxckoutfx.config.option.inventory_button_preset.bottom_left"),
                translate("ui.blxckoutfx.config.option.inventory_button_preset.bottom_right"),
                translate("ui.blxckoutfx.config.option.inventory_button_preset.top_left"),
                translate("ui.blxckoutfx.config.option.inventory_button_preset.top_right"),
                translate("ui.blxckoutfx.config.option.inventory_button_preset.custom")
        );
    }

    private static List<String> inventoryButtonStyleOptions() {
        return Arrays.asList(
                translate("ui.blxckoutfx.config.option.inventory_button_style.classic"),
                translate("ui.blxckoutfx.config.option.inventory_button_style.short")
        );
    }

    private static String sectionTitle(Category category) {
        switch (category) {
            case OVERVIEW:
                return translate("ui.blxckoutfx.config.section.general");
            case PRESETS:
                return translate("ui.blxckoutfx.config.section.presets");
            case INVENTORY_BUTTON:
                return translate("ui.blxckoutfx.config.section.inventory_button");
            case AE2:
                return translate("ui.blxckoutfx.config.section.ae2");
            case ABOUT:
            default:
                return translate("ui.blxckoutfx.config.section.about");
        }
    }

    private static String presetLabel(int presetIndex) {
        switch (clampPresetIndex(presetIndex)) {
            case 0:
                return translate("preset.blxckoutfx.off");
            case 1:
                return translate("preset.blxckoutfx.soft");
            case 2:
                return translate("preset.blxckoutfx.balanced");
            case 3:
                return translate("preset.blxckoutfx.dark");
            default:
                return translate("preset.blxckoutfx.blxckout");
        }
    }

    private static String inventoryPresetLabel(BlxckoutFXClientConfig.InventoryButtonPreset preset) {
        if (preset == BlxckoutFXClientConfig.InventoryButtonPreset.BottomRight) {
            return translate("ui.blxckoutfx.config.option.inventory_button_preset.bottom_right");
        }
        if (preset == BlxckoutFXClientConfig.InventoryButtonPreset.TopLeft) {
            return translate("ui.blxckoutfx.config.option.inventory_button_preset.top_left");
        }
        if (preset == BlxckoutFXClientConfig.InventoryButtonPreset.TopRight) {
            return translate("ui.blxckoutfx.config.option.inventory_button_preset.top_right");
        }
        if (preset == BlxckoutFXClientConfig.InventoryButtonPreset.Custom) {
            return translate("ui.blxckoutfx.config.option.inventory_button_preset.custom");
        }
        return translate("ui.blxckoutfx.config.option.inventory_button_preset.bottom_left");
    }

    private static String inventoryStyleLabel(BlxckoutFXClientConfig.InventoryButtonStyle style) {
        return style == BlxckoutFXClientConfig.InventoryButtonStyle.Short
                ? translate("ui.blxckoutfx.config.option.inventory_button_style.short")
                : translate("ui.blxckoutfx.config.option.inventory_button_style.classic");
    }

    private String ellipsize(String value, int width) {
        if (this.fontRenderer.getStringWidth(value) <= width) {
            return value;
        }
        String ellipsis = "...";
        return this.fontRenderer.trimStringToWidth(value, Math.max(0, width - this.fontRenderer.getStringWidth(ellipsis))) + ellipsis;
    }

    private static int clampPresetIndex(int index) {
        return Math.max(0, Math.min(index, 4));
    }

    private static String translate(String key) {
        return BlxckoutFXI18n.format(key);
    }

    private interface ConfigControl {
        void render(Minecraft mc, FontRenderer font, int mouseX, int mouseY);

        default void renderOverlay(Minecraft mc, FontRenderer font, int mouseX, int mouseY) {
        }

        boolean mouseClicked(int mouseX, int mouseY, int button);
    }

    private interface ControlFactory {
        ConfigControl create(int contentRight, int rowY);
    }

    private void closeDropdownsExcept(DropdownControl except) {
        for (ConfigControl control : this.controls) {
            if (control instanceof DropdownControl && control != except) {
                ((DropdownControl) control).expanded = false;
            }
        }
    }

    private final class ButtonControl implements ConfigControl {
        private final int x;
        private final int y;
        private final int width;
        private final int height;
        private final Supplier<String> label;
        private final Runnable onPress;

        private ButtonControl(int x, int y, int width, int height, Supplier<String> label, Runnable onPress) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.label = label;
            this.onPress = onPress;
        }

        @Override
        public void render(Minecraft mc, FontRenderer font, int mouseX, int mouseY) {
            boolean hover = inside(mouseX, mouseY, this.x, this.y, this.width, this.height);
            drawRect(this.x, this.y, this.x + this.width, this.y + this.height, hover ? 0xFF1B1B1B : 0xFF111111);
            drawBorder(this.x, this.y, this.width, this.height, hover ? 0xFF444444 : BORDER_COLOR);
            drawCenteredString(font, ellipsize(font, this.label.get(), this.width - 8), this.x + this.width / 2, this.y + (this.height - 8) / 2, 0xFFE5E5E5);
        }

        @Override
        public boolean mouseClicked(int mouseX, int mouseY, int button) {
            if (button != 0 || !inside(mouseX, mouseY, this.x, this.y, this.width, this.height)) {
                return false;
            }
            closeDropdownsExcept(null);
            this.onPress.run();
            return true;
        }
    }

    private final class ToggleControl implements ConfigControl {
        private static final int WIDTH = 26;
        private static final int HEIGHT = 12;

        private final int x;
        private final int y;
        private final BooleanSupplier getter;
        private final Consumer<Boolean> setter;

        private ToggleControl(int x, int y, BooleanSupplier getter, Consumer<Boolean> setter) {
            this.x = x;
            this.y = y;
            this.getter = getter;
            this.setter = setter;
        }

        @Override
        public void render(Minecraft mc, FontRenderer font, int mouseX, int mouseY) {
            boolean value = this.getter.getAsBoolean();
            boolean hover = inside(mouseX, mouseY, this.x, this.y, WIDTH, HEIGHT);
            int bg = value ? (hover ? 0xFF7A7A7A : 0xFF666666) : (hover ? 0xFF1F1F1F : 0xFF111111);
            int innerLine = value ? 0xFF909090 : 0xFF2A2A2A;
            int knobColor = value ? 0xFF0D0D0D : 0xFFE8E8E8;
            drawRect(this.x, this.y, this.x + WIDTH, this.y + HEIGHT, bg);
            drawBorder(this.x, this.y, WIDTH, HEIGHT, BORDER_COLOR);
            drawRect(this.x + 2, this.y + HEIGHT / 2, this.x + WIDTH - 2, this.y + HEIGHT / 2 + 1, innerLine);

            int knobX = value ? this.x + WIDTH - 9 : this.x + 2;
            drawRect(knobX, this.y + 2, knobX + 7, this.y + HEIGHT - 2, knobColor);
        }

        @Override
        public boolean mouseClicked(int mouseX, int mouseY, int button) {
            if (button != 0 || !inside(mouseX, mouseY, this.x, this.y, WIDTH, HEIGHT)) {
                return false;
            }
            closeDropdownsExcept(null);
            this.setter.accept(!this.getter.getAsBoolean());
            return true;
        }
    }

    private final class DropdownControl implements ConfigControl {
        private static final int HEIGHT = 16;
        private static final int ITEM_HEIGHT = 14;
        private static final int ARROW_WIDTH = 14;
        private static final int TEXT_PADDING = 6;

        private final int x;
        private final int y;
        private final int width;
        private final List<String> options;
        private final IntSupplier selectedIndexGetter;
        private final IntConsumer selectedIndexSetter;
        private boolean expanded;

        private DropdownControl(int x, int y, int width, List<String> options, IntSupplier selectedIndexGetter, IntConsumer selectedIndexSetter) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.options = options;
            this.selectedIndexGetter = selectedIndexGetter;
            this.selectedIndexSetter = selectedIndexSetter;
        }

        @Override
        public void render(Minecraft mc, FontRenderer font, int mouseX, int mouseY) {
            boolean hover = insideMain(mouseX, mouseY);
            boolean arrowHover = insideArrow(mouseX, mouseY);
            int bg = hover || this.expanded ? 0xFF1B1B1B : 0xFF111111;
            drawRect(this.x, this.y, this.x + this.width, this.y + HEIGHT, bg);
            drawBorder(this.x, this.y, this.width, HEIGHT, BORDER_COLOR);
            drawRect(this.x + this.width - ARROW_WIDTH, this.y + 1, this.x + this.width - 1, this.y + HEIGHT - 1, arrowHover || this.expanded ? 0xFF202020 : 0xFF171717);
            drawRect(this.x + this.width - ARROW_WIDTH, this.y, this.x + this.width - ARROW_WIDTH + 1, this.y + HEIGHT, BORDER_COLOR);

            int labelX = this.x + TEXT_PADDING;
            int labelWidth = Math.max(0, this.width - ARROW_WIDTH - TEXT_PADDING * 2);
            String text = this.options.get(clampIndex(this.selectedIndexGetter.getAsInt()));
            drawString(font, ellipsize(font, text, labelWidth), labelX, this.y + (HEIGHT - 8) / 2, 0xFFE5E5E5);
            drawCenteredString(font, "v", this.x + this.width - ARROW_WIDTH / 2, this.y + 4, 0xFFB8B8B8);
        }

        @Override
        public void renderOverlay(Minecraft mc, FontRenderer font, int mouseX, int mouseY) {
            if (!this.expanded) {
                return;
            }

            PopupBounds bounds = popupBounds();
            drawRect(bounds.x, bounds.y, bounds.x + bounds.width, bounds.y + bounds.height, 0xFF0E0E0E);
            drawBorder(bounds.x, bounds.y, bounds.width, bounds.height, BORDER_COLOR);

            for (int i = 0; i < this.options.size(); i++) {
                int optionY = bounds.y + i * ITEM_HEIGHT;
                boolean hover = inside(mouseX, mouseY, bounds.x, optionY, bounds.width, ITEM_HEIGHT);
                boolean selected = i == currentIndex();
                if (selected) {
                    drawRect(bounds.x + 1, optionY + 1, bounds.x + bounds.width - 1, optionY + ITEM_HEIGHT, 0xFF1C1C1C);
                } else if (hover) {
                    drawRect(bounds.x + 1, optionY + 1, bounds.x + bounds.width - 1, optionY + ITEM_HEIGHT, 0xFF171717);
                }
                drawString(font, ellipsize(font, this.options.get(i), bounds.width - TEXT_PADDING * 2), bounds.x + TEXT_PADDING, optionY + 3, selected ? TEXT_COLOR : 0xFFCACACA);
            }
        }

        @Override
        public boolean mouseClicked(int mouseX, int mouseY, int button) {
            if (this.expanded) {
                PopupBounds bounds = popupBounds();
                if (button == 0) {
                    for (int i = 0; i < this.options.size(); i++) {
                        if (inside(mouseX, mouseY, bounds.x, bounds.y + i * ITEM_HEIGHT, bounds.width, ITEM_HEIGHT)) {
                            this.selectedIndexSetter.accept(i);
                            this.expanded = false;
                            return true;
                        }
                    }
                }

                if (!insideMain(mouseX, mouseY)) {
                    this.expanded = false;
                    return true;
                }
            }

            if (!insideMain(mouseX, mouseY)) {
                return false;
            }

            closeDropdownsExcept(this);
            if (insideArrow(mouseX, mouseY)) {
                if (button == 0) {
                    this.expanded = !this.expanded;
                    return true;
                }
                return false;
            }

            if (button == 0) {
                this.selectedIndexSetter.accept(nextIndex());
                this.expanded = false;
                return true;
            }
            if (button == 1) {
                this.selectedIndexSetter.accept(previousIndex());
                this.expanded = false;
                return true;
            }

            return false;
        }

        private boolean insideMain(int mouseX, int mouseY) {
            return inside(mouseX, mouseY, this.x, this.y, this.width, HEIGHT);
        }

        private boolean insideArrow(int mouseX, int mouseY) {
            return inside(mouseX, mouseY, this.x + this.width - ARROW_WIDTH, this.y, ARROW_WIDTH, HEIGHT);
        }

        private int clampIndex(int index) {
            return Math.max(0, Math.min(index, this.options.size() - 1));
        }

        private int currentIndex() {
            return clampIndex(this.selectedIndexGetter.getAsInt());
        }

        private int nextIndex() {
            return (currentIndex() + 1) % this.options.size();
        }

        private int previousIndex() {
            int next = currentIndex() - 1;
            return next < 0 ? this.options.size() - 1 : next;
        }

        private PopupBounds popupBounds() {
            int popupWidth = Math.min(this.width, 192);
            int popupHeight = this.options.size() * ITEM_HEIGHT;
            int popupX = this.x;
            if (popupX + popupWidth > BlxckoutFXConfigScreen.this.width) {
                popupX = Math.max(0, BlxckoutFXConfigScreen.this.width - popupWidth);
            }

            int belowY = this.y + HEIGHT;
            int aboveY = this.y - popupHeight;
            int popupY;
            if (belowY + popupHeight <= BlxckoutFXConfigScreen.this.height || aboveY < 0) {
                popupY = Math.min(belowY, Math.max(0, BlxckoutFXConfigScreen.this.height - popupHeight));
            } else {
                popupY = Math.max(0, aboveY);
            }

            return new PopupBounds(popupX, popupY, popupWidth, popupHeight);
        }
    }

    private static final class PopupBounds {
        private final int x;
        private final int y;
        private final int width;
        private final int height;

        private PopupBounds(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }

    private static class FlatButton extends GuiButton {
        FlatButton(int buttonId, int x, int y, int width, int height, String buttonText) {
            super(buttonId, x, y, width, height, buttonText);
        }

        @Override
        public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
            if (!this.visible) {
                return;
            }

            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            int bg = this.hovered ? 0xFF1B1B1B : 0xFF111111;
            int border = this.hovered ? 0xFF444444 : BORDER_COLOR;
            int text = this.enabled ? 0xFFE5E5E5 : 0xFF7A7A7A;

            drawRect(this.x, this.y, this.x + this.width, this.y + this.height, bg);
            drawBorder(this.x, this.y, this.width, this.height, border);
            drawCenteredString(mc.fontRenderer, ellipsize(mc.fontRenderer, this.displayString, this.width - 8), this.x + this.width / 2, this.y + (this.height - 8) / 2, text);
        }
    }

    private static final class CategoryButton extends FlatButton {
        private final Category category;

        CategoryButton(int buttonId, int x, int y, int width, int height, String buttonText, Category category) {
            super(buttonId, x, y, width, height, buttonText);
            this.category = category;
        }

        @Override
        public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
            if (!this.visible) {
                return;
            }

            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            boolean selected = !this.enabled;
            int bg = selected ? 0xFF171717 : this.hovered ? 0x88151515 : 0x00000000;
            int text = selected ? TEXT_COLOR : 0xFFCFCFCF;

            if (bg != 0) {
                drawRect(this.x, this.y, this.x + this.width, this.y + this.height, bg);
            }
            if (selected) {
                drawRect(this.x, this.y, this.x + 2, this.y + this.height, 0xFF9D9D9D);
            }

            int iconX = this.x + 8;
            int iconY = this.y + 6;
            drawTexture(GUI_TEXTURE_ROOT + this.category.iconId + ".png", iconX + 1, iconY + 1, 10, 10, 8, 8);
            drawString(mc.fontRenderer, ellipsize(mc.fontRenderer, this.displayString, this.width - 28), this.x + 22, this.y + 8, text);
        }
    }

    private static final class SidebarGroupLabel {
        private final int x;
        private final int y;
        private final String text;

        SidebarGroupLabel(int x, int y, String text) {
            this.x = x;
            this.y = y;
            this.text = text;
        }
    }

    private static final class SearchGroup {
        private final Category category;
        private final List<SearchRowSpec> rows;

        private SearchGroup(Category category, List<SearchRowSpec> rows) {
            this.category = category;
            this.rows = rows;
        }
    }

    private static final class SearchRowSpec {
        private final String titleKey;
        private final String descriptionKey;
        private final int reservedControlWidth;
        private final ControlFactory controlFactory;

        private SearchRowSpec(String titleKey, String descriptionKey, int reservedControlWidth, ControlFactory controlFactory) {
            this.titleKey = titleKey;
            this.descriptionKey = descriptionKey;
            this.reservedControlWidth = reservedControlWidth;
            this.controlFactory = controlFactory;
        }

        private boolean matches(String needle) {
            return translate(this.titleKey).toLowerCase(Locale.ROOT).contains(needle)
                    || translate(this.descriptionKey).toLowerCase(Locale.ROOT).contains(needle);
        }
    }

    private static void drawBorder(int x, int y, int width, int height, int color) {
        drawRect(x, y, x + width, y + 1, color);
        drawRect(x, y + height - 1, x + width, y + height, color);
        drawRect(x, y, x + 1, y + height, color);
        drawRect(x + width - 1, y, x + width, y + height, color);
    }

    private static void drawTexture(String texturePath, int x, int y, int width, int height, int sourceWidth, int sourceHeight) {
        drawTexture(resolveGuiTexture(texturePath), x, y, width, height, sourceWidth, sourceHeight);
    }

    private static void drawTexture(ResourceLocation texture, int x, int y, int width, int height, int sourceWidth, int sourceHeight) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        drawScaledCustomSizeModalRect(x, y, 0, 0, sourceWidth, sourceHeight, width, height, sourceWidth, sourceHeight);
    }

    private static ResourceLocation resolveGuiTexture(String texturePath) {
        ResourceLocation cached = GUI_TEXTURE_CACHE.get(texturePath);
        if (cached != null) {
            return cached;
        }

        ResourceLocation fallback = new ResourceLocation(MODID, texturePath);
        String classpathPath = "assets/" + MODID + "/" + texturePath;
        try (InputStream stream = BlxckoutFXConfigScreen.class.getClassLoader().getResourceAsStream(classpathPath)) {
            if (stream == null) {
                GUI_TEXTURE_CACHE.put(texturePath, fallback);
                return fallback;
            }

            BufferedImage image = ImageIO.read(stream);
            if (image == null) {
                GUI_TEXTURE_CACHE.put(texturePath, fallback);
                return fallback;
            }

            ResourceLocation dynamic = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation(
                    "blxckoutfx_gui_" + sanitizeTextureKey(texturePath),
                    new DynamicTexture(image)
            );
            GUI_TEXTURE_CACHE.put(texturePath, dynamic);
            return dynamic;
        } catch (IOException ignored) {
            GUI_TEXTURE_CACHE.put(texturePath, fallback);
            return fallback;
        }
    }

    private static String sanitizeTextureKey(String texturePath) {
        return texturePath.toLowerCase(Locale.ROOT).replace('/', '_').replace('\\', '_').replace('.', '_');
    }

    private static boolean inside(int mouseX, int mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }

    private static String ellipsize(FontRenderer font, String value, int width) {
        if (value == null) {
            return "";
        }
        if (font.getStringWidth(value) <= width) {
            return value;
        }

        String ellipsis = "...";
        return font.trimStringToWidth(value, Math.max(0, width - font.getStringWidth(ellipsis))) + ellipsis;
    }

    private enum PreviewTab {
        GUI("ui.blxckoutfx.config.preview.tab.gui"),
        BUTTONS("ui.blxckoutfx.config.preview.tab.buttons");

        private final String labelKey;

        PreviewTab(String labelKey) {
            this.labelKey = labelKey;
        }
    }

    private static final class PreviewBounds {
        private final int x;
        private final int y;

        private PreviewBounds(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private static final class PreviewPalette {
        private final String label;
        private final String guiHeadline;
        private final String guiText;
        private final String buttonHeadline;
        private final String buttonText;
        private final int surface;
        private final int panel;
        private final int inset;
        private final int slot;
        private final int slotBorder;
        private final int buttonPrimary;
        private final int buttonSecondary;
        private final int border;

        private PreviewPalette(String label, String guiHeadline, String guiText, String buttonHeadline, String buttonText,
                               int surface, int panel, int inset, int slot, int slotBorder, int buttonPrimary, int buttonSecondary, int border) {
            this.label = label;
            this.guiHeadline = guiHeadline;
            this.guiText = guiText;
            this.buttonHeadline = buttonHeadline;
            this.buttonText = buttonText;
            this.surface = surface;
            this.panel = panel;
            this.inset = inset;
            this.slot = slot;
            this.slotBorder = slotBorder;
            this.buttonPrimary = buttonPrimary;
            this.buttonSecondary = buttonSecondary;
            this.border = border;
        }

        private static PreviewPalette forPreset(int presetIndex) {
            float normalized = previewPresetNormalized(presetIndex);
            int surface = shade(0x12, normalized, 0.62F);
            int panel = shade(0x2A, normalized, 0.72F);
            int inset = shade(0x31, normalized, 0.75F);
            int slot = shade(0x38, normalized, 0.78F);
            int slotBorder = shade(0x4B, normalized, 0.55F);
            int buttonPrimary = shade(0x3A, normalized, 0.70F);
            int buttonSecondary = shade(0x27, normalized, 0.66F);
            int border = shade(0x4B, normalized, 0.35F);

            switch (clampPresetIndex(presetIndex)) {
                case 0:
                    return new PreviewPalette(
                            translate("ui.blxckoutfx.config.preview.preset.off.label"),
                            translate("ui.blxckoutfx.config.preview.preset.off.headline"),
                            translate("ui.blxckoutfx.config.preview.preset.off.text"),
                            translate("ui.blxckoutfx.config.preview.preset.off.button_headline"),
                            translate("ui.blxckoutfx.config.preview.preset.off.button_text"),
                            surface, panel, inset, slot, slotBorder, buttonPrimary, buttonSecondary, border
                    );
                case 1:
                    return new PreviewPalette(
                            translate("ui.blxckoutfx.config.preview.preset.soft.label"),
                            translate("ui.blxckoutfx.config.preview.preset.soft.headline"),
                            translate("ui.blxckoutfx.config.preview.preset.soft.text"),
                            translate("ui.blxckoutfx.config.preview.preset.soft.button_headline"),
                            translate("ui.blxckoutfx.config.preview.preset.soft.button_text"),
                            surface, panel, inset, slot, slotBorder, buttonPrimary, buttonSecondary, border
                    );
                case 2:
                    return new PreviewPalette(
                            translate("ui.blxckoutfx.config.preview.preset.balanced.label"),
                            translate("ui.blxckoutfx.config.preview.preset.balanced.headline"),
                            translate("ui.blxckoutfx.config.preview.preset.balanced.text"),
                            translate("ui.blxckoutfx.config.preview.preset.balanced.button_headline"),
                            translate("ui.blxckoutfx.config.preview.preset.balanced.button_text"),
                            surface, panel, inset, slot, slotBorder, buttonPrimary, buttonSecondary, border
                    );
                case 3:
                    return new PreviewPalette(
                            translate("ui.blxckoutfx.config.preview.preset.dark.label"),
                            translate("ui.blxckoutfx.config.preview.preset.dark.headline"),
                            translate("ui.blxckoutfx.config.preview.preset.dark.text"),
                            translate("ui.blxckoutfx.config.preview.preset.dark.button_headline"),
                            translate("ui.blxckoutfx.config.preview.preset.dark.button_text"),
                            surface, panel, inset, slot, slotBorder, buttonPrimary, buttonSecondary, border
                    );
                default:
                    return new PreviewPalette(
                            translate("ui.blxckoutfx.config.preview.preset.blxckout.label"),
                            translate("ui.blxckoutfx.config.preview.preset.blxckout.headline"),
                            translate("ui.blxckoutfx.config.preview.preset.blxckout.text"),
                            translate("ui.blxckoutfx.config.preview.preset.blxckout.button_headline"),
                            translate("ui.blxckoutfx.config.preview.preset.blxckout.button_text"),
                            surface, panel, inset, slot, slotBorder, buttonPrimary, buttonSecondary, border
                    );
            }
        }
    }

    private static float previewPresetNormalized(int presetIndex) {
        float divideFactor;
        switch (clampPresetIndex(presetIndex)) {
            case 0:
                divideFactor = 1.0F;
                break;
            case 1:
                divideFactor = 2.0F;
                break;
            case 2:
                divideFactor = 3.2F;
                break;
            case 3:
                divideFactor = 4.3F;
                break;
            default:
                divideFactor = 5.5F;
                break;
        }
        return Math.max(0.0F, Math.min((divideFactor - 2.0F) / 3.5F, 1.0F));
    }

    private static int shade(int baseGray, float normalized, float strength) {
        int value = Math.max(5, Math.min(255, Math.round(baseGray * (1.0F - normalized * strength))));
        return 0xFF000000 | (value << 16) | (value << 8) | value;
    }

    private enum Category {
        OVERVIEW("ui.blxckoutfx.config.group.general", "overview", "ui.blxckoutfx.config.category.overview.title", "ui.blxckoutfx.config.category.overview.description"),
        PRESETS("ui.blxckoutfx.config.group.blxckoutfx", "presets", "ui.blxckoutfx.config.category.presets.title", "ui.blxckoutfx.config.category.presets.description"),
        INVENTORY_BUTTON("ui.blxckoutfx.config.group.blxckoutfx", "inventory_button", "ui.blxckoutfx.config.category.inventory_button.title", "ui.blxckoutfx.config.category.inventory_button.description"),
        AE2("ui.blxckoutfx.config.group.integrations", "ae2", "ui.blxckoutfx.config.category.ae2.title", "ui.blxckoutfx.config.category.ae2.description"),
        ABOUT("ui.blxckoutfx.config.group.about", "about", "ui.blxckoutfx.config.category.about.title", "ui.blxckoutfx.config.category.about.description");

        private final String groupKey;
        private final String iconId;
        private final String titleKey;
        private final String descriptionKey;

        Category(String groupKey, String iconId, String titleKey, String descriptionKey) {
            this.groupKey = groupKey;
            this.iconId = iconId;
            this.titleKey = titleKey;
            this.descriptionKey = descriptionKey;
        }

        private static Category fromConfigValue(String value) {
            if (value != null) {
                for (Category category : values()) {
                    if (category.name().equalsIgnoreCase(value)) {
                        return category;
                    }
                }
            }
            return OVERVIEW;
        }
    }
}
