package com.lopew.blxckoutfx.client.integration.journeymap;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class BlxckoutFXJourneyMapIconColorizer {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Map<ResourceLocation, DynamicTexture> WAYPOINT_ACTION_ICONS = new ConcurrentHashMap<>();
    private static final int ACTION_ICON_CHANNEL = 0xF0;

    public static DynamicTexture getWaypointActionIcon(ResourceLocation iconResource, DynamicTexture fallback) {
        if (!isWaypointActionIcon(iconResource) || !isWaypointManagerScreen()) {
            return fallback;
        }

        DynamicTexture cached = WAYPOINT_ACTION_ICONS.get(iconResource);
        if (cached != null) {
            return cached;
        }

        DynamicTexture recolored = createWaypointActionIcon(iconResource);
        if (recolored == null) {
            return fallback;
        }

        DynamicTexture existing = WAYPOINT_ACTION_ICONS.putIfAbsent(iconResource, recolored);
        if (existing != null) {
            recolored.close();
            return existing;
        }

        return recolored;
    }

    private static DynamicTexture createWaypointActionIcon(ResourceLocation iconResource) {
        Minecraft minecraft = Minecraft.getInstance();

        try (InputStream stream = minecraft.getResourceManager().open(iconResource);
             NativeImage sourceImage = NativeImage.read(stream)) {
            NativeImage recoloredImage = new NativeImage(sourceImage.getWidth(), sourceImage.getHeight(), true);

            for (int y = 0; y < sourceImage.getHeight(); y++) {
                for (int x = 0; x < sourceImage.getWidth(); x++) {
                    int pixel = sourceImage.getPixelRGBA(x, y);
                    int alpha = pixel >>> 24;
                    if (alpha == 0) {
                        recoloredImage.setPixelRGBA(x, y, 0);
                        continue;
                    }

                    recoloredImage.setPixelRGBA(x, y, packGray(alpha, ACTION_ICON_CHANNEL));
                }
            }

            DynamicTexture texture = new DynamicTexture(recoloredImage);
            texture.upload();
            return texture;
        } catch (IOException exception) {
            LOGGER.warn("Failed to recolor JourneyMap icon {}", iconResource, exception);
            return null;
        }
    }

    private static int packGray(int alpha, int channel) {
        return (alpha << 24) | (channel << 16) | (channel << 8) | channel;
    }

    private static boolean isWaypointActionIcon(ResourceLocation iconResource) {
        if (iconResource == null) {
            return false;
        }

        String path = iconResource.getPath();
        return path.endsWith("teleport.png")
                || path.endsWith("pin.png")
                || path.endsWith("share.png");
    }

    private static boolean isWaypointManagerScreen() {
        Screen screen = Minecraft.getInstance().screen;
        return screen != null
                && screen.getClass().getName().startsWith("journeymap.client.ui.waypointmanager.");
    }

    private BlxckoutFXJourneyMapIconColorizer() {
    }
}
