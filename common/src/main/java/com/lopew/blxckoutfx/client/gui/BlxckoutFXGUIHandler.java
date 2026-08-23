package com.lopew.blxckoutfx.client.gui;

import com.lopew.blxckoutfx.client.screen.BlxckoutFXScreenExclusions;
import com.lopew.blxckoutfx.client.render.BlxckoutFXShaders;
import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import com.lopew.blxckoutfx.client.integration.ae2.BlxckoutFXAe2Support;
import com.lopew.blxckoutfx.client.config.BlxckoutFXConfigScreen;
import com.lopew.blxckoutfx.client.config.BlxckoutFXClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.GuiGraphics;
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
import java.util.List;
import java.util.Map;

public final class BlxckoutFXGUIHandler {
    private static final int CLASSIC_BUTTON_WIDTH = 95;
    private static final int CLASSIC_BUTTON_HEIGHT = 20;
    private static final String PATCHOULI_BOOK_SCREEN_PREFIX = "vazkii.patchouli.client.book.gui.";
    private static final String JEI_RECIPE_SCREEN = "mezz.jei.gui.recipes.RecipesGui";
    private static BlxckoutFXClientConfig config = BlxckoutFXClientConfig.load();
    private static final Map<Screen, InventoryButton> inventoryButtons = new IdentityHashMap<>();

    private static void reloadConfig() {
        onConfigChanged(BlxckoutFXClientConfig.load());
    }

    public static void onConfigChanged(BlxckoutFXClientConfig newConfig) {
        config = newConfig == null ? BlxckoutFXClientConfig.load() : newConfig;
        BlxckoutFXShaders.syncPresetIndex(config.getPresetIndex());
        BlxckoutFXAe2Support.setAe2ThemeMode(config.getAe2ThemeMode());
        BlxckoutFXAe2Support.setAe2ThemeEnabled(config.isAe2ThemeEnabled());

        Screen screen = Minecraft.getInstance().screen;
        InventoryButton button = getButton(screen);
        if (button != null) {
            blxckoutfx$syncButtonState(screen, button);
        }
    }

    public static void reloadForDevelopment() {
        onConfigChanged(BlxckoutFXClientConfig.load());
    }

    public static void onScreenInit(Screen screen, ButtonSink buttonSink) {
        reloadConfig();
        inventoryButtons.remove(screen);
        removeExistingInventoryButtons(screen, buttonSink);

        if (!config.isInventoryButtonEnabled()) return;

        if (!isSupportedScreen(screen)) {
            return;
        }

        BlxckoutFXClientConfig.InventoryButtonStyle style = blxckoutfx$isCompactButtonScreen(screen)
                ? BlxckoutFXClientConfig.InventoryButtonStyle.Short
                : config.getInventoryButtonStyle();
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
        int screenWidth = blxckoutfx$getButtonScreenWidth(screen);
        int screenHeight = blxckoutfx$getButtonScreenHeight(screen);
        config.migrateCustomInventoryButtonPosition(screenWidth, screenHeight, buttonWidth, buttonHeight);
        int x = config.getInventoryButtonScreenX(screenWidth, buttonWidth);
        int y = config.getInventoryButtonScreenY(screenHeight, buttonHeight);

        InventoryButton button = new InventoryButton(
                x, y,
                screenWidth, screenHeight,
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
                },
                movedButton -> config.setInventoryButtonScreenPosition(
                        movedButton.getX(),
                        movedButton.getY(),
                        blxckoutfx$getButtonScreenWidth(screen),
                        blxckoutfx$getButtonScreenHeight(screen),
                        movedButton.getWidth(),
                        movedButton.getHeight()
                )
        );
        inventoryButtons.put(screen, button);
        updateCycleButtonPresentation(button, screen);

        button.clampToScreen(screenWidth, screenHeight);

        if (!blxckoutfx$usesDetachedInventoryButtonRender(screen)) {
            buttonSink.add(button);
        }

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

        if (minecraft == null || screen == null || inventoryButton == null) {
            return;
        }

        blxckoutfx$syncButtonState(screen, inventoryButton);
        if (!inventoryButton.isDraggingButton()) {
            return;
        }

        MouseHandler mouseHandler = minecraft.mouseHandler;
        boolean leftMousePressed = GLFW.glfwGetMouseButton(minecraft.getWindow().getWindow(), GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS;
        double mouseX = mouseHandler.xpos() * minecraft.getWindow().getGuiScaledWidth() / minecraft.getWindow().getScreenWidth();
        double mouseY = mouseHandler.ypos() * minecraft.getWindow().getGuiScaledHeight() / minecraft.getWindow().getScreenHeight();
        if (leftMousePressed) {
            inventoryButton.tickDrag(mouseX, mouseY, true);
        } else {
            inventoryButton.tickDrag(mouseX, mouseY, false);
        }
    }

    public static void handleScreenRender(Screen screen, double mouseX, double mouseY) {
        InventoryButton inventoryButton = getButton(screen);
        if (screen == null || inventoryButton == null) {
            return;
        }

        blxckoutfx$syncButtonState(screen, inventoryButton);
    }

    public static void renderJeiInventoryButton(Screen screen, GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        blxckoutfx$renderDetachedInventoryButton(screen, graphics, mouseX, mouseY, partialTick, false);
    }

