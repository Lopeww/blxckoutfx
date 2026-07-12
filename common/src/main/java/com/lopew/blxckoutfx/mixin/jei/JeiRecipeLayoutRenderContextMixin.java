package com.lopew.blxckoutfx.mixin.jei;

import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Pseudo
@Mixin(targets = "mezz.jei.library.gui.recipes.RecipeLayout", remap = false)
public class JeiRecipeLayoutRenderContextMixin {
    @Inject(method = "drawRecipe", at = @At("HEAD"), require = 0)
    private void blxckoutfx$enterJeiInformationRecipeRender(GuiGraphics guiGraphics, int mouseX, int mouseY, CallbackInfo ci) {
        BlxckoutFXRenderContext.enterJeiRecipeLayoutRender();

        if (blxckoutfx$isInformationRecipe()) {
            BlxckoutFXRenderContext.enterJeiInformationRecipeRender();
        }
    }

    @Inject(method = "drawRecipe", at = @At("RETURN"), require = 0)
    private void blxckoutfx$exitJeiInformationRecipeRender(GuiGraphics guiGraphics, int mouseX, int mouseY, CallbackInfo ci) {
        if (BlxckoutFXRenderContext.isRenderingJeiInformationRecipe()) {
            BlxckoutFXRenderContext.exitJeiInformationRecipeRender();
        }

        BlxckoutFXRenderContext.exitJeiRecipeLayoutRender();
    }

    private boolean blxckoutfx$isInformationRecipe() {
        try {
            Field recipeCategoryField = this.getClass().getDeclaredField("recipeCategory");
            recipeCategoryField.setAccessible(true);
            Object recipeCategory = recipeCategoryField.get(this);

            Method getRecipeType = recipeCategory.getClass().getMethod("getRecipeType");
            Object recipeType = getRecipeType.invoke(recipeCategory);

            Method getUid = recipeType.getClass().getMethod("getUid");
            Object uid = getUid.invoke(recipeType);
            return uid instanceof ResourceLocation resourceLocation
                    && "jei".equals(resourceLocation.getNamespace())
                    && "information".equals(resourceLocation.getPath());
        } catch (ReflectiveOperationException exception) {
            return false;
        }
    }
}

