package com.lopew.blxckoutfx.mixin.core;

import com.lopew.blxckoutfx.client.integration.ae2.BlxckoutFXAe2Support;
import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiGraphics.class)
public class GuiGraphicsItemRenderMixin {
    @Unique
    private boolean blxckoutfx$cappedAe2ItemDarkening;

    @Inject(
            method = "renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;IIII)V",
            at = @At("HEAD"),
            require = 0
    )
    private void blxckoutfx$capAe2ItemDarkening(
            LivingEntity entity,
            Level level,
            ItemStack stack,
            int x,
            int y,
            int seed,
            int guiOffset,
            CallbackInfo ci
    ) {
        if (!BlxckoutFXRenderContext.isRenderingAe2Screen() || !BlxckoutFXAe2Support.isAe2ThemeEnabled()) {
            return;
        }

        blxckoutfx$cappedAe2ItemDarkening = true;
        BlxckoutFXRenderContext.enterBalancedCappedDarkening();
    }

    @Inject(
            method = "renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;IIII)V",
            at = @At("RETURN"),
            require = 0
    )
    private void blxckoutfx$restoreAe2ItemDarkeningCap(
            LivingEntity entity,
            Level level,
            ItemStack stack,
            int x,
            int y,
            int seed,
            int guiOffset,
            CallbackInfo ci
    ) {
        if (!blxckoutfx$cappedAe2ItemDarkening) {
            return;
        }

        blxckoutfx$cappedAe2ItemDarkening = false;
        BlxckoutFXRenderContext.exitBalancedCappedDarkening();
    }
}

