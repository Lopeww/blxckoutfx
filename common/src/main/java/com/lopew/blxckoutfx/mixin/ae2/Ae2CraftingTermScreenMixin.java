package com.lopew.blxckoutfx.mixin.ae2;

import com.lopew.blxckoutfx.client.integration.ae2.BlxckoutFXAe2Support;
import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "appeng.client.gui.me.items.CraftingTermScreen", remap = false)
public abstract class Ae2CraftingTermScreenMixin {
    @Unique
    private boolean blxckoutfx$enteredCraftingTermRender;

    @Inject(method = "render", at = @At("HEAD"), require = 0)
    private void blxckoutfx$enterCraftingTermRender(
            GuiGraphics guiGraphics,
            int mouseX,
            int mouseY,
            float partialTick,
            CallbackInfo ci
    ) {
        Screen self = (Screen) (Object) this;
        if (!BlxckoutFXAe2Support.isAe2CraftingTermScreenLike(self)) {
            return;
        }

        blxckoutfx$enteredCraftingTermRender = true;
        BlxckoutFXRenderContext.enterAe2CraftingTermScreenRender();
    }

    @Inject(method = "render", at = @At("RETURN"), require = 0)
    private void blxckoutfx$exitCraftingTermRender(
            GuiGraphics guiGraphics,
            int mouseX,
            int mouseY,
            float partialTick,
            CallbackInfo ci
    ) {
        if (!blxckoutfx$enteredCraftingTermRender) {
            return;
        }

        blxckoutfx$enteredCraftingTermRender = false;
        BlxckoutFXRenderContext.exitAe2CraftingTermScreenRender();
    }
}

