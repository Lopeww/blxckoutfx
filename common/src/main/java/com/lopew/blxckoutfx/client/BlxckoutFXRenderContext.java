package com.lopew.blxckoutfx.client;

public final class BlxckoutFXRenderContext {
    private static int buttonRenderDepth;
    private static int textureDarkeningSuppressDepth;

    public static void enterButtonRender() {
        buttonRenderDepth++;
    }

    public static void exitButtonRender() {
        buttonRenderDepth = Math.max(0, buttonRenderDepth - 1);
    }

    public static boolean isRenderingButton() {
        return buttonRenderDepth > 0;
    }

    public static void suppressTextureDarkening() {
        textureDarkeningSuppressDepth++;
    }

    public static void allowTextureDarkening() {
        textureDarkeningSuppressDepth = Math.max(0, textureDarkeningSuppressDepth - 1);
    }

    public static boolean isTextureDarkeningSuppressed() {
        return textureDarkeningSuppressDepth > 0;
    }

    private BlxckoutFXRenderContext() {
    }
}
