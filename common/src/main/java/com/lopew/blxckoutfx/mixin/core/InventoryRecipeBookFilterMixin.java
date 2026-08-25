package com.lopew.blxckoutfx.mixin.core;

import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.StateSwitchingButton;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StateSwitchingButton.class)
public class InventoryRecipeBookFilterMixin {
    @Unique
    private static final String BLXCKOUTFX_RECIPE_BOOK_TEXTURE = "textures/gui/recipe_book.png";
    @Unique
    private static final String[] BLXCKOUTFX_FILTER_CHECK_MASK = {
            "..........................", "..........................", "..........................",
            "..........................", "..........................", ".........#................",
            "........##................", "...#...##.................", "...##.##.................",
            "....###...................", ".....#....................", "..........................",
            "..........................", "..........................", "..........................", ".........................."
    };
    @Unique
    private static final String[] BLXCKOUTFX_FILTER_X_MASK = {
            "..........................", "..........................", "..........................",
            "..........................", "..........................", "..##....##................",
            "...##..##.................", "....####...................", ".....##...................",
            "....####..................", "...##..##.................", "..##....##................",
            "..........................", "..........................", "..........................", ".........................."
    };
    @Unique
    private static final String[] BLXCKOUTFX_FILTER_ITEM_MASK = {
            "..........................", "..........................", ".................##.......",
            "...............######.....", ".............##########...", ".............###########..",
            ".............###########..", ".............###########..", ".............###########..",
            ".............###########..", ".............###########..", ".............##########...",
            "...............######.....", ".................##.......", "..........................", ".........................."
    };

    @Unique
    private ResourceLocation blxckoutfx$resourceLocation;
    @Unique
    private int blxckoutfx$xTexStart;
    @Unique
    private int blxckoutfx$yTexStart;
    @Unique
    private int blxckoutfx$xDiffTex;
    @Unique
    private int blxckoutfx$yDiffTex;

    @Inject(method = "initTextureValues", at = @At("RETURN"))
    private void blxckoutfx$captureTextureState(
            int xTexStart,
            int yTexStart,
            int xDiffTex,
            int yDiffTex,
            ResourceLocation resourceLocation,
            CallbackInfo ci
    ) {
        this.blxckoutfx$xTexStart = xTexStart;
        this.blxckoutfx$yTexStart = yTexStart;
        this.blxckoutfx$xDiffTex = xDiffTex;
        this.blxckoutfx$yDiffTex = yDiffTex;
        this.blxckoutfx$resourceLocation = resourceLocation;
    }

    @Inject(method = "renderWidget", at = @At("RETURN"))
    private void blxckoutfx$redrawFilterIcons(
            GuiGraphics graphics,
            int mouseX,
            int mouseY,
            float partialTick,
            CallbackInfo ci
    ) {
        StateSwitchingButton button = (StateSwitchingButton) (Object) this;
        if (!blxckoutfx$isInventoryRecipeBookFilter(button)) {
            return;
        }

        boolean stateTriggered = button.isStateTriggered();
        int sourceX = this.blxckoutfx$xTexStart + (stateTriggered ? this.blxckoutfx$xDiffTex : 0);
        int sourceY = this.blxckoutfx$yTexStart + (button.isHoveredOrFocused() ? this.blxckoutfx$yDiffTex : 0);
        BlxckoutFXRenderContext.suppressTextureDarkening();
        try {
            blxckoutfx$redrawMask(graphics, button.getX(), button.getY(), sourceX, sourceY,
                    stateTriggered ? BLXCKOUTFX_FILTER_CHECK_MASK : BLXCKOUTFX_FILTER_X_MASK);
            blxckoutfx$redrawMask(graphics, button.getX(), button.getY(), sourceX, sourceY, BLXCKOUTFX_FILTER_ITEM_MASK);
        } finally {
            BlxckoutFXRenderContext.allowTextureDarkening();
        }
    }

    @Unique
    private boolean blxckoutfx$isInventoryRecipeBookFilter(StateSwitchingButton button) {
        return Minecraft.getInstance().screen instanceof InventoryScreen
                && button.getWidth() == 26
                && button.getHeight() == 16
                && this.blxckoutfx$xTexStart == 152
                && this.blxckoutfx$yTexStart == 41
                && this.blxckoutfx$xDiffTex == 28
                && this.blxckoutfx$yDiffTex == 18
                && this.blxckoutfx$resourceLocation != null
                && "minecraft".equals(this.blxckoutfx$resourceLocation.getNamespace())
                && BLXCKOUTFX_RECIPE_BOOK_TEXTURE.equals(this.blxckoutfx$resourceLocation.getPath());
    }

    @Unique
    private void blxckoutfx$redrawMask(GuiGraphics graphics, int x, int y, int sourceX, int sourceY, String[] mask) {
        for (int row = 0; row < mask.length; row++) {
            String maskRow = mask[row];
            int runStart = maskRow.indexOf('#');
            while (runStart >= 0) {
                int runEnd = maskRow.indexOf('.', runStart);
                if (runEnd < 0) {
                    runEnd = maskRow.length();
                }

                graphics.blit(this.blxckoutfx$resourceLocation, x + runStart, y + row, sourceX + runStart, sourceY + row, runEnd - runStart, 1);
                runStart = maskRow.indexOf('#', runEnd);
            }
        }
    }
}
