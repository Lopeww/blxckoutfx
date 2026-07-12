package com.lopew.blxckoutfx.client.integration.ae2;

import com.lopew.blxckoutfx.client.BlxckoutFXRenderContext;
import com.lopew.blxckoutfx.client.BlxckoutFXShaders;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.Deque;

public final class BlxckoutFXAe2IconHooks {
    private static final int NO_SCOPE = 0;
    private static final int PRESERVE_ICON = 1;
    private static final int SORT_BUTTON_REMAP = 2;
    private static final ThreadLocal<Deque<Integer>> ICON_SCOPES = new ThreadLocal<Deque<Integer>>() {
        @Override
        protected Deque<Integer> initialValue() {
            return new ArrayDeque<>();
        }
    };

    public static void beginIconRender(Object button) {
        int scope = NO_SCOPE;
        if (BlxckoutFXShaders.isEnabled() && isAe2Screen()) {
            if (needsDarkGlyphRemap(button)) {
                scope = SORT_BUTTON_REMAP;
                BlxckoutFXRenderContext.enterAe2SortButtonIconRemap();
            } else {
                scope = PRESERVE_ICON;
                BlxckoutFXRenderContext.suppressTextureDarkening();
            }
        }
        ICON_SCOPES.get().push(scope);
    }

    public static void endIconRender() {
        Deque<Integer> scopes = ICON_SCOPES.get();
        if (scopes.isEmpty()) {
            return;
        }

        int scope = scopes.pop();
        if (scope == SORT_BUTTON_REMAP) {
            BlxckoutFXRenderContext.exitAe2SortButtonIconRemap();
        } else if (scope == PRESERVE_ICON) {
            BlxckoutFXRenderContext.allowTextureDarkening();
        }

        if (scopes.isEmpty()) {
            ICON_SCOPES.remove();
        }
    }

    public static int adjustSearchFieldBackgroundColor(int color) {
        if (!BlxckoutFXShaders.isBalancedPresetActive() && !BlxckoutFXShaders.isDarkPresetActive()) {
            return color;
        }

        int red = ((color >>> 16) & 0xFF) / 2;
        int green = ((color >>> 8) & 0xFF) / 2;
        int blue = (color & 0xFF) / 2;
        return (color & 0xFF000000) | (red << 16) | (green << 8) | blue;
    }

    private static boolean isAe2Screen() {
        Minecraft minecraft = Minecraft.getMinecraft();
        GuiScreen screen = minecraft == null ? null : minecraft.currentScreen;
        return screen != null && screen.getClass().getName().startsWith("appeng.client.gui.");
    }

    private static boolean needsDarkGlyphRemap(Object button) {
        return hasSetting(button, "SORT_") || hasSetting(button, "POWER_UNITS");
    }

    private static boolean hasSetting(Object button, String settingName) {
        if (button == null) {
            return false;
        }

        try {
            Method getSetting = button.getClass().getMethod("getSetting");
            Object setting = getSetting.invoke(button);
            if (!(setting instanceof Enum)) {
                return false;
            }
            String actualName = ((Enum<?>) setting).name();
            return settingName.endsWith("_") ? actualName.startsWith(settingName) : settingName.equals(actualName);
        } catch (ReflectiveOperationException ignored) {
            return false;
        }
    }

    private BlxckoutFXAe2IconHooks() {
    }
}
