package com.lopew.blxckoutfx.mixin.core;

import com.lopew.blxckoutfx.client.integration.ae2.BlxckoutFXAe2Support;
import com.lopew.blxckoutfx.client.render.BlxckoutFXShaders;
import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderPipelines;
import com.lopew.blxckoutfx.client.render.BlxckoutFXRenderContext;
import com.lopew.blxckoutfx.client.screen.BlxckoutFXScreenExclusions;
import com.lopew.blxckoutfx.mixin.core.GlRenderPassAccessor;
import com.mojang.blaze3d.opengl.GlProgram;
import com.mojang.blaze3d.opengl.GlRenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.lwjgl.opengl.GL20;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;

@Mixin(targets = "com.mojang.blaze3d.opengl.GlCommandEncoder", remap = false)
public class GlCommandEncoderMixin {
    private static final String AE2_INTERFACE_SCREEN_CLASS = "appeng.client.gui.implementations.InterfaceScreen";
    private static final String EXTENDED_AE_INTERFACE_SCREEN_CLASS =
            "com.glodblock.github.extendedae.client.gui.GuiExInterface";

    @Inject(
            method = "trySetup(Lcom/mojang/blaze3d/opengl/GlRenderPass;Ljava/util/Collection;)Z",
            at = @At("TAIL")
    )
    private void blxckoutfx$injectShaderUniforms(@Coerce Object renderPassObj,
                                                  Collection<String> dynamicUniforms,
                                                  CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) {
            return;
        }

        GlRenderPassAccessor accessor = (GlRenderPassAccessor) renderPassObj;
        GlRenderPipeline glRenderPipeline = accessor.blxckoutfx$getPipeline();
        if (glRenderPipeline == null) {
            return;
        }

        RenderPipeline pipeline = glRenderPipeline.info();
        GlProgram glProgram = glRenderPipeline.program();
        if (pipeline == null || glProgram == null) {
            return;
        }

        int programId = glProgram.getProgramId();

        int factorLoc = GL20.glGetUniformLocation(programId, "DivideFactor");
        if (factorLoc != -1) {
            boolean shouldDarken = shouldDarken(pipeline);
            float factor = shouldDarken ? getDivideFactor(pipeline) : 1.0F;
            GL20.glUniform1f(factorLoc, factor);
        }

        int scaleLoc = GL20.glGetUniformLocation(programId, "PerceptionScale");
        if (scaleLoc != -1) {
            GL20.glUniform3f(scaleLoc, 0.299F, 0.587F, 0.114F);
        }

        int ae2CustomRemapModeLoc = GL20.glGetUniformLocation(programId, "Ae2CustomRemapMode");
        if (ae2CustomRemapModeLoc != -1) {
            boolean customRemapActive = BlxckoutFXAe2Support.isBlackoutAe2ThemeActive()
                    && BlxckoutFXRenderContext.isRenderingAe2CustomRemap();
            GL20.glUniform1f(ae2CustomRemapModeLoc, customRemapActive ? 1.0F : 0.0F);
        }

        int ae2ConfigArrowRectLoc = GL20.glGetUniformLocation(programId, "Ae2ConfigArrowRect");
        if (ae2ConfigArrowRectLoc != -1) {
            Screen currentScreen = Minecraft.getInstance().gui.screen();
            float rectX1 = 0.0F;
            float rectY1 = 0.0F;
            float rectX2 = 0.0F;
            float rectY2 = 0.0F;

            if (isInterfaceScreen(currentScreen)) {
                rectX1 = BlxckoutFXRenderContext.getAe2ConfigArrowRectX1();
                rectY1 = BlxckoutFXRenderContext.getAe2ConfigArrowRectY1();
                rectX2 = BlxckoutFXRenderContext.getAe2ConfigArrowRectX2();
                rectY2 = BlxckoutFXRenderContext.getAe2ConfigArrowRectY2();
            }

            GL20.glUniform4f(
                    ae2ConfigArrowRectLoc,
                    rectX1,
                    rectY1,
                    rectX2,
                    rectY2
            );
        }

