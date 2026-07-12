package com.lopew.blxckoutfx.mixin.core;

import com.lopew.blxckoutfx.client.gui.InventoryButton;
import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractButton.class)
public class AbstractButtonMixin {
    @Unique
    private boolean blxckoutfx$inventoryButtonRender;

    @Inject(method = "renderWidget", at = @At("HEAD"))
    private void blxckoutfx$enterButtonRender(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        BlxckoutFXRenderContext.enterButtonRender();
        if ((Object) this instanceof InventoryButton) {
            blxckoutfx$inventoryButtonRender = true;
            BlxckoutFXRenderContext.enterInventoryButtonRender();
        }
    }

    @Inject(method = "renderWidget", at = @At("RETURN"))
    private void blxckoutfx$exitButtonRender(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (blxckoutfx$inventoryButtonRender) {
            blxckoutfx$inventoryButtonRender = false;
            BlxckoutFXRenderContext.exitInventoryButtonRender();
        }
        BlxckoutFXRenderContext.exitButtonRender();
    }
}

