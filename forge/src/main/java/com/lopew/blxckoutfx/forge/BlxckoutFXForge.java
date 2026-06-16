package com.lopew.blxckoutfx.forge;

import com.lopew.blxckoutfx.BlxckoutFX;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod(BlxckoutFX.MOD_ID)
public final class BlxckoutFXForge {
    public BlxckoutFXForge() {
        BlxckoutFX.init();
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> BlxckoutFXForgeClient::init);
    }
}
