package com.lopew.blxckoutfx.mixin.jei;

import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Method;

@Pseudo
@Mixin(targets = "mezz.jei.library.gui.recipes.RecipeLayout", remap = false)
public class JeiRecipeLayoutRenderContextMixin {
    @Inject(method = "drawRecipe", at = @At("HEAD"), require = 0)
    private void blxckoutfx$enterJeiRecipeLayoutRender(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, CallbackInfo ci) {
        BlxckoutFXRenderContext.enterJeiRecipeLayoutRender();

        if (blxckoutfx$isInformationRecipe()) {
            BlxckoutFXRenderContext.enterJeiInformationRecipeRender();
        }
    }

    @Inject(method = "drawRecipe", at = @At("RETURN"), require = 0)
    private void blxckoutfx$exitJeiRecipeLayoutRender(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, CallbackInfo ci) {
        if (BlxckoutFXRenderContext.isRenderingJeiInformationRecipe()) {
            BlxckoutFXRenderContext.exitJeiInformationRecipeRender();
        }

        BlxckoutFXRenderContext.exitJeiRecipeLayoutRender();
    }

    private boolean blxckoutfx$isInformationRecipe() {
        try {
            Method getRecipeCategory = this.getClass().getMethod("getRecipeCategory");
            Object recipeCategory = getRecipeCategory.invoke(this);

            Method getRecipeType = recipeCategory.getClass().getMethod("getRecipeType");
            Object recipeType = getRecipeType.invoke(recipeCategory);

            Method getUid = recipeType.getClass().getMethod("getUid");
            Object uid = getUid.invoke(recipeType);
            return uid instanceof Identifier identifier
                    && "jei".equals(identifier.getNamespace())
                    && "information".equals(identifier.getPath());
        } catch (ReflectiveOperationException exception) {
            return false;
        }
    }
}
