package com.lopew.blxckoutfx.client.config.widget;

import com.lopew.blxckoutfx.client.config.BlxckoutFXConfigScreen;
import java.util.function.BooleanSupplier;
import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

/** Mirrors the reference developer reload feedback without affecting async resource actions. */
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

    @Override public int getWidth() { return this.width; }
    @Override public int getHeight() { return this.height; }
    @Override public void setBounds(int x, int y) { this.x = x; this.y = y; }

    @Override
    public void render(GuiGraphics graphics, Font font, int mouseX, int mouseY) {
        refreshState();
        boolean ready = this.state == State.READY;
        int borderColor = BlxckoutFXConfigScreen.borderColor();
        int backgroundColor = ready && inside(mouseX, mouseY) ? 0xFF1B1B1B : 0xFF111111;
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
            graphics.drawCenteredString(font, ConfigTextLayout.ellipsize(font, this.label, this.width - 8), this.x + this.width / 2, this.y + (this.height - 8) / 2, 0xFFE5E5E5);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        refreshState();
        if (button != 0 || this.state != State.READY || !inside(mouseX, mouseY)) return false;
        this.lockedUntil = Util.getMillis() + RESULT_DISPLAY_MILLIS;
        try {
            this.state = this.reloadAction.getAsBoolean() ? State.SUCCESS : State.FAILURE;
        } catch (RuntimeException exception) {
            this.state = State.FAILURE;
        }
        return true;
    }

    private void refreshState() { if (this.state != State.READY && Util.getMillis() >= this.lockedUntil) this.state = State.READY; }
    private void drawBorder(GuiGraphics g, int c) { g.fill(x, y, x + width, y + 1, c); g.fill(x, y + height - 1, x + width, y + height, c); g.fill(x, y, x + 1, y + height, c); g.fill(x + width - 1, y, x + width, y + height, c); }
    private void drawCheckmark(GuiGraphics g, int c) { int x = this.x + width / 2, y = this.y + height / 2; g.fill(x - 6, y, x - 3, y + 2, c); g.fill(x - 4, y + 2, x - 1, y + 4, c); g.fill(x - 2, y, x + 1, y + 2, c); g.fill(x, y - 2, x + 3, y, c); g.fill(x + 2, y - 4, x + 5, y - 2, c); }
    private void drawCross(GuiGraphics g, int c) { int x = this.x + width / 2, y = this.y + height / 2; g.fill(x - 5, y - 5, x - 3, y - 3, c); g.fill(x + 3, y - 5, x + 5, y - 3, c); g.fill(x - 3, y - 3, x - 1, y - 1, c); g.fill(x + 1, y - 3, x + 3, y - 1, c); g.fill(x - 1, y - 1, x + 1, y + 1, c); g.fill(x - 3, y + 1, x - 1, y + 3, c); g.fill(x + 1, y + 1, x + 3, y + 3, c); g.fill(x - 5, y + 3, x - 3, y + 5, c); g.fill(x + 3, y + 3, x + 5, y + 5, c); }
    private boolean inside(double mouseX, double mouseY) { return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height; }
    private enum State { READY, SUCCESS, FAILURE }
}
