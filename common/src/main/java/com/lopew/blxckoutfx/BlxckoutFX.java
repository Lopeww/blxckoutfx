package com.lopew.blxckoutfx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class BlxckoutFX {
    public static final String MOD_ID = "blxckoutfx";
    public static final String MOD_NAME = "BlxckoutFX";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static void init() {
        LOGGER.info("{} initialized", MOD_NAME);
    }

    private BlxckoutFX() {
    }
}
