package com.lopew.blxckoutfx.mixin.journeymap;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.lopew.blxckoutfx.client.integration.journeymap.BlxckoutFXJourneyMapIconColorizer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(targets = "journeymap.client.ui.component.buttons.BasicImageButton", remap = false)
public class JourneyMapBasicImageButtonMixin {
    @WrapOperation(
            method = "renderWidget",
            at = @At(
                    value = "INVOKE",
                    target = "Ljourneymap/client/texture/TextureCache;getTexture(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/client/renderer/texture/DynamicTexture;"
            ),
            require = 0
    )
    private DynamicTexture blxckoutfx$useRecoloredWaypointActionIcon(
            ResourceLocation iconResource,
            Operation<DynamicTexture> original
    ) {
        return blxckoutfx$getRecoloredIcon(iconResource, original);
    }

    @WrapOperation(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Ljourneymap/client/texture/TextureCache;getTexture(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/client/renderer/texture/DynamicTexture;"
            ),
            require = 0
    )
    private DynamicTexture blxckoutfx$useRecoloredWaypointActionIconLegacy(
            ResourceLocation iconResource,
            Operation<DynamicTexture> original
    ) {
        return blxckoutfx$getRecoloredIcon(iconResource, original);
    }

    @WrapOperation(
            method = "method_48579",
            at = @At(
                    value = "INVOKE",
                    target = "Ljourneymap/client/texture/TextureCache;getTexture(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/client/renderer/texture/DynamicTexture;"
            ),
            require = 0
    )
    private DynamicTexture blxckoutfx$useRecoloredWaypointActionIconFabric(
            ResourceLocation iconResource,
            Operation<DynamicTexture> original
    ) {
        return blxckoutfx$getRecoloredIcon(iconResource, original);
    }

    @Unique
    private DynamicTexture blxckoutfx$getRecoloredIcon(
            ResourceLocation iconResource,
            Operation<DynamicTexture> original
    ) {
        DynamicTexture texture = original.call(iconResource);
        return BlxckoutFXJourneyMapIconColorizer.getWaypointActionIcon(iconResource, texture);
    }
}

