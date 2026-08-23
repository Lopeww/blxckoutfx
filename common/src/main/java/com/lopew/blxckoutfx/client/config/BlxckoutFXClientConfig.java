package com.lopew.blxckoutfx.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lopew.blxckoutfx.client.integration.ae2.BlxckoutFXAe2Support;
import com.lopew.blxckoutfx.client.render.BlxckoutFXShaders;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class BlxckoutFXClientConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = Path.of("config", "blxckoutfx-client.json");
    private static final int DEFAULT_INVENTORY_BUTTON_OFFSET_X = 6;
    private static final int DEFAULT_INVENTORY_BUTTON_OFFSET_Y = 10;

    private int presetIndex = 2;
    private boolean darkModeEnabled = true;
    private double guiDarkness = 0.65D;
    private double buttonDarkness = 0.55D;
    private String lastConfigCategory = "OVERVIEW";
    private String ae2ThemeMode = Ae2ThemeMode.Blxckout.name();
    private boolean ae2ThemeEnabled = true;
    private boolean developerMode;

    private boolean inventoryButtonEnabled = true;
    private boolean inventoryButtonLocked = false;
    private String inventoryButtonPreset = InventoryButtonPreset.BottomLeft.name();
    private String inventoryButtonStyle = InventoryButtonStyle.Classic.name();
    private int inventoryButtonOffsetX = DEFAULT_INVENTORY_BUTTON_OFFSET_X;
    private int inventoryButtonOffsetY = DEFAULT_INVENTORY_BUTTON_OFFSET_Y;
    private boolean inventoryButtonCustomPositionAbsolute;

    @SuppressWarnings("unused")
    private int inventoryButtonX = DEFAULT_INVENTORY_BUTTON_OFFSET_X;
    @SuppressWarnings("unused")
    private int inventoryButtonY = -1;

    public static BlxckoutFXClientConfig load() {
        try {
            if (!Files.exists(CONFIG_PATH)) {
                BlxckoutFXClientConfig config = new BlxckoutFXClientConfig();
                config.save();
                return config;
            }

            String json = Files.readString(CONFIG_PATH);
            BlxckoutFXClientConfig config = GSON.fromJson(json, BlxckoutFXClientConfig.class);
            if (config == null) {
                return new BlxckoutFXClientConfig();
            }

            if (config.migrateLegacyInventoryButtonPosition(json)) {
                config.save();
            }

            if (config.normalize()) {
                config.save();
            }
            return config;
        } catch (IOException | RuntimeException exception) {
            return new BlxckoutFXClientConfig();
        }
    }

    public int getPresetIndex() {
        return presetIndex;
    }

    public void setPresetIndex(int presetIndex) {
        this.presetIndex = clamp(presetIndex, 0, 4);
        this.darkModeEnabled = this.presetIndex != 0;
        BlxckoutFXShaders.syncPresetIndex(this.presetIndex);
        save();
    }

    public boolean isDarkModeEnabled() {
        return darkModeEnabled;
    }

    public void setDarkModeEnabled(boolean darkModeEnabled) {
        this.darkModeEnabled = darkModeEnabled;
        if (!darkModeEnabled) {
            this.presetIndex = 0;
        } else if (this.presetIndex == 0) {
            this.presetIndex = 2;
        }
        BlxckoutFXShaders.syncPresetIndex(this.presetIndex);
        save();
    }

    public double getGuiDarkness() {
        return guiDarkness;
    }

    public void setGuiDarkness(double guiDarkness) {
        this.guiDarkness = clamp01(guiDarkness);
        save();
    }

    public double getButtonDarkness() {
        return buttonDarkness;
    }

    public void setButtonDarkness(double buttonDarkness) {
        this.buttonDarkness = clamp01(buttonDarkness);
        save();
    }

    public String getLastConfigCategory() {
        return lastConfigCategory;
    }

    public void setLastConfigCategory(String lastConfigCategory) {
        this.lastConfigCategory = lastConfigCategory == null || lastConfigCategory.isBlank() ? "OVERVIEW" : lastConfigCategory;
        save();
    }

    public Ae2ThemeMode getAe2ThemeMode() {
        return Ae2ThemeMode.fromName(ae2ThemeMode);
    }

    public void setAe2ThemeMode(Ae2ThemeMode ae2ThemeMode) {
        this.ae2ThemeMode = sanitizeAe2ThemeMode(ae2ThemeMode).name();
        BlxckoutFXAe2Support.setAe2ThemeMode(Ae2ThemeMode.fromName(this.ae2ThemeMode));
        save();
    }

    public boolean isAe2ThemeEnabled() {
        return ae2ThemeEnabled;
    }

    public void setAe2ThemeEnabled(boolean ae2ThemeEnabled) {
        this.ae2ThemeEnabled = ae2ThemeEnabled;
        BlxckoutFXAe2Support.setAe2ThemeEnabled(ae2ThemeEnabled);
        save();
    }

    public boolean isDeveloperMode() {
        return developerMode;
    }

    public void setDeveloperMode(boolean developerMode) {
        this.developerMode = developerMode;
        save();
    }

    public boolean isInventoryButtonEnabled() {
        return inventoryButtonEnabled;
    }

    public void setInventoryButtonEnabled(boolean enabled) {
        this.inventoryButtonEnabled = enabled;
        save();
    }

    public boolean isInventoryButtonLocked() {
        return inventoryButtonLocked;
    }

    public void setInventoryButtonLocked(boolean locked) {
        this.inventoryButtonLocked = locked;
        save();
    }

    public int getInventoryButtonOffsetX() {
        return inventoryButtonOffsetX;
    }

    public int getInventoryButtonOffsetY() {
        return inventoryButtonOffsetY;
    }

    public InventoryButtonPreset getInventoryButtonPreset() {
        return InventoryButtonPreset.fromName(this.inventoryButtonPreset);
    }

    public void setInventoryButtonPreset(InventoryButtonPreset preset) {
        InventoryButtonPreset resolvedPreset = preset == null ? InventoryButtonPreset.BottomLeft : preset;
        this.inventoryButtonPreset = resolvedPreset.name();
        if (resolvedPreset != InventoryButtonPreset.Custom) {
            this.inventoryButtonOffsetX = DEFAULT_INVENTORY_BUTTON_OFFSET_X;
            this.inventoryButtonOffsetY = DEFAULT_INVENTORY_BUTTON_OFFSET_Y;
            this.inventoryButtonCustomPositionAbsolute = false;
            syncLegacyInventoryButtonPosition();
        }
        save();
    }

    public InventoryButtonStyle getInventoryButtonStyle() {
        return InventoryButtonStyle.fromName(this.inventoryButtonStyle);
    }

    public void setInventoryButtonStyle(InventoryButtonStyle style) {
        this.inventoryButtonStyle = (style == null ? InventoryButtonStyle.Classic : style).name();
        save();
    }

    public int getInventoryButtonScreenX(int screenWidth, int buttonWidth) {
        int maxX = Math.max(0, screenWidth - buttonWidth);
        if (getInventoryButtonPreset() == InventoryButtonPreset.Custom && this.inventoryButtonCustomPositionAbsolute) {
            return clamp(this.inventoryButtonOffsetX, 0, maxX);
        }
        return switch (getInventoryButtonPreset()) {
            case BottomRight, TopRight -> clamp(maxX - this.inventoryButtonOffsetX, 0, maxX);
            default -> clamp(this.inventoryButtonOffsetX, 0, maxX);
        };
    }

    public int getInventoryButtonScreenY(int screenHeight, int buttonHeight) {
        int maxY = Math.max(0, screenHeight - buttonHeight);
        if (getInventoryButtonPreset() == InventoryButtonPreset.Custom && this.inventoryButtonCustomPositionAbsolute) {
            return clamp(this.inventoryButtonOffsetY, 0, maxY);
        }
        return switch (getInventoryButtonPreset()) {
            case TopLeft, TopRight -> clamp(this.inventoryButtonOffsetY, 0, maxY);
            default -> clamp(maxY - this.inventoryButtonOffsetY, 0, maxY);
        };
    }

    public void setInventoryButtonOffset(int offsetX, int offsetY) {
        this.inventoryButtonPreset = InventoryButtonPreset.Custom.name();
        this.inventoryButtonOffsetX = Math.max(0, offsetX);
        this.inventoryButtonOffsetY = Math.max(0, offsetY);
        this.inventoryButtonCustomPositionAbsolute = false;
        syncLegacyInventoryButtonPosition();
        save();
    }

    public void setInventoryButtonScreenPosition(int x, int y, int screenWidth, int screenHeight, int buttonWidth, int buttonHeight) {
        int maxX = Math.max(0, screenWidth - buttonWidth);
        int maxY = Math.max(0, screenHeight - buttonHeight);
        this.inventoryButtonPreset = InventoryButtonPreset.Custom.name();
        this.inventoryButtonOffsetX = clamp(x, 0, maxX);
        this.inventoryButtonOffsetY = clamp(y, 0, maxY);
        this.inventoryButtonCustomPositionAbsolute = true;
        syncLegacyInventoryButtonPosition();
        save();
    }

    public void migrateCustomInventoryButtonPosition(int screenWidth, int screenHeight, int buttonWidth, int buttonHeight) {
        if (getInventoryButtonPreset() != InventoryButtonPreset.Custom || this.inventoryButtonCustomPositionAbsolute) {
            return;
        }

        int x = getInventoryButtonScreenX(screenWidth, buttonWidth);
        int y = getInventoryButtonScreenY(screenHeight, buttonHeight);
        this.inventoryButtonOffsetX = x;
        this.inventoryButtonOffsetY = y;
        this.inventoryButtonCustomPositionAbsolute = true;
        syncLegacyInventoryButtonPosition();
        save();
    }

    private boolean migrateLegacyInventoryButtonPosition(String json) {
        try {
            JsonObject root = JsonParser.parseString(json).getAsJsonObject();
            if (root.has("inventoryButtonOffsetX") && root.has("inventoryButtonOffsetY")) {
                return false;
            }

            if (root.has("inventoryButtonX") && root.get("inventoryButtonX").isJsonPrimitive()) {
                this.inventoryButtonOffsetX = Math.max(0, root.get("inventoryButtonX").getAsInt());
            } else {
                this.inventoryButtonOffsetX = DEFAULT_INVENTORY_BUTTON_OFFSET_X;
            }

            this.inventoryButtonOffsetY = DEFAULT_INVENTORY_BUTTON_OFFSET_Y;
            syncLegacyInventoryButtonPosition();
            return true;
        } catch (RuntimeException ignored) {
            this.inventoryButtonOffsetX = DEFAULT_INVENTORY_BUTTON_OFFSET_X;
            this.inventoryButtonOffsetY = DEFAULT_INVENTORY_BUTTON_OFFSET_Y;
            syncLegacyInventoryButtonPosition();
            return true;
        }
    }

    private boolean normalize() {
        boolean changed = false;
        this.inventoryButtonOffsetX = Math.max(0, this.inventoryButtonOffsetX);
        this.inventoryButtonOffsetY = Math.max(0, this.inventoryButtonOffsetY);
        this.inventoryButtonPreset = InventoryButtonPreset.fromName(this.inventoryButtonPreset).name();
        this.inventoryButtonStyle = InventoryButtonStyle.fromName(this.inventoryButtonStyle).name();
        this.presetIndex = clamp(this.presetIndex, 0, 4);
        this.darkModeEnabled = this.presetIndex != 0 && this.darkModeEnabled;
        this.guiDarkness = clamp01(this.guiDarkness);
        this.buttonDarkness = clamp01(this.buttonDarkness);
        this.lastConfigCategory = this.lastConfigCategory == null || this.lastConfigCategory.isBlank() ? "OVERVIEW" : this.lastConfigCategory;
        Ae2ThemeMode sanitizedTheme = sanitizeAe2ThemeMode(Ae2ThemeMode.fromName(this.ae2ThemeMode));
        if (!this.ae2ThemeMode.equals(sanitizedTheme.name())) {
            this.ae2ThemeMode = sanitizedTheme.name();
            changed = true;
        }
        BlxckoutFXAe2Support.setAe2ThemeMode(Ae2ThemeMode.fromName(this.ae2ThemeMode));
        BlxckoutFXAe2Support.setAe2ThemeEnabled(this.ae2ThemeEnabled);
        syncLegacyInventoryButtonPosition();
        return changed;
    }

    private static Ae2ThemeMode sanitizeAe2ThemeMode(Ae2ThemeMode mode) {
        if (mode == null || mode == Ae2ThemeMode.Blackout) {
            return Ae2ThemeMode.Blxckout;
        }

        return mode;
    }

    private void syncLegacyInventoryButtonPosition() {
        this.inventoryButtonX = this.inventoryButtonOffsetX;
        this.inventoryButtonY = -1;
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(value, max));
    }

    private static double clamp01(double value) {
        return Math.max(0.0D, Math.min(value, 1.0D));
    }

    private void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            Files.writeString(CONFIG_PATH, GSON.toJson(this));
        } catch (IOException exception) {
            System.err.println("BlxckoutFX: Failed to save config: " + exception.getMessage());
        }
    }

    public enum Ae2ThemeMode {
        Blxckout,
        Blackout;

        public static Ae2ThemeMode fromName(String value) {
            if (value == null || value.isBlank()) {
                return Blxckout;
            }

            for (Ae2ThemeMode mode : values()) {
                if (mode.name().equalsIgnoreCase(value)) {
                    return mode;
                }
            }

            return Blxckout;
        }
    }

    public enum InventoryButtonPreset {
        BottomLeft,
        BottomRight,
        TopLeft,
        TopRight,
        Custom;

        public static InventoryButtonPreset fromName(String value) {
            if (value == null || value.isBlank()) {
                return BottomLeft;
            }

            for (InventoryButtonPreset preset : values()) {
                if (preset.name().equalsIgnoreCase(value)) {
                    return preset;
                }
            }

            return BottomLeft;
        }
    }

    public enum InventoryButtonStyle {
        Classic,
        Short;

        public static InventoryButtonStyle fromName(String value) {
            if (value == null || value.isBlank()) {
                return Classic;
            }

            for (InventoryButtonStyle style : values()) {
                if (style.name().equalsIgnoreCase(value)) {
                    return style;
                }
            }

            return Classic;
        }
    }
}