        int ae2ConfigArrowSecondRectLoc = GL20.glGetUniformLocation(programId, "Ae2ConfigArrowSecondRect");
        if (ae2ConfigArrowSecondRectLoc != -1) {
            Screen currentScreen = Minecraft.getInstance().gui.screen();
            float rectX1 = 0.0F;
            float rectY1 = 0.0F;
            float rectX2 = 0.0F;
            float rectY2 = 0.0F;

            if (isInterfaceScreen(currentScreen)) {
                rectX1 = BlxckoutFXRenderContext.getAe2ConfigArrowSecondRectX1();
                rectY1 = BlxckoutFXRenderContext.getAe2ConfigArrowSecondRectY1();
                rectX2 = BlxckoutFXRenderContext.getAe2ConfigArrowSecondRectX2();
                rectY2 = BlxckoutFXRenderContext.getAe2ConfigArrowSecondRectY2();
            }

            GL20.glUniform4f(
                    ae2ConfigArrowSecondRectLoc,
                    rectX1,
                    rectY1,
                    rectX2,
                    rectY2
            );
        }

    }

    private static boolean isInterfaceScreen(Screen screen) {
        if (screen == null) {
            return false;
        }

        String screenClass = screen.getClass().getName();
        return AE2_INTERFACE_SCREEN_CLASS.equals(screenClass)
                || EXTENDED_AE_INTERFACE_SCREEN_CLASS.equals(screenClass);
    }

    private static boolean shouldDarken(RenderPipeline pipeline) {
        if (!BlxckoutFXShaders.isEnabled()) {
            return false;
        }

        if (BlxckoutFXRenderContext.isTextureDarkeningSuppressed()) {
            return false;
        }

        Minecraft minecraft = Minecraft.getInstance();
        Screen screen = minecraft.gui.screen();

        BlxckoutFXScreenExclusions.ScreenPolicy policy = BlxckoutFXScreenExclusions.getScreenPolicy(screen);
        if (policy == BlxckoutFXScreenExclusions.ScreenPolicy.NONE) {
            return false;
        }

        boolean guiTextured = "pipeline/gui_textured".equals(pipeline.getLocation().getPath());

        if (BlxckoutFXRenderPipelines.BUTTON_TEXTURED_LOCATION.equals(pipeline.getLocation())) {
            if (policy == BlxckoutFXScreenExclusions.ScreenPolicy.PARTIAL) {
                return true;
            }
            return true;
        }

        if (BlxckoutFXRenderPipelines.JEI_WIDGET_TEXTURED_LOCATION.equals(pipeline.getLocation())) {
            return true;
        }

        if (BlxckoutFXRenderPipelines.MODMENU_ICON_BUTTON_TEXTURED_LOCATION.equals(pipeline.getLocation())) {
            return true;
        }

        if (BlxckoutFXRenderPipelines.AE2_BLACKOUT_TEXTURED_LOCATION.equals(pipeline.getLocation())) {
            return false;
        }

        if (BlxckoutFXRenderContext.isRenderingAe2TooltipBackground()
                && BlxckoutFXAe2Support.isBlackoutAe2ThemeActive()) {
            return true;
        }

        if (BlxckoutFXAe2Support.isAe2Screen(screen) && !BlxckoutFXAe2Support.shouldDarkenAe2Screen()) {
            return false;
        }

        if (policy == BlxckoutFXScreenExclusions.ScreenPolicy.PARTIAL) {
            return false;
        }

        return screen != null
                && minecraft.level != null
                && !isModListScreen(screen)
                && guiTextured;
    }

    private static float getDivideFactor(RenderPipeline pipeline) {
        if (BlxckoutFXRenderPipelines.JEI_WIDGET_TEXTURED_LOCATION.equals(pipeline.getLocation())) {
            return Math.min(BlxckoutFXShaders.getCurrentPresetDivideFactor(), 2.35F);
        }

        return BlxckoutFXShaders.getCurrentPresetDivideFactor();
    }

    private static boolean isModListScreen(Screen screen) {
        String className = screen.getClass().getName().toLowerCase();
        return className.contains("modlist")
                || className.equals("com.terraformersmc.modmenu.gui.modsscreen")
                || className.startsWith("com.terraformersmc.modmenu.")
                || className.startsWith("net.neoforged.neoforge.client.gui.mod");
    }
}
