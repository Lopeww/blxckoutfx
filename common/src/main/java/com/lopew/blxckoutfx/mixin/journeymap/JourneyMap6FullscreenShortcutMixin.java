package com.lopew.blxckoutfx.mixin.journeymap;

import com.lopew.blxckoutfx.client.integration.journeymap.BlxckoutFXJourneyMapFullscreenShortcut;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "journeymap.client.ui.fullscreen.Fullscreen", remap = false)
public class JourneyMap6FullscreenShortcutMixin {
    @Inject(method = "layoutButtons(Lnet/minecraft/client/gui/GuiGraphics;)V", at = @At("RETURN"), require = 1)
    private void blxckoutfx$layoutFullscreenShortcut(CallbackInfo ci) {
        BlxckoutFXJourneyMapFullscreenShortcut.layoutShortcut((Screen) (Object) this);
    }
}
