package com.lopew.blxckoutfx.mixin.core;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.CommandSuggestions;
import net.minecraft.client.gui.screens.ChatScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CommandSuggestions.SuggestionsList.class)
public class CommandSuggestionsListMixin {
    private static final int SELECTED_SUGGESTION_COLOR = 0xFFFFFF00;
    private static final int RELOAD_COLOR = 0xFF75D6A3;
    private static final int RESOURCE_RELOAD_COLOR = 0xFF70A7FF;
    private static final int ALL_RELOAD_COLOR = 0xFFE5B96B;
    private static final int SELECTED_RELOAD_COLOR = 0xFFC5F6D8;
    private static final int SELECTED_RESOURCE_RELOAD_COLOR = 0xFFC8DBFF;
    private static final int SELECTED_ALL_RELOAD_COLOR = 0xFFFFE0A3;

    @Redirect(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;III)I"
            )
    )
    private int blxckoutfx$drawColoredReloadSuggestion(GuiGraphics graphics, Font font, String suggestion, int x, int y, int color) {
        return graphics.drawString(font, suggestion, x, y, suggestionColor(suggestion, color));
    }

    private int suggestionColor(String suggestion, int originalColor) {
        if (!(Minecraft.getInstance().screen instanceof ChatScreen chatScreen)) {
            return originalColor;
        }

        String input = ((ChatScreenAccessor) chatScreen).blxckoutfx$getInput().getValue();
        if (!input.startsWith("/bfx ") && !input.startsWith("/blxckout ") && !input.startsWith("/blxckoutfx ")) {
            return originalColor;
        }
        return switch (suggestion) {
            case "reload" -> originalColor == SELECTED_SUGGESTION_COLOR ? SELECTED_RELOAD_COLOR : RELOAD_COLOR;
            case "reload-resources" -> originalColor == SELECTED_SUGGESTION_COLOR ? SELECTED_RESOURCE_RELOAD_COLOR : RESOURCE_RELOAD_COLOR;
            case "reload-all" -> originalColor == SELECTED_SUGGESTION_COLOR ? SELECTED_ALL_RELOAD_COLOR : ALL_RELOAD_COLOR;
            default -> originalColor;
        };
    }
}
