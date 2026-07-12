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
@Mixin(targets = "journeymap.client.ui.fullscreen.Fullscreen", remap = false)
public class JourneyMapFullscreenLogoMixin {
    @WrapOperation(
            method = "drawLogo",
            at = @At(
                    value = "INVOKE",
                    target = "Ljourneymap/client/render/draw/DrawUtil;drawImage(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/renderer/texture/DynamicTexture;DDZFD)V"
            ),
            require = 0
    )
    private void blxckoutfx$renderFullscreenLogoWithoutDarkening(
            GuiGraphics guiGraphics,
            DynamicTexture image,
            double x,
            double y,
            boolean centered,
            float scale,
            double zLevel,
            Operation<Void> original
    ) {
        blxckoutfx$renderNoDarkening(original, guiGraphics, image, x, y, centered, scale, zLevel);
    }

    @WrapOperation(
            method = "method_48579",
            at = @At(
                    value = "INVOKE",
                    target = "Ljourneymap/client/render/draw/DrawUtil;drawImage(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/renderer/texture/DynamicTexture;DDZFD)V"
            ),
            require = 0
    )
    private void blxckoutfx$renderFullscreenLogoWithoutDarkeningFabric(
            GuiGraphics guiGraphics,
            DynamicTexture image,
            double x,
            double y,
            boolean centered,
            float scale,
            double zLevel,
            Operation<Void> original
    ) {
        blxckoutfx$renderNoDarkening(original, guiGraphics, image, x, y, centered, scale, zLevel);
    }

    @Unique
    private void blxckoutfx$renderNoDarkening(
            Operation<Void> original,
            GuiGraphics guiGraphics,
            DynamicTexture image,
            double x,
            double y,
            boolean centered,
            float scale,
            double zLevel
    ) {
        BlxckoutFXRenderContext.suppressTextureDarkening();
        try {
            original.call(guiGraphics, image, x, y, centered, scale, zLevel);
        } finally {
            BlxckoutFXRenderContext.allowTextureDarkening();
        }
    }
}

