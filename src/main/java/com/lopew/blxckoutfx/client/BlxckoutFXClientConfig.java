package com.lopew.blxckoutfx.client;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = "blxckoutfx", category = "client")
public class BlxckoutFXClientConfig {
    private static final int DEFAULT_INVENTORY_BUTTON_OFFSET_X = 6;
    private static final int DEFAULT_INVENTORY_BUTTON_OFFSET_Y = 10;

    @Config.Comment("Shader preset index (0=Off, 1=Soft, 2=Balanced, 3=Dark, 4=Blxckout)")
    @Config.RangeInt(min = 0, max = 4)
    public static int presetIndex = 2;

    @Config.Comment("Enable GUI darkening. Disabling this forces the preset to Off.")
    public static boolean darkModeEnabled = true;

    @Config.Comment("Last selected category in the BlxckoutFX config screen")
    public static String lastConfigCategory = "OVERVIEW";

    @Config.Comment("Enable inventory button")
    public static boolean inventoryButtonEnabled = true;

    @Config.Comment("Lock the inventory button in place while keeping click actions active")
    public static boolean inventoryButtonLocked = false;

    @Config.Comment("Inventory button anchor preset")
    public static InventoryButtonPreset inventoryButtonPreset = InventoryButtonPreset.BottomLeft;

    @Config.Comment("Inventory button visual style")
    public static InventoryButtonStyle inventoryButtonStyle = InventoryButtonStyle.Classic;

    @Config.Comment("Inventory button offset X")
    @Config.RangeInt(min = 0)
    public static int inventoryButtonOffsetX = DEFAULT_INVENTORY_BUTTON_OFFSET_X;

    @Config.Comment("Inventory button offset Y")
    @Config.RangeInt(min = 0)
    public static int inventoryButtonOffsetY = DEFAULT_INVENTORY_BUTTON_OFFSET_Y;

    public static int getPresetIndex() {
        return presetIndex;
    }

    public static void setPresetIndex(int index) {
        presetIndex = clamp(index, 0, 4);
        darkModeEnabled = presetIndex != 0;
        BlxckoutFXShaders.syncPresetIndex(presetIndex);
        save();
    }

    public static boolean isDarkModeEnabled() {
        return darkModeEnabled && presetIndex != 0;
    }

    public static void setDarkModeEnabled(boolean enabled) {
        darkModeEnabled = enabled;
        if (!enabled) {
            presetIndex = 0;
        } else if (presetIndex == 0) {
            presetIndex = 2;
        }
        BlxckoutFXShaders.syncPresetIndex(presetIndex);
        save();
    }

    public static String getLastConfigCategory() {
        return lastConfigCategory == null || lastConfigCategory.trim().isEmpty() ? "OVERVIEW" : lastConfigCategory;
    }

    public static void setLastConfigCategory(String category) {
        lastConfigCategory = category == null || category.trim().isEmpty() ? "OVERVIEW" : category;
        save();
    }

    public static boolean isInventoryButtonEnabled() {
        return inventoryButtonEnabled;
    }

    public static void setInventoryButtonEnabled(boolean enabled) {
        inventoryButtonEnabled = enabled;
        save();
    }

    public static boolean isInventoryButtonLocked() {
        return inventoryButtonLocked;
    }

    public static void setInventoryButtonLocked(boolean locked) {
        inventoryButtonLocked = locked;
        save();
    }

    public static InventoryButtonPreset getInventoryButtonPreset() {
        return inventoryButtonPreset == null ? InventoryButtonPreset.BottomLeft : inventoryButtonPreset;
    }

    public static void setInventoryButtonPreset(InventoryButtonPreset preset) {
        inventoryButtonPreset = preset == null ? InventoryButtonPreset.BottomLeft : preset;
        if (inventoryButtonPreset != InventoryButtonPreset.Custom) {
            inventoryButtonOffsetX = DEFAULT_INVENTORY_BUTTON_OFFSET_X;
            inventoryButtonOffsetY = DEFAULT_INVENTORY_BUTTON_OFFSET_Y;
        }
        save();
    }

    public static InventoryButtonStyle getInventoryButtonStyle() {
        return inventoryButtonStyle == null ? InventoryButtonStyle.Classic : inventoryButtonStyle;
    }

    public static void setInventoryButtonStyle(InventoryButtonStyle style) {
        inventoryButtonStyle = style == null ? InventoryButtonStyle.Classic : style;
        save();
    }

    public static int getInventoryButtonOffsetX() {
        return inventoryButtonOffsetX;
    }

    public static int getInventoryButtonOffsetY() {
        return inventoryButtonOffsetY;
    }

    public static int getInventoryButtonScreenX(int screenWidth, int buttonWidth) {
        int maxX = Math.max(0, screenWidth - buttonWidth);
        switch (getInventoryButtonPreset()) {
            case BottomRight:
            case TopRight:
                return clamp(maxX - inventoryButtonOffsetX, 0, maxX);
            default:
                return clamp(inventoryButtonOffsetX, 0, maxX);
        }
    }

    public static int getInventoryButtonScreenY(int screenHeight, int buttonHeight) {
        int maxY = Math.max(0, screenHeight - buttonHeight);
        switch (getInventoryButtonPreset()) {
            case TopLeft:
            case TopRight:
                return clamp(inventoryButtonOffsetY, 0, maxY);
            default:
                return clamp(maxY - inventoryButtonOffsetY, 0, maxY);
        }
    }

    public static void setInventoryButtonOffset(int offsetX, int offsetY) {
        inventoryButtonPreset = InventoryButtonPreset.Custom;
        inventoryButtonOffsetX = Math.max(0, offsetX);
        inventoryButtonOffsetY = Math.max(0, offsetY);
        save();
    }

    public static void setInventoryButtonScreenPosition(int x, int y, int screenWidth, int screenHeight, int buttonWidth, int buttonHeight) {
        int maxX = Math.max(0, screenWidth - buttonWidth);
        int maxY = Math.max(0, screenHeight - buttonHeight);
        int clampedX = clamp(x, 0, maxX);
        int clampedY = clamp(y, 0, maxY);

        setInventoryButtonOffset(clampedX, maxY - clampedY);
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(value, max));
    }

    public static void normalize() {
        presetIndex = clamp(presetIndex, 0, 4);
        darkModeEnabled = darkModeEnabled && presetIndex != 0;
        if (inventoryButtonPreset == null) {
            inventoryButtonPreset = InventoryButtonPreset.BottomLeft;
        }
        if (inventoryButtonStyle == null) {
            inventoryButtonStyle = InventoryButtonStyle.Classic;
        }
        inventoryButtonOffsetX = Math.max(0, inventoryButtonOffsetX);
        inventoryButtonOffsetY = Math.max(0, inventoryButtonOffsetY);
        if (lastConfigCategory == null || lastConfigCategory.trim().isEmpty()) {
            lastConfigCategory = "OVERVIEW";
        }
        BlxckoutFXShaders.syncPresetIndex(presetIndex);
    }

    public static void save() {
        normalize();
        ConfigManager.sync("blxckoutfx", Config.Type.INSTANCE);
    }

    public static class ConfigEventHandler {
        @SubscribeEvent
        public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals("blxckoutfx")) {
                ConfigManager.sync("blxckoutfx", Config.Type.INSTANCE);
                normalize();
            }
        }
    }

    public enum InventoryButtonPreset {
        BottomLeft,
        BottomRight,
        TopLeft,
        TopRight,
        Custom
    }

    public enum InventoryButtonStyle {
        Classic,
        Short
    }
}
