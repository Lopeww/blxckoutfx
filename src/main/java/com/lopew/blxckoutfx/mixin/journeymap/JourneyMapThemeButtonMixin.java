package com.lopew.blxckoutfx.mixin.journeymap;

import com.lopew.blxckoutfx.client.BlxckoutFXRenderContext;
import com.lopew.blxckoutfx.client.integration.journeymap.BlxckoutFXJourneyMapThemeSupport;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "journeymap.client.ui.theme.ThemeButton", remap = false)
public class JourneyMapThemeButtonMixin {
    @Inject(method = "drawNativeButton", at = @At("HEAD"), require = 0, remap = false)
    private void blxckoutfx$enterPuristButtonDarkening(Minecraft minecraft, int mouseX, int mouseY, CallbackInfo ci) {
        if (BlxckoutFXJourneyMapThemeSupport.isPuristTheme(this)) {
            BlxckoutFXRenderContext.enterButtonRender();
        }
    }

    @Inject(method = "drawNativeButton", at = @At("RETURN"), require = 0, remap = false)
    private void blxckoutfx$exitPuristButtonDarkening(Minecraft minecraft, int mouseX, int mouseY, CallbackInfo ci) {
        if (BlxckoutFXJourneyMapThemeSupport.isPuristTheme(this)) {
            BlxckoutFXRenderContext.exitButtonRender();
        }
    }
}
