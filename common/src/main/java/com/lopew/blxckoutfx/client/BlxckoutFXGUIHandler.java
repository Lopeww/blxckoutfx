package com.lopew.blxckoutfx.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.MouseHandler;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.util.IdentityHashMap;
import java.util.Map;

public final class BlxckoutFXGUIHandler {
    private static final int INVENTORY_BUTTON_WIDTH = 80;
    private static final int INVENTORY_BUTTON_HEIGHT = 20;

    private static BlxckoutFXClientConfig config = BlxckoutFXClientConfig.load();
    private static final Map<Screen, InventoryButton> inventoryButtons = new IdentityHashMap<>();
    private static boolean wasLeftMousePressed;

    private static void reloadConfig() {
        config = BlxckoutFXClientConfig.load();
    }

    public static void onScreenInit(Screen screen, ButtonSink buttonSink) {
        reloadConfig();
        wasLeftMousePressed = false;
        inventoryButtons.remove(screen);

        if (!config.isInventoryButtonEnabled()) return;

        if (!isSupportedScreen(screen)) {
            return;
        }

        int x = config.getInventoryButtonScreenX(screen.width, INVENTORY_BUTTON_WIDTH);
        int y = config.getInventoryButtonScreenY(screen.height, INVENTORY_BUTTON_HEIGHT);

        InventoryButton button = new InventoryButton(
                x, y, INVENTORY_BUTTON_WIDTH, INVENTORY_BUTTON_HEIGHT,
                screen.width, screen.height,
                createCycleButtonLabel(),
                btn -> {
                    BlxckoutFXShaders.cyclePreset();
                    updateCycleButtonPresentation(btn, screen);

                    Minecraft minecraft = Minecraft.getInstance();

                    if (minecraft.player != null) {
                        minecraft.player.displayClientMessage(
                                createPresetChangedMessage(),
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
            wasLeftMousePressed = false;
            reloadConfig();
        }
    }

    public static void handleClientTick(Minecraft minecraft) {
        Screen screen = minecraft == null ? null : minecraft.screen;
        InventoryButton inventoryButton = getButton(screen);

        if (minecraft == null || !(screen instanceof AbstractContainerScreen<?>) || inventoryButton == null) {
            wasLeftMousePressed = false;
            return;
        }

        MouseHandler mouseHandler = minecraft.mouseHandler;
        boolean leftMousePressed = GLFW.glfwGetMouseButton(minecraft.getWindow().getWindow(), GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS;
        double mouseX = mouseHandler.xpos() * minecraft.getWindow().getGuiScaledWidth() / minecraft.getWindow().getScreenWidth();
        double mouseY = mouseHandler.ypos() * minecraft.getWindow().getGuiScaledHeight() / minecraft.getWindow().getScreenHeight();
        boolean overButton = inventoryButton.isMouseOver(mouseX, mouseY);

        if (leftMousePressed && !wasLeftMousePressed && overButton) {
            inventoryButton.beginDrag(mouseX, mouseY);
        }

        if (leftMousePressed && inventoryButton.isDraggingButton()) {
            inventoryButton.updateDrag(mouseX, mouseY);
        }

        if (!leftMousePressed && wasLeftMousePressed && inventoryButton.isDraggingButton()) {
            inventoryButton.finishDrag();
        }

        wasLeftMousePressed = leftMousePressed;
    }

    public static boolean onMouseClicked(double mouseX, double mouseY, int button) {
        return onMouseClicked(Minecraft.getInstance().screen, mouseX, mouseY, button);
    }

    public static boolean onMouseClicked(Screen screen, double mouseX, double mouseY, int button) {
        InventoryButton inventoryButton = getButton(screen);
        boolean handled = inventoryButton != null && inventoryButton.mouseClicked(mouseX, mouseY, button);

        if (handled) {
            wasLeftMousePressed = true;
        }

        return handled;
    }

    public static boolean onMouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        return onMouseDragged(Minecraft.getInstance().screen, mouseX, mouseY, button, dragX, dragY);
    }

    public static boolean onMouseDragged(Screen screen, double mouseX, double mouseY, int button, double dragX, double dragY) {
        InventoryButton inventoryButton = getButton(screen);
        return inventoryButton != null && inventoryButton.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    public static boolean onMouseReleased(double mouseX, double mouseY, int button) {
        return onMouseReleased(Minecraft.getInstance().screen, mouseX, mouseY, button);
    }

    public static boolean onMouseReleased(Screen screen, double mouseX, double mouseY, int button) {
        InventoryButton inventoryButton = getButton(screen);
        boolean handled = inventoryButton != null && inventoryButton.mouseReleased(mouseX, mouseY, button);

        if (handled) {
            wasLeftMousePressed = false;
        }

        return handled;
    }

    private static InventoryButton getButton(Screen screen) {
        return screen == null ? null : inventoryButtons.get(screen);
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

    private static Component createCycleButtonLabel() {
        return Component.empty()
                .append("FX: ")
                .append(BlxckoutFXShaders.getCurrentPresetComponent());
    }

    private static void updateCycleButtonPresentation(Button button, Screen screen) {
        button.setMessage(createCycleButtonLabel());
        button.setTooltip(Tooltip.create(createCycleButtonTooltipText(screen)));
    }

    private static Component createCycleButtonTooltipText(Screen screen) {
        String key = screen instanceof TitleScreen
                ? "tooltip.blxckoutfx.cycle_button.title"
                : "tooltip.blxckoutfx.cycle_button.inventory";

        return Component.translatable(key);
    }

    private static Component createPresetChangedMessage() {
        return Component.empty()
                .append("BlxckoutFX: ")
                .append(BlxckoutFXShaders.getCurrentPresetComponent());
    }

    @FunctionalInterface
    public interface ButtonSink {
        void add(InventoryButton button);
    }

    private BlxckoutFXGUIHandler() {
    }
}
