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
    private static final String PATCHOULI_BOOK_SCREEN_PREFIX = "vazkii.patchouli.client.book.gui.";
    private static final String JEI_SCREEN_PREFIX = "mezz.jei.";
    private static BlxckoutFXClientConfig config = BlxckoutFXClientConfig.load();
    private static final Map<Screen, InventoryButton> inventoryButtons = new IdentityHashMap<>();
    private static Screen lastScreen;

    private static void reloadConfig() {
        onConfigChanged(BlxckoutFXClientConfig.load());
    }

    public static void onConfigChanged(BlxckoutFXClientConfig newConfig) {
        config = newConfig == null ? BlxckoutFXClientConfig.load() : newConfig;
        BlxckoutFXShaders.syncPresetIndex(config.getPresetIndex());
        BlxckoutFXAe2Support.setAe2ThemeMode(config.getAe2ThemeMode());
        BlxckoutFXAe2Support.setAe2ThemeEnabled(config.isAe2ThemeEnabled());
        syncCurrentScreenButtonState();
    }

    public static void reloadForDevelopment() {
        onConfigChanged(BlxckoutFXClientConfig.load());
    }

    public static void onScreenInit(Screen screen, ButtonSink buttonSink) {
        inventoryButtons.remove(screen);
        lastScreen = screen;
        reloadConfig();

        if (!config.isInventoryButtonEnabled() || !isSupportedScreen(screen)) {
            return;
        }

        BlxckoutFXClientConfig.InventoryButtonStyle style = isCompactButtonScreen(screen)
                ? BlxckoutFXClientConfig.InventoryButtonStyle.Short
                : config.getInventoryButtonStyle();
        boolean ae2Screen = BlxckoutFXAe2Support.isAe2Screen(screen);
        InventoryButton.Ae2Variant ae2Variant = style == BlxckoutFXClientConfig.InventoryButtonStyle.Classic && ae2Screen
                ? InventoryButton.Ae2Variant.CLASSIC_ICON_TEXT
                : InventoryButton.Ae2Variant.NONE;

        removeExistingInventoryButtons(screen);
        int buttonWidth = InventoryButton.widthFor(style);
        int buttonHeight = InventoryButton.heightFor(style);
        int screenWidth = getButtonScreenWidth(screen);
        int screenHeight = getButtonScreenHeight(screen);
        config.migrateCustomInventoryButtonPosition(screenWidth, screenHeight, buttonWidth, buttonHeight);
        int x = config.getInventoryButtonScreenX(screenWidth, buttonWidth);
        int y = config.getInventoryButtonScreenY(screenHeight, buttonHeight);

        InventoryButton button = new InventoryButton(
                x,
                y,
                style,
                ae2Variant,
                createCycleButtonLabel(screen),
                btn -> {
                    BlxckoutFXShaders.cyclePreset();
                    updateCycleButtonPresentation(screen, btn);
                    notifyPresetChanged(screen);
                },
                movedButton -> config.setInventoryButtonScreenPosition(
                        movedButton.getX(),
                        movedButton.getY(),
                        getButtonScreenWidth(screen),
                        getButtonScreenHeight(screen),
                        movedButton.getWidth(),
                        movedButton.getHeight()
                ),
                screenWidth,
                screenHeight
        );

        inventoryButtons.put(screen, button);
        // Patchouli and JEI recipe screens bypass the normal widget render pass.
        if (!usesDetachedInventoryButtonRender(screen)) {
            buttonSink.add(button);
        }
        syncButtonState(screen, button);
    }

    public static void onScreenClose(Screen screen) {
        inventoryButtons.remove(screen);
        if (screen == lastScreen) {
            lastScreen = null;
        }
        reloadConfig();
    }

    public static void handleClientTick(Minecraft minecraft) {
        Screen screen = minecraft == null ? null : minecraft.screen;
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

        syncButtonState(screen, inventoryButton);

        if (!inventoryButton.isDraggingButton()) {
            return;
        }

        MouseHandler mouseHandler = minecraft.mouseHandler;
        boolean leftMousePressed = GLFW.glfwGetMouseButton(minecraft.getWindow().getWindow(), GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS;
        double mouseX = mouseHandler.xpos() * minecraft.getWindow().getGuiScaledWidth() / minecraft.getWindow().getScreenWidth();
        double mouseY = mouseHandler.ypos() * minecraft.getWindow().getGuiScaledHeight() / minecraft.getWindow().getScreenHeight();

        if (leftMousePressed) {
            inventoryButton.updateDrag(mouseX, mouseY);
        } else {
            inventoryButton.finishDrag();
        }
    }

    public static void handleScreenRender(Screen screen, double mouseX, double mouseY) {
        InventoryButton inventoryButton = getButton(screen);
        if (inventoryButton == null) {
            return;
        }

        syncButtonState(screen, inventoryButton);
    }

    public static void renderPatchouliInventoryButton(Screen screen, net.minecraft.client.gui.GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderDetachedInventoryButton(screen, graphics, mouseX, mouseY, partialTick, true);
    }

    public static void renderJeiInventoryButton(Screen screen, net.minecraft.client.gui.GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderDetachedInventoryButton(screen, graphics, mouseX, mouseY, partialTick, false);
    }

    private static void renderDetachedInventoryButton(
            Screen screen,
            net.minecraft.client.gui.GuiGraphics graphics,
            int mouseX,
            int mouseY,
            float partialTick,
            boolean patchouli
    ) {
        if (patchouli ? !isPatchouliBookScreen(screen) : !isJeiRecipeScreen(screen)) {
            return;
        }

        InventoryButton inventoryButton = getButton(screen);
        if (inventoryButton == null) {
            return;
        }

        syncButtonState(screen, inventoryButton);
        if (inventoryButton.visible) {
            inventoryButton.renderWithoutTooltip(graphics, mouseX, mouseY, partialTick);
            renderPatchouliInventoryButtonTooltip(inventoryButton, graphics, mouseX, mouseY);
        }
    }

    private static void renderPatchouliInventoryButtonTooltip(
            InventoryButton inventoryButton,
            net.minecraft.client.gui.GuiGraphics graphics,
            int mouseX,
            int mouseY
    ) {
        Tooltip tooltip = inventoryButton.getTooltip();
        if (tooltip == null || !inventoryButton.isMouseOver(mouseX, mouseY)) {
            return;
        }

        graphics.renderTooltip(Minecraft.getInstance().font, tooltip.toCharSequence(Minecraft.getInstance()), mouseX, mouseY);
    }

    public static boolean onMouseClicked(double mouseX, double mouseY, int button) {
        Minecraft minecraft = Minecraft.getInstance();
        return minecraft != null && onMouseClicked(minecraft.screen, mouseX, mouseY, button);
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
        return inventoryButton.isDraggingButton();
    }

    public static boolean onMouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        Minecraft minecraft = Minecraft.getInstance();
        return minecraft != null && onMouseDragged(minecraft.screen, mouseX, mouseY, button, dragX, dragY);
    }

    public static boolean onMouseDragged(Screen screen, double mouseX, double mouseY, int button, double dragX, double dragY) {
        InventoryButton inventoryButton = getButton(screen);

        if (screen == null || !isSupportedScreen(screen) || button != GLFW.GLFW_MOUSE_BUTTON_LEFT || inventoryButton == null || isInventoryButtonLocked() || !inventoryButton.isDraggingButton()) {
            return false;
        }

        inventoryButton.updateDrag(mouseX, mouseY);
        return true;
    }

    public static boolean onMouseReleased(double mouseX, double mouseY, int button) {
        Minecraft minecraft = Minecraft.getInstance();
        return minecraft != null && onMouseReleased(minecraft.screen, mouseX, mouseY, button);
    }

    public static boolean onMouseReleased(Screen screen, double mouseX, double mouseY, int button) {
        InventoryButton inventoryButton = getButton(screen);

        if (screen == null || !isSupportedScreen(screen) || button != GLFW.GLFW_MOUSE_BUTTON_LEFT || inventoryButton == null || isInventoryButtonLocked() || !inventoryButton.isDraggingButton()) {
            return false;
        }

        inventoryButton.finishDrag();
        return true;
    }

    public static boolean isInventoryButtonLocked() {
        return config.isInventoryButtonLocked();
    }

    private static void syncCurrentScreenButtonState() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft == null || minecraft.screen == null) {
            return;
        }

        InventoryButton inventoryButton = getButton(minecraft.screen);
        if (inventoryButton != null) {
            syncButtonState(minecraft.screen, inventoryButton);
        }
    }

    private static void syncButtonState(Screen screen, InventoryButton button) {
        if (screen == null || button == null) {
            return;
        }

        int screenWidth = getButtonScreenWidth(screen);
        int screenHeight = getButtonScreenHeight(screen);
        button.setScreenSize(screenWidth, screenHeight);
        button.visible = config.isInventoryButtonEnabled() && isSupportedScreen(screen);
        button.active = button.visible;

        if (!button.visible) {
            button.cancelDrag();
            return;
        }

        updateCycleButtonPresentation(screen, button);
        button.clampToScreen(screenWidth, screenHeight);

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
        if (screen == null || BlxckoutFXScreenExclusions.isExcludedScreen(screen)) {
            return false;
        }

        if (screen instanceof TitleScreen) {
            return true;
        }

        if (screen instanceof AbstractContainerScreen<?>) {
            return true;
        }

        return isCompactButtonScreen(screen);
    }

    private static boolean isCompactButtonScreen(Screen screen) {
        if (screen == null) {
            return false;
        }

        String className = screen.getClass().getName();
        return isPatchouliBookScreen(screen)
                || className.startsWith(JEI_SCREEN_PREFIX);
    }

    private static boolean usesDetachedInventoryButtonRender(Screen screen) {
        return isPatchouliBookScreen(screen) || isJeiRecipeScreen(screen);
    }

    private static boolean isPatchouliBookScreen(Screen screen) {
        return screen != null && screen.getClass().getName().startsWith(PATCHOULI_BOOK_SCREEN_PREFIX);
    }

    private static boolean isJeiRecipeScreen(Screen screen) {
        return screen != null && "mezz.jei.gui.recipes.RecipesGui".equals(screen.getClass().getName());
    }

    private static int getButtonScreenWidth(Screen screen) {
        if (isPatchouliBookScreen(screen)) {
            return Minecraft.getInstance().getWindow().getGuiScaledWidth();
        }
        return screen.width;
    }

    private static int getButtonScreenHeight(Screen screen) {
        if (isPatchouliBookScreen(screen)) {
            return Minecraft.getInstance().getWindow().getGuiScaledHeight();
        }
        return screen.height;
    }

    private static Component createCycleButtonLabel(Screen screen) {
        return BlxckoutFXShaders.getCurrentPresetComponent();
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
        Component preset = BlxckoutFXShaders.getCurrentPresetComponent();
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
        BlxckoutFXShaders.cyclePresetBackward();
        updateCycleButtonPresentation(screen, button);
        notifyPresetChanged(screen);
    }

    private static void notifyPresetChanged(Screen screen) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft != null && minecraft.player != null) {
            minecraft.player.displayClientMessage(createPresetChangedMessage(screen), true);
        }
    }

    private static Component createPresetChangedMessage(Screen screen) {
        return Component.translatable("ui.blxckoutfx.message.mod_preset_changed", BlxckoutFXShaders.getCurrentPresetComponent());
    }

    private static void openConfig(Screen screen) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft != null) {
            minecraft.setScreen(new BlxckoutFXConfigScreen(screen));
        }
    }

    @FunctionalInterface
    public interface ButtonSink {
        void add(InventoryButton button);
    }

    private BlxckoutFXGUIHandler() {
    }
}
