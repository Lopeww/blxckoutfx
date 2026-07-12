package com.lopew.blxckoutfx.mixin.ae2;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.lopew.blxckoutfx.client.integration.ae2.BlxckoutFXAe2Support;
import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "appeng.client.gui.widgets.IconButton", remap = false)
public class Ae2IconButtonMixin {
    @Unique
    private boolean blxckoutfx$transferButtonRender;
    @Unique
    private boolean blxckoutfx$sortButtonRender;
    @Unique
    private boolean blxckoutfx$schedulingButtonRender;

    @Inject(method = "renderWidget", at = @At("HEAD"), require = 0)
    private void blxckoutfx$enterTransferButtonRender(
            GuiGraphics guiGraphics,
            int mouseX,
            int mouseY,
            float partialTick,
            CallbackInfo ci
    ) {
        Button self = (Button) (Object) this;
        if (!blxckoutfx$shouldUseTransferButtonScope(self)) {
            return;
        }

        int[] fbRect = blxckoutfx$toFramebufferRect(self);
        this.blxckoutfx$transferButtonRender = true;
        BlxckoutFXRenderContext.enterAe2TransferButtonRender(fbRect[0], fbRect[1], fbRect[2], fbRect[3]);
    }

    @Inject(method = "renderWidget", at = @At("RETURN"), require = 0)
    private void blxckoutfx$exitTransferButtonRender(
            GuiGraphics guiGraphics,
            int mouseX,
            int mouseY,
            float partialTick,
            CallbackInfo ci
    ) {
        if (!this.blxckoutfx$transferButtonRender) {
            return;
        }

        this.blxckoutfx$transferButtonRender = false;
        BlxckoutFXRenderContext.exitAe2TransferButtonRender();
    }

    @Inject(method = "renderWidget", at = @At("HEAD"), require = 0)
    private void blxckoutfx$enterSortButtonRender(
            GuiGraphics guiGraphics,
            int mouseX,
            int mouseY,
            float partialTick,
            CallbackInfo ci
    ) {
        Button self = (Button) (Object) this;
        if (!blxckoutfx$shouldUseSortButtonScope(self)) {
            return;
        }

        int[] fbRect = blxckoutfx$toFramebufferRect(self);
        this.blxckoutfx$sortButtonRender = true;
        BlxckoutFXRenderContext.enterAe2SortButtonRender(fbRect[0], fbRect[1], fbRect[2], fbRect[3]);
    }

    @Inject(method = "renderWidget", at = @At("RETURN"), require = 0)
    private void blxckoutfx$exitSortButtonRender(
            GuiGraphics guiGraphics,
            int mouseX,
            int mouseY,
            float partialTick,
            CallbackInfo ci
    ) {
        if (!this.blxckoutfx$sortButtonRender) {
            return;
        }

        this.blxckoutfx$sortButtonRender = false;
        BlxckoutFXRenderContext.exitAe2SortButtonRender();
    }

    @Inject(method = "renderWidget", at = @At("HEAD"), require = 0)
    private void blxckoutfx$enterSchedulingButtonRender(
            GuiGraphics guiGraphics,
            int mouseX,
            int mouseY,
            float partialTick,
            CallbackInfo ci
    ) {
        Button self = (Button) (Object) this;
        if (!blxckoutfx$shouldUseSchedulingButtonScope(self)) {
            return;
        }

        int[] fbRect = blxckoutfx$toFramebufferRect(self);
        this.blxckoutfx$schedulingButtonRender = true;
        BlxckoutFXRenderContext.enterAe2SchedulingButtonRender(fbRect[0], fbRect[1], fbRect[2], fbRect[3]);
    }

    @Inject(method = "renderWidget", at = @At("RETURN"), require = 0)
    private void blxckoutfx$exitSchedulingButtonRender(
            GuiGraphics guiGraphics,
            int mouseX,
            int mouseY,
            float partialTick,
            CallbackInfo ci
    ) {
        if (!this.blxckoutfx$schedulingButtonRender) {
            return;
        }

        this.blxckoutfx$schedulingButtonRender = false;
        BlxckoutFXRenderContext.exitAe2SchedulingButtonRender();
    }

    @WrapOperation(
            method = "renderWidget",
            at = @At(
                    value = "INVOKE",
                    target = "Lappeng/client/gui/style/Blitter;blit(Lnet/minecraft/client/gui/GuiGraphics;)V",
                    ordinal = 1
            ),
            require = 0
    )
    private void blxckoutfx$keepAe2HalfIconReadable(
            @Coerce Object blitter,
            GuiGraphics guiGraphics,
            Operation<Void> original
    ) {
        blxckoutfx$renderAe2Icon(blitter, guiGraphics, original);
    }

    @WrapOperation(
            method = "renderWidget",
            at = @At(
                    value = "INVOKE",
                    target = "Lappeng/client/gui/style/Blitter;blit(Lnet/minecraft/client/gui/GuiGraphics;)V",
                    ordinal = 3
            ),
            require = 0
    )
    private void blxckoutfx$keepAe2FullIconReadable(
            @Coerce Object blitter,
            GuiGraphics guiGraphics,
            Operation<Void> original
    ) {
        blxckoutfx$renderAe2Icon(blitter, guiGraphics, original);
    }

