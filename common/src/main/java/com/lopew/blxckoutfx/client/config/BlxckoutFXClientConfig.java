package com.lopew.blxckoutfx.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lopew.blxckoutfx.client.integration.ae2.BlxckoutFXAe2Support;
import com.lopew.blxckoutfx.client.render.BlxckoutFXShaders;
import com.lopew.blxckoutfx.client.gui.BlxckoutFXGUIHandler;

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
    private String ae2ThemeMode = Ae2ThemeMode.Blackout.name();
    private boolean ae2ThemeEnabled = true;

    private boolean inventoryButtonEnabled = true;
    private boolean inventoryButtonLocked = false;
    private String inventoryButtonPreset = InventoryButtonPreset.BottomLeft.name();
    private String inventoryButtonStyle = InventoryButtonStyle.Classic.name();
    private int inventoryButtonOffsetX = DEFAULT_INVENTORY_BUTTON_OFFSET_X;
    private int inventoryButtonOffsetY = DEFAULT_INVENTORY_BUTTON_OFFSET_Y;

    private String lastConfigCategory = "OVERVIEW";

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

            config.normalize();

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
        BlxckoutFXGUIHandler.onConfigChanged(this);
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
        BlxckoutFXGUIHandler.onConfigChanged(this);
    }

    public Ae2ThemeMode getAe2ThemeMode() {
        return Ae2ThemeMode.fromName(this.ae2ThemeMode);
    }

    public void setAe2ThemeMode(Ae2ThemeMode ae2ThemeMode) {
        this.ae2ThemeMode = sanitizeAe2ThemeMode(ae2ThemeMode).name();
        BlxckoutFXAe2Support.setAe2ThemeMode(Ae2ThemeMode.fromName(this.ae2ThemeMode));
        save();
        BlxckoutFXGUIHandler.onConfigChanged(this);
    }

    public boolean isAe2ThemeEnabled() {
        return ae2ThemeEnabled;
    }

    public void setAe2ThemeEnabled(boolean ae2ThemeEnabled) {
        this.ae2ThemeEnabled = ae2ThemeEnabled;
        BlxckoutFXAe2Support.setAe2ThemeEnabled(ae2ThemeEnabled);
        save();
        BlxckoutFXGUIHandler.onConfigChanged(this);
    }

    public boolean isInventoryButtonEnabled() {
        return inventoryButtonEnabled;
    }

    public void setInventoryButtonEnabled(boolean enabled) {
        this.inventoryButtonEnabled = enabled;
        save();
        BlxckoutFXGUIHandler.onConfigChanged(this);
    }

    public boolean isInventoryButtonLocked() {
        return inventoryButtonLocked;
    }

    public void setInventoryButtonLocked(boolean locked) {
        this.inventoryButtonLocked = locked;
        save();
        BlxckoutFXGUIHandler.onConfigChanged(this);
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
            syncLegacyInventoryButtonPosition();
        }
        save();
        BlxckoutFXGUIHandler.onConfigChanged(this);
    }

    public InventoryButtonStyle getInventoryButtonStyle() {
        return InventoryButtonStyle.fromName(this.inventoryButtonStyle);
    }

    public void setInventoryButtonStyle(InventoryButtonStyle style) {
        this.inventoryButtonStyle = (style == null ? InventoryButtonStyle.Classic : style).name();
        save();
        BlxckoutFXGUIHandler.onConfigChanged(this);
    }

    public int getInventoryButtonOffsetX() {
        return inventoryButtonOffsetX;
    }

    public int getInventoryButtonOffsetY() {
        return inventoryButtonOffsetY;
    }

    public int getInventoryButtonScreenX(int screenWidth, int buttonWidth) {
        int maxX = Math.max(0, screenWidth - buttonWidth);
        return switch (getInventoryButtonPreset()) {
            case BottomRight, TopRight -> clamp(maxX - this.inventoryButtonOffsetX, 0, maxX);
            default -> clamp(this.inventoryButtonOffsetX, 0, maxX);
        };
    }

    public int getInventoryButtonScreenY(int screenHeight, int buttonHeight) {
        int maxY = Math.max(0, screenHeight - buttonHeight);
        return switch (getInventoryButtonPreset()) {
            case TopLeft, TopRight -> clamp(this.inventoryButtonOffsetY, 0, maxY);
            default -> clamp(maxY - this.inventoryButtonOffsetY, 0, maxY);
        };
    }

    public void setInventoryButtonOffset(int offsetX, int offsetY) {
        this.inventoryButtonPreset = InventoryButtonPreset.Custom.name();
        this.inventoryButtonOffsetX = Math.max(0, offsetX);
        this.inventoryButtonOffsetY = Math.max(0, offsetY);
        syncLegacyInventoryButtonPosition();
        save();
        BlxckoutFXGUIHandler.onConfigChanged(this);
    }

    public void setInventoryButtonScreenPosition(int x, int y, int screenWidth, int screenHeight, int buttonWidth, int buttonHeight) {
        int maxX = Math.max(0, screenWidth - buttonWidth);
        int maxY = Math.max(0, screenHeight - buttonHeight);
        int clampedX = clamp(x, 0, maxX);
        int clampedY = clamp(y, 0, maxY);

        setInventoryButtonOffset(clampedX, maxY - clampedY);
    }

    public String getLastConfigCategory() {
        return lastConfigCategory;
    }

    public void setLastConfigCategory(String lastConfigCategory) {
        this.lastConfigCategory = lastConfigCategory == null || lastConfigCategory.isBlank() ? "OVERVIEW" : lastConfigCategory;
        save();
    }

    private boolean migrateLegacyInventoryButtonPosition(String json) {
        try {
            JsonObject root = JsonParser.parseString(json).getAsJsonObject();
            if (root.has("inventoryButtonOffsetX") && root.has("inventoryButtonOffsetY")) {
                return false;
            }

            if (root.has("inventoryButtonX") && root.get("inventoryButtonX").isJsonPrimitive()) {
                int legacyX = root.get("inventoryButtonX").getAsInt();
                this.inventoryButtonOffsetX = legacyX >= 0 ? legacyX : DEFAULT_INVENTORY_BUTTON_OFFSET_X;
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

    private void normalize() {
        this.darkModeEnabled = this.presetIndex != 0 && this.darkModeEnabled;
        this.ae2ThemeMode = sanitizeAe2ThemeMode(Ae2ThemeMode.fromName(this.ae2ThemeMode)).name();
        BlxckoutFXAe2Support.setAe2ThemeMode(Ae2ThemeMode.fromName(this.ae2ThemeMode));
        BlxckoutFXAe2Support.setAe2ThemeEnabled(this.ae2ThemeEnabled);
        this.inventoryButtonPreset = InventoryButtonPreset.fromName(this.inventoryButtonPreset).name();
        this.inventoryButtonStyle = InventoryButtonStyle.fromName(this.inventoryButtonStyle).name();
        this.inventoryButtonOffsetX = Math.max(0, this.inventoryButtonOffsetX);
        this.inventoryButtonOffsetY = Math.max(0, this.inventoryButtonOffsetY);
        this.lastConfigCategory = this.lastConfigCategory == null || this.lastConfigCategory.isBlank() ? "OVERVIEW" : this.lastConfigCategory;
        syncLegacyInventoryButtonPosition();
    }

    private static Ae2ThemeMode sanitizeAe2ThemeMode(Ae2ThemeMode mode) {
        return mode == null ? Ae2ThemeMode.Blackout : mode;
    }

    private void syncLegacyInventoryButtonPosition() {
        this.inventoryButtonX = this.inventoryButtonOffsetX;
        this.inventoryButtonY = -1;
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(value, max));
    }

    private void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            Files.writeString(CONFIG_PATH, GSON.toJson(this));
        } catch (IOException e) {
            System.err.println("BlxckoutFX: Failed to save config: " + e.getMessage());
        }
    }

    public enum Ae2ThemeMode {
        Blxckout,
        Blackout;

        public static Ae2ThemeMode fromName(String value) {
            if (value == null || value.isBlank()) {
                return Blackout;
            }

            for (Ae2ThemeMode mode : values()) {
                if (mode.name().equalsIgnoreCase(value)) {
                    return mode;
                }
            }

            return Blackout;
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
