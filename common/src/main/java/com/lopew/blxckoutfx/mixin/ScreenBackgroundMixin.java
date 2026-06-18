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
public class ScreenBackgroundMixin {
    @Inject(method = "extractBackground", at = @At("HEAD"), require = 0)
    private void blxckoutfx$blurContainerBackground(GuiGraphicsExtractor graphics,
                                                     int mouseX,
                                                     int mouseY,
                                                     float partialTick,
                                                     CallbackInfo ci) {
        Screen screen = (Screen) (Object) this;
        boolean apply = BlxckoutFXShaders.isEnabled()
                && BlxckoutFXShaders.shouldApplyToScreen(screen)
                && isSupportedScreen(screen);

        if (apply) {
            graphics.blurBeforeThisStratum();
        }
    }

    private static boolean isSupportedScreen(Screen screen) {
        if (screen instanceof AbstractContainerScreen<?>) {
            return true;
        }

        return screen.getClass().getName().startsWith("mezz.jei");
    }
}
