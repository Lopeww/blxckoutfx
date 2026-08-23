package com.lopew.blxckoutfx.mixin.patchouli;

import com.lopew.blxckoutfx.client.gui.BlxckoutFXGUIHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "vazkii.patchouli.client.book.gui.GuiBook", remap = false)
public class PatchouliBookScreenMixin {
    @Unique
    private int blxckoutfx$rawMouseX;
    @Unique
    private int blxckoutfx$rawMouseY;

    @Inject(method = "render", at = @At("HEAD"), require = 0)
    private void blxckoutfx$captureUnscaledMousePosition(
            GuiGraphics graphics,
            int mouseX,
            int mouseY,
            float partialTick,
            CallbackInfo ci
    ) {
        this.blxckoutfx$rawMouseX = mouseX;
        this.blxckoutfx$rawMouseY = mouseY;
    }

    @Inject(method = "render", at = @At("RETURN"), require = 0)
    private void blxckoutfx$renderInventoryButtonLast(
            GuiGraphics graphics,
            int mouseX,
            int mouseY,
            float partialTick,
            CallbackInfo ci
    ) {
        BlxckoutFXGUIHandler.renderPatchouliInventoryButton(
                (Screen) (Object) this,
                graphics,
                this.blxckoutfx$rawMouseX,
                this.blxckoutfx$rawMouseY,
                partialTick
        );
    }
}
