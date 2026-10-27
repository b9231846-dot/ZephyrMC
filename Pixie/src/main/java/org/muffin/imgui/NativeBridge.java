package org.muffin.imgui;

public class NativeBridge {
    static {
        try {
            System.loadLibrary("pixie");
        } catch (Throwable ignored) {}
    }

    public static native boolean onTouchEvent(int action, float x, float y);
}
