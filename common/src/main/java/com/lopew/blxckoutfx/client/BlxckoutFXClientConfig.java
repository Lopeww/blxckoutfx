package com.lopew.blxckoutfx.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class BlxckoutFXClientConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = Path.of("config", "blxckoutfx-client.json");
    private static final int DEFAULT_INVENTORY_BUTTON_OFFSET_X = 6;
    private static final int DEFAULT_INVENTORY_BUTTON_OFFSET_Y = 10;

    private int presetIndex = 2;

    private boolean inventoryButtonEnabled = true;
    private int inventoryButtonOffsetX = DEFAULT_INVENTORY_BUTTON_OFFSET_X;
    private int inventoryButtonOffsetY = DEFAULT_INVENTORY_BUTTON_OFFSET_Y;

    // Legacy absolute-position fields. Keep them so old config files can be read cleanly.
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
        this.presetIndex = presetIndex;
        save();
    }

    public boolean isInventoryButtonEnabled() {
        return inventoryButtonEnabled;
    }

    public void setInventoryButtonEnabled(boolean enabled) {
        this.inventoryButtonEnabled = enabled;
        save();
    }

    public int getInventoryButtonOffsetX() {
        return inventoryButtonOffsetX;
    }

    public int getInventoryButtonOffsetY() {
        return inventoryButtonOffsetY;
    }

    public int getInventoryButtonScreenX(int screenWidth, int buttonWidth) {
        return clamp(this.inventoryButtonOffsetX, 0, Math.max(0, screenWidth - buttonWidth));
    }

    public int getInventoryButtonScreenY(int screenHeight, int buttonHeight) {
        int maxY = Math.max(0, screenHeight - buttonHeight);
        return clamp(maxY - this.inventoryButtonOffsetY, 0, maxY);
    }

    public void setInventoryButtonOffset(int offsetX, int offsetY) {
        this.inventoryButtonOffsetX = Math.max(0, offsetX);
        this.inventoryButtonOffsetY = Math.max(0, offsetY);
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

    private void normalize() {
        this.inventoryButtonOffsetX = Math.max(0, this.inventoryButtonOffsetX);
        this.inventoryButtonOffsetY = Math.max(0, this.inventoryButtonOffsetY);
        syncLegacyInventoryButtonPosition();
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
}
