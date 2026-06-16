package com.lopew.blxckoutfx.forge;

import com.lopew.blxckoutfx.BlxckoutFX;
import com.lopew.blxckoutfx.client.BlackoutFXKeybinds;
import com.lopew.blxckoutfx.client.BlxckoutFXGUIHandler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BlxckoutFX.MOD_ID, value = Dist.CLIENT)
public final class BlxckoutFXForgeClientEvents {
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            BlackoutFXKeybinds.handleClientTick(Minecraft.getInstance());
        }
    }

    @SubscribeEvent
    public static void onScreenInit(ScreenEvent.Init.Post event) {
        BlxckoutFXGUIHandler.onScreenInit(event.getScreen(), event::addListener);
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

    private BlxckoutFXForgeClientEvents() {
    }
}
