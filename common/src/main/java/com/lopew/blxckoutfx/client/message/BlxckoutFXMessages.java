package com.lopew.blxckoutfx.client.message;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public final class BlxckoutFXMessages {
    private static final int BRAND_BLX_COLOR = 0x71838F;
    private static final int BRAND_CK_COLOR = 0x7E919D;
    private static final int BRAND_OUT_COLOR = 0x8B9FAA;
    private static final int BRAND_FX_COLOR = 0x5DD6D0;
    private static final int DETAIL_COLOR = 0x878787;
    private static final int SUCCESS_COLOR = 0x75D6A3;
    private static final int WARNING_COLOR = 0xE5B96B;
    private static final int ERROR_COLOR = 0xE07777;

    public static void reloadComplete(Minecraft minecraft, long elapsedMillis) {
        show(minecraft, eventCard(
                SUCCESS_COLOR,
                Component.translatable("ui.blxckoutfx.reload.complete.title"),
                Component.translatable("ui.blxckoutfx.reload.complete.detail", elapsedMillis)
        ));
    }

    public static void reloadUnavailable(Minecraft minecraft) {
        show(minecraft, eventCard(
                WARNING_COLOR,
                Component.translatable("ui.blxckoutfx.reload.unavailable.title"),
                Component.translatable("ui.blxckoutfx.reload.unavailable.detail")
        ));
    }

    public static void reloadFailed(Minecraft minecraft) {
        show(minecraft, eventCard(
                ERROR_COLOR,
                Component.translatable("ui.blxckoutfx.reload.failed.title"),
                Component.translatable("ui.blxckoutfx.reload.failed.detail")
        ));
    }

    public static void resourceReloadStarted(Minecraft minecraft) {
        show(minecraft, eventCard(
                WARNING_COLOR,
                Component.translatable("ui.blxckoutfx.resource_reload.started.title"),
                Component.translatable("ui.blxckoutfx.resource_reload.started.detail")
        ));
    }

    public static void resourceReloadInProgress(Minecraft minecraft) {
        show(minecraft, eventCard(
                WARNING_COLOR,
                Component.translatable("ui.blxckoutfx.resource_reload.in_progress.title"),
                Component.translatable("ui.blxckoutfx.resource_reload.in_progress.detail")
        ));
    }

    public static void resourceReloadComplete(Minecraft minecraft, long elapsedMillis) {
        show(minecraft, eventCard(
                SUCCESS_COLOR,
                Component.translatable("ui.blxckoutfx.resource_reload.complete.title"),
                Component.translatable("ui.blxckoutfx.resource_reload.complete.detail", elapsedMillis)
        ));
    }

    public static void resourceReloadFailed(Minecraft minecraft) {
        show(minecraft, eventCard(
                ERROR_COLOR,
                Component.translatable("ui.blxckoutfx.resource_reload.failed.title"),
                Component.translatable("ui.blxckoutfx.resource_reload.failed.detail")
        ));
    }

    public static void allReloadStarted(Minecraft minecraft) {
        show(minecraft, eventCard(
                WARNING_COLOR,
                Component.translatable("ui.blxckoutfx.all_reload.started.title"),
                Component.translatable("ui.blxckoutfx.all_reload.started.detail")
        ));
    }

    public static void allReloadComplete(Minecraft minecraft, long elapsedMillis) {
        show(minecraft, eventCard(
                SUCCESS_COLOR,
                Component.translatable("ui.blxckoutfx.all_reload.complete.title"),
                Component.translatable("ui.blxckoutfx.all_reload.complete.detail", elapsedMillis)
        ));
    }

    public static void allReloadFailed(Minecraft minecraft) {
        show(minecraft, eventCard(
                ERROR_COLOR,
                Component.translatable("ui.blxckoutfx.all_reload.failed.title"),
                Component.translatable("ui.blxckoutfx.all_reload.failed.detail")
        ));
    }

    private static Component eventCard(int accentColor, Component title, Component detail) {
        return Component.empty()
                .append(Component.literal("\n"))
                .append(Component.translatable("ui.blxckoutfx.message.event_marker").withStyle(style -> style.withColor(accentColor)))
                .append(Component.translatable("ui.blxckoutfx.message.brand.blx").withStyle(style -> style.withColor(BRAND_BLX_COLOR)))
                .append(Component.translatable("ui.blxckoutfx.message.brand.ck").withStyle(style -> style.withColor(BRAND_CK_COLOR)))
                .append(Component.translatable("ui.blxckoutfx.message.brand.out").withStyle(style -> style.withColor(BRAND_OUT_COLOR)))
                .append(Component.translatable("ui.blxckoutfx.message.brand.fx").withStyle(style -> style.withColor(BRAND_FX_COLOR)))
                .append(Component.literal("  "))
                .append(title.copy().withStyle(style -> style.withColor(accentColor)))
                .append(Component.literal("\n  "))
                .append(detail.copy().withStyle(style -> style.withColor(DETAIL_COLOR)))
                .append(Component.literal("\n"));
    }

    private static void show(Minecraft minecraft, Component message) {
        if (minecraft != null && minecraft.player != null) {
            minecraft.player.displayClientMessage(message, false);
        }
    }

    private BlxckoutFXMessages() {
    }
}
