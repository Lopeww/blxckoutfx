package com.lopew.blxckoutfx.mixin;

import com.mojang.blaze3d.shaders.ShaderType;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.client.renderer.ShaderManager$CompilationCache")
public class ShaderManagerCompilationCacheMixin {

    @Inject(
            method = "getShaderSource(Lnet/minecraft/resources/Identifier;Lcom/mojang/blaze3d/shaders/ShaderType;)Ljava/lang/String;",
            at = @At("TAIL"),
            cancellable = true,
            require = 0
    )
    private void blxckoutfx$injectShaderSources(Identifier id, ShaderType type, CallbackInfoReturnable<String> cir) {
        if (type == ShaderType.FRAGMENT) {
            if (id.getNamespace().equals("minecraft")) {
                String path = id.getPath();
                if (path.equals("core/position_tex_color")) {
                    cir.setReturnValue(com.lopew.blxckoutfx.client.BlxckoutFXShaders.getDarkPositionTexColorShaderSource());
                }
            }
        }
    }
}
