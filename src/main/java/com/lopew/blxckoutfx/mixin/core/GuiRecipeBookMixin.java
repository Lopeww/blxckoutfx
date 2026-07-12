package com.lopew.blxckoutfx.mixin.core;

import com.lopew.blxckoutfx.client.BlxckoutFXRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.recipebook.GuiRecipeBook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiRecipeBook.class)
public class GuiRecipeBookMixin {
    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/recipebook/GuiRecipeBook;drawTexturedModalRect(IIIIII)V"
            ),
            require = 1
    )
    private void blxckoutfx$enterRecipeBookSearchIconRemap(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        BlxckoutFXRenderContext.enterRecipeBookSearchIconRemap();
    }

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/recipebook/GuiRecipeBook;drawTexturedModalRect(IIIIII)V",
                    shift = At.Shift.AFTER
            ),
            require = 1
    )
    private void blxckoutfx$exitRecipeBookSearchIconRemap(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        BlxckoutFXRenderContext.exitRecipeBookSearchIconRemap();
    }
}
