package com.lopew.blxckoutfx.neoforge;

import com.lopew.blxckoutfx.BlxckoutFX;
import com.lopew.blxckoutfx.client.gui.BlxckoutFXGUIHandler;
import com.lopew.blxckoutfx.client.gui.InventoryButton;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;

@EventBusSubscriber(modid = BlxckoutFX.MOD_ID, value = Dist.CLIENT)
public final class BlxckoutFXNeoForgeClientEvents {
    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        BlxckoutFXGUIHandler.handleClientTick(Minecraft.getInstance());
    }

    @SubscribeEvent
    public static void onScreenInit(ScreenEvent.Init.Post event) {
        BlxckoutFXGUIHandler.onScreenInit(event.getScreen(), new BlxckoutFXGUIHandler.ButtonSink() {
            @Override
            public void add(InventoryButton button) {
                event.addListener(button);
            }

            @Override
            public void remove(InventoryButton button) {
                event.removeListener(button);
            }
        });
    }

    @SubscribeEvent
    public static void onMousePressed(ScreenEvent.MouseButtonPressed.Pre event) {
        boolean handled = BlxckoutFXGUIHandler.onMouseClicked(event.getScreen(), event.getMouseX(), event.getMouseY(), event.getButton());

        if (handled) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onMouseDragged(ScreenEvent.MouseDragged.Pre event) {
        boolean handled = BlxckoutFXGUIHandler.onMouseDragged(event.getScreen(), event.getMouseX(), event.getMouseY(), event.getMouseButton(), event.getDragX(), event.getDragY());

        if (handled) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onMouseReleased(ScreenEvent.MouseButtonReleased.Pre event) {
        boolean handled = BlxckoutFXGUIHandler.onMouseReleased(event.getScreen(), event.getMouseX(), event.getMouseY(), event.getButton());

        if (handled) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onScreenClose(ScreenEvent.Closing event) {
        BlxckoutFXGUIHandler.onScreenClose(event.getScreen());
    }

    private BlxckoutFXNeoForgeClientEvents() {
    }
}
