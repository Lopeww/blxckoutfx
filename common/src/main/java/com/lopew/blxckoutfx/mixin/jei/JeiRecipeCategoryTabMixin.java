package com.lopew.blxckoutfx.mixin.jei;

import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "mezz.jei.gui.recipes.RecipeCategoryTab", remap = false)
public class JeiRecipeCategoryTabMixin {
    @Inject(
            method = "draw",
            at = @At(
                    value = "INVOKE",
                    target = "Lmezz/jei/gui/recipes/RecipeCategoryIconUtil;create(Lmezz/jei/api/recipe/category/IRecipeCategory;Lmezz/jei/api/recipe/IRecipeManager;Lmezz/jei/api/helpers/IGuiHelper;)Lmezz/jei/api/gui/drawable/IDrawable;"
            ),
            require = 0
    )
    private void blxckoutfx$suppressCategoryIconDarkening(CallbackInfo ci) {
        // The tab frame has already rendered; this covers both item and PNG category icons.
        BlxckoutFXRenderContext.suppressTextureDarkening();
    }

    @Inject(method = "draw", at = @At("RETURN"), require = 0)
    private void blxckoutfx$restoreCategoryIconDarkening(CallbackInfo ci) {
        BlxckoutFXRenderContext.allowTextureDarkening();
    }
}
