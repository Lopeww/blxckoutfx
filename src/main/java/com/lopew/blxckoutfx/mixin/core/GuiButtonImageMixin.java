package com.lopew.blxckoutfx.mixin.core;

import com.lopew.blxckoutfx.client.BlxckoutFXRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButtonImage;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiButtonImage.class)
public class GuiButtonImageMixin {
    @Shadow @Final private ResourceLocation resourceLocation;
    @Shadow @Final private int xTexStart;
    @Shadow @Final private int yTexStart;

    @Inject(method = "drawButton", at = @At("HEAD"), require = 1)
    private void blxckoutfx$enterImageButtonRender(Minecraft mc, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        BlxckoutFXRenderContext.enterButtonRender();
        BlxckoutFXRenderContext.enterIconColorPreserve();
        if (blxckoutfx$isRecipeBookButton()) {
            BlxckoutFXRenderContext.enterRecipeBookButtonIconRemap();
        }
    }

    @Inject(method = "drawButton", at = @At("RETURN"), require = 1)
    private void blxckoutfx$exitImageButtonRender(Minecraft mc, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        if (blxckoutfx$isRecipeBookButton()) {
            BlxckoutFXRenderContext.exitRecipeBookButtonIconRemap();
        }
        BlxckoutFXRenderContext.exitIconColorPreserve();
        BlxckoutFXRenderContext.exitButtonRender();
    }

    private boolean blxckoutfx$isRecipeBookButton() {
        if (this.resourceLocation == null) {
            return false;
        }

        String path = this.resourceLocation.getResourcePath();
        if ("textures/gui/container/inventory.png".equals(path)) {
            return this.xTexStart == 178 && this.yTexStart == 0;
        }

        if ("textures/gui/container/crafting_table.png".equals(path)) {
            return this.xTexStart == 0 && this.yTexStart == 168;
        }

        return false;
    }
}
