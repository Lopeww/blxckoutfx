package com.lopew.blxckoutfx.client.gui;

import com.lopew.blxckoutfx.client.config.BlxckoutFXClientConfig;
import com.lopew.blxckoutfx.client.config.BlxckoutFXConfigScreen;
import com.lopew.blxckoutfx.client.integration.ae2.BlxckoutFXAe2Support;
import com.lopew.blxckoutfx.client.render.BlxckoutFXShaders;
import com.lopew.blxckoutfx.client.screen.BlxckoutFXScreenExclusions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import org.lwjgl.glfw.GLFW;

import java.util.IdentityHashMap;
import java.util.Map;

public final class BlxckoutFXGUIHandler {
    private static BlxckoutFXClientConfig config = BlxckoutFXClientConfig.load();
    private static final Map<Screen, InventoryButton> inventoryButtons = new IdentityHashMap<>();
    private static Screen lastScreen;

    static {
        BlxckoutFXShaders.syncPresetIndex(config.getPresetIndex());
        BlxckoutFXAe2Support.syncFromConfig(config);
    }

    public static void onConfigChanged(BlxckoutFXClientConfig newConfig) {
        config = newConfig == null ? BlxckoutFXClientConfig.load() : newConfig;
        BlxckoutFXShaders.syncPresetIndex(config.getPresetIndex());
        BlxckoutFXAe2Support.syncFromConfig(config);
        syncCurrentScreenButtonState();
    }

    public static void onScreenInit(Screen screen, ButtonSink buttonSink) {
        inventoryButtons.remove(screen);
        lastScreen = screen;

        if (!config.isInventoryButtonEnabled() || !isSupportedScreen(screen)) {
            return;
        }

        BlxckoutFXClientConfig.InventoryButtonStyle style = config.getInventoryButtonStyle();
        boolean ae2Screen = BlxckoutFXAe2Support.isAe2Screen(screen);
        boolean ae2BlackoutMode = isAe2BlackoutToggleMode(screen);
        InventoryButton.Ae2Variant ae2Variant = style == BlxckoutFXClientConfig.InventoryButtonStyle.Short && ae2BlackoutMode
                ? InventoryButton.Ae2Variant.SHORT_ICON_ONLY
                : style == BlxckoutFXClientConfig.InventoryButtonStyle.Classic && ae2Screen
                ? InventoryButton.Ae2Variant.CLASSIC_ICON_TEXT
                : InventoryButton.Ae2Variant.NONE;

        removeExistingInventoryButtons(screen);
        int buttonWidth = InventoryButton.widthFor(style);
        int buttonHeight = InventoryButton.heightFor(style);
        int x = config.getInventoryButtonScreenX(screen.width, buttonWidth);
        int y = config.getInventoryButtonScreenY(screen.height, buttonHeight);

        InventoryButton button = new InventoryButton(
                x,
                y,
                style,
                ae2Variant,
                createCycleButtonLabel(screen),
                btn -> {
                    if (isAe2BlackoutToggleMode(screen)) {
                        toggleAe2BlackoutState();
                    } else {
                        BlxckoutFXShaders.cyclePreset();
                    }
                    updateCycleButtonPresentation(screen, btn);
                    notifyPresetChanged(screen);
                },
                movedButton -> config.setInventoryButtonScreenPosition(
                        movedButton.getX(),
                        movedButton.getY(),
                        screen.width,
                        screen.height,
                        movedButton.getWidth(),
                        movedButton.getHeight()
                ),
                screen.width,
                screen.height
        );

        inventoryButtons.put(screen, button);
        buttonSink.add(button);
        syncButtonState(screen, button);
    }

    public static void onScreenClose(Screen screen) {
        inventoryButtons.remove(screen);
        if (screen == lastScreen) {
            lastScreen = null;
        }
    }

    public static BlxckoutFXClientConfig getConfig() {
        return config;
    }

    public static boolean isInventoryButtonEnabled() {
        return config.isInventoryButtonEnabled();
    }

