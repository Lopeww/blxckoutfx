package com.lopew.blxckoutfx.mixin.core;

import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.StateSwitchingButton;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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

    @Shadow protected ResourceLocation resourceLocation;
    @Shadow protected boolean isStateTriggered;
    @Shadow protected int xTexStart;
    @Shadow protected int yTexStart;
    @Shadow protected int xDiffTex;
    @Shadow protected int yDiffTex;

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

        int sourceX = this.xTexStart + (this.isStateTriggered ? this.xDiffTex : 0);
        int sourceY = this.yTexStart + (button.isHoveredOrFocused() ? this.yDiffTex : 0);
        BlxckoutFXRenderContext.suppressTextureDarkening();
        try {
            blxckoutfx$redrawMask(graphics, button.getX(), button.getY(), sourceX, sourceY,
                    this.isStateTriggered ? BLXCKOUTFX_FILTER_CHECK_MASK : BLXCKOUTFX_FILTER_X_MASK);
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
                && this.xTexStart == 152
                && this.yTexStart == 41
                && this.xDiffTex == 28
                && this.yDiffTex == 18
                && this.resourceLocation != null
                && "minecraft".equals(this.resourceLocation.getNamespace())
                && BLXCKOUTFX_RECIPE_BOOK_TEXTURE.equals(this.resourceLocation.getPath());
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

                graphics.blit(this.resourceLocation, x + runStart, y + row, sourceX + runStart, sourceY + row, runEnd - runStart, 1);
                runStart = maskRow.indexOf('#', runEnd);
            }
        }
    }
}
