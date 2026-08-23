package com.lopew.blxckoutfx.client.config.widget;

import com.lopew.blxckoutfx.client.config.BlxckoutFXConfigScreen;
import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.function.BooleanSupplier;

/** A reload action button that keeps its result visible long enough to be noticed. */
public final class ReloadStateButtonWidget implements ControlWidget {
    private static final long RESULT_DISPLAY_MILLIS = 3_000L;
    private static final int SUCCESS_COLOR = 0xFF75D6A3;
    private static final int FAILURE_COLOR = 0xFFE07777;

    private final int width;
    private final int height;
    private final Component label;
    private final BooleanSupplier reloadAction;
    private int x;
    private int y;
    private State state = State.READY;
    private long lockedUntil;

    public ReloadStateButtonWidget(int x, int y, int width, int height, Component label, BooleanSupplier reloadAction) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.label = label;
        this.reloadAction = reloadAction;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public void setBounds(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void render(GuiGraphics graphics, Font font, int mouseX, int mouseY) {
        refreshState();
        boolean ready = this.state == State.READY;
        boolean hover = ready && inside(mouseX, mouseY);
        int borderColor = BlxckoutFXConfigScreen.borderColor();
        int backgroundColor = hover ? 0xFF1B1B1B : 0xFF111111;
        int iconColor = 0;

        if (this.state == State.SUCCESS) {
            backgroundColor = 0xFF10241C;
            borderColor = 0xFF3F8E6D;
            iconColor = SUCCESS_COLOR;
        } else if (this.state == State.FAILURE) {
            backgroundColor = 0xFF281516;
            borderColor = 0xFF9D4D50;
            iconColor = FAILURE_COLOR;
        }

        graphics.fill(this.x, this.y, this.x + this.width, this.y + this.height, backgroundColor);
        drawBorder(graphics, borderColor);

        if (this.state == State.SUCCESS) {
            drawCheckmark(graphics, iconColor);
        } else if (this.state == State.FAILURE) {
            drawCross(graphics, iconColor);
        } else {
            int textWidth = Math.max(0, this.width - 8);
            graphics.drawCenteredString(font, ConfigTextLayout.ellipsize(font, this.label, textWidth), this.x + this.width / 2, this.y + (this.height - 8) / 2, 0xFFE5E5E5);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        refreshState();
        if (button != 0 || this.state != State.READY || !inside(mouseX, mouseY)) {
            return false;
        }

        this.lockedUntil = Util.getMillis() + RESULT_DISPLAY_MILLIS;
        try {
            this.state = this.reloadAction.getAsBoolean() ? State.SUCCESS : State.FAILURE;
        } catch (RuntimeException exception) {
            this.state = State.FAILURE;
        }
        return true;
    }

    private void refreshState() {
        if (this.state != State.READY && Util.getMillis() >= this.lockedUntil) {
            this.state = State.READY;
        }
    }

    private void drawBorder(GuiGraphics graphics, int color) {
        graphics.fill(this.x, this.y, this.x + this.width, this.y + 1, color);
        graphics.fill(this.x, this.y + this.height - 1, this.x + this.width, this.y + this.height, color);
        graphics.fill(this.x, this.y, this.x + 1, this.y + this.height, color);
        graphics.fill(this.x + this.width - 1, this.y, this.x + this.width, this.y + this.height, color);
    }

    private void drawCheckmark(GuiGraphics graphics, int color) {
        int centerX = this.x + this.width / 2;
        int centerY = this.y + this.height / 2;
        graphics.fill(centerX - 6, centerY, centerX - 3, centerY + 2, color);
        graphics.fill(centerX - 4, centerY + 2, centerX - 1, centerY + 4, color);
        graphics.fill(centerX - 2, centerY, centerX + 1, centerY + 2, color);
        graphics.fill(centerX, centerY - 2, centerX + 3, centerY, color);
        graphics.fill(centerX + 2, centerY - 4, centerX + 5, centerY - 2, color);
    }

    private void drawCross(GuiGraphics graphics, int color) {
        int centerX = this.x + this.width / 2;
        int centerY = this.y + this.height / 2;
        graphics.fill(centerX - 5, centerY - 5, centerX - 3, centerY - 3, color);
        graphics.fill(centerX + 3, centerY - 5, centerX + 5, centerY - 3, color);
        graphics.fill(centerX - 3, centerY - 3, centerX - 1, centerY - 1, color);
        graphics.fill(centerX + 1, centerY - 3, centerX + 3, centerY - 1, color);
        graphics.fill(centerX - 1, centerY - 1, centerX + 1, centerY + 1, color);
        graphics.fill(centerX - 3, centerY + 1, centerX - 1, centerY + 3, color);
        graphics.fill(centerX + 1, centerY + 1, centerX + 3, centerY + 3, color);
        graphics.fill(centerX - 5, centerY + 3, centerX - 3, centerY + 5, color);
        graphics.fill(centerX + 3, centerY + 3, centerX + 5, centerY + 5, color);
    }

    private boolean inside(double mouseX, double mouseY) {
        return mouseX >= this.x && mouseX < this.x + this.width && mouseY >= this.y && mouseY < this.y + this.height;
    }

    private enum State {
        READY,
        SUCCESS,
        FAILURE
    }
}
