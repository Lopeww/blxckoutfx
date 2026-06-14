package com.lopew.blxckoutfx.neoforge;

import com.lopew.blxckoutfx.BlxckoutFX;
import com.lopew.blxckoutfx.client.BlackoutFXKeybinds;
import com.lopew.blxckoutfx.client.BlxckoutFXGUIHandler;
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
        BlackoutFXKeybinds.handleClientTick(Minecraft.getInstance());
    }

    @SubscribeEvent
    public static void onScreenInit(ScreenEvent.Init.Post event) {
        BlxckoutFXGUIHandler.onScreenInit(event.getScreen(), event::addListener);
    }

    @SubscribeEvent
    public static void onScreenClose(ScreenEvent.Closing event) {
        BlxckoutFXGUIHandler.onScreenClose(event.getScreen());
    }

    private BlxckoutFXNeoForgeClientEvents() {
    }
}
