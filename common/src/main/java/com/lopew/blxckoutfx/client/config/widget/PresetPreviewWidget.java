package com.lopew.blxckoutfx.client.config.widget;

import com.lopew.blxckoutfx.client.config.BlxckoutFXConfigScreen;
import com.lopew.blxckoutfx.client.render.BlxckoutFXShaders;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.function.IntSupplier;

public final class PresetPreviewWidget implements ControlWidget {
    private static final int WIDTH = 320;
    private static final int HEIGHT = 138;
    private static final int TAB_WIDTH = 60;
    private static final int TAB_HEIGHT = 16;
    private static final int PREVIEW_BUTTON_MIN_WIDTH = 78;
    private static final int PREVIEW_BUTTON_MAX_WIDTH = 112;

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
    public void render(GuiGraphics graphics, Font font, int mouseX, int mouseY) {
        PreviewPalette palette = PreviewPalette.forPreset(this.presetIndexGetter.getAsInt());
        int panelX = this.x;
        int panelY = this.y + TAB_HEIGHT - 1;
        int panelW = WIDTH;
        int panelH = HEIGHT - TAB_HEIGHT + 1;

        for (int i = 0; i < PreviewTab.values().length; i++) {
            PreviewTab tab = PreviewTab.values()[i];
            int tabX = this.x + i * (TAB_WIDTH + 4);
            int tabY = this.y;
            boolean selected = tab == this.selectedTab;
            boolean hover = inside(mouseX, mouseY, tabX, tabY, TAB_WIDTH, TAB_HEIGHT);
            int bg = selected ? 0xFF161616 : (hover ? 0xFF131313 : 0xFF101010);
            graphics.fill(tabX, tabY, tabX + TAB_WIDTH, tabY + TAB_HEIGHT, bg);
            drawBorder(graphics, tabX, tabY, TAB_WIDTH, TAB_HEIGHT, selected ? 0xFF585858 : BlxckoutFXConfigScreen.borderColor());
            if (!selected) {
                graphics.fill(tabX + 1, tabY + TAB_HEIGHT - 1, tabX + TAB_WIDTH - 1, tabY + TAB_HEIGHT, 0xFF101010);
            }
            graphics.drawCenteredString(font, ConfigTextLayout.ellipsize(font, tab.label, TAB_WIDTH - 6), tabX + TAB_WIDTH / 2, tabY + 4, selected ? 0xFFF1F1F1 : 0xFFB7B7B7);
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

        for (int i = 0; i < PreviewTab.values().length; i++) {
            int tabX = this.x + i * (TAB_WIDTH + 4);
            int tabY = this.y;
            if (inside(mouseX, mouseY, tabX, tabY, TAB_WIDTH, TAB_HEIGHT)) {
                this.selectedTab = PreviewTab.values()[i];
                return true;
            }
        }

        return inside(mouseX, mouseY, this.x, this.y, WIDTH, HEIGHT);
    }

    private void renderGuiPreview(GuiGraphics graphics, Font font, PreviewPalette palette, int x, int y) {
        graphics.drawString(font, Component.translatable("ui.blxckoutfx.config.preview.gui.container"), x, y, 0xFFD7D7D7, false);
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
        graphics.drawString(font, palette.label, infoX, frameY + 8, 0xFFE4E4E4, false);
        int infoWidth = 146;
        int headlineHeight = ConfigTextLayout.drawWrapped(graphics, font, palette.guiHeadline, infoX, frameY + 22, infoWidth, 0xFFA9A9A9);
        ConfigTextLayout.drawWrapped(graphics, font, palette.guiText, infoX, frameY + 22 + headlineHeight + 4, infoWidth, 0xFF9A9A9A);
    }

    private void renderButtonPreview(GuiGraphics graphics, Font font, PreviewPalette palette, int x, int y) {
        graphics.drawString(font, Component.translatable("ui.blxckoutfx.config.preview.buttons.container"), x, y, 0xFFD7D7D7, false);
        int buttonY = y + 20;
        renderButtonSample(graphics, font, x, buttonY, dynamicPreviewButtonWidth(font, Component.translatable("ui.blxckoutfx.config.preview.buttons.apply")), 16, Component.translatable("ui.blxckoutfx.config.preview.buttons.apply"), palette.buttonPrimary, palette.border);
        renderButtonSample(graphics, font, x, buttonY + 24, dynamicPreviewButtonWidth(font, Component.translatable("ui.blxckoutfx.config.preview.buttons.cancel")), 16, Component.translatable("ui.blxckoutfx.config.preview.buttons.cancel"), palette.buttonSecondary, palette.border);
        renderButtonSample(graphics, font, x, buttonY + 48, dynamicPreviewButtonWidth(font, Component.translatable("ui.blxckoutfx.config.preview.buttons.inventory")), 16, Component.translatable("ui.blxckoutfx.config.preview.buttons.inventory"), palette.buttonPrimary, palette.border);

        int infoX = x + PREVIEW_BUTTON_MAX_WIDTH + 14;
        graphics.drawString(font, palette.label, infoX, buttonY + 4, 0xFFE4E4E4, false);
        int infoWidth = WIDTH - (infoX - x) - 12;
        int headlineHeight = ConfigTextLayout.drawWrapped(graphics, font, palette.buttonHeadline, infoX, buttonY + 18, infoWidth, 0xFFA9A9A9);
        ConfigTextLayout.drawWrapped(graphics, font, palette.buttonText, infoX, buttonY + 18 + headlineHeight + 4, infoWidth, 0xFF9A9A9A);
    }

    private void renderButtonSample(GuiGraphics graphics, Font font, int x, int y, int width, int height, Component label, int fill, int border) {
        graphics.fill(x, y, x + width, y + height, fill);
        drawBorder(graphics, x, y, width, height, border);
        graphics.drawCenteredString(font, ConfigTextLayout.ellipsize(font, label, width - 8), x + width / 2, y + 4, 0xFFE9E9E9);
    }

    private static int dynamicPreviewButtonWidth(Font font, Component label) {
        return Math.min(PREVIEW_BUTTON_MAX_WIDTH, Math.max(PREVIEW_BUTTON_MIN_WIDTH, font.width(label) + 12));
    }

    private static void drawBorder(GuiGraphics graphics, int x, int y, int width, int height, int color) {
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
            float factor = BlxckoutFXShaders.getPresetDivideFactor(presetIndex);
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
