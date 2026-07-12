package com.lopew.blxckoutfx.client.config.widget;

import com.lopew.blxckoutfx.client.config.BlxckoutFXConfigScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.function.IntPredicate;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

public final class DropdownWidget implements ControlWidget {
    private static final int MIN_WIDTH = 110;
    private static final int HEIGHT = 16;
    private static final int ITEM_HEIGHT = 14;
    private static final int ARROW_WIDTH = 14;
    private static final int TEXT_PADDING = 6;
    private static final int MAX_POPUP_WIDTH = 192;

    private final List<Component> options;
    private final IntSupplier selectedIndexGetter;
    private final IntConsumer selectedIndexSetter;
    private final IntPredicate optionEnabled;
    private int width = MIN_WIDTH;
    private int x;
    private int y;
    private boolean expanded;

    public DropdownWidget(List<Component> options, IntSupplier selectedIndexGetter, IntConsumer selectedIndexSetter) {
        this(options, selectedIndexGetter, selectedIndexSetter, index -> true);
    }

    public DropdownWidget(List<Component> options, IntSupplier selectedIndexGetter, IntConsumer selectedIndexSetter, IntPredicate optionEnabled) {
        this.options = options;
        this.selectedIndexGetter = selectedIndexGetter;
        this.selectedIndexSetter = selectedIndexSetter;
        this.optionEnabled = optionEnabled == null ? index -> true : optionEnabled;
    }

    @Override
    public int getWidth() {
        return this.width;
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
        boolean hover = insideMain(mouseX, mouseY);
        boolean arrowHover = insideArrow(mouseX, mouseY);
        int bg = hover || this.expanded ? 0xFF1B1B1B : 0xFF111111;
        graphics.fill(this.x, this.y, this.x + this.width, this.y + HEIGHT, bg);
        drawBorder(graphics, this.x, this.y, this.width, HEIGHT);
        graphics.fill(this.x + this.width - ARROW_WIDTH, this.y + 1, this.x + this.width - 1, this.y + HEIGHT - 1, arrowHover || this.expanded ? 0xFF202020 : 0xFF171717);
        graphics.fill(this.x + this.width - ARROW_WIDTH, this.y, this.x + this.width - ARROW_WIDTH + 1, this.y + HEIGHT, BlxckoutFXConfigScreen.borderColor());
        renderSelectedLabel(graphics, font, hover, this.expanded);
        graphics.drawString(font, Component.literal("v"), this.x + this.width - 10, this.y + 4, 0xFFB8B8B8, false);
    }

    @Override
    public void renderOverlay(GuiGraphics graphics, Font font, int mouseX, int mouseY) {
        if (!this.expanded) {
            return;
        }

        PopupBounds bounds = popupBounds();
        graphics.fill(bounds.x, bounds.y, bounds.x + bounds.width, bounds.y + bounds.height, 0xFF0E0E0E);
        drawBorder(graphics, bounds.x, bounds.y, bounds.width, bounds.height);

        for (int i = 0; i < this.options.size(); i++) {
            int optionY = bounds.y + i * ITEM_HEIGHT;
            boolean hover = insideOption(mouseX, mouseY, bounds, i);
            boolean selected = i == currentIndex();
            boolean enabled = isEnabled(i);
            if (selected) {
                graphics.fill(bounds.x + 1, optionY + 1, bounds.x + bounds.width - 1, optionY + ITEM_HEIGHT, 0xFF1C1C1C);
            } else if (hover && enabled) {
                graphics.fill(bounds.x + 1, optionY + 1, bounds.x + bounds.width - 1, optionY + ITEM_HEIGHT, 0xFF171717);
            }
            int color = selected
                    ? 0xFFF1F1F1
                    : enabled ? 0xFFCACACA : 0xFF878787;
            graphics.drawString(font, ConfigTextLayout.ellipsize(font, this.options.get(i), bounds.width - TEXT_PADDING * 2), bounds.x + TEXT_PADDING, optionY + 3, color, false);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.expanded) {
            if (button != 0) {
                return false;
            }
            PopupBounds bounds = popupBounds();
            for (int i = 0; i < this.options.size(); i++) {
                if (insideOption(mouseX, mouseY, bounds, i)) {
                    if (isEnabled(i)) {
                        this.selectedIndexSetter.accept(i);
                    }
                    this.expanded = false;
                    return true;
                }
            }
            if (!insideMain(mouseX, mouseY)) {
                this.expanded = false;
            }
        }

        if (!insideMain(mouseX, mouseY)) {
            return false;
        }

        if (insideArrow(mouseX, mouseY)) {
            if (button == 0) {
                this.expanded = !this.expanded;
                return true;
            }
            return false;
        }

        if (button == 0) {
            this.selectedIndexSetter.accept(nextIndex());
            this.expanded = false;
            return true;
        }
        if (button == 1) {
            this.selectedIndexSetter.accept(previousIndex());
            this.expanded = false;
            return true;
        }

        return false;
    }

