package com.lopew.blxckoutfx.mixin;

import org.spongepowered.asm.mixin.Mixin;

/**
 * GlRenderPassMixin - the GlRenderPassAccessor @Mixin interface handles field access directly.
 * This class is kept for compatibility but the accessor interface does the actual work.
 */
@Mixin(targets = "com.mojang.blaze3d.opengl.GlRenderPass", remap = false)
public class GlRenderPassMixin {
    // Field access is handled via GlRenderPassAccessor
}
