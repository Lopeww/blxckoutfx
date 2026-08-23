package com.lopew.blxckoutfx.client.reload;

import com.lopew.blxckoutfx.BlxckoutFX;
import com.lopew.blxckoutfx.client.config.BlxckoutFXClientConfig;
import com.lopew.blxckoutfx.client.gui.BlxckoutFXGUIHandler;
import com.lopew.blxckoutfx.client.message.BlxckoutFXMessages;
import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import java.util.concurrent.atomic.AtomicBoolean;
import net.minecraft.client.Minecraft;

/** Client-only developer reload operations shared by commands and the config screen. */
public final class BlxckoutFXReloadService {
    private static final AtomicBoolean RESOURCE_RELOAD_IN_FLIGHT = new AtomicBoolean();

    public static boolean reload() {
        Minecraft minecraft = Minecraft.getInstance();
        if (!isDeveloperMode(minecraft)) {
            return false;
        }

        long startedAt = System.nanoTime();
        try {
            reloadClientState();
            BlxckoutFXMessages.reloadComplete(minecraft, elapsedMillis(startedAt));
            return true;
        } catch (RuntimeException exception) {
            BlxckoutFX.LOGGER.error("Failed to reload BlxckoutFX client state", exception);
            BlxckoutFXMessages.reloadFailed(minecraft);
            return false;
        }
    }

    public static boolean reloadResources() {
        return reloadResources(false);
    }

    public static boolean reloadAll() {
        return reloadResources(true);
    }

    private static boolean reloadResources(boolean reloadClientStateAfterSuccess) {
        Minecraft minecraft = Minecraft.getInstance();
        if (!isDeveloperMode(minecraft)) {
            return false;
        }
        if (!RESOURCE_RELOAD_IN_FLIGHT.compareAndSet(false, true)) {
            BlxckoutFXMessages.resourceReloadInProgress(minecraft);
            return false;
        }

        long startedAt = System.nanoTime();

        try {
            if (reloadClientStateAfterSuccess) {
                BlxckoutFXMessages.allReloadStarted(minecraft);
            } else {
                BlxckoutFXMessages.resourceReloadStarted(minecraft);
            }
            minecraft.reloadResourcePacks().whenComplete((ignored, throwable) -> minecraft.execute(() -> {
                try {
                    if (throwable != null) {
                        BlxckoutFX.LOGGER.error("Minecraft resource reload failed", throwable);
                        showFailure(minecraft, reloadClientStateAfterSuccess);
                        return;
                    }

                    if (reloadClientStateAfterSuccess) {
                        reloadClientState();
                    }
                    showComplete(minecraft, reloadClientStateAfterSuccess, startedAt);
                } catch (RuntimeException exception) {
                    BlxckoutFX.LOGGER.error("Failed to finish BlxckoutFX reload", exception);
                    showFailure(minecraft, reloadClientStateAfterSuccess);
                } finally {
                    RESOURCE_RELOAD_IN_FLIGHT.set(false);
                }
            }));
            return true;
        } catch (RuntimeException exception) {
            RESOURCE_RELOAD_IN_FLIGHT.set(false);
            BlxckoutFX.LOGGER.error("Failed to start Minecraft resource reload", exception);
            showFailure(minecraft, reloadClientStateAfterSuccess);
            return false;
        }
    }

    private static boolean isDeveloperMode(Minecraft minecraft) {
        if (BlxckoutFXClientConfig.load().isDeveloperMode()) {
            return true;
        }
        BlxckoutFXMessages.reloadUnavailable(minecraft);
        return false;
    }

    private static void reloadClientState() {
        BlxckoutFXRenderContext.reset();
        BlxckoutFXGUIHandler.reloadForDevelopment();
    }

    private static void showComplete(Minecraft minecraft, boolean all, long startedAt) {
        if (all) {
            BlxckoutFXMessages.allReloadComplete(minecraft, elapsedMillis(startedAt));
        } else {
            BlxckoutFXMessages.resourceReloadComplete(minecraft, elapsedMillis(startedAt));
        }
    }

    private static void showFailure(Minecraft minecraft, boolean all) {
        if (all) {
            BlxckoutFXMessages.allReloadFailed(minecraft);
        } else {
            BlxckoutFXMessages.resourceReloadFailed(minecraft);
        }
    }

    private static long elapsedMillis(long startedAt) {
        return (System.nanoTime() - startedAt) / 1_000_000L;
    }

    private BlxckoutFXReloadService() {
    }
}
