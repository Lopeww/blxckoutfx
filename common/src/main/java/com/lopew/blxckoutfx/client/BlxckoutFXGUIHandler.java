package com.lopew.blxckoutfx.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;

public final class BlxckoutFXGUIHandler {
    private static BlxckoutFXClientConfig config = BlxckoutFXClientConfig.load();

    private static void reloadConfig() {
        config = BlxckoutFXClientConfig.load();
    }

    public static void onScreenInit(Screen screen, ButtonSink buttonSink) {
        reloadConfig();

        if (!config.isInventoryButtonEnabled()) return;

        if (!isSupportedScreen(screen)) {
            return;
        }

        int x = config.getInventoryButtonX() == -1 ? 10 : config.getInventoryButtonX();
        int y = config.getInventoryButtonY() == -1
                ? Minecraft.getInstance().getWindow().getGuiScaledHeight() - 30
                : config.getInventoryButtonY();

        InventoryButton button = new InventoryButton(
                x, y, 80, 20,
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
                }
        );
        updateCycleButtonPresentation(button);

        button.clampToScreen(screen.width, screen.height);

        if (button.getX() != x || button.getY() != y) {
            config.setInventoryButtonPosition(button.getX(), button.getY());
        }

        buttonSink.add(button);
    }

    public static void onScreenClose(Screen screen) {
        if (isSupportedScreen(screen)) {
            reloadConfig();
        }
    }

    private static boolean isSupportedScreen(Screen screen) {
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