    public static void handleClientTick(Minecraft minecraft) {
        Screen screen = minecraft == null ? null : minecraft.gui.screen();
        if (screen != lastScreen) {
            if (lastScreen != null) {
                inventoryButtons.remove(lastScreen);
            }
            lastScreen = screen;
        }

        InventoryButton inventoryButton = getButton(screen);
        if (minecraft == null || screen == null || inventoryButton == null) {
            return;
        }

        if (!(screen instanceof AbstractContainerScreen<?>)) {
            return;
        }

        syncButtonState(screen, inventoryButton);

        if (!inventoryButton.isDraggingButton()) {
            return;
        }

        MouseHandler mouseHandler = minecraft.mouseHandler;
        boolean leftMousePressed = GLFW.glfwGetMouseButton(minecraft.getWindow().handle(), GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS;
        double mouseX = mouseHandler.xpos() * minecraft.getWindow().getGuiScaledWidth() / minecraft.getWindow().getScreenWidth();
        double mouseY = mouseHandler.ypos() * minecraft.getWindow().getGuiScaledHeight() / minecraft.getWindow().getScreenHeight();

        if (leftMousePressed) {
            inventoryButton.updateDrag(mouseX, mouseY);
        } else {
            inventoryButton.finishDrag(mouseX, mouseY);
        }
    }

    public static boolean onMouseClicked(Screen screen, double mouseX, double mouseY, int button) {
        InventoryButton inventoryButton = getButton(screen);

        if (screen == null || inventoryButton == null || !isSupportedScreen(screen) || !inventoryButton.visible || !inventoryButton.active || !inventoryButton.isMouseOver(mouseX, mouseY)) {
            return false;
        }

        if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
            cyclePresetBackward(screen, inventoryButton);
            return true;
        }

        if (button == GLFW.GLFW_MOUSE_BUTTON_MIDDLE) {
            openConfig(screen);
            return true;
        }

        if (button != GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            return false;
        }

        if (isInventoryButtonLocked()) {
            inventoryButton.activate();
            return true;
        }

        inventoryButton.beginDrag(mouseX, mouseY);
        return true;
    }

    public static boolean onMouseDragged(Screen screen, double mouseX, double mouseY, int button, double dragX, double dragY) {
        InventoryButton inventoryButton = getButton(screen);

        if (screen == null || !isSupportedScreen(screen) || button != GLFW.GLFW_MOUSE_BUTTON_LEFT || inventoryButton == null || isInventoryButtonLocked() || !inventoryButton.isDraggingButton()) {
            return false;
        }

        inventoryButton.updateDrag(mouseX, mouseY);
        return true;
    }

    public static boolean onMouseReleased(Screen screen, double mouseX, double mouseY, int button) {
        InventoryButton inventoryButton = getButton(screen);

        if (screen == null || !isSupportedScreen(screen) || button != GLFW.GLFW_MOUSE_BUTTON_LEFT || inventoryButton == null || isInventoryButtonLocked() || !inventoryButton.isDraggingButton()) {
            return false;
        }

        inventoryButton.finishDrag(mouseX, mouseY);
        return true;
    }

    public static boolean isInventoryButtonLocked() {
        return config.isInventoryButtonLocked();
    }

