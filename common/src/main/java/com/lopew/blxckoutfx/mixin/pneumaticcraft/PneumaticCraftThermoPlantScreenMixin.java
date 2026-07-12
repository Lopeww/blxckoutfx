package com.lopew.blxckoutfx.mixin.pneumaticcraft;

import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import com.lopew.blxckoutfx.client.render.BlxckoutFXTextColors;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "me.desht.pneumaticcraft.client.gui.ThermopneumaticProcessingPlantScreen", remap = false)
public class PneumaticCraftThermoPlantScreenMixin {
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation("pneumaticcraft", "textures/gui/gui_thermopneumatic_processing_plant.png");
    private static final int[][] SIDE_ARROW_SEGMENTS = {
            {132, 89, 2, 1},
            {131, 90, 4, 1},
            {130, 91, 6, 1},
            {129, 92, 8, 1},
            {128, 93, 10, 1},
            {127, 94, 12, 1},
            {130, 95, 6, 1},
            {130, 96, 6, 1},
            {130, 97, 6, 1},
            {130, 98, 6, 1},
            {130, 99, 6, 1},
            {130, 100, 6, 1},
    };

    @ModifyArg(
            method = "renderLabels",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/util/FormattedCharSequence;FFIZ)I",
                    ordinal = 0
            ),
            index = 4,
            require = 0
    )
    private int blxckoutfx$adjustThermoTitleColor(int color) {
        return BlxckoutFXTextColors.adjustGeneralTextColor(color, true, false);
    }

    @Inject(method = "renderBg", at = @At("HEAD"), require = 0)
    private void blxckoutfx$capThermoDarkeningStart(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY, CallbackInfo ci) {
        BlxckoutFXRenderContext.enterBalancedCappedDarkening();
    }

    @Inject(method = "renderBg", at = @At("RETURN"), require = 0)
    private void blxckoutfx$capThermoDarkeningEnd(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY, CallbackInfo ci) {
        BlxckoutFXRenderContext.exitBalancedCappedDarkening();

        int leftPos = blxckoutfx$getIntField("leftPos");
        int topPos = blxckoutfx$getIntField("topPos");
        if (leftPos == Integer.MIN_VALUE || topPos == Integer.MIN_VALUE) {
            return;
        }

        BlxckoutFXRenderContext.suppressTextureDarkening();
        try {
            guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
            for (int[] segment : SIDE_ARROW_SEGMENTS) {
                guiGraphics.blit(GUI_TEXTURE, leftPos + segment[0], topPos + segment[1], segment[0], segment[1], segment[2], segment[3]);
            }
        } finally {
            BlxckoutFXRenderContext.allowTextureDarkening();
        }
    }

    private int blxckoutfx$getIntField(String fieldName) {
        Class<?> type = this.getClass();
        while (type != null) {
            try {
                java.lang.reflect.Field field = type.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.getInt(this);
            } catch (ReflectiveOperationException ignored) {
                type = type.getSuperclass();
            }
        }
        return Integer.MIN_VALUE;
    }
}
