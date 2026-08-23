package com.lopew.blxckoutfx.mixin.core;

import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ImageButton.class)
public class InventoryRecipeBookButtonMixin {
    @Unique
    private static final String BLXCKOUTFX_RECIPE_BUTTON_TEXTURE = "textures/gui/recipe_button.png";
    @Unique
    private static final String[] BLXCKOUTFX_RECIPE_BOOK_ICON_MASK = {
            "....................", "....................", "..........###.......",
            "........######......", "......#########.....", "....############....",
            "..###############...", "..###############...", "..###############...",
            "..###############...", "..###############...", "....############....",
            ".....#########......", "......######........", ".......###..........",
            "....................", "....................", "...................."
    };

    @Shadow @Final protected ResourceLocation resourceLocation;
    @Shadow @Final protected int xTexStart;
    @Shadow @Final protected int yTexStart;
    @Shadow @Final protected int yDiffTex;
    @Shadow @Final protected int textureWidth;
    @Shadow @Final protected int textureHeight;

    @Inject(method = "renderWidget", at = @At("RETURN"))
    private void blxckoutfx$redrawRecipeBookIcon(
            GuiGraphics graphics,
            int mouseX,
            int mouseY,
            float partialTick,
            CallbackInfo ci
    ) {
        ImageButton button = (ImageButton) (Object) this;
        if (!blxckoutfx$isInventoryRecipeBookButton(button)) {
            return;
        }

        int sourceY = this.yTexStart + (button.isHoveredOrFocused() ? this.yDiffTex : 0);
        BlxckoutFXRenderContext.suppressTextureDarkening();
        try {
            blxckoutfx$redrawMaskedIcon(graphics, button.getX(), button.getY(), this.xTexStart, sourceY);
        } finally {
            BlxckoutFXRenderContext.allowTextureDarkening();
        }
    }

    @Unique
    private boolean blxckoutfx$isInventoryRecipeBookButton(ImageButton button) {
        return Minecraft.getInstance().screen instanceof InventoryScreen
                && button.getWidth() == 20
                && button.getHeight() == 18
                && this.xTexStart == 0
                && this.yTexStart == 0
                && this.yDiffTex == 19
                && this.textureWidth == 256
                && this.textureHeight == 256
                && "minecraft".equals(this.resourceLocation.getNamespace())
                && BLXCKOUTFX_RECIPE_BUTTON_TEXTURE.equals(this.resourceLocation.getPath());
    }

    @Unique
    private void blxckoutfx$redrawMaskedIcon(GuiGraphics graphics, int x, int y, int sourceX, int sourceY) {
        for (int row = 0; row < BLXCKOUTFX_RECIPE_BOOK_ICON_MASK.length; row++) {
            String maskRow = BLXCKOUTFX_RECIPE_BOOK_ICON_MASK[row];
            int runStart = maskRow.indexOf('#');
            while (runStart >= 0) {
                int runEnd = maskRow.indexOf('.', runStart);
                if (runEnd < 0) {
                    runEnd = maskRow.length();
                }

                graphics.blit(this.resourceLocation, x + runStart,
                        y + row,
                        sourceX + runStart,
                        sourceY + row,
                        runEnd - runStart,
                        1
                );
                runStart = maskRow.indexOf('#', runEnd);
            }
        }
    }
}
