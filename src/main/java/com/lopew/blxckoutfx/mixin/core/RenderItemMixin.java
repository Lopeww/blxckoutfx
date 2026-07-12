package com.lopew.blxckoutfx.mixin.core;

import com.lopew.blxckoutfx.client.BlxckoutFXRenderContext;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderItem.class)
public class RenderItemMixin {
    @Inject(method = "renderItemModelIntoGUI", at = @At("HEAD"), require = 1)
    private void blxckoutfx$enterItemGuiRender(ItemStack stack, int x, int y, IBakedModel model, CallbackInfo ci) {
        BlxckoutFXRenderContext.enterItemRender();
    }

    @Inject(method = "renderItemModelIntoGUI", at = @At("RETURN"), require = 1)
    private void blxckoutfx$exitItemGuiRender(ItemStack stack, int x, int y, IBakedModel model, CallbackInfo ci) {
        BlxckoutFXRenderContext.exitItemRender();
    }
}
