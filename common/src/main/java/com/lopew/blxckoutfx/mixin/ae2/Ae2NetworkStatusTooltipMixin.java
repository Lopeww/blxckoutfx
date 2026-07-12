package com.lopew.blxckoutfx.mixin.ae2;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;

import java.util.List;

@Pseudo
@Mixin(targets = "appeng.client.gui.me.networktool.NetworkStatusScreen", remap = false)
public class Ae2NetworkStatusTooltipMixin {
    @WrapOperation(
            method = "drawFG",
            at = @At(
                    value = "INVOKE",
                    target = "Lappeng/client/gui/me/networktool/NetworkStatusScreen;drawTooltipWithHeader(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IILjava/util/List;)V"
            ),
            require = 0
    )
    private void blxckoutfx$fixAe2NetworkStatusTooltipOffset(
            @Coerce Object screen,
            GuiGraphicsExtractor guiGraphics,
            int x,
            int y,
            List<?> lines,
            Operation<Void> original
    ) {
        original.call(screen, guiGraphics, x + blxckoutfx$getGuiLeft(screen), y + blxckoutfx$getGuiTop(screen), lines);
    }

    private static int blxckoutfx$getGuiLeft(Object screen) {
        Object value = blxckoutfx$invokeNoArg(screen, "getGuiLeft");
        return value instanceof Integer integer ? integer : 0;
    }

    private static int blxckoutfx$getGuiTop(Object screen) {
        Object value = blxckoutfx$invokeNoArg(screen, "getGuiTop");
        return value instanceof Integer integer ? integer : 0;
    }

    private static Object blxckoutfx$invokeNoArg(Object target, String methodName) {
        try {
            return target.getClass().getMethod(methodName).invoke(target);
        } catch (ReflectiveOperationException ignored) {
            return null;
        }
    }
}
