package com.lopew.blxckoutfx.client.reload;

import com.lopew.blxckoutfx.BlxckoutFX;
import com.lopew.blxckoutfx.client.config.BlxckoutFXClientConfig;
import com.lopew.blxckoutfx.client.gui.BlxckoutFXGUIHandler;
import com.lopew.blxckoutfx.client.message.BlxckoutFXMessages;
import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import net.minecraft.client.Minecraft;

import java.util.concurrent.atomic.AtomicBoolean;

public final class BlxckoutFXReloadService {
    private static final AtomicBoolean RESOURCE_RELOAD_IN_PROGRESS = new AtomicBoolean();

    public static boolean reload() {
        Minecraft minecraft = Minecraft.getInstance();
        BlxckoutFXClientConfig config = BlxckoutFXClientConfig.load();
        if (!config.isDeveloperMode()) {
            BlxckoutFXMessages.reloadUnavailable(minecraft);
            return false;
        }

        long startedAt = System.nanoTime();
        try {
            reloadClientState();
            BlxckoutFX.LOGGER.info("Reloaded BlxckoutFX client state");
            BlxckoutFXMessages.reloadComplete(minecraft, (System.nanoTime() - startedAt) / 1_000_000L);
            return true;
        } catch (RuntimeException exception) {
            BlxckoutFX.LOGGER.error("Failed to reload BlxckoutFX client state", exception);
            BlxckoutFXMessages.reloadFailed(minecraft);
            return false;
        }
    }

    public static boolean reloadAll() {
        Minecraft minecraft = Minecraft.getInstance();
        BlxckoutFXClientConfig config = BlxckoutFXClientConfig.load();
        if (!config.isDeveloperMode()) {
            BlxckoutFXMessages.reloadUnavailable(minecraft);
            return false;
        }
        if (!tryAcquireResourceReload(minecraft)) {
            return false;
        }

        long startedAt = System.nanoTime();
        try {
            reloadClientState();
            BlxckoutFXMessages.allReloadStarted(minecraft);
            minecraft.reloadResourcePacks().whenComplete((ignored, throwable) -> {
                try {
                    minecraft.execute(() -> {
                        try {
                            if (throwable != null) {
                                BlxckoutFX.LOGGER.error("Failed to reload all BlxckoutFX development state", throwable);
                                BlxckoutFXMessages.allReloadFailed(minecraft);
                                return;
                            }

                            BlxckoutFX.LOGGER.info("Reloaded BlxckoutFX client state and Minecraft resources");
                            BlxckoutFXMessages.allReloadComplete(minecraft, (System.nanoTime() - startedAt) / 1_000_000L);
                        } finally {
                            RESOURCE_RELOAD_IN_PROGRESS.set(false);
                        }
                    });
                } catch (RuntimeException exception) {
                    RESOURCE_RELOAD_IN_PROGRESS.set(false);
                    throw exception;
                }
            });
            return true;
        } catch (RuntimeException exception) {
            RESOURCE_RELOAD_IN_PROGRESS.set(false);
            BlxckoutFX.LOGGER.error("Failed to start all BlxckoutFX reload operations", exception);
            BlxckoutFXMessages.allReloadFailed(minecraft);
            return false;
        }
    }

    public static boolean reloadResources() {
        Minecraft minecraft = Minecraft.getInstance();
        BlxckoutFXClientConfig config = BlxckoutFXClientConfig.load();
        if (!config.isDeveloperMode()) {
            BlxckoutFXMessages.reloadUnavailable(minecraft);
            return false;
        }
        if (!tryAcquireResourceReload(minecraft)) {
            return false;
        }

        long startedAt = System.nanoTime();
        try {
            BlxckoutFXMessages.resourceReloadStarted(minecraft);
            minecraft.reloadResourcePacks().whenComplete((ignored, throwable) -> {
                try {
                    minecraft.execute(() -> {
                        try {
                            if (throwable != null) {
                                BlxckoutFX.LOGGER.error("Failed to reload Minecraft resources", throwable);
                                BlxckoutFXMessages.resourceReloadFailed(minecraft);
                                return;
                            }

                            BlxckoutFX.LOGGER.info("Reloaded Minecraft resources for BlxckoutFX");
                            BlxckoutFXMessages.resourceReloadComplete(minecraft, (System.nanoTime() - startedAt) / 1_000_000L);
                        } finally {
                            RESOURCE_RELOAD_IN_PROGRESS.set(false);
                        }
                    });
                } catch (RuntimeException exception) {
                    RESOURCE_RELOAD_IN_PROGRESS.set(false);
                    throw exception;
                }
            });
            return true;
        } catch (RuntimeException exception) {
            RESOURCE_RELOAD_IN_PROGRESS.set(false);
            BlxckoutFX.LOGGER.error("Failed to start Minecraft resource reload", exception);
            BlxckoutFXMessages.resourceReloadFailed(minecraft);
            return false;
        }
    }

    private static void reloadClientState() {
        BlxckoutFXRenderContext.reset();
        BlxckoutFXGUIHandler.reloadForDevelopment();
    }

    private static boolean tryAcquireResourceReload(Minecraft minecraft) {
        if (RESOURCE_RELOAD_IN_PROGRESS.compareAndSet(false, true)) {
            return true;
        }

        BlxckoutFXMessages.resourceReloadInProgress(minecraft);
        return false;
    }

    private BlxckoutFXReloadService() {
    }
}
