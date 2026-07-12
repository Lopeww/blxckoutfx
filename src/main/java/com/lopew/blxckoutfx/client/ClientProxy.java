package com.lopew.blxckoutfx.client;

import com.lopew.blxckoutfx.CommonProxy;
import net.minecraftforge.common.MinecraftForge;

public final class ClientProxy extends CommonProxy {
    @Override
    public void init() {
        BlxckoutFXClientConfig.normalize();
        MinecraftForge.EVENT_BUS.register(new BlxckoutFXClientConfig.ConfigEventHandler());
        MinecraftForge.EVENT_BUS.register(new BlxckoutFXGUIHandler());
        MinecraftForge.EVENT_BUS.register(new BlxckoutFXKeybinds());
        BlxckoutFXKeybinds.init();
    }
}
