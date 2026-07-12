package com.lopew.blxckoutfx.mixin.pneumaticcraft;

import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "me.desht.pneumaticcraft.client.gui.widget.WidgetAnimatedStat$StatIcon", remap = false)
public class PneumaticCraftAnimatedStatIconMixin {
    @Unique
    private static final int DEBUG_PANEL_X_OFFSET = -12;
    @Unique
    private static final int DEBUG_PANEL_Y_OFFSET = -12;
    @Unique
    private static final int DEBUG_PANEL_WIDTH = 76;
    @Unique
    private static final int DEBUG_PANEL_HEIGHT = 88;
    @Unique
    private boolean blxckoutfx$suppressedAnimatedStatIconDarkening;

    @Inject(method = "render", at = @At("HEAD"), require = 0)
    private void blxckoutfx$suppressAnimatedStatIconDarkening(GuiGraphics guiGraphics, int x, int y, boolean leftSided, CallbackInfo ci) {
        if (!blxckoutfx$usesResourceTexture()) {
            return;
        }

        blxckoutfx$suppressedAnimatedStatIconDarkening = true;
        BlxckoutFXRenderContext.suppressTextureDarkening();
    }

    @Inject(method = "render", at = @At("RETURN"), require = 0)
    private void blxckoutfx$restoreAnimatedStatIconDarkening(GuiGraphics guiGraphics, int x, int y, boolean leftSided, CallbackInfo ci) {
        if (!blxckoutfx$suppressedAnimatedStatIconDarkening) {
            return;
        }

        blxckoutfx$suppressedAnimatedStatIconDarkening = false;
        BlxckoutFXRenderContext.allowTextureDarkening();
        blxckoutfx$drawDebugPanelBounds(guiGraphics, x, y, leftSided);
    }

    @Unique
    private boolean blxckoutfx$usesResourceTexture() {
        try {
            java.lang.reflect.Field field = this.getClass().getDeclaredField("texture");
            field.setAccessible(true);
            Object either = field.get(this);
            if (either == null) {
                return false;
            }

            Object right = either.getClass().getMethod("right").invoke(either);
            return (boolean) right.getClass().getMethod("isPresent").invoke(right);
        } catch (ReflectiveOperationException ignored) {
            return false;
        }
    }

    @Unique
    private void blxckoutfx$drawDebugPanelBounds(GuiGraphics guiGraphics, int x, int y, boolean leftSided) {
        int x1 = x + (leftSided ? -DEBUG_PANEL_WIDTH - 4 : DEBUG_PANEL_X_OFFSET);
        int y1 = y + DEBUG_PANEL_Y_OFFSET;
        int x2 = x1 + DEBUG_PANEL_WIDTH;
        int y2 = y1 + DEBUG_PANEL_HEIGHT;

        guiGraphics.fill(x1, y1, x2, y2, 0x2200FFFF);
        guiGraphics.fill(x1, y1, x2, y1 + 2, 0xFFFF0000);
        guiGraphics.fill(x1, y2 - 2, x2, y2, 0xFFFF0000);
        guiGraphics.fill(x1, y1, x1 + 2, y2, 0xFFFF0000);
        guiGraphics.fill(x2 - 2, y1, x2, y2, 0xFFFF0000);
    }
}
