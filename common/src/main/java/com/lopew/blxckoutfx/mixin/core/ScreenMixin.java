package com.lopew.blxckoutfx.mixin.core;

import com.lopew.blxckoutfx.client.integration.ae2.BlxckoutFXAe2Support;
import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public class ScreenMixin {
    @Unique
    private boolean blxckoutfx$enteredAe2ScreenRender;

    @Inject(method = "render", at = @At("HEAD"))
    private void blxckoutfx$enterAe2ScreenRender(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        Screen self = (Screen) (Object) this;
        if (!BlxckoutFXAe2Support.isAe2Screen(self)) {
            return;
        }

        blxckoutfx$enteredAe2ScreenRender = true;
        BlxckoutFXRenderContext.enterAe2ScreenRender();
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void blxckoutfx$exitAe2ScreenRender(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (!blxckoutfx$enteredAe2ScreenRender) {
            return;
        }

        blxckoutfx$enteredAe2ScreenRender = false;
        BlxckoutFXRenderContext.exitAe2ScreenRender();
    }
}
