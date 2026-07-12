package com.lopew.blxckoutfx.client;

import com.lopew.blxckoutfx.client.config.BlxckoutFXConfigScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.lwjgl.input.Keyboard;

public final class BlxckoutFXKeybinds {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String CATEGORY = "key.categories.blxckoutfx";

    private static KeyBinding openConfigKey;

    public static void init() {
        openConfigKey = new KeyBinding(
                "key.blxckoutfx.open_config",
                Keyboard.KEY_O,
                CATEGORY
        );
        ClientRegistry.registerKeyBinding(openConfigKey);
        LOGGER.info("Registered BlxckoutFX config keybind: {}", openConfigKey.getKeyDescription());
    }

    public static KeyBinding getOpenConfigKey() {
        return openConfigKey;
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        try {
            if (openConfigKey != null && openConfigKey.isPressed()) {
                Minecraft minecraft = Minecraft.getMinecraft();
                if (minecraft != null) {
                    minecraft.displayGuiScreen(new BlxckoutFXConfigScreen(minecraft.currentScreen));
                }
            }
        } catch (Exception e) {
            LOGGER.error("Exception in BlxckoutFX key input handler", e);
        }
    }

    public BlxckoutFXKeybinds() {
    }
}
