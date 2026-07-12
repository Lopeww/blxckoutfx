package com.lopew.blxckoutfx.mixin.ae2;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.lopew.blxckoutfx.client.integration.ae2.BlxckoutFXAe2Support;
import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;

import java.lang.reflect.Field;

@Pseudo
@Mixin(targets = "com.glodblock.github.extendedae.client.button.EPPButton", remap = false)
public class ExtendedAeIconButtonsMixin {
    @Unique
    private static final String BLXCKOUTFX_SYMBOL_TEXTURE_PATH = "textures/guis/symbols.png";
    @Unique
    private static Field blxckoutfx$blitterTextureField;

    @WrapOperation(
            method = "renderWidget",
            at = @At(
                    value = "INVOKE",
                    target = "Lappeng/client/gui/style/Blitter;blit(Lnet/minecraft/client/gui/GuiGraphics;)V",
                    ordinal = 1
            ),
            require = 0
    )
    private void blxckoutfx$keepHalfSizeIconReadable(
            @Coerce Object blitter,
            GuiGraphics guiGraphics,
            Operation<Void> original
    ) {
        blxckoutfx$renderIcon(blitter, guiGraphics, original);
    }

    @WrapOperation(
            method = "renderWidget",
            at = @At(
                    value = "INVOKE",
                    target = "Lappeng/client/gui/style/Blitter;blit(Lnet/minecraft/client/gui/GuiGraphics;)V",
                    ordinal = 3
            ),
            require = 0
    )
    private void blxckoutfx$keepFullSizeIconReadable(
            @Coerce Object blitter,
            GuiGraphics guiGraphics,
            Operation<Void> original
    ) {
        blxckoutfx$renderIcon(blitter, guiGraphics, original);
    }

    @Unique
    private static void blxckoutfx$renderIcon(
            @Coerce Object blitter,
            GuiGraphics guiGraphics,
            Operation<Void> original
    ) {
        if (!blxckoutfx$shouldRetheme()) {
            original.call(blitter, guiGraphics);
            return;
        }

        if (blxckoutfx$isBlackSymbolIcon(blitter)) {
            BlxckoutFXRenderContext.enterAe2ForceWhiteIconRender();
            try {
                original.call(blitter, guiGraphics);
            } finally {
                BlxckoutFXRenderContext.exitAe2ForceWhiteIconRender();
            }
            return;
        }

        BlxckoutFXRenderContext.suppressTextureDarkening();
        try {
            original.call(blitter, guiGraphics);
        } finally {
            BlxckoutFXRenderContext.allowTextureDarkening();
        }
    }

    @Unique
    private static boolean blxckoutfx$shouldRetheme() {
        return BlxckoutFXRenderContext.isRenderingAe2Screen()
                && BlxckoutFXAe2Support.isAe2ThemeEnabled();
    }

    @Unique
    private static boolean blxckoutfx$isBlackSymbolIcon(Object blitter) {
        ResourceLocation texture = blxckoutfx$getBlitterTexture(blitter);
        if (texture == null) {
            return false;
        }
        String path = texture.getPath();
        return path != null && path.endsWith(BLXCKOUTFX_SYMBOL_TEXTURE_PATH);
    }

    @Unique
    private static ResourceLocation blxckoutfx$getBlitterTexture(Object blitter) {
        if (blitter == null) {
            return null;
        }

        try {
            Field field = blxckoutfx$blitterTextureField;
            if (field == null || !field.getDeclaringClass().isInstance(blitter)) {
                field = blxckoutfx$resolveTextureField(blitter.getClass());
                blxckoutfx$blitterTextureField = field;
            }
            if (field == null) {
                return null;
            }
            Object value = field.get(blitter);
            return value instanceof ResourceLocation location ? location : null;
        } catch (ReflectiveOperationException ignored) {
            return null;
        }
    }

    @Unique
    private static Field blxckoutfx$resolveTextureField(Class<?> type) {
        Class<?> current = type;
        while (current != null) {
            for (Field field : current.getDeclaredFields()) {
                if (ResourceLocation.class.equals(field.getType())) {
                    field.setAccessible(true);
                    return field;
                }
            }
            current = current.getSuperclass();
        }
        return null;
    }
}
