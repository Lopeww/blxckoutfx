package com.lopew.blxckoutfx.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

public class BlxckoutFXConfigScreen extends Screen {
    private static final String UI_PREFIX = "ui.blxckoutfx.config.";
    private static final int INVENTORY_BUTTON_WIDTH = 80;
    private static final int INVENTORY_BUTTON_HEIGHT = 20;
    private static final int MOVE_STEP = 1;
    private static final int FAST_MOVE_STEP = 10;

    private final Screen parent;

    private boolean inventoryButtonEnabled;
    private boolean movingInventoryButton;
    private int inventoryButtonOffsetX;
    private int inventoryButtonOffsetY;

    private Button toggleInventoryButton;
    private Button moveInventoryButton;
    private Button xValueButton;
    private Button yValueButton;

    public BlxckoutFXConfigScreen(Screen parent) {
        super(Component.translatable(UI_PREFIX + "title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        BlxckoutFXClientConfig config = BlxckoutFXClientConfig.load();
        this.inventoryButtonEnabled = config.isInventoryButtonEnabled();
        this.inventoryButtonOffsetX = config.getInventoryButtonOffsetX();
        this.inventoryButtonOffsetY = config.getInventoryButtonOffsetY();

        int centerX = this.width / 2;
        int startY = this.height / 2 - 70;

        this.toggleInventoryButton = Button.builder(Component.empty(), btn -> {
            this.inventoryButtonEnabled = !this.inventoryButtonEnabled;
            refreshTexts();
        }).pos(centerX - 100, startY).size(200, 20).build();

        this.moveInventoryButton = Button.builder(Component.empty(), btn -> {
            this.movingInventoryButton = !this.movingInventoryButton;
            refreshTexts();
        }).pos(centerX - 100, startY + 30).size(200, 20).build();

        addRenderableWidget(Button.builder(Component.translatable(UI_PREFIX + "x_minus"), btn -> {
            moveInventoryButton(-MOVE_STEP, 0);
            clampInventoryButtonPosition();
            refreshTexts();
        }).pos(centerX - 100, startY + 60).size(45, 20).build());

        this.xValueButton = Button.builder(Component.empty(), btn -> {
        }).pos(centerX - 50, startY + 60).size(100, 20).build();

        addRenderableWidget(Button.builder(Component.translatable(UI_PREFIX + "x_plus"), btn -> {
            moveInventoryButton(MOVE_STEP, 0);
            clampInventoryButtonPosition();
            refreshTexts();
        }).pos(centerX + 55, startY + 60).size(45, 20).build());

        addRenderableWidget(Button.builder(Component.translatable(UI_PREFIX + "y_minus"), btn -> {
            moveInventoryButton(0, MOVE_STEP);
            clampInventoryButtonPosition();
            refreshTexts();
        }).pos(centerX - 100, startY + 85).size(45, 20).build());

        this.yValueButton = Button.builder(Component.empty(), btn -> {
        }).pos(centerX - 50, startY + 85).size(100, 20).build();

        addRenderableWidget(Button.builder(Component.translatable(UI_PREFIX + "y_plus"), btn -> {
            moveInventoryButton(0, -MOVE_STEP);
            clampInventoryButtonPosition();
            refreshTexts();
        }).pos(centerX + 55, startY + 85).size(45, 20).build());

        addRenderableWidget(this.toggleInventoryButton);
        addRenderableWidget(this.moveInventoryButton);
        addRenderableWidget(this.xValueButton);
        addRenderableWidget(this.yValueButton);

        addRenderableWidget(Button.builder(Component.translatable(UI_PREFIX + "save"), btn -> {
            BlxckoutFXClientConfig updated = BlxckoutFXClientConfig.load();
            updated.setInventoryButtonEnabled(this.inventoryButtonEnabled);
            updated.setInventoryButtonOffset(this.inventoryButtonOffsetX, this.inventoryButtonOffsetY);
            Minecraft.getInstance().setScreen(this.parent);
        }).pos(centerX - 100, startY + 125).size(98, 20).build());

        addRenderableWidget(Button.builder(Component.translatable(UI_PREFIX + "cancel"), btn ->
                Minecraft.getInstance().setScreen(this.parent)
        ).pos(centerX + 2, startY + 125).size(98, 20).build());

        clampInventoryButtonPosition();
        refreshTexts();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, this.height / 2 - 80, 0xFFFFFF);
        renderInventoryButtonPreview(guiGraphics);

        if (this.movingInventoryButton) {
            guiGraphics.drawCenteredString(
                    this.font,
                    Component.translatable(UI_PREFIX + "move_help"),
                    this.width / 2,
                    this.height / 2 + 85,
                    0xFFFF55
            );
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.movingInventoryButton) {
            int step = (modifiers & GLFW.GLFW_MOD_SHIFT) != 0 ? FAST_MOVE_STEP : MOVE_STEP;

            if (keyCode == GLFW.GLFW_KEY_A || keyCode == GLFW.GLFW_KEY_LEFT) {
                moveInventoryButton(-step, 0);
                refreshTexts();
                return true;
            }

            if (keyCode == GLFW.GLFW_KEY_D || keyCode == GLFW.GLFW_KEY_RIGHT) {
                moveInventoryButton(step, 0);
                refreshTexts();
                return true;
            }

            if (keyCode == GLFW.GLFW_KEY_W || keyCode == GLFW.GLFW_KEY_UP) {
                moveInventoryButton(0, -step);
                refreshTexts();
                return true;
            }

            if (keyCode == GLFW.GLFW_KEY_S || keyCode == GLFW.GLFW_KEY_DOWN) {
                moveInventoryButton(0, step);
                refreshTexts();
                return true;
            }

            if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER || keyCode == GLFW.GLFW_KEY_ESCAPE) {
                this.movingInventoryButton = false;
                refreshTexts();
                return true;
            }
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void refreshTexts() {
        Component state = Component.translatable(this.inventoryButtonEnabled ? UI_PREFIX + "enabled" : UI_PREFIX + "disabled");
        this.toggleInventoryButton.setMessage(Component.translatable(UI_PREFIX + "inventory_button_state", state));
        this.moveInventoryButton.setMessage(Component.translatable(this.movingInventoryButton ? UI_PREFIX + "move_done" : UI_PREFIX + "move_button"));
        this.xValueButton.setMessage(Component.translatable(UI_PREFIX + "x_value", this.inventoryButtonOffsetX));
        this.yValueButton.setMessage(Component.translatable(UI_PREFIX + "bottom_value", this.inventoryButtonOffsetY));
    }

    private void moveInventoryButton(int deltaX, int deltaY) {
        this.inventoryButtonOffsetX += deltaX;
        this.inventoryButtonOffsetY -= deltaY;
        clampInventoryButtonPosition();
    }

    private void clampInventoryButtonPosition() {
        int maxX = Math.max(0, this.width - INVENTORY_BUTTON_WIDTH);
        int maxBottomOffset = Math.max(0, this.height - INVENTORY_BUTTON_HEIGHT);
        this.inventoryButtonOffsetX = Math.max(0, Math.min(this.inventoryButtonOffsetX, maxX));
        this.inventoryButtonOffsetY = Math.max(0, Math.min(this.inventoryButtonOffsetY, maxBottomOffset));
    }

    private void renderInventoryButtonPreview(GuiGraphics guiGraphics) {
        int x = this.inventoryButtonOffsetX;
        int y = this.height - INVENTORY_BUTTON_HEIGHT - this.inventoryButtonOffsetY;
        int borderColor = this.movingInventoryButton ? 0xFFFFFF55 : 0xFF777777;
        int fillColor = this.inventoryButtonEnabled ? 0xCC202020 : 0xCC401818;

        guiGraphics.fill(x - 1, y - 1, x + INVENTORY_BUTTON_WIDTH + 1, y + INVENTORY_BUTTON_HEIGHT + 1, borderColor);
        guiGraphics.fill(x, y, x + INVENTORY_BUTTON_WIDTH, y + INVENTORY_BUTTON_HEIGHT, fillColor);
        guiGraphics.drawCenteredString(
                this.font,
                Component.literal("FX"),
                x + INVENTORY_BUTTON_WIDTH / 2,
                y + 6,
                this.inventoryButtonEnabled ? 0xFFFFFF : 0xAAAAAA
        );
    }
}
