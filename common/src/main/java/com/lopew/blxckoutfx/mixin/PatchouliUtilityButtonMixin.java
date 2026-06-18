package com.lopew.blxckoutfx.mixin;

import com.lopew.blxckoutfx.client.BlxckoutFXRenderContext;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Pseudo
@Mixin(targets = "vazkii.patchouli.client.book.gui.button.GuiButtonBook", remap = false)
public class PatchouliUtilityButtonMixin {
    @Shadow
    @Final
    protected List<Component> tooltip;

    @Inject(method = "renderWidget", at = @At("HEAD"), require = 0)
    private void blxckoutfx$suppressPatchouliUtilityButtonDarkening(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        blxckoutfx$suppressIfUtilityButton();
    }

    @Inject(method = "renderWidget", at = @At("RETURN"), require = 0)
    private void blxckoutfx$restorePatchouliUtilityButtonDarkening(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        blxckoutfx$restoreIfUtilityButton();
    }

    @Inject(method = "method_48579", at = @At("HEAD"), require = 0)
    private void blxckoutfx$suppressPatchouliUtilityButtonDarkeningFabric(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        blxckoutfx$suppressIfUtilityButton();
    }

    @Inject(method = "method_48579", at = @At("RETURN"), require = 0)
    private void blxckoutfx$restorePatchouliUtilityButtonDarkeningFabric(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        blxckoutfx$restoreIfUtilityButton();
    }

    private void blxckoutfx$suppressIfUtilityButton() {
        if (blxckoutfx$isUtilityButton()) {
            BlxckoutFXRenderContext.suppressTextureDarkening();
        }
    }

    private void blxckoutfx$restoreIfUtilityButton() {
        if (blxckoutfx$isUtilityButton()) {
            BlxckoutFXRenderContext.allowTextureDarkening();
        }
    }

    private boolean blxckoutfx$isUtilityButton() {
        if (tooltip == null || tooltip.isEmpty()) {
            return false;
        }

        if (tooltip.get(0).getContents() instanceof TranslatableContents contents) {
            String key = contents.getKey();
            return "patchouli.gui.lexicon.button.resize".equals(key)
                    || "patchouli.gui.lexicon.button.history".equals(key)
                    || "patchouli.gui.lexicon.button.advancements".equals(key)
                    || "patchouli.gui.lexicon.button.editor".equals(key);
        }

        return false;
    }
}
