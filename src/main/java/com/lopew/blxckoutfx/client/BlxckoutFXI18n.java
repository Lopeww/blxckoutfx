package com.lopew.blxckoutfx.client;

import net.minecraft.client.resources.I18n;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.IllegalFormatException;
import java.util.Map;

public final class BlxckoutFXI18n {
    private static final Map<String, String> FALLBACKS = loadFallbacks();

    public static String format(String key, Object... args) {
        String translated = I18n.format(key, args);
        if (!key.equals(translated)) {
            return translated;
        }

        String fallback = FALLBACKS.get(key);
        if (fallback == null) {
            return key;
        }

        try {
            return String.format(fallback, args);
        } catch (IllegalFormatException ignored) {
            return fallback;
        }
    }

    private static Map<String, String> loadFallbacks() {
        Map<String, String> values = new HashMap<>();
        load(values, "assets/blxckoutfx/lang/en_us.lang");
        load(values, "assets/blxckoutfx/lang/en_US.lang");
        return values;
    }

    private static void load(Map<String, String> values, String path) {
        InputStream stream = BlxckoutFXI18n.class.getClassLoader().getResourceAsStream(path);
        if (stream == null) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty() || line.charAt(0) == '#') {
                    continue;
                }

                int separator = line.indexOf('=');
                if (separator <= 0) {
                    continue;
                }

                values.put(line.substring(0, separator), line.substring(separator + 1));
            }
        } catch (IOException ignored) {
        }
    }

    private BlxckoutFXI18n() {
    }
}
