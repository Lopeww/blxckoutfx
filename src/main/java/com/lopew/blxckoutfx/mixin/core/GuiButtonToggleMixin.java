package com.lopew.blxckoutfx.mixin.core;

import com.lopew.blxckoutfx.client.BlxckoutFXRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButtonToggle;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiButtonToggle.class)
public class GuiButtonToggleMixin {
    @Shadow protected ResourceLocation resourceLocation;
    @Shadow protected int xTexStart;
    @Shadow protected int yTexStart;

    @Inject(method = "drawButton", at = @At("HEAD"), require = 1)
    private void blxckoutfx$enterToggleButtonRender(Minecraft mc, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        BlxckoutFXRenderContext.enterButtonRender();
        BlxckoutFXRenderContext.enterIconColorPreserve();
        if (blxckoutfx$isRecipeBookToggleIconButton()) {
            BlxckoutFXRenderContext.enterRecipeBookToggleIconRemap();
        }
    }

    @Inject(method = "drawButton", at = @At("RETURN"), require = 1)
    private void blxckoutfx$exitToggleButtonRender(Minecraft mc, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        if (blxckoutfx$isRecipeBookToggleIconButton()) {
            BlxckoutFXRenderContext.exitRecipeBookToggleIconRemap();
        }
        BlxckoutFXRenderContext.exitIconColorPreserve();
        BlxckoutFXRenderContext.exitButtonRender();
    }

    private boolean blxckoutfx$isRecipeBookToggleIconButton() {
        if (this.resourceLocation == null) {
            return false;
        }

        return "textures/gui/recipe_book.png".equals(this.resourceLocation.getResourcePath())
                && this.xTexStart == 152
                && this.yTexStart == 41;
    }
}
