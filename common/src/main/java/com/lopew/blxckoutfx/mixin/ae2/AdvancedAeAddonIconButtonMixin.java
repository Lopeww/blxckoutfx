package com.lopew.blxckoutfx.mixin.ae2;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import com.lopew.blxckoutfx.client.render.BlxckoutFXShaders;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;

@Pseudo
@Mixin(targets = "net.pedroksl.ae2addonlib.client.widgets.AddonIconButton", remap = false)
public class AdvancedAeAddonIconButtonMixin {
    @WrapOperation(
            method = "renderWidget",
            at = @At(
                    value = "INVOKE",
                    target = "Lappeng/client/gui/style/Blitter;blit(Lnet/minecraft/client/gui/GuiGraphics;)V",
                    ordinal = 1
            ),
            require = 0
    )
    private void blxckoutfx$keepHalfSizeAdvancedAeIconReadable(
            @Coerce Object blitter,
            GuiGraphics guiGraphics,
            Operation<Void> original
    ) {
        blxckoutfx$renderAdvancedAeIconWithoutDarkening((Object) this, blitter, guiGraphics, original);
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
    private void blxckoutfx$keepFullSizeAdvancedAeIconReadable(
            @Coerce Object blitter,
            GuiGraphics guiGraphics,
            Operation<Void> original
    ) {
        blxckoutfx$renderAdvancedAeIconWithoutDarkening((Object) this, blitter, guiGraphics, original);
    }

    @Unique
    private static void blxckoutfx$renderAdvancedAeIconWithoutDarkening(
            Object button,
            @Coerce Object blitter,
            GuiGraphics guiGraphics,
            Operation<Void> original
    ) {
        if (!BlxckoutFXShaders.isEnabled() || !blxckoutfx$shouldKeepAdvancedAeIconReadable(button)) {
            original.call(blitter, guiGraphics);
            return;
        }

        BlxckoutFXRenderContext.suppressTextureDarkening();
        try {
            original.call(blitter, guiGraphics);
        } finally {
            BlxckoutFXRenderContext.allowTextureDarkening();
        }
    }

    @Unique
    private static boolean blxckoutfx$shouldKeepAdvancedAeIconReadable(Object button) {
        if (button == null) {
            return false;
        }

        String buttonClassName = button.getClass().getName();
        if (buttonClassName.startsWith("net.pedroksl.advanced_ae.")) {
            return blxckoutfx$isReadableAdvancedAeToggleButton(button);
        }

        if (buttonClassName.startsWith("net.pedroksl.ae2addonlib.")
                && blxckoutfx$isAdvancedAeScreen(Minecraft.getInstance().screen)) {
            return blxckoutfx$isReadableAdvancedAeActionButton(button);
        }

        return false;
    }

    @Unique
    private static boolean blxckoutfx$isReadableAdvancedAeToggleButton(Object button) {

        Object icon = blxckoutfx$invokeNoArg(button, "getIcon");
        if (icon instanceof Enum<?> enumIcon) {
            String iconClassName = enumIcon.getDeclaringClass().getName();
            String iconName = enumIcon.name();
            if ("net.pedroksl.advanced_ae.client.gui.widgets.AAEIcon".equals(iconClassName)
                    && blxckoutfx$isReadableAdvancedAeIcon(iconName)) {
                return true;
            }
        }

        Object setting = blxckoutfx$invokeNoArg(button, "getSetting");
        Object settingName = setting == null ? null : blxckoutfx$invokeNoArg(setting, "getName");
        return settingName instanceof String name && blxckoutfx$isReadableAdvancedAeSetting(name);
    }

    @Unique
    private static boolean blxckoutfx$isReadableAdvancedAeActionButton(Object button) {
        Object action = blxckoutfx$invokeNoArg(button, "getAction");
        if (action instanceof Enum<?> enumAction) {
            String actionClassName = enumAction.getDeclaringClass().getName();
            String actionName = enumAction.name();
            if ("net.pedroksl.ae2addonlib.client.widgets.AddonActionItems".equals(actionClassName)
                    && ("DIRECTIONAL_OUTPUT".equals(actionName)
                    || "CLEAR".equals(actionName)
                    || "FLUID_FLUSH".equals(actionName))) {
                return true;
            }
        }

        Object icon = blxckoutfx$invokeNoArg(button, "getIcon");
        if (icon instanceof Enum<?> enumIcon) {
            String iconClassName = enumIcon.getDeclaringClass().getName();
            String iconName = enumIcon.name();
            if ("net.pedroksl.ae2addonlib.client.widgets.LibIcons".equals(iconClassName)
                    && ("DIRECTION_OUTPUT".equals(iconName) || "CLEAR_SMALL".equals(iconName))) {
                return true;
            }
        }

        return false;
    }

    @Unique
    private static boolean blxckoutfx$isAdvancedAeScreen(Screen screen) {
        return screen != null && screen.getClass().getName().startsWith("net.pedroksl.advanced_ae.");
    }

    @Unique
    private static boolean blxckoutfx$isReadableAdvancedAeIcon(String iconName) {
        return "REGULATE_ON".equals(iconName)
                || "REGULATE_OFF".equals(iconName)
                || "ME_EXPORT_ON".equals(iconName)
                || "ME_EXPORT_OFF".equals(iconName)
                || "SHOW_ON_CRAFTER_TERMINAL".equals(iconName)
                || "HIDE_ON_CRAFTER_TERMINAL".equals(iconName)
                || "CRAFTER_TERMINAL_VISIBLE".equals(iconName)
                || "CRAFTER_TERMINAL_ALL".equals(iconName)
                || "CRAFTER_TERMINAL_NOT_FULL".equals(iconName);
    }

    @Unique
    private static boolean blxckoutfx$isReadableAdvancedAeSetting(String settingName) {
        return "regulate_stock".equalsIgnoreCase(settingName)
                || "show_quantum_crafters".equalsIgnoreCase(settingName)
                || "me_export".equalsIgnoreCase(settingName)
                || "quantum_crafter_terminal".equalsIgnoreCase(settingName);
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
}
