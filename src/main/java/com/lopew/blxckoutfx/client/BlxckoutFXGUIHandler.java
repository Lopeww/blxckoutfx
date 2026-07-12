package com.lopew.blxckoutfx.client;

import com.lopew.blxckoutfx.client.config.BlxckoutFXConfigScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public final class BlxckoutFXGUIHandler {
    private static final int CYCLE_BUTTON_ID = 0x0B1A;
    private static final int LEFT_BUTTON = 0;
    private static final int RIGHT_BUTTON = 1;
    private static final int MIDDLE_BUTTON = 2;
    private static final Map<GuiScreen, InventoryPresetButton> INVENTORY_BUTTONS = new WeakHashMap<>();

    @SubscribeEvent
    public void onDrawScreenPre(GuiScreenEvent.DrawScreenEvent.Pre event) {
        BlxckoutFXRenderContext.enterDarkenedScreenRender();
    }

    @SubscribeEvent
    public void onDrawScreenPost(GuiScreenEvent.DrawScreenEvent.Post event) {
        BlxckoutFXRenderContext.exitDarkenedScreenRender();

        InventoryPresetButton button = findInventoryButton(event.getGui());
        if (button != null && button.visible && button.isMouseInside(event.getMouseX(), event.getMouseY())) {
            drawTooltip(event.getGui(), event.getMouseX(), event.getMouseY());
        }
    }

    @SubscribeEvent
    public void onGuiInit(GuiScreenEvent.InitGuiEvent.Post event) {
        GuiScreen screen = event.getGui();
        INVENTORY_BUTTONS.remove(screen);

        if (!BlxckoutFXClientConfig.isInventoryButtonEnabled() || !isSupportedScreen(screen)) {
            return;
        }

        BlxckoutFXClientConfig.InventoryButtonStyle style = BlxckoutFXClientConfig.getInventoryButtonStyle();
        int buttonWidth = InventoryPresetButton.widthFor(style);
        int buttonHeight = InventoryPresetButton.heightFor(style);
        int x = BlxckoutFXClientConfig.getInventoryButtonScreenX(screen.width, buttonWidth);
        int y = BlxckoutFXClientConfig.getInventoryButtonScreenY(screen.height, buttonHeight);

        InventoryPresetButton button = new InventoryPresetButton(
                CYCLE_BUTTON_ID,
                x,
                y,
                style,
                isAe2Screen(screen),
                () -> cyclePresetForward(screen, findInventoryButton(screen)),
                movedButton -> BlxckoutFXClientConfig.setInventoryButtonScreenPosition(
                        movedButton.x,
                        movedButton.y,
                        screen.width,
                        screen.height,
                        movedButton.width,
                        movedButton.height
                ),
                BlxckoutFXClientConfig::isInventoryButtonLocked,
                screen.width,
                screen.height
        );
        updateButtonPresentation(button);
        event.getButtonList().add(button);
        INVENTORY_BUTTONS.put(screen, button);
    }

    @SubscribeEvent
    public void onMouseInputPre(GuiScreenEvent.MouseInputEvent.Pre event) {
        GuiScreen screen = event.getGui();
        InventoryPresetButton button = findInventoryButton(screen);
        if (button == null || !isSupportedScreen(screen) || !button.visible || !button.enabled) {
            if (button != null) {
                button.cancelDrag();
            }
            return;
        }

        Minecraft minecraft = Minecraft.getMinecraft();
        int mouseX = Mouse.getEventX() * screen.width / minecraft.displayWidth;
        int mouseY = screen.height - Mouse.getEventY() * screen.height / minecraft.displayHeight - 1;
        int eventButton = Mouse.getEventButton();
        boolean buttonState = Mouse.getEventButtonState();

        if (button.isDragging()) {
            if (eventButton == LEFT_BUTTON && !buttonState) {
                button.updateDrag(mouseX, mouseY);
                button.finishDrag();
                event.setCanceled(true);
                return;
            }
            if (eventButton == -1 && Mouse.isButtonDown(LEFT_BUTTON)) {
                button.updateDrag(mouseX, mouseY);
                event.setCanceled(true);
                return;
            }
        }

        if (!buttonState || !button.isMouseInside(mouseX, mouseY)) {
            return;
        }

        if (eventButton == RIGHT_BUTTON) {
            cyclePresetBackward(screen, button);
            event.setCanceled(true);
            return;
        }

        if (eventButton == MIDDLE_BUTTON) {
            Minecraft.getMinecraft().displayGuiScreen(new BlxckoutFXConfigScreen(screen));
            event.setCanceled(true);
            return;
        }

        if (eventButton == LEFT_BUTTON) {
            if (BlxckoutFXClientConfig.isInventoryButtonLocked()) {
                button.activate();
            } else {
                button.beginDrag(mouseX, mouseY);
            }
            event.setCanceled(true);
        }
    }

    private static InventoryPresetButton findInventoryButton(GuiScreen screen) {
        InventoryPresetButton button = INVENTORY_BUTTONS.get(screen);
        if (button == null) {
            return null;
        }

        button.setScreenSize(screen.width, screen.height);
        button.visible = BlxckoutFXClientConfig.isInventoryButtonEnabled() && isSupportedScreen(screen);
        button.enabled = button.visible;
        if (!button.visible) {
            button.cancelDrag();
            return button;
        }

        updateButtonPresentation(button);
        button.clampToScreen(screen.width, screen.height);
        return button;
    }

    private static void cyclePresetForward(GuiScreen screen, InventoryPresetButton button) {
        BlxckoutFXShaders.cyclePreset();
        if (button != null) {
            updateButtonPresentation(button);
        }
        notifyPresetChanged();
    }

    private static void cyclePresetBackward(GuiScreen screen, InventoryPresetButton button) {
        BlxckoutFXShaders.cyclePresetBackward();
        updateButtonPresentation(button);
        notifyPresetChanged();
    }

    private static void updateButtonPresentation(InventoryPresetButton button) {
        button.setStyle(BlxckoutFXClientConfig.getInventoryButtonStyle());
        button.setLabelSource(translatedPresetLabel());
    }

    private static void notifyPresetChanged() {
        Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft != null && minecraft.player != null) {
            minecraft.player.sendStatusMessage(new TextComponentString(
                    BlxckoutFXI18n.format("ui.blxckoutfx.message.mod_preset_changed", translatedPresetLabel())
            ), true);
        }
    }

    private static void drawTooltip(GuiScreen screen, int mouseX, int mouseY) {
        String preset = translatedPresetLabel();
        List<String> lines = new ArrayList<>(Arrays.asList(
                TextFormatting.GRAY + BlxckoutFXI18n.format("ui.blxckoutfx.inventory_button.tooltip.preset") + TextFormatting.WHITE + preset,
                TextFormatting.GOLD + BlxckoutFXI18n.format("ui.blxckoutfx.inventory_button.tooltip.lmb") + TextFormatting.DARK_GRAY + TextFormatting.ITALIC + BlxckoutFXI18n.format("ui.blxckoutfx.inventory_button.tooltip.next_theme"),
                TextFormatting.GOLD + BlxckoutFXI18n.format("ui.blxckoutfx.inventory_button.tooltip.rmb") + TextFormatting.DARK_GRAY + TextFormatting.ITALIC + BlxckoutFXI18n.format("ui.blxckoutfx.inventory_button.tooltip.previous_theme"),
                TextFormatting.GOLD + BlxckoutFXI18n.format("ui.blxckoutfx.inventory_button.tooltip.move") + TextFormatting.DARK_GRAY + TextFormatting.ITALIC + BlxckoutFXI18n.format("ui.blxckoutfx.inventory_button.tooltip.drag"),
                TextFormatting.GOLD + BlxckoutFXI18n.format("ui.blxckoutfx.inventory_button.tooltip.mmb") + TextFormatting.DARK_GRAY + TextFormatting.ITALIC + BlxckoutFXI18n.format("ui.blxckoutfx.inventory_button.tooltip.open_config")
        ));
        if (BlxckoutFXClientConfig.isInventoryButtonLocked()) {
            lines.add(TextFormatting.RED + "" + TextFormatting.ITALIC + BlxckoutFXI18n.format("ui.blxckoutfx.inventory_button.tooltip.locked"));
        }

        int tooltipHeight = 8 + Math.max(0, lines.size() - 1) * 10 + (lines.size() > 1 ? 2 : 0);
        GuiUtils.drawHoveringText(lines, mouseX + 3, mouseY - 3 - tooltipHeight, screen.width, screen.height, -1, Minecraft.getMinecraft().fontRenderer);
    }

    private static String translatedPresetLabel() {
        return BlxckoutFXI18n.format(BlxckoutFXShaders.getCurrentPresetName());
    }

    private static boolean isSupportedScreen(GuiScreen screen) {
        if (screen == null || BlxckoutFXScreenExclusions.isExcludedScreen(screen)) {
            return false;
        }
        if (screen instanceof GuiMainMenu || screen instanceof GuiContainer) {
            return true;
        }
        return screen.getClass().getName().startsWith("mezz.jei");
    }

    private static boolean isAe2Screen(GuiScreen screen) {
        return screen != null && screen.getClass().getName().startsWith("appeng.client.gui.");
    }
}
