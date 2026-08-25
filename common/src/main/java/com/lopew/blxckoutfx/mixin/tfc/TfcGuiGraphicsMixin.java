package com.lopew.blxckoutfx.mixin.tfc;

import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import static com.lopew.blxckoutfx.client.compat.tfc.TfcGuiGraphicsSupport.*;

/** Preserves TFC's colour-coded GUI data without exempting its container backgrounds. */
@Pseudo
@Mixin(targets = {
        "net.dries007.tfc.client.screen.AnvilScreen",
        "net.dries007.tfc.client.screen.BlastFurnaceScreen",
        "net.dries007.tfc.client.screen.CrucibleScreen",
        "net.dries007.tfc.client.screen.GrillScreen",
        "net.dries007.tfc.client.screen.PotScreen",
        "net.dries007.tfc.client.screen.BarrelScreen",
        "net.dries007.tfc.client.screen.button.AnvilPlanButton",
        "net.dries007.tfc.client.screen.button.AnvilWeldButton",
        "net.dries007.tfc.client.screen.button.KnappingButton",
        "net.dries007.tfc.client.screen.button.PlayerInventoryTabButton",
        "net.dries007.tfc.client.screen.KnappingScreen",
        "net.dries007.tfc.compat.jei.category.KnappingRecipeCategory",
        "net.dries007.tfc.compat.patchouli.component.KnappingComponent",
        "mezz.jei.library.gui.recipes.RecipeLayout",
        "mezz.jei.library.gui.ingredients.RecipeSlot",
        "vazkii.patchouli.client.book.page.PageImage",
        "com.eerussianguy.firmalife.client.screen.StovetopPotScreen",
        "com.eerussianguy.firmalife.client.screen.KegScreen"
})
public class TfcGuiGraphicsMixin {
    @Unique private static int blxckoutfx$tfcJeiIngredientRenderDepth;
    @Unique private boolean blxckoutfx$planResultItemRender;
    @Unique private boolean blxckoutfx$jeiKnappingTextureDarkeningSuppressed;
    @Unique private boolean blxckoutfx$knappingButtonTextureDarkeningSuppressed;
    @Unique private int blxckoutfx$knappingScreenTextureDarkeningSuppressDepth;
    @Unique private boolean blxckoutfx$crucibleFluidRender;
    @Unique private boolean blxckoutfx$potFluidRender;
    @Unique private boolean blxckoutfx$barrelFluidRender;

    @ModifyArgs(
            method = "renderBg",
            at = @At(value = "INVOKE", target = "Lnet/dries007/tfc/client/RenderHelpers;fillAreaWithSprite(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;IIIIII)V"),
            require = 0,
            remap = false
    )
    private void blxckoutfx$enterCrucibleFluidRender(Args args) {
        if (blxckoutfx$isCrucibleScreen((Object) this)
                || blxckoutfx$isBlastFurnaceScreen((Object) this)
                || blxckoutfx$isBarrelScreen((Object) this)
                || blxckoutfx$isKegScreen((Object) this)) {
            int x = args.get(2);
            int y = args.get(3);
            int width = args.get(4);
            int height = args.get(5);
            blxckoutfx$crucibleFluidRender = true;
            BlxckoutFXRenderContext.enterTfcCrucibleFluidRender(x, y, x + width, y + height);
        }
    }

    @Inject(
            method = "renderBg",
            at = @At(value = "INVOKE", target = "Lnet/dries007/tfc/client/RenderHelpers;fillAreaWithSprite(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;IIIIII)V", shift = At.Shift.AFTER),
            require = 0,
            remap = false
    )
    private void blxckoutfx$exitCrucibleFluidRender(GuiGraphics graphics, float partialTick, int mouseX, int mouseY, CallbackInfo ci) {
        if (blxckoutfx$crucibleFluidRender) {
            blxckoutfx$crucibleFluidRender = false;
            BlxckoutFXRenderContext.exitTfcCrucibleFluidRender();
        }
    }

