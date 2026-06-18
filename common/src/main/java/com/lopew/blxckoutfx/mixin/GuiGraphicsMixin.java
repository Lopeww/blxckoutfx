package com.lopew.blxckoutfx.mixin;

import com.lopew.blxckoutfx.client.BlxckoutFXRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiGraphics.class)
public class GuiGraphicsMixin {
    @Inject(method = {
            "innerBlit(Lnet/minecraft/resources/ResourceLocation;IIIIIFFFF)V",
            "m_280444_(Lnet/minecraft/resources/ResourceLocation;IIIIIFFFF)V"
    }, at = @At("HEAD"))
    private void blxckoutfx$suppressModMenuIconDarkening(
            ResourceLocation atlasLocation,
            int x1,
            int x2,
            int y1,
            int y2,
            int blitOffset,
            float minU,
            float maxU,
            float minV,
            float maxV,
            CallbackInfo ci
    ) {
        if (isModsScreenLogo(atlasLocation)) {
            BlxckoutFXRenderContext.suppressTextureDarkening();
        }
    }

    @Inject(method = {
            "innerBlit(Lnet/minecraft/resources/ResourceLocation;IIIIIFFFF)V",
            "m_280444_(Lnet/minecraft/resources/ResourceLocation;IIIIIFFFF)V"
    }, at = @At("RETURN"))
    private void blxckoutfx$restoreModMenuIconDarkening(
            ResourceLocation atlasLocation,
            int x1,
            int x2,
            int y1,
            int y2,
            int blitOffset,
            float minU,
            float maxU,
            float minV,
            float maxV,
            CallbackInfo ci
    ) {
        if (isModsScreenLogo(atlasLocation)) {
            BlxckoutFXRenderContext.allowTextureDarkening();
        }
    }

    private static boolean isModsScreenLogo(ResourceLocation atlasLocation) {
        String namespace = atlasLocation.getNamespace();
        String path = atlasLocation.getPath();
        boolean modMenuLogo = ("modmenu".equals(namespace) && path.endsWith("_icon"))
                || ("minecraft".equals(namespace) && "textures/misc/unknown_pack.png".equals(path));

        if (!modMenuLogo) {
            return false;
        }

        return isFabricModMenuScreen();
    }

    private static boolean isFabricModMenuScreen() {
        Screen screen = Minecraft.getInstance().screen;
        return screen != null && screen.getClass().getName().startsWith("com.terraformersmc.modmenu.gui.");
    }

}