    @Unique
    private void blxckoutfx$renderAe2Icon(
            @Coerce Object blitter,
            GuiGraphics guiGraphics,
            Operation<Void> original
    ) {
        if (this.blxckoutfx$transferButtonRender) {
            original.call(blitter, guiGraphics);
            return;
        }

        if (this.blxckoutfx$sortButtonRender) {
            original.call(blitter, guiGraphics);
            return;
        }

        if (this.blxckoutfx$schedulingButtonRender) {
            original.call(blitter, guiGraphics);
            return;
        }

        if (!BlxckoutFXAe2Support.isBlxckoutAe2ThemeActive()
                || !BlxckoutFXRenderContext.isRenderingAe2Screen()) {
            original.call(blitter, guiGraphics);
            return;
        }

        boolean powerUnitIcon = blxckoutfx$isPowerUnitButton((Button) (Object) this);
        if (!powerUnitIcon) {
            BlxckoutFXRenderContext.suppressTextureDarkening();
        }
        BlxckoutFXRenderContext.enterAe2IconRender();
        if (powerUnitIcon) {
            BlxckoutFXRenderContext.enterAe2PowerUnitIconRender();
        }
        try {
            original.call(blitter, guiGraphics);
        } finally {
            if (powerUnitIcon) {
                BlxckoutFXRenderContext.exitAe2PowerUnitIconRender();
            }
            BlxckoutFXRenderContext.exitAe2IconRender();
            if (!powerUnitIcon) {
                BlxckoutFXRenderContext.allowTextureDarkening();
            }
        }
    }

    @Unique
    private static boolean blxckoutfx$isPowerUnitButton(Button self) {
        Screen screen = Minecraft.getInstance().screen;
        if (!BlxckoutFXAe2Support.isAe2Screen(screen)) {
            return false;
        }

        Object icon = blxckoutfx$invokeNoArg(self, "getIcon");
        if (icon instanceof Enum<?> enumIcon) {
            String iconName = enumIcon.name();
            if ("POWER_UNIT_AE".equals(iconName)
                    || "POWER_UNIT_FE".equals(iconName)
                    || "POWER_UNIT_RF".equals(iconName)) {
                return true;
            }
        }

        Object setting = blxckoutfx$invokeNoArg(self, "getSetting");
        Object settingName = setting == null ? null : blxckoutfx$invokeNoArg(setting, "getName");
        return settingName instanceof String name && "power_units".equalsIgnoreCase(name);
    }

    @Unique
    private static boolean blxckoutfx$shouldUseTransferButtonScope(Button self) {
        if (!BlxckoutFXAe2Support.isBlxckoutAe2ThemeActive()
                || !BlxckoutFXRenderContext.isRenderingAe2Screen()) {
            return false;
        }

        Screen currentScreen = Minecraft.getInstance().screen;
        if (!BlxckoutFXAe2Support.isAe2CraftingTermScreenLike(currentScreen)) {
            return false;
        }

        if (!"appeng.client.gui.widgets.ActionButton".equals(self.getClass().getName())) {
            return false;
        }

        Object icon = blxckoutfx$invokeNoArg(self, "getIcon");
        if (!(icon instanceof Enum<?> enumIcon)) {
            return false;
        }

        String iconName = enumIcon.name();
        return "ARROW_UP".equals(iconName) || "ARROW_DOWN".equals(iconName);
    }

    @Unique
    private static boolean blxckoutfx$shouldUseSortButtonScope(Button self) {
        if (!BlxckoutFXAe2Support.isBlxckoutAe2ThemeActive()
                || !BlxckoutFXRenderContext.isRenderingAe2Screen()) {
            return false;
        }

        if (!blxckoutfx$isSettingToggleButton(self)) {
            return false;
        }

        Object setting = blxckoutfx$invokeNoArg(self, "getSetting");
        if (setting != null) {
            Object settingNameObj = blxckoutfx$invokeNoArg(setting, "getName");
            if (settingNameObj instanceof String settingName && settingName.startsWith("SORT_")) {
                return true;
            }
        }

        Object icon = blxckoutfx$invokeNoArg(self, "getIcon");
        if (blxckoutfx$isSortButtonIcon(icon)) {
            return true;
        }

        return blxckoutfx$looksLikeSortToggle(self);
    }

    @Unique
    private static boolean blxckoutfx$isSettingToggleButton(Button self) {
        Class<?> current = self.getClass();
        while (current != null) {
            if ("appeng.client.gui.widgets.SettingToggleButton".equals(current.getName())) {
                return true;
            }
            current = current.getSuperclass();
        }
        return false;
    }