    @ModifyArgs(
            method = "renderBg",
            at = @At(value = "INVOKE", target = "Lnet/dries007/tfc/client/RenderHelpers;fillAreaWithSprite(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;IIIIII)V"),
            require = 0,
            remap = false
    )
    private void blxckoutfx$enterPotFluidRender(Args args) {
        if (blxckoutfx$isPotScreen((Object) this) || blxckoutfx$isStovetopPotScreen((Object) this)) {
            int x = args.get(2);
            int y = args.get(3);
            int width = args.get(4);
            int height = args.get(5);
            blxckoutfx$potFluidRender = true;
            BlxckoutFXRenderContext.enterTfcFirepitPotFluidRender(x, y, x + width, y + height);
        }
    }

    @Inject(
            method = "renderBg",
            at = @At(value = "INVOKE", target = "Lnet/dries007/tfc/client/RenderHelpers;fillAreaWithSprite(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;IIIIII)V", shift = At.Shift.AFTER),
            require = 0,
            remap = false
    )
    private void blxckoutfx$exitPotFluidRender(GuiGraphics graphics, float partialTick, int mouseX, int mouseY, CallbackInfo ci) {
        if (blxckoutfx$potFluidRender) {
            blxckoutfx$potFluidRender = false;
            BlxckoutFXRenderContext.exitTfcFirepitPotFluidRender();
        }
    }

