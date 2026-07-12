package com.lopew.blxckoutfx.mixin.ae2;

import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "appeng.client.gui.AEBaseScreen", remap = false, priority = 50)
public abstract class Ae2BaseScreenWarningMixin {
    @Unique
    private boolean blxckoutfx$enteredAe2WarningTextRender;

    @Inject(method = "drawText", at = @At("HEAD"), require = 0)
    private void blxckoutfx$enterAe2WarningTextRender(
            @Coerce Object guiGraphics,
            @Coerce Object text,
            @Coerce Object override,
            CallbackInfo ci
    ) {
        if (!blxckoutfx$isAe2WarningPalette(text, override)) {
            return;
        }

        blxckoutfx$enteredAe2WarningTextRender = true;
        BlxckoutFXRenderContext.enterAe2WarningTextRender();
    }

    @Inject(method = "drawText", at = @At("RETURN"), require = 0)
    private void blxckoutfx$exitAe2WarningTextRender(
            @Coerce Object guiGraphics,
            @Coerce Object text,
            @Coerce Object override,
            CallbackInfo ci
    ) {
        if (!blxckoutfx$enteredAe2WarningTextRender) {
            return;
        }

        blxckoutfx$enteredAe2WarningTextRender = false;
        BlxckoutFXRenderContext.exitAe2WarningTextRender();
    }

    @ModifyArg(
            method = "drawText",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/util/FormattedCharSequence;IIIZ)I"
            ),
            index = 4,
            require = 0
    )
    private int blxckoutfx$preserveAe2WarningTextColor(int color) {
        if (blxckoutfx$enteredAe2WarningTextRender && !blxckoutfx$isAe2WarningColor(color)) {
            return 0xFC0000;
        }

        return color;
    }

    @Unique
    private static boolean blxckoutfx$isAe2WarningColor(int color) {
        int rgb = color & 0xFFFFFF;
        return rgb == 0xFC0000 || rgb == 0x3E0000;
    }

    @Unique
    private static boolean blxckoutfx$isAe2WarningPalette(Object text, Object override) {
        if (blxckoutfx$isErrorPalette(text)) {
            return true;
        }

        if (blxckoutfx$containsWarningText(text)) {
            return true;
        }

        return blxckoutfx$containsWarningText(override);
    }

    @Unique
    private static boolean blxckoutfx$isErrorPalette(Object value) {
        if (value == null) {
            return false;
        }

        try {
            Object palette = value.getClass().getMethod("getColor").invoke(value);
            if (!(palette instanceof Enum<?> enumValue)) {
                return false;
            }

            String name = enumValue.name();
            return "ERROR".equals(name) || "TEXTFIELD_ERROR".equals(name);
        } catch (ReflectiveOperationException ignored) {
            return false;
        }
    }

    @Unique
    private static boolean blxckoutfx$containsWarningText(Object value) {
        if (value == null) {
            return false;
        }

        try {
            Object component = value.getClass().getMethod("getText").invoke(value);
            if (component == null) {
                return false;
            }

            String text = component.getClass().getMethod("getString").invoke(component).toString();
            return text.contains("Out of Power");
        } catch (ReflectiveOperationException ignored) {
            try {
                Object component = value.getClass().getMethod("getContent").invoke(value);
                if (component == null) {
                    return false;
                }

                String text = component.getClass().getMethod("getString").invoke(component).toString();
                return text.contains("Out of Power");
            } catch (ReflectiveOperationException ignoredToo) {
                return false;
            }
        }
    }
}

