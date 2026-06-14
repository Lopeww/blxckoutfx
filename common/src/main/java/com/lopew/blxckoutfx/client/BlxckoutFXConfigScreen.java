package com.lopew.blxckoutfx.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class BlxckoutFXConfigScreen extends Screen {
    private static final String UI_PREFIX = "ui.blxckoutfx.config.";
    private static final int INVENTORY_BUTTON_WIDTH = 80;
    private static final int INVENTORY_BUTTON_HEIGHT = 20;

    private final Screen parent;

    private boolean inventoryButtonEnabled;
    private int inventoryButtonX;
    private int inventoryButtonY;

    private Button toggleInventoryButton;
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
        this.inventoryButtonX = config.getInventoryButtonX();
        this.inventoryButtonY = config.getInventoryButtonY();

        int centerX = this.width / 2;
        int startY = this.height / 2 - 50;

        this.toggleInventoryButton = Button.builder(Component.empty(), btn -> {
            this.inventoryButtonEnabled = !this.inventoryButtonEnabled;
            refreshTexts();
        }).pos(centerX - 100, startY).size(200, 20).build();

        addRenderableWidget(Button.builder(Component.translatable(UI_PREFIX + "x_minus"), btn -> {
            this.inventoryButtonX--;
            clampInventoryButtonPosition();
            refreshTexts();
        }).pos(centerX - 100, startY + 30).size(45, 20).build());

        this.xValueButton = Button.builder(Component.empty(), btn -> {
        }).pos(centerX - 50, startY + 30).size(100, 20).build();

        addRenderableWidget(Button.builder(Component.translatable(UI_PREFIX + "x_plus"), btn -> {
            this.inventoryButtonX++;
            clampInventoryButtonPosition();
            refreshTexts();
        }).pos(centerX + 55, startY + 30).size(45, 20).build());

        addRenderableWidget(Button.builder(Component.translatable(UI_PREFIX + "y_minus"), btn -> {
            this.inventoryButtonY--;
            clampInventoryButtonPosition();
            refreshTexts();
        }).pos(centerX - 100, startY + 55).size(45, 20).build());

        this.yValueButton = Button.builder(Component.empty(), btn -> {
        }).pos(centerX - 50, startY + 55).size(100, 20).build();

        addRenderableWidget(Button.builder(Component.translatable(UI_PREFIX + "y_plus"), btn -> {
            this.inventoryButtonY++;
            clampInventoryButtonPosition();
            refreshTexts();
        }).pos(centerX + 55, startY + 55).size(45, 20).build());

        addRenderableWidget(this.toggleInventoryButton);
        addRenderableWidget(this.xValueButton);
        addRenderableWidget(this.yValueButton);

        addRenderableWidget(Button.builder(Component.translatable(UI_PREFIX + "save"), btn -> {
            BlxckoutFXClientConfig updated = BlxckoutFXClientConfig.load();
            updated.setInventoryButtonEnabled(this.inventoryButtonEnabled);
            updated.setInventoryButtonPosition(this.inventoryButtonX, this.inventoryButtonY);
            Minecraft.getInstance().setScreen(this.parent);
        }).pos(centerX - 100, startY + 95).size(98, 20).build());

        addRenderableWidget(Button.builder(Component.translatable(UI_PREFIX + "cancel"), btn ->
                Minecraft.getInstance().setScreen(this.parent)
        ).pos(centerX + 2, startY + 95).size(98, 20).build());

        clampInventoryButtonPosition();
        refreshTexts();
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
        graphics.centeredText(this.font, this.title, this.width / 2, this.height / 2 - 80, 0xFFFFFF);
    }

    private void refreshTexts() {
        Component state = Component.translatable(this.inventoryButtonEnabled ? UI_PREFIX + "enabled" : UI_PREFIX + "disabled");
        this.toggleInventoryButton.setMessage(Component.translatable(UI_PREFIX + "inventory_button_state", state));
        this.xValueButton.setMessage(Component.translatable(UI_PREFIX + "x_value", this.inventoryButtonX));
        this.yValueButton.setMessage(Component.translatable(UI_PREFIX + "y_value", this.inventoryButtonY));
    }

    private void clampInventoryButtonPosition() {
        int maxX = Math.max(0, this.width - INVENTORY_BUTTON_WIDTH);
        int maxY = Math.max(0, this.height - INVENTORY_BUTTON_HEIGHT);
        this.inventoryButtonX = Math.max(0, Math.min(this.inventoryButtonX, maxX));
        this.inventoryButtonY = Math.max(0, Math.min(this.inventoryButtonY, maxY));
    }
}
