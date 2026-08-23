package com.lopew.blxckoutfx.mixin.jei;

import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;

@Pseudo
@Mixin(targets = "mezz.jei.gui.recipes.RecipeCategoryTab", remap = false)
public class JeiRecipeCategoryTabMixin {
    @WrapOperation(
            method = "draw",
            at = @At(
                    value = "INVOKE",
                    target = "Lmezz/jei/api/gui/drawable/IDrawable;draw(Lnet/minecraft/client/gui/GuiGraphics;II)V"
            ),
            require = 0
    )
    private void blxckoutfx$preserveCategoryIcon(
            @Coerce Object icon,
            GuiGraphics graphics,
            int x,
            int y,
            Operation<Void> original
    ) {
        BlxckoutFXRenderContext.suppressTextureDarkening();
        try {
            original.call(icon, graphics, x, y);
        } finally {
            BlxckoutFXRenderContext.allowTextureDarkening();
        }
    }
}
