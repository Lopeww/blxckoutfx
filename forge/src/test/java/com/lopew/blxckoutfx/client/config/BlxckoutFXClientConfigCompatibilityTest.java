package com.lopew.blxckoutfx.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class BlxckoutFXClientConfigCompatibilityTest {
    private static final Gson GSON = new GsonBuilder().create();

    @Test
    void legacyJsonWithoutJourneyMapFieldsUsesSafeDefaults() {
        BlxckoutFXClientConfig config = GSON.fromJson("{\"presetIndex\":2,\"inventoryButtonEnabled\":true}", BlxckoutFXClientConfig.class);

        assertFalse(config.hasJourneyMapFullscreenButtonCustomPosition());
        assertEquals(0, config.getJourneyMapFullscreenButtonX(320, 20));
        assertEquals(0, config.getJourneyMapFullscreenButtonY(240, 20));
    }
}
