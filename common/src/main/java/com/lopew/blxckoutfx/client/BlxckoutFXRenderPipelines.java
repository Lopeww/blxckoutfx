package com.lopew.blxckoutfx.client;

import com.lopew.blxckoutfx.BlxckoutFX;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.resources.Identifier;

public final class BlxckoutFXRenderPipelines {
    public static final Identifier BUTTON_TEXTURED_LOCATION = Identifier.fromNamespaceAndPath(
            BlxckoutFX.MOD_ID,
            "pipeline/button_textured"
    );

    public static final RenderPipeline BUTTON_TEXTURED = RenderPipeline.builder()
            .withLocation(BUTTON_TEXTURED_LOCATION)
            .withUniform("DynamicTransforms", UniformType.UNIFORM_BUFFER)
            .withUniform("Projection", UniformType.UNIFORM_BUFFER)
            .withVertexShader("core/position_tex_color")
            .withFragmentShader("core/position_tex_color")
            .withSampler("Sampler0")
            .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
            .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, VertexFormat.Mode.QUADS)
            .build();

    private BlxckoutFXRenderPipelines() {
    }
}
