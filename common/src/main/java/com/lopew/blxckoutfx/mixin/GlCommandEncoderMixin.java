package com.lopew.blxckoutfx.mixin;

import com.lopew.blxckoutfx.client.BlxckoutFXShaders;
import com.lopew.blxckoutfx.client.BlxckoutFXRenderPipelines;
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
            float factor = shouldDarken(pipeline) ? BlxckoutFXShaders.getCurrentPresetDivideFactor() : 1.0F;
            GL20.glUniform1f(factorLoc, factor);
        }

        int scaleLoc = GL20.glGetUniformLocation(programId, "PerceptionScale");
        if (scaleLoc != -1) {
            GL20.glUniform3f(scaleLoc, 0.299F, 0.587F, 0.114F);
        }
    }

    private static boolean shouldDarken(RenderPipeline pipeline) {
        if (!BlxckoutFXShaders.isEnabled()) {
            return false;
        }

        if (BlxckoutFXRenderPipelines.BUTTON_TEXTURED_LOCATION.equals(pipeline.getLocation())) {
            return true;
        }

        Minecraft minecraft = Minecraft.getInstance();
        Screen screen = minecraft.screen;
        return screen != null
                && minecraft.level != null
                && !isModListScreen(screen)
                && "pipeline/gui_textured".equals(pipeline.getLocation().getPath());
    }

    private static boolean isModListScreen(Screen screen) {
        String className = screen.getClass().getName().toLowerCase();
        return className.contains("modlist")
                || className.equals("com.terraformersmc.modmenu.gui.modsscreen")
                || className.startsWith("com.terraformersmc.modmenu.")
                || className.startsWith("net.neoforged.neoforge.client.gui.mod");
    }
}
