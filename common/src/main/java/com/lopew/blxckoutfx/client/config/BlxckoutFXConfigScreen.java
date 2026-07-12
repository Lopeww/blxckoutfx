package com.lopew.blxckoutfx.client.config;

import com.lopew.blxckoutfx.client.render.BlxckoutFXShaders;
import com.lopew.blxckoutfx.client.config.widget.ButtonWidget;
import com.lopew.blxckoutfx.client.config.widget.ConfigTextLayout;
import com.lopew.blxckoutfx.client.config.widget.DropdownWidget;
import com.lopew.blxckoutfx.client.config.widget.PresetPreviewWidget;
import com.lopew.blxckoutfx.client.config.widget.SettingRow;
import com.lopew.blxckoutfx.client.config.widget.SettingsPanelWidget;
import com.lopew.blxckoutfx.client.config.widget.SidebarWidget;
import com.lopew.blxckoutfx.client.config.widget.ToggleWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

public class BlxckoutFXConfigScreen extends Screen {
    private static final int PANEL_COLOR = 0xF0060606;
    private static final int SIDEBAR_COLOR = 0xF00A0A0A;
    private static final int CONTENT_COLOR = 0xE0101010;
    private static final int BORDER_COLOR = 0xFF232323;
    private static final int TEXT_COLOR = 0xFFF1F1F1;
    private static final int MUTED_TEXT_COLOR = 0xFF8E8E8E;
    private static final int ACCENT_COLOR = 0xFF4A4A4A;

    private final Screen parent;

    private SidebarWidget sidebar;
    private SettingsPanelWidget settingsPanel;
    private PresetPreviewWidget presetPreviewWidget;
    private ButtonWidget doneButton;
    private ButtonWidget cancelButton;

    private BlxckoutFXClientConfig config;
    private Category selectedCategory = Category.OVERVIEW;
    private boolean darkModeEnabled;
    private boolean inventoryButtonEnabled;
    private boolean inventoryButtonLocked;
    private int presetIndex;
    private BlxckoutFXClientConfig.Ae2ThemeMode ae2ThemeMode;
    private BlxckoutFXClientConfig.InventoryButtonPreset inventoryButtonPreset;
    private BlxckoutFXClientConfig.InventoryButtonStyle inventoryButtonStyle;
    private String searchQuery = "";

    private int rootX;
    private int rootY;
    private int rootW;
    private int rootH;
    private int sidebarW;

