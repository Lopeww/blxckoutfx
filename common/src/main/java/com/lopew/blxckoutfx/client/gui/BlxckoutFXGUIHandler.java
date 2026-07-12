package com.lopew.blxckoutfx.client.gui;

import com.lopew.blxckoutfx.client.screen.BlxckoutFXScreenExclusions;
import com.lopew.blxckoutfx.client.render.BlxckoutFXShaders;
import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import com.lopew.blxckoutfx.client.integration.ae2.BlxckoutFXAe2Support;
import com.lopew.blxckoutfx.client.config.BlxckoutFXConfigScreen;
import com.lopew.blxckoutfx.client.config.BlxckoutFXClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;
import java.util.IdentityHashMap;
import java.util.Map;

public final class BlxckoutFXGUIHandler {
    private static final int CLASSIC_BUTTON_WIDTH = 95;
    private static final int CLASSIC_BUTTON_HEIGHT = 20;
    private static BlxckoutFXClientConfig config = BlxckoutFXClientConfig.load();
    private static final Map<Screen, InventoryButton> inventoryButtons = new IdentityHashMap<>();

    private static void reloadConfig() {
        config = BlxckoutFXClientConfig.load();
    }

    public static void onScreenInit(Screen screen, ButtonSink buttonSink) {
        reloadConfig();
        inventoryButtons.remove(screen);

        if (!config.isInventoryButtonEnabled()) return;

        if (!isSupportedScreen(screen)) {
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
        int buttonWidth = style == BlxckoutFXClientConfig.InventoryButtonStyle.Classic
                ? CLASSIC_BUTTON_WIDTH
                : InventoryButton.widthFor(style);
        int buttonHeight = style == BlxckoutFXClientConfig.InventoryButtonStyle.Classic
                ? CLASSIC_BUTTON_HEIGHT
                : InventoryButton.heightFor(style);
        int x = config.getInventoryButtonScreenX(screen.width, buttonWidth);
        int y = config.getInventoryButtonScreenY(screen.height, buttonHeight);

        InventoryButton button = new InventoryButton(
                x, y,
                screen.width, screen.height,
                style,
                ae2Variant,
                createCycleButtonLabel(screen),
                btn -> {
                    if (isAe2BlackoutToggleMode(screen)) {
                        toggleAe2BlackoutState();
                    } else {
                        BlxckoutFXShaders.cyclePreset();
                    }

                    updateCycleButtonPresentation(btn, screen);

                    Minecraft minecraft = Minecraft.getInstance();

                    if (minecraft.player != null) {
                        minecraft.player.displayClientMessage(
                                createPresetChangedMessage(screen),
                                true
                        );
                    }
                }
        );
        inventoryButtons.put(screen, button);
        updateCycleButtonPresentation(button, screen);

        button.clampToScreen(screen.width, screen.height);

        buttonSink.add(button);

    }

    public static void onScreenClose(Screen screen) {
        if (isSupportedScreen(screen)) {
            inventoryButtons.remove(screen);
            reloadConfig();
        }
    }

    public static void handleClientTick(Minecraft minecraft) {
        Screen screen = minecraft == null ? null : minecraft.screen;
        InventoryButton inventoryButton = getButton(screen);

        if (minecraft == null || !(screen instanceof AbstractContainerScreen<?>) || inventoryButton == null) {
            return;
        }

        inventoryButton.updateScreenBounds(screen.width, screen.height);
        inventoryButton.restoreConfiguredPosition();
        blxckoutfx$ensureButtonAttached(screen, inventoryButton);
        inventoryButton.active = true;
        inventoryButton.visible = true;
    }

    public static void handleScreenRender(Screen screen, double mouseX, double mouseY) {
        InventoryButton inventoryButton = getButton(screen);
        if (screen == null || inventoryButton == null) {
            return;
        }

        inventoryButton.updateScreenBounds(screen.width, screen.height);
        inventoryButton.restoreConfiguredPosition();
        blxckoutfx$ensureButtonAttached(screen, inventoryButton);
        inventoryButton.active = true;
        inventoryButton.visible = true;

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft == null) {
            return;
        }

        long windowHandle = minecraft.getWindow().getWindow();
        boolean leftDown = GLFW.glfwGetMouseButton(windowHandle, GLFW.GLFW_MOUSE_BUTTON_1) == GLFW.GLFW_PRESS;
        inventoryButton.tickDrag(mouseX, mouseY, leftDown);
    }

    public static boolean onMouseClicked(double mouseX, double mouseY, int button) {
        return onMouseClicked(Minecraft.getInstance().screen, mouseX, mouseY, button);
    }

    public static boolean onMouseClicked(Screen screen, double mouseX, double mouseY, int button) {
        InventoryButton inventoryButton = getButton(screen);
        if (inventoryButton != null && inventoryButton.isMouseOver(mouseX, mouseY)) {
            if (button == 1) {
                cyclePresetBackward(screen, inventoryButton);
                return true;
            }

            if (button == 2) {
                Minecraft.getInstance().setScreen(new BlxckoutFXConfigScreen(screen));
                return true;
            }
        }

        boolean handled = inventoryButton != null && inventoryButton.mouseClicked(mouseX, mouseY, button);
        return handled;
    }

