package com.lopew.blxckoutfx.core;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.Name("BlxckoutFXCore")
public class BlxckoutFXCorePlugin implements IFMLLoadingPlugin {
    public BlxckoutFXCorePlugin() {
        MixinBootstrap.init();
        Mixins.addConfiguration("blxckoutfx.mixins.json");
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[] { "com.lopew.blxckoutfx.core.BlxckoutFXAccessTransformer" };
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
