package com.lopew.blxckoutfx.platform;

public final class BlxckoutFXPlatform {
    private enum RuntimeFlavor {
        UNKNOWN,
        FABRIC,
        NEOFORGE
    }

    private static volatile RuntimeFlavor runtime = RuntimeFlavor.UNKNOWN;

    private BlxckoutFXPlatform() {
    }

    public static boolean isFabricRuntime() {
        if (runtime == RuntimeFlavor.FABRIC) {
            return true;
        }
        if (runtime == RuntimeFlavor.NEOFORGE) {
            return false;
        }
        return isClassPresent("net.fabricmc.loader.api.FabricLoader");
    }

    public static boolean isNeoForgeRuntime() {
        if (runtime == RuntimeFlavor.NEOFORGE) {
            return true;
        }
        if (runtime == RuntimeFlavor.FABRIC) {
            return false;
        }
        return isClassPresent("net.neoforged.fml.ModContainer");
    }

    public static void markFabricRuntime() {
        runtime = RuntimeFlavor.FABRIC;
    }

    public static void markNeoForgeRuntime() {
        runtime = RuntimeFlavor.NEOFORGE;
    }

    private static boolean isClassPresent(String className) {
        try {
            Class.forName(className, false, BlxckoutFXPlatform.class.getClassLoader());
            return true;
        } catch (ClassNotFoundException ignored) {
            return false;
        }
    }
}
