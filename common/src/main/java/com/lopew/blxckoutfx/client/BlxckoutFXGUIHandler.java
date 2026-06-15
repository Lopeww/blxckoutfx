package com.lopew.blxckoutfx.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
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
                createCycleButtonLabel(),
                btn -> {
                    BlxckoutFXShaders.cyclePreset();
                    updateCycleButtonPresentation(btn);

                    Minecraft minecraft = Minecraft.getInstance();

                    if (minecraft.player != null) {
                        minecraft.player.sendOverlayMessage(
                                createPresetChangedMessage()
                        );
                    }
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
        updateCycleButtonPresentation(button);

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
        boolean leftMousePressed = GLFW.glfwGetMouseButton(minecraft.getWindow().handle(), GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS;
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
            inventoryButton.finishDrag(mouseX, mouseY);
        }

        wasLeftMousePressed = leftMousePressed;
    }

    public static boolean onMouseClicked(Screen screen, double mouseX, double mouseY, int button) {
        InventoryButton inventoryButton = getButton(screen);

        if (button != GLFW.GLFW_MOUSE_BUTTON_LEFT || inventoryButton == null || !inventoryButton.isMouseOver(mouseX, mouseY)) {
            return false;
        }

        inventoryButton.beginDrag(mouseX, mouseY);
        wasLeftMousePressed = true;
        return true;
    }

    public static boolean onMouseDragged(Screen screen, double mouseX, double mouseY, int button, double dragX, double dragY) {
        InventoryButton inventoryButton = getButton(screen);

        if (button != GLFW.GLFW_MOUSE_BUTTON_LEFT || inventoryButton == null || !inventoryButton.isDraggingButton()) {
            return false;
        }

        inventoryButton.updateDrag(mouseX, mouseY);
        return true;
    }

    public static boolean onMouseReleased(Screen screen, double mouseX, double mouseY, int button) {
        InventoryButton inventoryButton = getButton(screen);

        if (button != GLFW.GLFW_MOUSE_BUTTON_LEFT || inventoryButton == null || !inventoryButton.isDraggingButton()) {
            return false;
        }

        inventoryButton.finishDrag(mouseX, mouseY);
        wasLeftMousePressed = false;
        return true;
    }

    private static InventoryButton getButton(Screen screen) {
        return screen == null ? null : inventoryButtons.get(screen);
    }

    private static boolean isSupportedScreen(Screen screen) {
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

    private static void updateCycleButtonPresentation(Button button) {
        button.setMessage(createCycleButtonLabel());
        button.setTooltip(Tooltip.create(createCycleButtonTooltipText()));
    }

    private static Component createCycleButtonTooltipText() {
        if (BlxckoutFXShaders.isBlxckoutPresetActive()) {
            return Component.translatable("tooltip.blxckoutfx.cycle_button.blxckout");
        }

        return Component.translatable("tooltip.blxckoutfx.cycle_button");
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
