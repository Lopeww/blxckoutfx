package com.lopew.blxckoutfx.mixin;

import com.lopew.blxckoutfx.client.BlxckoutFXTextColors;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Pseudo
@Mixin(targets = "vazkii.patchouli.client.book.text.Word", remap = false)
public class PatchouliWordMixin {
    @ModifyArg(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;IIIZ)I"
            ),
            index = 1,
            require = 0
    )
    private Component blxckoutfx$adjustPatchouliWordTextColor(Component component) {
        return blxckoutfx$adjustPatchouliTextColor(component);
    }

    @ModifyArg(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/class_332;method_51439(Lnet/minecraft/class_327;Lnet/minecraft/class_2561;IIIZ)I"
            ),
            index = 1,
            require = 0
    )
    private Component blxckoutfx$adjustPatchouliWordTextColorFabric(Component component) {
        return blxckoutfx$adjustPatchouliTextColor(component);
    }

    private static Component blxckoutfx$adjustPatchouliTextColor(Component component) {
        Style style = component.getStyle();
        TextColor color = style.getColor();

        if (color == null) {
            return component;
        }

        int adjustedColor = BlxckoutFXTextColors.adjustPatchouliBookTextColor(color.getValue());

        if (adjustedColor == color.getValue()) {
            return component;
        }

        MutableComponent copy = component.copy();
        return copy.withStyle(style.withColor(TextColor.fromRgb(adjustedColor)));
    }
}
