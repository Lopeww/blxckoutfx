package com.lopew.blxckoutfx.client.config.widget;

import com.lopew.blxckoutfx.client.config.BlxckoutFXConfigScreen;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;

import java.util.function.IntSupplier;

public final class PresetPreviewWidget implements ControlWidget {
    private static final int WIDTH = 320;
    private static final int HEIGHT = 138;
    private static final int TAB_WIDTH = 60;
    private static final int TAB_GAP = 4;
    private static final int TAB_HEIGHT = 16;

    private final IntSupplier presetIndexGetter;
    private int x;
    private int y;
    private PreviewTab selectedTab = PreviewTab.GUI;

    public PresetPreviewWidget(IntSupplier presetIndexGetter) {
        this.presetIndexGetter = presetIndexGetter;
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
    public int getPreferredWidth(Font font, int maxWidth) {
        return Math.min(WIDTH, Math.max(0, maxWidth));
    }

    @Override
    public int getPreferredHeight(Font font, int width) {
        return HEIGHT;
    }

    @Override
    public void render(GuiGraphicsExtractor graphics, Font font, int mouseX, int mouseY) {
        PreviewPalette palette = PreviewPalette.forPreset(this.presetIndexGetter.getAsInt());
        int panelX = this.x;
        int panelY = this.y + TAB_HEIGHT - 1;
        int panelW = WIDTH;
        int panelH = HEIGHT - TAB_HEIGHT + 1;

        for (PreviewTab tab : PreviewTab.values()) {
            int tabX = tab == PreviewTab.GUI ? this.x : this.x + TAB_WIDTH + TAB_GAP;
            int tabW = TAB_WIDTH;
            int tabY = this.y;
            boolean selected = tab == this.selectedTab;
            boolean hover = inside(mouseX, mouseY, tabX, tabY, tabW, TAB_HEIGHT);
            int bg = selected ? 0xFF161616 : hover ? 0xFF131313 : 0xFF101010;
            graphics.fill(tabX, tabY, tabX + tabW, tabY + TAB_HEIGHT, bg);
            drawBorder(graphics, tabX, tabY, tabW, TAB_HEIGHT, selected ? 0xFF585858 : BlxckoutFXConfigScreen.borderColor());
            if (!selected) {
                graphics.fill(tabX + 1, tabY + TAB_HEIGHT - 1, tabX + tabW - 1, tabY + TAB_HEIGHT, 0xFF101010);
            }
            Component label = ConfigTextLayout.ellipsize(font, tab.label, tabW - 6);
            graphics.centeredText(font, label, tabX + tabW / 2, ConfigTextLayout.controlTextY(font, tabY, TAB_HEIGHT), selected ? 0xFFF1F1F1 : 0xFFB7B7B7);
        }

        graphics.fill(panelX, panelY, panelX + panelW, panelY + panelH, palette.surface);
        drawBorder(graphics, panelX, panelY, panelW, panelH, BlxckoutFXConfigScreen.borderColor());

        if (this.selectedTab == PreviewTab.GUI) {
            renderGuiPreview(graphics, font, palette, panelX + 12, panelY + 12);
        } else {
            renderButtonPreview(graphics, font, palette, panelX + 12, panelY + 12);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) {
            return false;
        }

        for (PreviewTab tab : PreviewTab.values()) {
            int tabX = tab == PreviewTab.GUI ? this.x : this.x + TAB_WIDTH + TAB_GAP;
            int tabW = TAB_WIDTH;
            if (inside(mouseX, mouseY, tabX, this.y, tabW, TAB_HEIGHT)) {
                this.selectedTab = tab;
                return true;
            }
        }

        return inside(mouseX, mouseY, this.x, this.y, WIDTH, HEIGHT);
    }

    private void renderGuiPreview(GuiGraphicsExtractor graphics, Font font, PreviewPalette palette, int x, int y) {
        graphics.text(font, Component.translatable("ui.blxckoutfx.config.preview.gui.container"), x, y, 0xFFD7D7D7);
        int frameX = x;
        int frameY = y + 12;
        graphics.fill(frameX, frameY, frameX + 134, frameY + 76, palette.panel);
        drawBorder(graphics, frameX, frameY, 134, 76, palette.border);
        graphics.fill(frameX + 8, frameY + 16, frameX + 126, frameY + 24, palette.inset);
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 4; col++) {
                int slotX = frameX + 10 + col * 22;
                int slotY = frameY + 32 + row * 22;
                graphics.fill(slotX, slotY, slotX + 18, slotY + 18, palette.slot);
                drawBorder(graphics, slotX, slotY, 18, 18, palette.slotBorder);
            }
        }

