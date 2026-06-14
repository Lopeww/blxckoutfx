package com.lopew.blxckoutfx;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

public final class BlxckoutFX {
    public static final String MOD_ID = "blxckoutfx";
    public static final String MOD_NAME = "BlxckoutFX";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static void init() {
        LOGGER.info("{} initialized", MOD_NAME);
    }

    private BlxckoutFX() {
    }
}