    public static void renderPatchouliInventoryButton(Screen screen, GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        blxckoutfx$renderDetachedInventoryButton(screen, graphics, mouseX, mouseY, partialTick, true);
    }

    private static void blxckoutfx$renderDetachedInventoryButton(
            Screen screen,
            GuiGraphics graphics,
            int mouseX,
            int mouseY,
            float partialTick,
            boolean patchouli
    ) {
        if (patchouli ? !blxckoutfx$isPatchouliBookScreen(screen) : !blxckoutfx$isJeiRecipeScreen(screen)) {
            return;
        }

        InventoryButton inventoryButton = getButton(screen);
        if (inventoryButton == null) {
            return;
        }

        blxckoutfx$syncButtonState(screen, inventoryButton);
        if (inventoryButton.visible) {
            inventoryButton.renderWithoutTooltip(graphics, mouseX, mouseY, partialTick);
            blxckoutfx$renderDetachedInventoryButtonTooltip(inventoryButton, graphics, mouseX, mouseY);
        }
    }

    private static void blxckoutfx$renderDetachedInventoryButtonTooltip(InventoryButton inventoryButton, GuiGraphics graphics, int mouseX, int mouseY) {
        Tooltip tooltip = inventoryButton.getTooltip();
        if (tooltip == null || !inventoryButton.isMouseOver(mouseX, mouseY)) {
            return;
        }

        graphics.renderTooltip(Minecraft.getInstance().font, tooltip.toCharSequence(Minecraft.getInstance()), mouseX, mouseY);
    }

    public static boolean onMouseClicked(double mouseX, double mouseY, int button) {
        return onMouseClicked(Minecraft.getInstance().screen, mouseX, mouseY, button);
    }

    public static boolean onMouseClicked(Screen screen, double mouseX, double mouseY, int button) {
        InventoryButton inventoryButton = getButton(screen);
        if (screen == null || inventoryButton == null || !isSupportedScreen(screen)
                || !inventoryButton.visible || !inventoryButton.active || !inventoryButton.isMouseOver(mouseX, mouseY)) {
            return false;
        }

        if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
            cyclePresetBackward(screen, inventoryButton);
            return true;
        }

        if (button == GLFW.GLFW_MOUSE_BUTTON_MIDDLE) {
            Minecraft.getInstance().setScreen(new BlxckoutFXConfigScreen(screen));
            return true;
        }

        if (button != GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            return false;
        }

        return inventoryButton.mouseClicked(mouseX, mouseY, button);
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
            button.clampToScreen(blxckoutfx$getButtonScreenWidth(screen), blxckoutfx$getButtonScreenHeight(screen));
        }
    }

    private static void blxckoutfx$syncButtonState(Screen screen, InventoryButton button) {
        int screenWidth = blxckoutfx$getButtonScreenWidth(screen);
        int screenHeight = blxckoutfx$getButtonScreenHeight(screen);
        button.updateScreenBounds(screenWidth, screenHeight);
        button.restoreConfiguredPosition();
        button.visible = config.isInventoryButtonEnabled() && isSupportedScreen(screen);
        button.active = button.visible;

        if (!button.visible) {
            return;
        }

        updateCycleButtonPresentation(button, screen);
        button.clampToScreen(screenWidth, screenHeight);
        if (!blxckoutfx$usesDetachedInventoryButtonRender(screen)) {
            blxckoutfx$ensureButtonAttached(screen, button);
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

    private static void removeExistingInventoryButtons(Screen screen, ButtonSink buttonSink) {
        if (screen == null) {
            return;
        }

        for (GuiEventListener listener : List.copyOf(screen.children())) {
            if (listener instanceof InventoryButton inventoryButton) {
                buttonSink.remove(inventoryButton);
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

        return screen.getClass().getName().startsWith("mezz.jei") || blxckoutfx$isPatchouliBookScreen(screen);
    }

    private static boolean blxckoutfx$usesDetachedInventoryButtonRender(Screen screen) {
        return blxckoutfx$isJeiRecipeScreen(screen) || blxckoutfx$isPatchouliBookScreen(screen);
    }

    private static boolean blxckoutfx$isJeiRecipeScreen(Screen screen) {
        return screen != null && JEI_RECIPE_SCREEN.equals(screen.getClass().getName());
    }

    private static boolean blxckoutfx$isPatchouliBookScreen(Screen screen) {
        return screen != null && screen.getClass().getName().startsWith(PATCHOULI_BOOK_SCREEN_PREFIX);
    }

    private static boolean blxckoutfx$isCompactButtonScreen(Screen screen) {
        return blxckoutfx$isPatchouliBookScreen(screen)
                || (screen != null && screen.getClass().getName().startsWith("mezz.jei"));
    }

    private static int blxckoutfx$getButtonScreenWidth(Screen screen) {
        return blxckoutfx$isPatchouliBookScreen(screen)
                ? Minecraft.getInstance().getWindow().getGuiScaledWidth()
                : screen.width;
    }

    private static int blxckoutfx$getButtonScreenHeight(Screen screen) {
        return blxckoutfx$isPatchouliBookScreen(screen)
                ? Minecraft.getInstance().getWindow().getGuiScaledHeight()
                : screen.height;
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

    public interface ButtonSink {
        void add(InventoryButton button);

        void remove(InventoryButton button);
    }

    private BlxckoutFXGUIHandler() {
    }
}
