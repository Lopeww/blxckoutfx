package com.lopew.blxckoutfx.mixin.core;

import com.lopew.blxckoutfx.client.access.ChatScreenInputAccess;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.ChatScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ChatScreen.class)
public interface ChatScreenAccessor extends ChatScreenInputAccess {
    @Override
    @Accessor("input")
    EditBox blxckoutfx$getInput();
}
