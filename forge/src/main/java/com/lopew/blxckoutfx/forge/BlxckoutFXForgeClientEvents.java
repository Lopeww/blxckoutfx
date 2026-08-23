package com.lopew.blxckoutfx.forge;

import com.lopew.blxckoutfx.BlxckoutFX;
import com.lopew.blxckoutfx.client.config.BlxckoutFXConfigScreen;
import com.lopew.blxckoutfx.client.gui.BlxckoutFXGUIHandler;
import com.lopew.blxckoutfx.client.reload.BlxckoutFXReloadService;
import com.lopew.blxckoutfx.client.render.BlxckoutFXShaders;
import net.minecraft.commands.Commands;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.IOException;

public final class BlxckoutFXForgeClientEvents {
    @Mod.EventBusSubscriber(modid = BlxckoutFX.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static final class ModBusEvents {
        @SubscribeEvent
        public static void onRegisterShaders(RegisterShadersEvent event) {
            try {
                BlxckoutFXShaders.registerShaders((id, vertexFormat, loadCallback) ->
                        event.registerShader(new ShaderInstance(event.getResourceProvider(), id, vertexFormat), loadCallback));
                BlxckoutFX.LOGGER.info("Registered BlxckoutFX GUI shaders");
            } catch (IOException exception) {
                throw new RuntimeException("Failed to load BlxckoutFX shaders", exception);
            }
        }
    }

    @Mod.EventBusSubscriber(modid = BlxckoutFX.MOD_ID, value = Dist.CLIENT)
    public static final class ForgeBusEvents {
        @SubscribeEvent
        public static void onRegisterClientCommands(RegisterClientCommandsEvent event) {
            event.getDispatcher().register(
                    Commands.literal("blxckout")
                            .executes(context -> openConfigScreen())
                            .then(Commands.literal("reload")
                                    .executes(context -> BlxckoutFXReloadService.reload() ? 1 : 0))
                            .then(Commands.literal("reload-resources")
                                    .executes(context -> BlxckoutFXReloadService.reloadResources() ? 1 : 0))
                            .then(Commands.literal("reload-all")
                                    .executes(context -> BlxckoutFXReloadService.reloadAll() ? 1 : 0))
            );
            event.getDispatcher().register(
                    Commands.literal("blxckoutfx")
                            .executes(context -> openConfigScreen())
                            .then(Commands.literal("reload")
                                    .executes(context -> BlxckoutFXReloadService.reload() ? 1 : 0))
                            .then(Commands.literal("reload-resources")
                                    .executes(context -> BlxckoutFXReloadService.reloadResources() ? 1 : 0))
                            .then(Commands.literal("reload-all")
                                    .executes(context -> BlxckoutFXReloadService.reloadAll() ? 1 : 0))
            );
            event.getDispatcher().register(
                    Commands.literal("bfx")
                            .executes(context -> openConfigScreen())
                            .then(Commands.literal("reload")
                                    .executes(context -> BlxckoutFXReloadService.reload() ? 1 : 0))
                            .then(Commands.literal("reload-resources")
                                    .executes(context -> BlxckoutFXReloadService.reloadResources() ? 1 : 0))
                            .then(Commands.literal("reload-all")
                                    .executes(context -> BlxckoutFXReloadService.reloadAll() ? 1 : 0))
            );
        }

        private static int openConfigScreen() {
            Minecraft minecraft = Minecraft.getInstance();
            // Chat closes immediately after command execution, so open on the next client task.
            minecraft.tell(() -> minecraft.setScreen(new BlxckoutFXConfigScreen(minecraft.screen)));
            return 1;
        }

        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if (event.phase == TickEvent.Phase.END) {
                BlxckoutFXGUIHandler.handleClientTick(Minecraft.getInstance());
            }
        }

        @SubscribeEvent
        public static void onScreenInit(ScreenEvent.Init.Post event) {
            BlxckoutFXGUIHandler.onScreenInit(event.getScreen(), event::addListener);
        }

        @SubscribeEvent
        public static void onMousePressed(ScreenEvent.MouseButtonPressed.Pre event) {
            if (BlxckoutFXGUIHandler.onMouseClicked(event.getScreen(), event.getMouseX(), event.getMouseY(), event.getButton())) {
                event.setCanceled(true);
            }
        }

        @SubscribeEvent
        public static void onMouseDragged(ScreenEvent.MouseDragged.Pre event) {
            if (BlxckoutFXGUIHandler.onMouseDragged(
                    event.getScreen(),
                    event.getMouseX(),
                    event.getMouseY(),
                    event.getMouseButton(),
                    event.getDragX(),
                    event.getDragY()
            )) {
                event.setCanceled(true);
            }
        }

        @SubscribeEvent
        public static void onMouseReleased(ScreenEvent.MouseButtonReleased.Pre event) {
            if (BlxckoutFXGUIHandler.onMouseReleased(event.getScreen(), event.getMouseX(), event.getMouseY(), event.getButton())) {
                event.setCanceled(true);
            }
        }

        @SubscribeEvent
        public static void onScreenRenderPost(ScreenEvent.Render.Post event) {
            BlxckoutFXGUIHandler.handleScreenRender(event.getScreen(), event.getMouseX(), event.getMouseY());
        }

        @SubscribeEvent
        public static void onScreenClose(ScreenEvent.Closing event) {
            BlxckoutFXGUIHandler.onScreenClose(event.getScreen());
        }
    }

    private BlxckoutFXForgeClientEvents() {
    }
}
