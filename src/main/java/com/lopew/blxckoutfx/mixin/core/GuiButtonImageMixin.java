package com.lopew.blxckoutfx.mixin.core;

import com.lopew.blxckoutfx.client.BlxckoutFXRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButtonImage;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiButtonImage.class)
public class GuiButtonImageMixin {
    @Unique
    private boolean blxckoutfx$recipeBookButton;

    @Inject(
            method = "<init>(IIIIIIIILnet/minecraft/util/ResourceLocation;)V",
            at = @At("RETURN"),
            require = 1
    )
    private void blxckoutfx$captureRecipeBookButton(
            int buttonId,
            int x,
            int y,
            int width,
            int height,
            int xTexStart,
            int yTexStart,
            int yDiffText,
            ResourceLocation resourceLocation,
            CallbackInfo ci
    ) {
        if (resourceLocation == null) {
            this.blxckoutfx$recipeBookButton = false;
            return;
        }

        String path = resourceLocation.getResourcePath();
        this.blxckoutfx$recipeBookButton =
                ("textures/gui/container/inventory.png".equals(path) && xTexStart == 178 && yTexStart == 0)
                        || ("textures/gui/container/crafting_table.png".equals(path) && xTexStart == 0 && yTexStart == 168);
    }

    @Inject(method = "drawButton(Lnet/minecraft/client/Minecraft;IIF)V", at = @At("HEAD"), require = 1)
    private void blxckoutfx$enterImageButtonRender(Minecraft mc, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        BlxckoutFXRenderContext.enterButtonRender();
        BlxckoutFXRenderContext.enterIconColorPreserve();
        if (this.blxckoutfx$recipeBookButton) {
            BlxckoutFXRenderContext.enterRecipeBookButtonIconRemap();
        }
    }

    @Inject(method = "drawButton(Lnet/minecraft/client/Minecraft;IIF)V", at = @At("RETURN"), require = 1)
    private void blxckoutfx$exitImageButtonRender(Minecraft mc, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        if (this.blxckoutfx$recipeBookButton) {
            BlxckoutFXRenderContext.exitRecipeBookButtonIconRemap();
        }
        BlxckoutFXRenderContext.exitIconColorPreserve();
        BlxckoutFXRenderContext.exitButtonRender();
    }
}
