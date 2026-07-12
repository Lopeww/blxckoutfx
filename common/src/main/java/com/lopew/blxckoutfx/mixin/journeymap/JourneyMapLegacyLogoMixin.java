package com.lopew.blxckoutfx.mixin.journeymap;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(targets = "journeymap.client.ui.component.screens.JmUILegacy", remap = false)
public class JourneyMapLegacyLogoMixin {
    @WrapOperation(
            method = "drawLogo",
            at = @At(
                    value = "INVOKE",
                    target = "Ljourneymap/client/render/draw/DrawUtil;drawImage(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/renderer/texture/DynamicTexture;DDZFD)V"
            ),
            require = 0
    )
    private void blxckoutfx$renderOptionLogoWithoutDarkening(
            GuiGraphics guiGraphics,
            DynamicTexture image,
            double x,
            double y,
            boolean centered,
            float scale,
            double zLevel,
            Operation<Void> original
    ) {
        if (!blxckoutfx$shouldSuppressOptionLogo()) {
            original.call(guiGraphics, image, x, y, centered, scale, zLevel);
            return;
        }

        BlxckoutFXRenderContext.suppressTextureDarkening();
        try {
            original.call(guiGraphics, image, x, y, centered, scale, zLevel);
        } finally {
            BlxckoutFXRenderContext.allowTextureDarkening();
        }
    }

    @Unique
    private boolean blxckoutfx$shouldSuppressOptionLogo() {
        return ((Object) this).getClass().getName().startsWith("journeymap.client.ui.option.");
    }
}