    private boolean insideMain(double mouseX, double mouseY) {
        return mouseX >= this.x && mouseX < this.x + this.width && mouseY >= this.y && mouseY < this.y + HEIGHT;
    }

    private boolean insideOption(double mouseX, double mouseY, PopupBounds bounds, int index) {
        int optionY = bounds.y + index * ITEM_HEIGHT;
        return mouseX >= bounds.x && mouseX < bounds.x + bounds.width && mouseY >= optionY && mouseY < optionY + ITEM_HEIGHT;
    }

    private boolean insideArrow(double mouseX, double mouseY) {
        return mouseX >= this.x + this.width - ARROW_WIDTH && mouseX < this.x + this.width && mouseY >= this.y && mouseY < this.y + HEIGHT;
    }

    private int clampIndex(int index) {
        return Math.max(0, Math.min(index, this.options.size() - 1));
    }

    private int currentIndex() {
        int index = clampIndex(this.selectedIndexGetter.getAsInt());
        if (isEnabled(index)) {
            return index;
        }
        for (int offset = 1; offset < this.options.size(); offset++) {
            int next = (index + offset) % this.options.size();
            if (isEnabled(next)) {
                return next;
            }
        }
        return index;
    }

    private int nextIndex() {
        int start = currentIndex();
        for (int offset = 1; offset <= this.options.size(); offset++) {
            int next = (start + offset) % this.options.size();
            if (isEnabled(next)) {
                return next;
            }
        }
        return start;
    }

    private int previousIndex() {
        int start = currentIndex();
        for (int offset = 1; offset <= this.options.size(); offset++) {
            int next = (start - offset) % this.options.size();
            if (next < 0) {
                next += this.options.size();
            }
            if (isEnabled(next)) {
                return next;
            }
        }
        return start;
    }

    private boolean isEnabled(int index) {
        return this.optionEnabled.test(index);
    }

    private PopupBounds popupBounds() {
        int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();
        int popupWidth = Math.min(this.width, MAX_POPUP_WIDTH);
        int popupHeight = this.options.size() * ITEM_HEIGHT;
        int popupX = this.x;
        if (popupX + popupWidth > screenWidth) {
            popupX = Math.max(0, screenWidth - popupWidth);
        }
        if (popupX < 0) {
            popupX = 0;
        }

        int belowY = this.y + HEIGHT;
        int aboveY = this.y - popupHeight;
        int popupY;
        if (belowY + popupHeight <= screenHeight || aboveY < 0) {
            popupY = belowY;
            if (popupY + popupHeight > screenHeight) {
                popupY = Math.max(0, screenHeight - popupHeight);
            }
        } else {
            popupY = Math.max(0, aboveY);
        }

        return new PopupBounds(popupX, popupY, popupWidth, popupHeight);
    }

    private static void drawBorder(GuiGraphics graphics, int x, int y, int width, int height) {
        graphics.fill(x, y, x + width, y + 1, BlxckoutFXConfigScreen.borderColor());
        graphics.fill(x, y + height - 1, x + width, y + height, BlxckoutFXConfigScreen.borderColor());
        graphics.fill(x, y, x + 1, y + height, BlxckoutFXConfigScreen.borderColor());
        graphics.fill(x + width - 1, y, x + width, y + height, BlxckoutFXConfigScreen.borderColor());
    }

    private void renderSelectedLabel(GuiGraphics graphics, Font font, boolean hover, boolean open) {
        int labelX = this.x + TEXT_PADDING;
        int labelY = this.y + (HEIGHT - font.lineHeight) / 2;
        int labelWidth = Math.max(0, this.width - ARROW_WIDTH - TEXT_PADDING * 2);
        Component text = this.options.get(clampIndex(this.selectedIndexGetter.getAsInt()));
        boolean marquee = hover || open;
        ConfigTextLayout.drawClippedOrMarquee(graphics, font, text, labelX, labelY, labelWidth, isEnabled(currentIndex()) ? 0xFFE5E5E5 : 0xFF9A9A9A, marquee);
    }

    private record PopupBounds(int x, int y, int width, int height) {
    }
}
