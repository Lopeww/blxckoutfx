package com.lopew.blxckoutfx.mixin.core;

import com.lopew.blxckoutfx.client.BlxckoutFXRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiUnicodeGlyphButton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ GuiButtonExt.class, GuiUnicodeGlyphButton.class })
public class ForgeConfigButtonMixin {
    @Inject(method = "drawButton(Lnet/minecraft/client/Minecraft;IIF)V", at = @At("HEAD"), require = 1)
    private void blxckoutfx$enterForgeConfigButtonRender(
            Minecraft minecraft,
            int mouseX,
            int mouseY,
            float partialTicks,
            CallbackInfo callbackInfo
    ) {
        BlxckoutFXRenderContext.enterButtonRender();
    }

    @Inject(method = "drawButton(Lnet/minecraft/client/Minecraft;IIF)V", at = @At("RETURN"), require = 1)
    private void blxckoutfx$exitForgeConfigButtonRender(
            Minecraft minecraft,
            int mouseX,
            int mouseY,
            float partialTicks,
            CallbackInfo callbackInfo
    ) {
        BlxckoutFXRenderContext.exitButtonRender();
    }
}
