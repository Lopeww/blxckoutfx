package com.lopew.blxckoutfx.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class BlxckoutFXClientConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = Path.of("config", "blxckoutfx-client.json");

    private int presetIndex = 2;

    private boolean inventoryButtonEnabled = true;
    private int inventoryButtonX = -1;
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

            // Sanitize: reset coordinates that are clearly from a different screen resolution
            // or from an old build that used hardcoded values (e.g. 403).
            // Values > 2000 are impossible on any reasonable screen so treat as invalid.
            if (config.inventoryButtonX > 2000) config.inventoryButtonX = -1;
            if (config.inventoryButtonY > 2000) config.inventoryButtonY = -1;

            return config;
        } catch (IOException exception) {
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

    public int getInventoryButtonX() {
        return inventoryButtonX;
    }

    public int getInventoryButtonY() {
        return inventoryButtonY;
    }

    public void setInventoryButtonPosition(int x, int y) {
        this.inventoryButtonX = x;
        this.inventoryButtonY = y;
        save();
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