    @Unique
    private static Object blxckoutfx$invokeNoArg(Object target, String methodName) {
        Class<?> current = target.getClass();
        while (current != null) {
            try {
                var method = current.getDeclaredMethod(methodName);
                method.setAccessible(true);
                return method.invoke(target);
            } catch (ReflectiveOperationException ignored) {
                current = current.getSuperclass();
            }
        }
        return null;
    }

    @Unique
    private static boolean blxckoutfx$isSortButtonIcon(Object icon) {
        if (!(icon instanceof Enum<?> enumIcon)) {
            return false;
        }

        String iconName = enumIcon.name();
        return "SORT_BY_NAME".equals(iconName)
                || "SORT_BY_AMOUNT".equals(iconName)
                || "SORT_BY_MOD".equals(iconName)
                || "SORT_BY_INVENTORY_TWEAKS".equals(iconName)
                || "ARROW_UP".equals(iconName)
                || "ARROW_DOWN".equals(iconName);
    }

    @Unique
    private static boolean blxckoutfx$looksLikeSortToggle(Button self) {
        String className = self.getClass().getName();
        if (!className.contains("SettingToggleButton")) {
            return false;
        }

        String message = self.getMessage().getString();
        if (blxckoutfx$hasSortTextSignal(message)) {
            return true;
        }

        Object tooltip = blxckoutfx$invokeNoArg(self, "getTooltipMessage");
        if (tooltip instanceof java.util.List<?> tooltipLines) {
            for (Object line : tooltipLines) {
                if (line != null && blxckoutfx$hasSortTextSignal(line.toString())) {
                    return true;
                }
            }
        }

        return false;
    }

    @Unique
    private static boolean blxckoutfx$shouldUseSchedulingButtonScope(Button self) {
        if (!BlxckoutFXAe2Support.isBlxckoutAe2ThemeActive()
                || !BlxckoutFXRenderContext.isRenderingAe2Screen()) {
            return false;
        }

        if (!blxckoutfx$isSettingToggleButton(self)) {
            return false;
        }

        Object setting = blxckoutfx$invokeNoArg(self, "getSetting");
        if (setting != null) {
            Object settingNameObj = blxckoutfx$invokeNoArg(setting, "getName");
            if ("SCHEDULING_MODE".equals(settingNameObj)) {
                return true;
            }
        }

        Object icon = blxckoutfx$invokeNoArg(self, "getIcon");
        if (blxckoutfx$isSchedulingButtonIcon(icon)) {
            return true;
        }

        return blxckoutfx$looksLikeSchedulingToggle(self);
    }

    @Unique
    private static boolean blxckoutfx$isSchedulingButtonIcon(Object icon) {
        if (!(icon instanceof Enum<?> enumIcon)) {
            return false;
        }

        String iconName = enumIcon.name();
        return "SCHEDULING_DEFAULT".equals(iconName)
                || "SCHEDULING_ROUND_ROBIN".equals(iconName)
                || "SCHEDULING_RANDOM".equals(iconName);
    }

    @Unique
    private static boolean blxckoutfx$looksLikeSchedulingToggle(Button self) {
        String className = self.getClass().getName();
        if (!className.contains("SettingToggleButton")) {
            return false;
        }

        String message = self.getMessage().getString();
        if (blxckoutfx$hasSchedulingTextSignal(message)) {
            return true;
        }

        Object tooltip = blxckoutfx$invokeNoArg(self, "getTooltipMessage");
        if (tooltip instanceof java.util.List<?> tooltipLines) {
            for (Object line : tooltipLines) {
                if (line != null && blxckoutfx$hasSchedulingTextSignal(line.toString())) {
                    return true;
                }
            }
        }

        return false;
    }

    @Unique
    private static boolean blxckoutfx$hasSchedulingTextSignal(String text) {
        String normalized = text.toLowerCase(java.util.Locale.ROOT);
        return normalized.contains("scheduling");
    }

    @Unique
    private static boolean blxckoutfx$hasSortTextSignal(String text) {
        String normalized = text.toLowerCase(java.util.Locale.ROOT);
        return normalized.contains("sort")
                || normalized.contains("ascending")
                || normalized.contains("descending");
    }

    @Unique
    private static int[] blxckoutfx$toFramebufferRect(Button button) {
        var window = Minecraft.getInstance().getWindow();
        double scale = Math.max(1.0D, window.getGuiScale());
        int framebufferHeight = window.getHeight();
        boolean halfSize = Boolean.TRUE.equals(blxckoutfx$invokeNoArg(button, "isHalfSize"));
        int width = halfSize ? 8 : button.getWidth();
        int height = halfSize ? 8 : button.getHeight();

        int guiX1 = button.getX() - 1;
        int guiY1 = button.getY() - 1;
        int guiX2 = button.getX() + width + 1;
        int guiY2 = button.getY() + height + 1;

        int x1 = (int) Math.floor(guiX1 * scale);
        int x2 = (int) Math.ceil(guiX2 * scale);
        int y1 = framebufferHeight - (int) Math.ceil(guiY2 * scale);
        int y2 = framebufferHeight - (int) Math.floor(guiY1 * scale);
        return new int[]{x1, y1, x2, y2};
    }
}