    public static boolean onMouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        return onMouseDragged(Minecraft.getInstance().screen, mouseX, mouseY, button, dragX, dragY);
    }

    public static boolean onMouseDragged(Screen screen, double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (isInventoryButtonLocked()) {
            return false;
        }

        InventoryButton inventoryButton = getButton(screen);
        return inventoryButton != null && inventoryButton.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    public static boolean onMouseReleased(double mouseX, double mouseY, int button) {
        return onMouseReleased(Minecraft.getInstance().screen, mouseX, mouseY, button);
    }

    public static boolean onMouseReleased(Screen screen, double mouseX, double mouseY, int button) {
        InventoryButton inventoryButton = getButton(screen);
        return inventoryButton != null && inventoryButton.mouseReleased(mouseX, mouseY, button);
    }

    public static boolean isInventoryButtonLocked() {
        return config.isInventoryButtonLocked();
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

    private static void blxckoutfx$ensureButtonAttached(Screen screen, InventoryButton button) {
        if (screen == null || button == null) {
            return;
        }

        if (!screen.children().contains(button)) {
            blxckoutfx$tryReattachButton(screen, button);
            updateCycleButtonPresentation(button, screen);
            button.clampToScreen(screen.width, screen.height);
        }
    }

    private static void blxckoutfx$tryReattachButton(Screen screen, InventoryButton button) {
        Class<?> current = screen.getClass();
        while (current != null) {
            try {
                var method = current.getDeclaredMethod("addRenderableWidget", net.minecraft.client.gui.components.events.GuiEventListener.class);
                method.setAccessible(true);
                method.invoke(screen, button);
                return;
            } catch (ReflectiveOperationException ignored) {
                current = current.getSuperclass();
            }
        }
    }

    private static boolean isSupportedScreen(Screen screen) {
        if (screen == null) {
            return false;
        }

        if (screen instanceof TitleScreen) {
            return true;
        }

        if (screen instanceof AbstractContainerScreen<?>) {
            return true;
        }

        String className = screen.getClass().getName();
        return className.startsWith("mezz.jei");
    }

    private static Component createCycleButtonLabel(Screen screen) {
        if (isAe2BlackoutToggleMode(screen)) {
            return getAe2BlackoutStateComponent();
        }

        return BlxckoutFXShaders.getCurrentPresetComponent();
    }

    private static void updateCycleButtonPresentation(Button button, Screen screen) {
        Component label = createCycleButtonLabel(screen);
        if (button instanceof InventoryButton inventoryButton) {
            inventoryButton.setShortLabelSource(label);
            button.setMessage(Component.empty());
        } else {
            button.setMessage(label);
        }
        button.setTooltip(Tooltip.create(createCycleButtonTooltipText(screen)));
    }

    private static Component createCycleButtonTooltipText(Screen screen) {
        Component presetComponent = isAe2BlackoutToggleMode(screen)
                ? getAe2BlackoutStateComponent()
                : BlxckoutFXShaders.getCurrentPresetComponent();

        Component tooltip = Component.empty()
                .append(Component.translatable("ui.blxckoutfx.inventory_button.tooltip.preset").withStyle(ChatFormatting.GRAY))
                .append(presetComponent.copy().withStyle(ChatFormatting.WHITE))
                .append("\n")
                .append(Component.translatable("ui.blxckoutfx.inventory_button.tooltip.lmb").withStyle(ChatFormatting.GOLD))
                .append(Component.translatable("ui.blxckoutfx.inventory_button.tooltip.next_theme").withStyle(style -> style.withColor(ChatFormatting.DARK_GRAY).withItalic(true)))
                .append("\n")
                .append(Component.translatable("ui.blxckoutfx.inventory_button.tooltip.rmb").withStyle(ChatFormatting.GOLD))
                .append(Component.translatable("ui.blxckoutfx.inventory_button.tooltip.previous_theme").withStyle(style -> style.withColor(ChatFormatting.DARK_GRAY).withItalic(true)))
                .append("\n")
                .append(Component.translatable("ui.blxckoutfx.inventory_button.tooltip.move").withStyle(ChatFormatting.GOLD))
                .append(Component.translatable("ui.blxckoutfx.inventory_button.tooltip.drag").withStyle(style -> style.withColor(ChatFormatting.DARK_GRAY).withItalic(true)))
                .append("\n")
                .append(Component.translatable("ui.blxckoutfx.inventory_button.tooltip.mmb").withStyle(ChatFormatting.GOLD))
                .append(Component.translatable("ui.blxckoutfx.inventory_button.tooltip.open_config").withStyle(style -> style.withColor(ChatFormatting.DARK_GRAY).withItalic(true)));

        if (isInventoryButtonLocked()) {
            tooltip = tooltip.copy()
                    .append("\n")
                    .append(Component.translatable("ui.blxckoutfx.inventory_button.tooltip.locked")
                            .withStyle(style -> style.withColor(ChatFormatting.RED).withItalic(true)));
        }

        return tooltip;
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
        boolean nextEnabled = !config.isAe2ThemeEnabled();
        config.setAe2ThemeEnabled(nextEnabled);
        reloadConfig();
    }

    private static void cyclePresetBackward(Screen screen, InventoryButton button) {
        if (isAe2BlackoutToggleMode(screen)) {
            toggleAe2BlackoutState();
            updateCycleButtonPresentation(button, screen);
            notifyPresetChanged(screen);
            return;
        }

        BlxckoutFXShaders.cyclePresetBackward();
        updateCycleButtonPresentation(button, screen);
        notifyPresetChanged(screen);
    }

    private static void notifyPresetChanged(Screen screen) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null) {
            minecraft.player.displayClientMessage(createPresetChangedMessage(screen), true);
        }
    }

    @FunctionalInterface
    public interface ButtonSink {
        void add(InventoryButton button);
    }

    private BlxckoutFXGUIHandler() {
    }
}
