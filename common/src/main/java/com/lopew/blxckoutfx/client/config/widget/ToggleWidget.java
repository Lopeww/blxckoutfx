package com.lopew.blxckoutfx.client.config.widget;

import com.lopew.blxckoutfx.client.config.BlxckoutFXConfigScreen;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public final class ToggleWidget implements ControlWidget {
    private static final int WIDTH = 26;
    private static final int HEIGHT = 12;

    private final BooleanSupplier getter;
    private final Consumer<Boolean> setter;
    private int x;
    private int y;

    public ToggleWidget(BooleanSupplier getter, Consumer<Boolean> setter) {
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    public int getWidth() {
        return WIDTH;
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }

    @Override
    public void setBounds(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void render(GuiGraphics graphics, Font font, int mouseX, int mouseY) {
        boolean value = this.getter.getAsBoolean();
        boolean hover = inside(mouseX, mouseY);
        int bg = value ? (hover ? 0xFF7A7A7A : 0xFF666666) : (hover ? 0xFF1F1F1F : 0xFF111111);
        int innerLine = value ? 0xFF909090 : 0xFF2A2A2A;
        int knobColor = value ? 0xFF0D0D0D : 0xFFE8E8E8;
        graphics.fill(this.x, this.y, this.x + WIDTH, this.y + HEIGHT, bg);
        graphics.fill(this.x, this.y, this.x + WIDTH, this.y + 1, BlxckoutFXConfigScreen.borderColor());
        graphics.fill(this.x, this.y + HEIGHT - 1, this.x + WIDTH, this.y + HEIGHT, BlxckoutFXConfigScreen.borderColor());
        graphics.fill(this.x, this.y, this.x + 1, this.y + HEIGHT, BlxckoutFXConfigScreen.borderColor());
        graphics.fill(this.x + WIDTH - 1, this.y, this.x + WIDTH, this.y + HEIGHT, BlxckoutFXConfigScreen.borderColor());
        graphics.fill(this.x + 2, this.y + HEIGHT / 2, this.x + WIDTH - 2, this.y + HEIGHT / 2 + 1, innerLine);

        int knobX = value ? this.x + WIDTH - 9 : this.x + 2;
        graphics.fill(knobX, this.y + 2, knobX + 7, this.y + HEIGHT - 2, knobColor);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0 || !inside(mouseX, mouseY)) {
            return false;
        }

        this.setter.accept(!this.getter.getAsBoolean());
        return true;
    }

    private boolean inside(double mouseX, double mouseY) {
        return mouseX >= this.x && mouseX < this.x + WIDTH && mouseY >= this.y && mouseY < this.y + HEIGHT;
    }
}