    private static void syncCurrentScreenButtonState() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft == null || minecraft.gui.screen() == null) {
            return;
        }

        InventoryButton inventoryButton = getButton(minecraft.gui.screen());
        if (inventoryButton != null) {
            syncButtonState(minecraft.gui.screen(), inventoryButton);
        }
    }

    private static void syncButtonState(Screen screen, InventoryButton button) {
        if (screen == null || button == null) {
            return;
        }

        button.setScreenSize(screen.width, screen.height);
        button.visible = config.isInventoryButtonEnabled() && isSupportedScreen(screen);
        button.active = button.visible;

        if (!button.visible) {
            button.cancelDrag();
            return;
        }

        updateCycleButtonPresentation(screen, button);
        button.clampToScreen(screen.width, screen.height);

        if (screen instanceof AbstractContainerScreen<?> && !screen.children().contains(button)) {
            tryReattachButton(screen, button);
        }
    }

    private static InventoryButton getButton(Screen screen) {
        if (screen == null) {
            return null;
        }

        InventoryButton button = inventoryButtons.get(screen);
        if (button != null) {
            return button;
        }

        for (GuiEventListener listener : screen.children()) {
            if (listener instanceof InventoryButton inventoryButton) {
                inventoryButtons.put(screen, inventoryButton);
                return inventoryButton;
            }
        }

        return null;
    }

    private static void tryReattachButton(Screen screen, InventoryButton button) {
        Class<?> current = screen.getClass();
        while (current != null) {
            try {
                var method = current.getDeclaredMethod("addRenderableWidget", GuiEventListener.class);
                method.setAccessible(true);
                method.invoke(screen, button);
                return;
            } catch (ReflectiveOperationException ignored) {
                current = current.getSuperclass();
            }
        }
    }

    private static void removeExistingInventoryButtons(Screen screen) {
        if (screen == null) {
            return;
        }

        screen.children().removeIf(listener -> listener instanceof InventoryButton);
    }

    private static boolean isSupportedScreen(Screen screen) {
        if (screen == null
                || BlxckoutFXScreenExclusions.isExcludedScreen(screen)
                || BlxckoutFXScreenExclusions.isPartialScreen(screen)) {
            return false;
        }

        if (screen instanceof TitleScreen) {
            return true;
        }

        if (screen instanceof AbstractContainerScreen<?>) {
            return true;
        }

        return screen.getClass().getName().startsWith("mezz.jei");
    }

    private static Component createCycleButtonLabel(Screen screen) {
        return isAe2BlackoutToggleMode(screen)
                ? getAe2BlackoutStateComponent()
                : BlxckoutFXShaders.getCurrentPresetComponent();
    }

    private static void updateCycleButtonPresentation(Screen screen, Button button) {
        Component label = createCycleButtonLabel(screen);
        button.setMessage(label);

        if (button instanceof InventoryButton inventoryButton) {
            inventoryButton.setShortLabelSource(label);
        }

        button.setTooltip(Tooltip.create(createCycleButtonTooltipText(screen)));
    }

    private static Component createCycleButtonTooltipText(Screen screen) {
        Component preset = isAe2BlackoutToggleMode(screen)
                ? getAe2BlackoutStateComponent()
                : BlxckoutFXShaders.getCurrentPresetComponent();
        Style labelStyle = Style.EMPTY.withColor(TextColor.fromRgb(0xAAAAAA));
        Style presetStyle = Style.EMPTY.withColor(TextColor.fromRgb(0xFFFFFF));
        Style goldStyle = Style.EMPTY.withColor(TextColor.fromRgb(0xFFAA00));
        Style darkGrayItalic = Style.EMPTY.withColor(TextColor.fromRgb(0x616161)).withItalic(true);
        Style lockedStyle = Style.EMPTY.withColor(TextColor.fromRgb(0xFF5555)).withItalic(true);

        MutableComponent tooltip = Component.empty()
                .append(Component.translatable("ui.blxckoutfx.inventory_button.tooltip.preset").withStyle(labelStyle))
                .append(preset.copy().withStyle(presetStyle))
                .append(Component.literal("\n"))
                .append(Component.translatable("ui.blxckoutfx.inventory_button.tooltip.lmb").withStyle(goldStyle))
                .append(Component.translatable("ui.blxckoutfx.inventory_button.tooltip.next_theme").withStyle(darkGrayItalic))
                .append(Component.literal("\n"))
                .append(Component.translatable("ui.blxckoutfx.inventory_button.tooltip.rmb").withStyle(goldStyle))
                .append(Component.translatable("ui.blxckoutfx.inventory_button.tooltip.previous_theme").withStyle(darkGrayItalic))
                .append(Component.literal("\n"))
                .append(Component.translatable("ui.blxckoutfx.inventory_button.tooltip.move").withStyle(goldStyle))
                .append(Component.translatable("ui.blxckoutfx.inventory_button.tooltip.drag").withStyle(darkGrayItalic))
                .append(Component.literal("\n"))
                .append(Component.translatable("ui.blxckoutfx.inventory_button.tooltip.mmb").withStyle(goldStyle))
                .append(Component.translatable("ui.blxckoutfx.inventory_button.tooltip.open_config").withStyle(darkGrayItalic));

        if (isInventoryButtonLocked()) {
            tooltip.append(Component.literal("\n"))
                    .append(Component.translatable("ui.blxckoutfx.inventory_button.tooltip.locked").withStyle(lockedStyle));
        }

        return tooltip;
    }

    private static void cyclePresetBackward(Screen screen, InventoryButton button) {
        if (isAe2BlackoutToggleMode(screen)) {
            toggleAe2BlackoutState();
        } else {
            BlxckoutFXShaders.cyclePresetBackward();
        }
        updateCycleButtonPresentation(screen, button);
        notifyPresetChanged(screen);
    }

    private static void notifyPresetChanged(Screen screen) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft != null && minecraft.player != null) {
            minecraft.player.sendOverlayMessage(createPresetChangedMessage(screen));
        }
    }

    private static Component createPresetChangedMessage(Screen screen) {
        if (isAe2BlackoutToggleMode(screen)) {
            return Component.translatable("ui.blxckoutfx.message.ae2_preset_changed", getAe2BlackoutStateComponent());
        }

        return Component.translatable("ui.blxckoutfx.message.mod_preset_changed", BlxckoutFXShaders.getCurrentPresetComponent());
    }

    private static boolean isAe2BlackoutToggleMode(Screen screen) {
        return screen != null
                && BlxckoutFXAe2Support.isAe2Screen(screen)
                && config.getAe2ThemeMode() == BlxckoutFXClientConfig.Ae2ThemeMode.Blackout;
    }

    private static Component getAe2BlackoutStateComponent() {
        return config.isAe2ThemeEnabled()
                ? Component.translatable("ui.blxckoutfx.config.ae2_mode.blackout")
                : Component.translatable("preset.blxckoutfx.off");
    }

    private static void toggleAe2BlackoutState() {
        config.setAe2ThemeEnabled(!config.isAe2ThemeEnabled());
    }

    private static void openConfig(Screen screen) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft != null) {
            minecraft.gui.setScreen(new BlxckoutFXConfigScreen(screen));
        }
    }

    @FunctionalInterface
    public interface ButtonSink {
        void add(InventoryButton button);
    }

    private BlxckoutFXGUIHandler() {
    }
}
