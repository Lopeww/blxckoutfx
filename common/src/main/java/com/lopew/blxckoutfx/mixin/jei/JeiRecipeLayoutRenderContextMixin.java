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
import java.util.Optional;

@Pseudo
@Mixin(targets = "mezz.jei.library.gui.recipes.RecipeLayout", remap = false)
public class JeiRecipeLayoutRenderContextMixin {
    private static final ClassValue<Optional<Method>> RECIPE_CATEGORY_METHODS = new ClassValue<>() {
        @Override
        protected Optional<Method> computeValue(Class<?> type) {
            return findPublicMethod(type, "getRecipeCategory");
        }
    };
    private static final ClassValue<Optional<Method>> RECIPE_TYPE_METHODS = new ClassValue<>() {
        @Override
        protected Optional<Method> computeValue(Class<?> type) {
            return findPublicMethod(type, "getRecipeType");
        }
    };
    private static final ClassValue<Optional<Method>> UID_METHODS = new ClassValue<>() {
        @Override
        protected Optional<Method> computeValue(Class<?> type) {
            return findPublicMethod(type, "getUid");
        }
    };

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
            Method getRecipeCategory = RECIPE_CATEGORY_METHODS.get(this.getClass()).orElse(null);
            if (getRecipeCategory == null) {
                return false;
            }
            Object recipeCategory = getRecipeCategory.invoke(this);
            if (recipeCategory == null) {
                return false;
            }

            Method getRecipeType = RECIPE_TYPE_METHODS.get(recipeCategory.getClass()).orElse(null);
            if (getRecipeType == null) {
                return false;
            }
            Object recipeType = getRecipeType.invoke(recipeCategory);
            if (recipeType == null) {
                return false;
            }

            Method getUid = UID_METHODS.get(recipeType.getClass()).orElse(null);
            if (getUid == null) {
                return false;
            }
            Object uid = getUid.invoke(recipeType);
            return uid instanceof Identifier identifier
                    && "jei".equals(identifier.getNamespace())
                    && "information".equals(identifier.getPath());
        } catch (ReflectiveOperationException exception) {
            return false;
        }
    }

    private static Optional<Method> findPublicMethod(Class<?> type, String name) {
        try {
            return Optional.of(type.getMethod(name));
        } catch (NoSuchMethodException ignored) {
            return Optional.empty();
        }
    }
}