        int infoX = frameX + 146;
        graphics.text(font, palette.label, infoX, frameY + 8, 0xFFE4E4E4);
        graphics.textWithWordWrap(font, palette.guiHeadline, infoX, frameY + 22, 146, 0xFFA9A9A9);
        graphics.textWithWordWrap(font, palette.guiText, infoX, frameY + 42, 146, 0xFF9A9A9A);
    }

    private void renderButtonPreview(GuiGraphicsExtractor graphics, Font font, PreviewPalette palette, int x, int y) {
        graphics.text(font, Component.translatable("ui.blxckoutfx.config.preview.buttons.container"), x, y, 0xFFD7D7D7);
        int buttonY = y + 20;
        renderButtonSample(graphics, font, x, buttonY, 78, 16, Component.translatable("ui.blxckoutfx.config.preview.buttons.apply"), palette.buttonPrimary, palette.border);
        renderButtonSample(graphics, font, x, buttonY + 24, 78, 16, Component.translatable("ui.blxckoutfx.config.preview.buttons.cancel"), palette.buttonSecondary, palette.border);
        renderButtonSample(graphics, font, x, buttonY + 48, 92, 16, Component.translatable("ui.blxckoutfx.config.preview.buttons.inventory"), palette.buttonPrimary, palette.border);

        int infoX = x + 126;
        graphics.text(font, palette.label, infoX, buttonY + 4, 0xFFE4E4E4);
        graphics.textWithWordWrap(font, palette.buttonHeadline, infoX, buttonY + 18, 152, 0xFFA9A9A9);
        graphics.textWithWordWrap(font, palette.buttonText, infoX, buttonY + 38, 152, 0xFF9A9A9A);
    }

    private void renderButtonSample(GuiGraphicsExtractor graphics, Font font, int x, int y, int width, int height, Component label, int fill, int border) {
        graphics.fill(x, y, x + width, y + height, fill);
        drawBorder(graphics, x, y, width, height, border);
        Component clipped = ConfigTextLayout.ellipsize(font, label, width - 8);
        graphics.centeredText(font, clipped, x + width / 2, ConfigTextLayout.controlTextY(font, y, height), 0xFFE9E9E9);
    }

    private static void drawBorder(GuiGraphicsExtractor graphics, int x, int y, int width, int height, int color) {
        graphics.fill(x, y, x + width, y + 1, color);
        graphics.fill(x, y + height - 1, x + width, y + height, color);
        graphics.fill(x, y, x + 1, y + height, color);
        graphics.fill(x + width - 1, y, x + width, y + height, color);
    }

    private static boolean inside(double mouseX, double mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }

    private enum PreviewTab {
        GUI(Component.translatable("ui.blxckoutfx.config.preview.tab.gui")),
        BUTTONS(Component.translatable("ui.blxckoutfx.config.preview.tab.buttons"));

        private final Component label;

        PreviewTab(Component label) {
            this.label = label;
        }
    }

    private record PreviewPalette(
            Component label,
            Component guiHeadline,
            Component guiText,
            Component buttonHeadline,
            Component buttonText,
            int surface,
            int panel,
            int inset,
            int slot,
            int slotBorder,
            int buttonPrimary,
            int buttonSecondary,
            int border
    ) {
        private static PreviewPalette forPreset(int presetIndex) {
            float factor = switch (Math.max(0, Math.min(presetIndex, 4))) {
                case 0 -> 1.0F;
                case 1 -> 2.0F;
                case 2 -> 3.2F;
                case 3 -> 4.3F;
                default -> 5.5F;
            };
            float normalized = Math.max(0.0F, Math.min((factor - 2.0F) / 3.5F, 1.0F));
            int surface = shade(0x121212, normalized, 0.62F);
            int panel = shade(0x2A2A2A, normalized, 0.72F);
            int inset = shade(0x313131, normalized, 0.75F);
            int slot = shade(0x383838, normalized, 0.78F);
            int slotBorder = shade(0x4B4B4B, normalized, 0.55F);
            int buttonPrimary = shade(0x3A3A3A, normalized, 0.70F);
            int buttonSecondary = shade(0x272727, normalized, 0.66F);
            int border = shade(0x4B4B4B, normalized, 0.35F);

            return switch (Math.max(0, Math.min(presetIndex, 4))) {
                case 0 -> new PreviewPalette(
                        Component.translatable("ui.blxckoutfx.config.preview.preset.off.label"),
                        Component.translatable("ui.blxckoutfx.config.preview.preset.off.headline"),
                        Component.translatable("ui.blxckoutfx.config.preview.preset.off.text"),
                        Component.translatable("ui.blxckoutfx.config.preview.preset.off.button_headline"),
                        Component.translatable("ui.blxckoutfx.config.preview.preset.off.button_text"),
                        surface, panel, inset, slot, slotBorder, buttonPrimary, buttonSecondary, border
                );
                case 1 -> new PreviewPalette(
                        Component.translatable("ui.blxckoutfx.config.preview.preset.soft.label"),
                        Component.translatable("ui.blxckoutfx.config.preview.preset.soft.headline"),
                        Component.translatable("ui.blxckoutfx.config.preview.preset.soft.text"),
                        Component.translatable("ui.blxckoutfx.config.preview.preset.soft.button_headline"),
                        Component.translatable("ui.blxckoutfx.config.preview.preset.soft.button_text"),
                        surface, panel, inset, slot, slotBorder, buttonPrimary, buttonSecondary, border
                );
                case 2 -> new PreviewPalette(
                        Component.translatable("ui.blxckoutfx.config.preview.preset.balanced.label"),
                        Component.translatable("ui.blxckoutfx.config.preview.preset.balanced.headline"),
                        Component.translatable("ui.blxckoutfx.config.preview.preset.balanced.text"),
                        Component.translatable("ui.blxckoutfx.config.preview.preset.balanced.button_headline"),
                        Component.translatable("ui.blxckoutfx.config.preview.preset.balanced.button_text"),
                        surface, panel, inset, slot, slotBorder, buttonPrimary, buttonSecondary, border
                );
                case 3 -> new PreviewPalette(
                        Component.translatable("ui.blxckoutfx.config.preview.preset.dark.label"),
                        Component.translatable("ui.blxckoutfx.config.preview.preset.dark.headline"),
                        Component.translatable("ui.blxckoutfx.config.preview.preset.dark.text"),
                        Component.translatable("ui.blxckoutfx.config.preview.preset.dark.button_headline"),
                        Component.translatable("ui.blxckoutfx.config.preview.preset.dark.button_text"),
                        surface, panel, inset, slot, slotBorder, buttonPrimary, buttonSecondary, border
                );
                default -> new PreviewPalette(
                        Component.translatable("ui.blxckoutfx.config.preview.preset.blxckout.label"),
                        Component.translatable("ui.blxckoutfx.config.preview.preset.blxckout.headline"),
                        Component.translatable("ui.blxckoutfx.config.preview.preset.blxckout.text"),
                        Component.translatable("ui.blxckoutfx.config.preview.preset.blxckout.button_headline"),
                        Component.translatable("ui.blxckoutfx.config.preview.preset.blxckout.button_text"),
                        surface, panel, inset, slot, slotBorder, buttonPrimary, buttonSecondary, border
                );
            };
        }

        private static int shade(int baseGray, float normalized, float strength) {
            int channel = baseGray & 0xFF;
            int value = Math.max(5, Math.min(255, Math.round(channel * (1.0F - normalized * strength))));
            return 0xFF000000 | (value << 16) | (value << 8) | value;
        }
    }
}
