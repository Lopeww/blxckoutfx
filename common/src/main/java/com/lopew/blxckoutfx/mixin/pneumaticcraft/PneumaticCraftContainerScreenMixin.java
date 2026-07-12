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
@Mixin(targets = "me.desht.pneumaticcraft.client.gui.AbstractPneumaticCraftContainerScreen", remap = false)
public class PneumaticCraftContainerScreenMixin {
    private static final ResourceLocation LIQUID_COMPRESSOR_TEXTURE = new ResourceLocation("pneumaticcraft", "textures/gui/gui_liquid_compressor.png");
    private static final ResourceLocation ADVANCED_LIQUID_COMPRESSOR_TEXTURE = new ResourceLocation("pneumaticcraft", "textures/gui/gui_advanced_liquid_compressor.png");
    private static final ResourceLocation THERMO_PLANT_TEXTURE = new ResourceLocation("pneumaticcraft", "textures/gui/gui_thermopneumatic_processing_plant.png");
    private static final int[][] LIQUID_COMPRESSOR_UPGRADE_ICON_SEGMENTS = {
            {27, 14, 2, 1},
            {26, 15, 4, 1},
            {25, 16, 6, 1},
            {24, 17, 8, 1},
            {24, 18, 9, 1},
            {24, 19, 10, 1},
            {25, 20, 6, 1},
            {25, 21, 6, 1},
            {25, 22, 6, 1},
            {25, 23, 6, 1},
            {25, 24, 6, 1},
            {25, 25, 6, 1},
    };
    private static final int[][] THERMO_PLANT_SIDE_ARROW_SEGMENTS = {
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
    private boolean blxckoutfx$enteredPneumaticCraftScreenRender;

    @Inject(method = "render", at = @At("HEAD"), require = 0)
    private void blxckoutfx$enterPneumaticCraftScreenRender(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        blxckoutfx$enteredPneumaticCraftScreenRender = true;
        BlxckoutFXRenderContext.enterPneumaticCraftScreenRender();
    }

    @Inject(method = "render", at = @At("RETURN"), require = 0)
    private void blxckoutfx$exitPneumaticCraftScreenRender(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (!blxckoutfx$enteredPneumaticCraftScreenRender) {
            return;
        }

        blxckoutfx$enteredPneumaticCraftScreenRender = false;
        BlxckoutFXRenderContext.exitPneumaticCraftScreenRender();
    }

    @ModifyArg(
            method = "renderLabels",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;IIIZ)I",
                    ordinal = 0
            ),
            index = 4,
            require = 0
    )
    private int blxckoutfx$adjustMachineTitleColor(int color) {
        return BlxckoutFXTextColors.adjustGeneralTextColor(color, true, false);
    }

    @ModifyArg(
            method = "renderLabels",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;IIIZ)I",
                    ordinal = 1
            ),
            index = 4,
            require = 0
    )
    private int blxckoutfx$adjustInventoryTitleColor(int color) {
        return BlxckoutFXTextColors.adjustGeneralTextColor(color, true, false);
    }

    @Inject(method = "renderBg", at = @At("RETURN"), require = 0)
    private void blxckoutfx$redrawSpecialGuiSegments(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY, CallbackInfo ci) {
        ResourceLocation texture = blxckoutfx$getGuiTexture();
        if (texture == null) {
            return;
        }

        int[][] segments;
        if (LIQUID_COMPRESSOR_TEXTURE.equals(texture) || ADVANCED_LIQUID_COMPRESSOR_TEXTURE.equals(texture)) {
            segments = LIQUID_COMPRESSOR_UPGRADE_ICON_SEGMENTS;
        } else if (THERMO_PLANT_TEXTURE.equals(texture)) {
            segments = THERMO_PLANT_SIDE_ARROW_SEGMENTS;
        } else {
            return;
        }

        int leftPos = blxckoutfx$getIntField("leftPos");
        int topPos = blxckoutfx$getIntField("topPos");
        if (leftPos == Integer.MIN_VALUE || topPos == Integer.MIN_VALUE) {
            return;
        }

        BlxckoutFXRenderContext.suppressTextureDarkening();
        try {
            guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
            for (int[] segment : segments) {
                guiGraphics.blit(texture, leftPos + segment[0], topPos + segment[1], segment[0], segment[1], segment[2], segment[3]);
            }
        } finally {
            BlxckoutFXRenderContext.allowTextureDarkening();
        }
    }

    private ResourceLocation blxckoutfx$getGuiTexture() {
        try {
            return (ResourceLocation) this.getClass().getMethod("getGuiTexture").invoke(this);
        } catch (ReflectiveOperationException ignored) {
            return null;
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