    public BlxckoutFXConfigScreen(Screen parent) {
        super(Component.translatable("ui.blxckoutfx.config.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.config = BlxckoutFXClientConfig.load();
        this.selectedCategory = Category.fromConfigValue(this.config.getLastConfigCategory());
        this.darkModeEnabled = this.config.isDarkModeEnabled();
        this.inventoryButtonEnabled = this.config.isInventoryButtonEnabled();
        this.inventoryButtonLocked = this.config.isInventoryButtonLocked();
        this.presetIndex = clampPresetIndex(this.config.getPresetIndex());
        this.ae2ThemeMode = this.config.getAe2ThemeMode();
        this.inventoryButtonPreset = this.config.getInventoryButtonPreset();
        this.inventoryButtonStyle = this.config.getInventoryButtonStyle();

        this.rootW = Math.min(810, this.width - 88);
        this.rootH = Math.min(468, this.height - 76);
        this.rootX = (this.width - this.rootW) / 2;
        this.rootY = (this.height - this.rootH) / 2;
        this.sidebarW = Math.min(212, Math.max(188, this.rootW / 4));

        int footerH = 30;
        int contentX = this.rootX + this.sidebarW;
        int contentY = this.rootY;
        int contentW = this.rootW - this.sidebarW;
        int contentH = this.rootH - footerH;

        this.sidebar = new SidebarWidget(
                this.rootX,
                this.rootY,
                this.sidebarW,
                this.rootH,
                List.of(Category.values()),
                this.selectedCategory,
                category -> {
                    this.selectedCategory = category;
                    this.config.setLastConfigCategory(category.name());
                    rebuildRows();
                },
                search -> {
                    this.searchQuery = search;
                    rebuildRows();
                },
                () -> Util.getPlatform().openUri("https://discord.com/invite/rUD6tynDaK"),
                () -> Util.getPlatform().openUri("https://www.curseforge.com/minecraft/mc-mods/blxckoutfx"),
                () -> Util.getPlatform().openUri("https://modrinth.com/mod/blxckoutfx")
        );

        this.settingsPanel = new SettingsPanelWidget(contentX, contentY, contentW, contentH);
        if (this.presetPreviewWidget == null) {
            this.presetPreviewWidget = new PresetPreviewWidget(() -> this.presetIndex);
        }

        int buttonY = this.rootY + this.rootH - 23;
        this.doneButton = new ButtonWidget(this.rootX + this.rootW - 76, buttonY, 64, 16, Component.translatable("ui.blxckoutfx.config.done"), this::saveAndClose);
        this.cancelButton = new ButtonWidget(this.rootX + this.rootW - 146, buttonY, 64, 16, Component.translatable("ui.blxckoutfx.config.cancel"), () -> Minecraft.getInstance().setScreen(this.parent));

        rebuildRows();
    }

    private void rebuildRows() {
        if (this.settingsPanel == null) {
            return;
        }

        if (!this.searchQuery.isBlank()) {
            rebuildSearchResults();
            return;
        }

        List<SettingRow> rows = new ArrayList<>();
        rows.addAll(buildRowsForCategory(this.selectedCategory, this.font, this.settingsPanel.getContentWidth()));

        if (this.selectedCategory == Category.AE2) {
            this.settingsPanel.setCategoryWithWarning(
                    this.selectedCategory.title(),
                    this.selectedCategory.description(),
                    Component.translatable("ui.blxckoutfx.config.warning.unsupported.title"),
                    Component.translatable("ui.blxckoutfx.config.warning.unsupported.description"),
                    rows
            );
        } else {
            this.settingsPanel.setCategory(
                    this.selectedCategory.title(),
                    this.selectedCategory.description(),
                    rows
            );
        }
    }

    private void rebuildSearchResults() {
        String needle = this.searchQuery.toLowerCase(Locale.ROOT);
        List<SettingRow> rows = new ArrayList<>();
        int matchCount = 0;

        for (Category category : Category.values()) {
            List<RowSpec> specs = buildSpecsForCategory(category);
            boolean categoryMatches = contains(category.title().getString(), needle)
                    || contains(category.description().getString(), needle)
                    || contains(category.group().getString(), needle)
                    || contains(category.section().getString(), needle);
            List<RowSpec> matches = new ArrayList<>();
            for (RowSpec spec : specs) {
                if (categoryMatches || spec.matches(needle)) {
                    matches.add(spec);
                }
            }

            if (!matches.isEmpty()) {
                rows.add(SettingRow.section(category.section()));
                for (RowSpec spec : matches) {
                    rows.add(spec.createRow(this.font, this.settingsPanel.getContentWidth()));
                    matchCount++;
                }
            }
        }

        if (rows.isEmpty()) {
            rows.add(SettingRow.section(Component.translatable("ui.blxckoutfx.config.search.results.section")));
            Component noMatchesTitle = Component.translatable("ui.blxckoutfx.config.search.no_matches.title");
            Component noMatchesDescription = Component.translatable("ui.blxckoutfx.config.search.no_matches.description", this.searchQuery);
            rows.add(new SettingRow(
                    noMatchesTitle,
                    noMatchesDescription,
                    null,
                    computeRowHeight(this.font, noMatchesTitle, noMatchesDescription, this.settingsPanel.getContentWidth(), 0, 0)
            ));
        }

        this.settingsPanel.setCategory(
                Component.translatable("ui.blxckoutfx.config.search.results.title"),
                Component.translatable("ui.blxckoutfx.config.search.results.description", matchCount, this.searchQuery),
                rows
        );
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);
        graphics.fill(0, 0, this.width, this.height, 0xCC000000);
        graphics.fill(this.rootX, this.rootY, this.rootX + this.rootW, this.rootY + this.rootH, PANEL_COLOR);
        graphics.fill(this.rootX, this.rootY, this.rootX + this.rootW, this.rootY + 1, BORDER_COLOR);
        graphics.fill(this.rootX, this.rootY + this.rootH - 1, this.rootX + this.rootW, this.rootY + this.rootH, BORDER_COLOR);
        graphics.fill(this.rootX, this.rootY, this.rootX + 1, this.rootY + this.rootH, BORDER_COLOR);
        graphics.fill(this.rootX + this.rootW - 1, this.rootY, this.rootX + this.rootW, this.rootY + this.rootH, BORDER_COLOR);

        graphics.fill(this.rootX + this.sidebarW, this.rootY, this.rootX + this.rootW, this.rootY + this.rootH - 30, CONTENT_COLOR);
        graphics.fill(this.rootX, this.rootY, this.rootX + this.sidebarW, this.rootY + this.rootH, SIDEBAR_COLOR);
        graphics.fill(this.rootX + this.sidebarW - 1, this.rootY, this.rootX + this.sidebarW, this.rootY + this.rootH, BORDER_COLOR);
        graphics.fill(this.rootX + this.sidebarW, this.rootY + this.rootH - 30, this.rootX + this.rootW, this.rootY + this.rootH - 29, BORDER_COLOR);

        this.sidebar.render(graphics, this.font, mouseX, mouseY);
        this.settingsPanel.render(graphics, this.font, mouseX, mouseY);
        this.cancelButton.render(graphics, this.font, mouseX, mouseY);
        this.doneButton.render(graphics, this.font, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && this.sidebar.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }
        if ((button == 0 || button == 1) && this.settingsPanel.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }
        if (button == 0 && (this.cancelButton.mouseClicked(mouseX, mouseY, button) || this.doneButton.mouseClicked(mouseX, mouseY, button))) {
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        return this.settingsPanel.mouseDragged(mouseX, mouseY, button, dragX, dragY)
                || super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.settingsPanel.mouseReleased(mouseX, mouseY, button);
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollY) {
        return this.sidebar.mouseScrolled(mouseX, mouseY, scrollY)
                || this.settingsPanel.mouseScrolled(mouseX, mouseY, scrollY)
                || super.mouseScrolled(mouseX, mouseY, scrollY);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return this.sidebar.keyPressed(keyCode, modifiers)
                || super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        return this.sidebar.charTyped(codePoint)
                || super.charTyped(codePoint, modifiers);
    }

    private void saveAndClose() {
        saveCurrentValues();
        Minecraft.getInstance().setScreen(this.parent);
    }

    private void saveCurrentValues() {
        this.config.setLastConfigCategory(this.selectedCategory.name());
        this.config.setPresetIndex(this.presetIndex);
        this.config.setDarkModeEnabled(this.darkModeEnabled);
        this.config.setAe2ThemeMode(this.ae2ThemeMode);
        this.config.setInventoryButtonEnabled(this.inventoryButtonEnabled);
        this.config.setInventoryButtonLocked(this.inventoryButtonLocked);
        this.config.setInventoryButtonPreset(this.inventoryButtonPreset);
        this.config.setInventoryButtonStyle(this.inventoryButtonStyle);
    }

    public enum Category {
        OVERVIEW("ui.blxckoutfx.config.group.general", "overview", "ui.blxckoutfx.config.category.overview.title", "ui.blxckoutfx.config.category.overview.description", "ui.blxckoutfx.config.section.general"),
        PRESETS("ui.blxckoutfx.config.group.blxckoutfx", "presets", "ui.blxckoutfx.config.category.presets.title", "ui.blxckoutfx.config.category.presets.description", "ui.blxckoutfx.config.section.presets"),
        INVENTORY_BUTTON("ui.blxckoutfx.config.group.blxckoutfx", "inventory_button", "ui.blxckoutfx.config.category.inventory_button.title", "ui.blxckoutfx.config.category.inventory_button.description", "ui.blxckoutfx.config.section.inventory_button"),
        AE2("ui.blxckoutfx.config.group.integrations", "ae2", "ui.blxckoutfx.config.category.ae2.title", "ui.blxckoutfx.config.category.ae2.description", "ui.blxckoutfx.config.section.ae2"),
        ABOUT("ui.blxckoutfx.config.group.about", "about", "ui.blxckoutfx.config.category.about.title", "ui.blxckoutfx.config.category.about.description", "ui.blxckoutfx.config.section.about");

        private final String groupKey;
        public final String iconId;
        private final String titleKey;
        private final String descriptionKey;
        private final String sectionKey;

        Category(String groupKey, String iconId, String titleKey, String descriptionKey, String sectionKey) {
            this.groupKey = groupKey;
            this.iconId = iconId;
            this.titleKey = titleKey;
            this.descriptionKey = descriptionKey;
            this.sectionKey = sectionKey;
        }

        public Component group() {
            return Component.translatable(this.groupKey);
        }

        public Component title() {
            return Component.translatable(this.titleKey);
        }

        public Component description() {
            return Component.translatable(this.descriptionKey);
        }

        public Component section() {
            return Component.translatable(this.sectionKey);
        }

        public static Category fromConfigValue(String value) {
            if (value == null || value.isBlank()) {
                return OVERVIEW;
            }

            for (Category category : values()) {
                if (category.name().equalsIgnoreCase(value)) {
                    return category;
                }
            }

            return OVERVIEW;
        }
    }

    private static Component presetLabel(int presetIndex) {
        return switch (clampPresetIndex(presetIndex)) {
            case 0 -> Component.translatable("preset.blxckoutfx.off");
            case 1 -> Component.translatable("preset.blxckoutfx.soft");
            case 2 -> Component.translatable("preset.blxckoutfx.balanced");
            case 3 -> Component.translatable("preset.blxckoutfx.dark");
            default -> Component.translatable("preset.blxckoutfx.blxckout");
        };
    }

    private static List<Component> presetOptions() {
        return List.of(
                Component.translatable("preset.blxckoutfx.off"),
                Component.translatable("preset.blxckoutfx.soft"),
                Component.translatable("preset.blxckoutfx.balanced"),
                Component.translatable("preset.blxckoutfx.dark"),
                Component.translatable("preset.blxckoutfx.blxckout")
        );
    }

    private static List<Component> inventoryButtonPresetOptions() {
        return List.of(
                Component.translatable("ui.blxckoutfx.config.option.inventory_button_preset.bottom_left"),
                Component.translatable("ui.blxckoutfx.config.option.inventory_button_preset.bottom_right"),
                Component.translatable("ui.blxckoutfx.config.option.inventory_button_preset.top_left"),
                Component.translatable("ui.blxckoutfx.config.option.inventory_button_preset.top_right"),
                Component.translatable("ui.blxckoutfx.config.option.inventory_button_preset.custom")
        );
    }

    private static List<Component> inventoryButtonStyleOptions() {
        return List.of(
                Component.translatable("ui.blxckoutfx.config.option.inventory_button_style.classic"),
                Component.translatable("ui.blxckoutfx.config.option.inventory_button_style.short")
        );
    }

    private static List<Component> ae2ThemeModeOptions() {
        return List.of(
                Component.translatable("ui.blxckoutfx.config.ae2_mode.blxckout")
        );
    }

    private static int clampPresetIndex(int presetIndex) {
        return Math.max(0, Math.min(presetIndex, 4));
    }

    private List<SettingRow> buildRowsForCategory(Category category, Font font, int contentWidth) {
        List<SettingRow> rows = new ArrayList<>();
        rows.add(SettingRow.section(category.section()));

        for (RowSpec spec : buildSpecsForCategory(category)) {
            rows.add(spec.createRow(font, contentWidth));
        }
        if (category == Category.PRESETS) {
            rows.add(SettingRow.section(Component.translatable("ui.blxckoutfx.config.section.preview")));
            rows.add(SettingRow.controlOnly(this.presetPreviewWidget, 146));
        }
        return rows;
    }

    private List<RowSpec> buildSpecsForCategory(Category category) {
        List<RowSpec> specs = new ArrayList<>();
        switch (category) {
            case OVERVIEW -> {
                specs.add(new RowSpec(
                        Component.translatable("ui.blxckoutfx.config.setting.enable_dark_mode.title"),
                        Component.translatable("ui.blxckoutfx.config.setting.enable_dark_mode.description"),
                        () -> new ToggleWidget(() -> this.darkModeEnabled, value -> this.darkModeEnabled = value)
                ));
                specs.add(new RowSpec(
                        Component.translatable("ui.blxckoutfx.config.setting.selected_preset.title"),
                        Component.translatable("ui.blxckoutfx.config.setting.selected_preset.description"),
                        () -> new ButtonWidget(0, 0, 82, 16, presetLabel(this.presetIndex), () -> {
                            this.selectedCategory = Category.PRESETS;
                            this.sidebar.setSelected(this.selectedCategory);
                            this.config.setLastConfigCategory(this.selectedCategory.name());
                            rebuildRows();
                        })
                ));
            }
            case PRESETS -> specs.add(new RowSpec(
                    Component.translatable("ui.blxckoutfx.config.setting.preset.title"),
                    Component.translatable("ui.blxckoutfx.config.setting.preset.description"),
                    () -> new DropdownWidget(presetOptions(), () -> this.presetIndex, index -> {
                        this.presetIndex = clampPresetIndex(index);
                        this.darkModeEnabled = this.presetIndex != 0;
                        rebuildRows();
                    })
            ));
            case INVENTORY_BUTTON -> {
                specs.add(new RowSpec(
                        Component.translatable("ui.blxckoutfx.config.setting.inventory_button_enabled.title"),
                        Component.translatable("ui.blxckoutfx.config.setting.inventory_button_enabled.description"),
                        () -> new ToggleWidget(() -> this.inventoryButtonEnabled, value -> this.inventoryButtonEnabled = value)
                ));
                specs.add(new RowSpec(
                        Component.translatable("ui.blxckoutfx.config.setting.button_lock.title"),
                        Component.translatable("ui.blxckoutfx.config.setting.button_lock.description"),
                        () -> new ToggleWidget(() -> this.inventoryButtonLocked, value -> this.inventoryButtonLocked = value)
                ));
                specs.add(new RowSpec(
                        Component.translatable("ui.blxckoutfx.config.setting.position_preset.title"),
                        Component.translatable("ui.blxckoutfx.config.setting.position_preset.description"),
                        () -> new DropdownWidget(
                                inventoryButtonPresetOptions(),
                                () -> this.inventoryButtonPreset.ordinal(),
                                index -> this.inventoryButtonPreset = BlxckoutFXClientConfig.InventoryButtonPreset.values()[Math.max(0, Math.min(index, BlxckoutFXClientConfig.InventoryButtonPreset.values().length - 1))]
                        )
                ));
                specs.add(new RowSpec(
                        Component.translatable("ui.blxckoutfx.config.setting.button_style.title"),
                        Component.translatable("ui.blxckoutfx.config.setting.button_style.description"),
                        () -> new DropdownWidget(
                                inventoryButtonStyleOptions(),
                                () -> this.inventoryButtonStyle == BlxckoutFXClientConfig.InventoryButtonStyle.Short ? 1 : 0,
                                index -> this.inventoryButtonStyle = index == 1
                                        ? BlxckoutFXClientConfig.InventoryButtonStyle.Short
                                        : BlxckoutFXClientConfig.InventoryButtonStyle.Classic
                        )
                ));
            }
            case AE2 -> {
                specs.add(new RowSpec(
                        Component.translatable("ui.blxckoutfx.config.setting.theme_mode.title"),
                        Component.translatable("ui.blxckoutfx.config.setting.theme_mode.description"),
                        () -> new DropdownWidget(
                                ae2ThemeModeOptions(),
                                () -> 0,
                                index -> this.ae2ThemeMode = BlxckoutFXClientConfig.Ae2ThemeMode.Blxckout,
                                index -> index == 0
                        )
                ));
            }
            case ABOUT -> {
                specs.add(new RowSpec(
                        Component.translatable("ui.blxckoutfx.config.setting.purpose.title"),
                        Component.translatable("ui.blxckoutfx.config.setting.purpose.description"),
                        null
                ));
                specs.add(new RowSpec(
                        Component.translatable("ui.blxckoutfx.config.setting.shader_driven.title"),
                        Component.translatable("ui.blxckoutfx.config.setting.shader_driven.description"),
                        null
                ));
                specs.add(new RowSpec(
                        Component.translatable("ui.blxckoutfx.config.setting.scope.title"),
                        Component.translatable("ui.blxckoutfx.config.setting.scope.description"),
                        null
                ));
                specs.add(new RowSpec(
                        Component.translatable("ui.blxckoutfx.config.setting.support.title"),
                        Component.translatable("ui.blxckoutfx.config.setting.support.description"),
                        null
                ));
            }
        }
        return specs;
    }

    private static boolean contains(String value, String needle) {
        return !StringUtil.isNullOrEmpty(value) && value.toLowerCase(Locale.ROOT).contains(needle);
    }

    public static int textColor() {
        return TEXT_COLOR;
    }

    public static int mutedTextColor() {
        return MUTED_TEXT_COLOR;
    }

    public static int accentColor() {
        return ACCENT_COLOR;
    }

    public static int borderColor() {
        return BORDER_COLOR;
    }

    private static final class RowSpec {
        private final Component title;
        private final Component description;
        private final Supplier<? extends com.lopew.blxckoutfx.client.config.widget.ControlWidget> controlFactory;

        private RowSpec(Component title, Component description, Supplier<? extends com.lopew.blxckoutfx.client.config.widget.ControlWidget> controlFactory) {
            this.title = title;
            this.description = description;
            this.controlFactory = controlFactory;
        }

        private SettingRow createRow(Font font, int contentWidth) {
            com.lopew.blxckoutfx.client.config.widget.ControlWidget control = this.controlFactory == null ? null : this.controlFactory.get();
            return new SettingRow(
                    this.title,
                    this.description,
                    control,
                    computeRowHeight(font, this.title, this.description, contentWidth, control == null ? 0 : control.getWidth(), control == null ? 0 : control.getHeight())
            );
        }

        private boolean matches(String needle) {
            return contains(this.title.getString(), needle) || contains(this.description.getString(), needle);
        }
    }

    private static int computeRowHeight(Font font, Component title, Component description, int contentWidth, int controlWidth, int controlHeight) {
        int textWidth = Math.max(1, contentWidth - controlWidth - 32);
        int titleHeight = ConfigTextLayout.wrappedHeight(font, title, textWidth);
        int descriptionHeight = ConfigTextLayout.wrappedHeight(font, description, textWidth);
        int textBlockHeight = titleHeight + (titleHeight > 0 && descriptionHeight > 0 ? 4 : 0) + descriptionHeight;
        return Math.max(50, Math.max(24 + textBlockHeight + 10, controlHeight > 0 ? controlHeight + 8 : 50));
    }
}
