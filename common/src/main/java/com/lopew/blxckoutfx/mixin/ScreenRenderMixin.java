package com.lopew.blxckoutfx.mixin;

import com.lopew.blxckoutfx.client.BlxckoutFXShaders;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public class ScreenRenderMixin {
    private static boolean appliedOverlay = false;

    @Inject(
            method = "extractRenderState",
            at = @At("TAIL")
    )
    private void blxckoutfx$drawDarkeningOverlayAtTail(GuiGraphicsExtractor guiGraphics, int pMouseX, int pMouseY, float pPartialTick, CallbackInfo ci) {
        if (!BlxckoutFXShaders.isEnabled() || appliedOverlay) {
            return;
        }

        Screen screen = (Screen) (Object) this;
        appliedOverlay = true;

        try {
            int colorOverlay = calculateOverlayColor();

            if (screen instanceof AbstractContainerScreen) {
                // For container screens (inventory, chest, etc.), darken only the panel area
                AbstractContainerScreen<?> containerScreen = (AbstractContainerScreen<?>) screen;
                int leftPos = (int) containerScreen.getClass().getField("leftPos").get(containerScreen);
                int topPos = (int) containerScreen.getClass().getField("topPos").get(containerScreen);
                int imageWidth = (int) containerScreen.getClass().getField("imageWidth").get(containerScreen);
                int imageHeight = (int) containerScreen.getClass().getField("imageHeight").get(containerScreen);

                var fillMethod = guiGraphics.getClass().getMethod("fill", int.class, int.class, int.class, int.class, int.class);
                fillMethod.invoke(guiGraphics, leftPos, topPos, leftPos + imageWidth, topPos + imageHeight, colorOverlay);
            } else {
                // For other screens (menus, settings, etc.), darken the entire screen
                var fillMethod = guiGraphics.getClass().getMethod("fill", int.class, int.class, int.class, int.class, int.class);
                fillMethod.invoke(guiGraphics, 0, 0, screen.width, screen.height, colorOverlay);
            }
        } catch (Exception e) {
            // Silently fail to avoid breaking the game
        } finally {
            appliedOverlay = false;
        }
    }

    private static int calculateOverlayColor() {
        if (BlxckoutFXShaders.isBalancedPresetActive()) {
            return 0x99000000; // 60% opacity black
        } else if (BlxckoutFXShaders.isDarkPresetActive()) {
            return 0xBB000000; // 73% opacity black
        } else if (BlxckoutFXShaders.isBlxckoutPresetActive()) {
            return 0xDD000000; // 86% opacity black
        }
        return 0x55000000; // 33% opacity black for soft
    }
}

