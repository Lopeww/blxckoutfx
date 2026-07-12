package com.lopew.blxckoutfx.mixin.pneumaticcraft;

import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractWidget.class)
public class PneumaticCraftWidgetRenderContextMixin {
    @Unique
    private boolean blxckoutfx$enteredPneumaticCraftWidgetRender;

    @Inject(method = "render", at = @At("HEAD"))
    private void blxckoutfx$enterPneumaticCraftWidgetRender(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (blxckoutfx$isPneumaticCraftWidgetRender()) {
            blxckoutfx$enteredPneumaticCraftWidgetRender = true;
            BlxckoutFXRenderContext.enterPneumaticCraftWidgetRender();
        }
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void blxckoutfx$exitPneumaticCraftWidgetRender(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (blxckoutfx$enteredPneumaticCraftWidgetRender) {
            blxckoutfx$enteredPneumaticCraftWidgetRender = false;
            BlxckoutFXRenderContext.exitPneumaticCraftWidgetRender();
        }
    }

    @Unique
    private boolean blxckoutfx$isPneumaticCraftWidgetRender() {
        Screen screen = Minecraft.getInstance().screen;
        return screen != null
                && screen.getClass().getName().startsWith("me.desht.pneumaticcraft.client.gui.")
                && ((Object) this).getClass().getName().startsWith("me.desht.pneumaticcraft.client.gui.widget.");
    }
}
