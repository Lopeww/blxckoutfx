package com.lopew.blxckoutfx.mixin.core;

import com.lopew.blxckoutfx.client.bridge.ImageButtonState;
import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
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

    @Inject(method = "renderWidget", at = @At("RETURN"))
    private void blxckoutfx$redrawRecipeBookIcon(
            GuiGraphics graphics,
            int mouseX,
            int mouseY,
            float partialTick,
            CallbackInfo ci
    ) {
        ImageButton button = (ImageButton) (Object) this;
        ImageButtonState state = (ImageButtonState) this;
        if (!blxckoutfx$isInventoryRecipeBookButton(button, state)) {
            return;
        }

        int sourceY = state.blxckoutfx$getYTexStart()
                + (button.isHoveredOrFocused() ? state.blxckoutfx$getYDiffTex() : 0);
        BlxckoutFXRenderContext.suppressTextureDarkening();
        try {
            blxckoutfx$redrawMaskedIcon(
                    graphics,
                    state.blxckoutfx$getResourceLocation(),
                    button.getX(),
                    button.getY(),
                    state.blxckoutfx$getXTexStart(),
                    sourceY
            );
        } finally {
            BlxckoutFXRenderContext.allowTextureDarkening();
        }
    }

    @Unique
    private boolean blxckoutfx$isInventoryRecipeBookButton(ImageButton button, ImageButtonState state) {
        ResourceLocation resourceLocation = state.blxckoutfx$getResourceLocation();
        return Minecraft.getInstance().screen instanceof InventoryScreen
                && button.getWidth() == 20
                && button.getHeight() == 18
                && state.blxckoutfx$getXTexStart() == 0
                && state.blxckoutfx$getYTexStart() == 0
                && state.blxckoutfx$getYDiffTex() == 19
                && state.blxckoutfx$getTextureWidth() == 256
                && state.blxckoutfx$getTextureHeight() == 256
                && resourceLocation != null
                && "minecraft".equals(resourceLocation.getNamespace())
                && BLXCKOUTFX_RECIPE_BUTTON_TEXTURE.equals(resourceLocation.getPath());
    }

    @Unique
    private void blxckoutfx$redrawMaskedIcon(
            GuiGraphics graphics,
            ResourceLocation resourceLocation,
            int x,
            int y,
            int sourceX,
            int sourceY
    ) {
        for (int row = 0; row < BLXCKOUTFX_RECIPE_BOOK_ICON_MASK.length; row++) {
            String maskRow = BLXCKOUTFX_RECIPE_BOOK_ICON_MASK[row];
            int runStart = maskRow.indexOf('#');
            while (runStart >= 0) {
                int runEnd = maskRow.indexOf('.', runStart);
                if (runEnd < 0) {
                    runEnd = maskRow.length();
                }

                graphics.blit(resourceLocation, x + runStart,
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
