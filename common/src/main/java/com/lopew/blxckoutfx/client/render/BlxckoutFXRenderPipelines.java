package com.lopew.blxckoutfx.client.render;

import com.lopew.blxckoutfx.BlxckoutFX;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.BindGroupLayouts;
import net.minecraft.resources.Identifier;

public final class BlxckoutFXRenderPipelines {
    public static final Identifier BUTTON_TEXTURED_LOCATION = Identifier.fromNamespaceAndPath(
            BlxckoutFX.MOD_ID,
            "pipeline/button_textured"
    );
    public static final Identifier CONTENT_ICON_TEXTURED_LOCATION = Identifier.fromNamespaceAndPath(
            BlxckoutFX.MOD_ID,
            "pipeline/content_icon_textured"
    );
    public static final Identifier WAYPOINT_ICON_TEXTURED_LOCATION = Identifier.fromNamespaceAndPath(
            BlxckoutFX.MOD_ID,
            "pipeline/waypoint_icon_textured"
    );
    public static final Identifier JEI_WIDGET_TEXTURED_LOCATION = Identifier.fromNamespaceAndPath(
            BlxckoutFX.MOD_ID,
            "pipeline/jei_widget_textured"
    );
    public static final Identifier AE2_BLACKOUT_TEXTURED_LOCATION = Identifier.fromNamespaceAndPath(
            BlxckoutFX.MOD_ID,
            "pipeline/ae2_blackout_textured"
    );
    public static final Identifier AE2_TRANSFER_BUTTON_TEXTURED_LOCATION = Identifier.fromNamespaceAndPath(
            BlxckoutFX.MOD_ID,
            "pipeline/ae2_transfer_button_textured"
    );
    public static final Identifier MODMENU_ICON_BUTTON_TEXTURED_LOCATION = Identifier.fromNamespaceAndPath(
            BlxckoutFX.MOD_ID,
            "pipeline/modmenu_icon_button_textured"
    );
    private static final Identifier AE2_BLACKOUT_POSITION_TEX_COLOR_SHADER = Identifier.fromNamespaceAndPath(
            BlxckoutFX.MOD_ID,
            "core/ae2_blackout_position_tex_color"
    );
    private static final Identifier AE2_TRANSFER_BUTTON_POSITION_TEX_COLOR_SHADER = Identifier.fromNamespaceAndPath(
            BlxckoutFX.MOD_ID,
            "core/ae2_transfer_button_position_tex_color"
    );
    private static final Identifier MODMENU_ICON_BUTTON_POSITION_TEX_COLOR_SHADER = Identifier.fromNamespaceAndPath(
            BlxckoutFX.MOD_ID,
            "core/modmenu_icon_button_position_tex_color"
    );

    public static final RenderPipeline BUTTON_TEXTURED = RenderPipeline.builder()
            .withLocation(BUTTON_TEXTURED_LOCATION)
            .withBindGroupLayout(BindGroupLayouts.MATRICES_PROJECTION)
            .withVertexShader("core/position_tex_color")
            .withFragmentShader("core/position_tex_color")
            .withBindGroupLayout(BindGroupLayouts.SAMPLER0)
            .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
            .withVertexBinding(0, DefaultVertexFormat.POSITION_TEX_COLOR)
            .withPrimitiveTopology(PrimitiveTopology.QUADS)
            .build();

    public static final RenderPipeline CONTENT_ICON_TEXTURED = RenderPipeline.builder()
            .withLocation(CONTENT_ICON_TEXTURED_LOCATION)
            .withBindGroupLayout(BindGroupLayouts.MATRICES_PROJECTION)
            .withVertexShader("core/position_tex_color")
            .withFragmentShader("core/position_tex_color")
            .withBindGroupLayout(BindGroupLayouts.SAMPLER0)
            .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
            .withVertexBinding(0, DefaultVertexFormat.POSITION_TEX_COLOR)
            .withPrimitiveTopology(PrimitiveTopology.QUADS)
            .build();

    public static final RenderPipeline WAYPOINT_ICON_TEXTURED = RenderPipeline.builder()
            .withLocation(WAYPOINT_ICON_TEXTURED_LOCATION)
            .withBindGroupLayout(BindGroupLayouts.MATRICES_PROJECTION)
            .withVertexShader("core/position_tex_color")
            .withFragmentShader("core/clean_position_tex_color")
            .withBindGroupLayout(BindGroupLayouts.SAMPLER0)
            .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
            .withVertexBinding(0, DefaultVertexFormat.POSITION_TEX_COLOR)
            .withPrimitiveTopology(PrimitiveTopology.QUADS)
            .build();

    public static final RenderPipeline JEI_WIDGET_TEXTURED = RenderPipeline.builder()
            .withLocation(JEI_WIDGET_TEXTURED_LOCATION)
            .withBindGroupLayout(BindGroupLayouts.MATRICES_PROJECTION)
            .withVertexShader("core/position_tex_color")
            .withFragmentShader("core/position_tex_color")
            .withBindGroupLayout(BindGroupLayouts.SAMPLER0)
            .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
            .withVertexBinding(0, DefaultVertexFormat.POSITION_TEX_COLOR)
            .withPrimitiveTopology(PrimitiveTopology.QUADS)
            .build();

    public static final RenderPipeline AE2_BLACKOUT_TEXTURED = RenderPipeline.builder()
            .withLocation(AE2_BLACKOUT_TEXTURED_LOCATION)
            .withBindGroupLayout(BindGroupLayouts.MATRICES_PROJECTION)
            .withVertexShader("core/position_tex_color")
            .withFragmentShader(AE2_BLACKOUT_POSITION_TEX_COLOR_SHADER)
            .withBindGroupLayout(BindGroupLayouts.SAMPLER0)
            .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
            .withVertexBinding(0, DefaultVertexFormat.POSITION_TEX_COLOR)
            .withPrimitiveTopology(PrimitiveTopology.QUADS)
            .build();

    public static final RenderPipeline AE2_TRANSFER_BUTTON_TEXTURED = RenderPipeline.builder()
            .withLocation(AE2_TRANSFER_BUTTON_TEXTURED_LOCATION)
            .withBindGroupLayout(BindGroupLayouts.MATRICES_PROJECTION)
            .withVertexShader("core/position_tex_color")
            .withFragmentShader(AE2_TRANSFER_BUTTON_POSITION_TEX_COLOR_SHADER)
            .withBindGroupLayout(BindGroupLayouts.SAMPLER0)
            .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
            .withVertexBinding(0, DefaultVertexFormat.POSITION_TEX_COLOR)
            .withPrimitiveTopology(PrimitiveTopology.QUADS)
            .build();

    public static final RenderPipeline MODMENU_ICON_BUTTON_TEXTURED = RenderPipeline.builder()
            .withLocation(MODMENU_ICON_BUTTON_TEXTURED_LOCATION)
            .withBindGroupLayout(BindGroupLayouts.MATRICES_PROJECTION)
            .withVertexShader("core/position_tex_color")
            .withFragmentShader(MODMENU_ICON_BUTTON_POSITION_TEX_COLOR_SHADER)
            .withBindGroupLayout(BindGroupLayouts.SAMPLER0)
            .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
            .withVertexBinding(0, DefaultVertexFormat.POSITION_TEX_COLOR)
            .withPrimitiveTopology(PrimitiveTopology.QUADS)
            .build();

    private BlxckoutFXRenderPipelines() {
    }
}
