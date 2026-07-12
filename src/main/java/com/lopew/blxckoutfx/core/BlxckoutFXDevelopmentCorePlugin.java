package com.lopew.blxckoutfx.core;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.Name("BlxckoutFXDevelopmentCore")
public final class BlxckoutFXDevelopmentCorePlugin extends BlxckoutFXCorePlugin {
    private static boolean mixinConfigured;

    public BlxckoutFXDevelopmentCorePlugin() {
        synchronized (BlxckoutFXDevelopmentCorePlugin.class) {
            if (mixinConfigured) {
                return;
            }

            MixinBootstrap.init();
            Mixins.addConfiguration("blxckoutfx.mixins.json");
            Mixins.addConfiguration("blxckoutfx.optional.mixins.json");
            mixinConfigured = true;
        }
    }
}