    @Inject(method = "render(Lnet/minecraft/client/gui/GuiGraphics;IIF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V", shift = At.Shift.BEFORE, remap = true), require = 0, remap = false)
    private void blxckoutfx$preservePatchouliPageImage(GuiGraphics graphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (blxckoutfx$isPatchouliPageImage((Object) this)) {
            BlxckoutFXRenderContext.suppressTextureDarkening();
        }
    }

    @Inject(method = "render(Lnet/minecraft/client/gui/GuiGraphics;IIF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V", shift = At.Shift.AFTER, remap = true), require = 0, remap = false)
    private void blxckoutfx$restorePatchouliPageImage(GuiGraphics graphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (blxckoutfx$isPatchouliPageImage((Object) this)) {
            BlxckoutFXRenderContext.allowTextureDarkening();
        }
    }

    @Inject(method = "render(Lnet/minecraft/client/gui/GuiGraphics;Lvazkii/patchouli/api/IComponentRenderContext;IILnet/dries007/tfc/common/recipes/KnappingRecipe;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;II)V", at = @At("HEAD"), require = 0, remap = false)
    private static void blxckoutfx$preservePatchouliKnapping(CallbackInfo ci) {
        BlxckoutFXRenderContext.suppressTextureDarkening();
    }

    @Inject(method = "render(Lnet/minecraft/client/gui/GuiGraphics;Lvazkii/patchouli/api/IComponentRenderContext;IILnet/dries007/tfc/common/recipes/KnappingRecipe;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;II)V", at = @At("RETURN"), require = 0, remap = false)
    private static void blxckoutfx$restorePatchouliKnapping(CallbackInfo ci) {
        BlxckoutFXRenderContext.allowTextureDarkening();
    }

    @Inject(method = "renderBg", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIFFIIII)V", shift = At.Shift.BEFORE, remap = true), require = 0, remap = false)
    private void blxckoutfx$preserveAnvilSpriteIndicators(GuiGraphics graphics, float partialTick, int mouseX, int mouseY, CallbackInfo ci) {
        if (blxckoutfx$isAnvilScreen((Object) this)) {
            BlxckoutFXRenderContext.suppressTextureDarkening();
        }
    }

    @Inject(method = "renderBg", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIFFIIII)V", shift = At.Shift.AFTER, remap = true), require = 0, remap = false)
    private void blxckoutfx$restoreAnvilSpriteIndicators(GuiGraphics graphics, float partialTick, int mouseX, int mouseY, CallbackInfo ci) {
        if (blxckoutfx$isAnvilScreen((Object) this)) {
            BlxckoutFXRenderContext.allowTextureDarkening();
        }
    }

    @Inject(method = "renderWidget", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;renderItem(Lnet/minecraft/world/item/ItemStack;II)V", shift = At.Shift.BEFORE, remap = true), require = 0, remap = false)
    private void blxckoutfx$preserveAnvilPlanResultItem(GuiGraphics graphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (blxckoutfx$isAnvilPlanButton((Object) this)) {
            blxckoutfx$planResultItemRender = true;
            BlxckoutFXRenderContext.suppressTextureDarkening();
        }
    }

    @Inject(method = "renderWidget", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;renderItem(Lnet/minecraft/world/item/ItemStack;II)V", shift = At.Shift.AFTER, remap = true), require = 0, remap = false)
    private void blxckoutfx$restoreAnvilPlanResultItem(GuiGraphics graphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (blxckoutfx$planResultItemRender) {
            blxckoutfx$planResultItemRender = false;
            BlxckoutFXRenderContext.allowTextureDarkening();
        }
    }

    @Inject(method = "renderWidget", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIFFIIII)V", shift = At.Shift.BEFORE, remap = true), require = 0, remap = false)
    private void blxckoutfx$preservePlayerInventoryTabIcon(GuiGraphics graphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (blxckoutfx$isPlayerInventoryTabButton((Object) this)) {
            BlxckoutFXRenderContext.suppressTextureDarkening();
        }
    }

    @Inject(method = "renderWidget", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIFFIIII)V", shift = At.Shift.AFTER, remap = true), require = 0, remap = false)
    private void blxckoutfx$restorePlayerInventoryTabIcon(GuiGraphics graphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (blxckoutfx$isPlayerInventoryTabButton((Object) this)) {
            BlxckoutFXRenderContext.allowTextureDarkening();
        }
    }

    @Inject(method = "drawRecipe", at = @At(value = "INVOKE", target = "Lmezz/jei/api/gui/ingredient/IRecipeSlotDrawable;draw(Lnet/minecraft/client/gui/GuiGraphics;Z)V", shift = At.Shift.BEFORE), require = 0, remap = false)
    private void blxckoutfx$enterTfcJeiIngredientRender(GuiGraphics graphics, int mouseX, int mouseY, CallbackInfo ci) {
        if (blxckoutfx$isTfcJeiRecipeLayout((Object) this)) {
            blxckoutfx$tfcJeiIngredientRenderDepth++;
        }
    }

    @Inject(method = "drawRecipe", at = @At(value = "INVOKE", target = "Lmezz/jei/api/gui/ingredient/IRecipeSlotDrawable;draw(Lnet/minecraft/client/gui/GuiGraphics;Z)V", shift = At.Shift.AFTER), require = 0, remap = false)
    private void blxckoutfx$exitTfcJeiIngredientRender(GuiGraphics graphics, int mouseX, int mouseY, CallbackInfo ci) {
        if (blxckoutfx$tfcJeiIngredientRenderDepth > 0) {
            blxckoutfx$tfcJeiIngredientRenderDepth--;
        }
    }

    @Inject(method = "drawIngredient", at = @At("HEAD"), require = 0, remap = false)
    private void blxckoutfx$preserveTfcJeiIngredient(CallbackInfo ci) {
        if (blxckoutfx$tfcJeiIngredientRenderDepth > 0) {
            BlxckoutFXRenderContext.suppressTextureDarkening();
        }
    }

    @Inject(method = "drawIngredient", at = @At("RETURN"), require = 0, remap = false)
    private void blxckoutfx$restoreTfcJeiIngredient(CallbackInfo ci) {
        if (blxckoutfx$tfcJeiIngredientRenderDepth > 0) {
            BlxckoutFXRenderContext.allowTextureDarkening();
        }
    }

    @Inject(method = "drawRecipe", at = @At("HEAD"), require = 0, remap = false)
    private void blxckoutfx$enterTfcJeiKnappingArrowRender(GuiGraphics graphics, int mouseX, int mouseY, CallbackInfo ci) {
        if (blxckoutfx$isTfcJeiKnappingRecipeLayout((Object) this)) {
            BlxckoutFXRenderContext.enterTfcJeiKnappingArrowRender();
        }
    }

    @Inject(method = "drawRecipe", at = @At("RETURN"), require = 0, remap = false)
    private void blxckoutfx$exitTfcJeiKnappingArrowRender(GuiGraphics graphics, int mouseX, int mouseY, CallbackInfo ci) {
        if (BlxckoutFXRenderContext.isRenderingTfcJeiKnappingArrow()) {
            BlxckoutFXRenderContext.exitTfcJeiKnappingArrowRender();
        }
    }

    @Inject(
            method = "draw(Lnet/dries007/tfc/common/recipes/KnappingRecipe;Lmezz/jei/api/gui/ingredient/IRecipeSlotsView;Lnet/minecraft/client/gui/GuiGraphics;DD)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/dries007/tfc/compat/jei/category/KnappingRecipeCategory;getTexture(Lmezz/jei/api/gui/ingredient/IRecipeSlotsView;Z)Lmezz/jei/api/gui/drawable/IDrawable;",
                    ordinal = 0
            ),
            require = 0,
            remap = false
    )
    private void blxckoutfx$preserveJeiKnappingTextures(CallbackInfo ci) {
        blxckoutfx$jeiKnappingTextureDarkeningSuppressed = true;
        BlxckoutFXRenderContext.suppressTextureDarkening();
    }

    @Inject(
            method = "draw(Lnet/dries007/tfc/common/recipes/KnappingRecipe;Lmezz/jei/api/gui/ingredient/IRecipeSlotsView;Lnet/minecraft/client/gui/GuiGraphics;DD)V",
            at = @At("RETURN"),
            require = 0,
            remap = false
    )
    private void blxckoutfx$restoreJeiKnappingTextures(CallbackInfo ci) {
        if (!blxckoutfx$jeiKnappingTextureDarkeningSuppressed) {
            return;
        }

        blxckoutfx$jeiKnappingTextureDarkeningSuppressed = false;
        BlxckoutFXRenderContext.allowTextureDarkening();
    }

    @Inject(method = "renderWidget", at = @At("HEAD"), require = 0, remap = false)
    private void blxckoutfx$preserveKnappingButtonTexture(
            GuiGraphics graphics,
            int mouseX,
            int mouseY,
            float partialTick,
            CallbackInfo ci
    ) {
        if (blxckoutfx$isKnappingButton((Object) this)) {
            blxckoutfx$knappingButtonTextureDarkeningSuppressed = true;
            BlxckoutFXRenderContext.suppressTextureDarkening();
        }
    }

    @Inject(method = "renderWidget", at = @At("RETURN"), require = 0, remap = false)
    private void blxckoutfx$restoreKnappingButtonTexture(
            GuiGraphics graphics,
            int mouseX,
            int mouseY,
            float partialTick,
            CallbackInfo ci
    ) {
        if (!blxckoutfx$knappingButtonTextureDarkeningSuppressed) {
            return;
        }

        blxckoutfx$knappingButtonTextureDarkeningSuppressed = false;
        BlxckoutFXRenderContext.allowTextureDarkening();
    }

    @Inject(
            method = "renderBg",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIFFIIII)V",
                    shift = At.Shift.BEFORE,
                    remap = true
            ),
            require = 0,
            remap = false
    )
    private void blxckoutfx$preserveKnappingScreenTexture(
            GuiGraphics graphics,
            float partialTick,
            int mouseX,
            int mouseY,
            CallbackInfo ci
    ) {
        if (blxckoutfx$isKnappingScreen((Object) this)) {
            blxckoutfx$knappingScreenTextureDarkeningSuppressDepth++;
            BlxckoutFXRenderContext.suppressTextureDarkening();
        }
    }

    @Inject(
            method = "renderBg",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIFFIIII)V",
                    shift = At.Shift.AFTER,
                    remap = true
            ),
            require = 0,
            remap = false
    )
    private void blxckoutfx$restoreKnappingScreenTexture(
            GuiGraphics graphics,
            float partialTick,
            int mouseX,
            int mouseY,
            CallbackInfo ci
    ) {
        if (blxckoutfx$knappingScreenTextureDarkeningSuppressDepth <= 0) {
            return;
        }

        blxckoutfx$knappingScreenTextureDarkeningSuppressDepth--;
        BlxckoutFXRenderContext.allowTextureDarkening();
    }

}
