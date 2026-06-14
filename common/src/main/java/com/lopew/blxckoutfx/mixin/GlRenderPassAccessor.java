package com.lopew.blxckoutfx.mixin;

import com.mojang.blaze3d.opengl.GlRenderPipeline;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(targets = "com.mojang.blaze3d.opengl.GlRenderPass", remap = false)
public interface GlRenderPassAccessor {

    @Accessor("pipeline")
    GlRenderPipeline blxckoutfx$getPipeline();
}
