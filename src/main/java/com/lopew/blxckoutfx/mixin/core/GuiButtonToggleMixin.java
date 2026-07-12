package com.lopew.blxckoutfx.mixin.core;

import com.lopew.blxckoutfx.client.BlxckoutFXRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButtonToggle;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiButtonToggle.class)
public class GuiButtonToggleMixin {
    @Unique
    private boolean blxckoutfx$recipeBookToggleIcon;

    // Capture the texture identity from stable method arguments instead of shadowing
    // GuiButtonToggle's private fields, whose field mappings are not emitted in refmaps.
    @Inject(method = "initTextureValues(IIIILnet/minecraft/util/ResourceLocation;)V", at = @At("RETURN"), require = 1)
    private void blxckoutfx$captureRecipeBookToggleIcon(
            int xTexStart,
            int yTexStart,
            int xDiffTex,
            int yDiffTex,
            ResourceLocation resourceLocation,
            CallbackInfo ci
    ) {
        this.blxckoutfx$recipeBookToggleIcon = resourceLocation != null
                && "textures/gui/recipe_book.png".equals(resourceLocation.getResourcePath())
                && xTexStart == 152
                && yTexStart == 41;
    }

    @Inject(method = "drawButton(Lnet/minecraft/client/Minecraft;IIF)V", at = @At("HEAD"), require = 1)
    private void blxckoutfx$enterToggleButtonRender(Minecraft mc, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        BlxckoutFXRenderContext.enterButtonRender();
        BlxckoutFXRenderContext.enterIconColorPreserve();
        if (this.blxckoutfx$recipeBookToggleIcon) {
            BlxckoutFXRenderContext.enterRecipeBookToggleIconRemap();
        }
    }

    @Inject(method = "drawButton(Lnet/minecraft/client/Minecraft;IIF)V", at = @At("RETURN"), require = 1)
    private void blxckoutfx$exitToggleButtonRender(Minecraft mc, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        if (this.blxckoutfx$recipeBookToggleIcon) {
            BlxckoutFXRenderContext.exitRecipeBookToggleIconRemap();
        }
        BlxckoutFXRenderContext.exitIconColorPreserve();
        BlxckoutFXRenderContext.exitButtonRender();
    }

}
