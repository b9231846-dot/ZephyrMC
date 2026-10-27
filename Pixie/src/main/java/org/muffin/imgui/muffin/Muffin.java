package org.muffin.imgui.muffin;

import android.content.Context;
import android.util.Log;

public class Muffin {

    static {
        try {
            System.loadLibrary("pixie");
            Log.i("ZephyrMC", "libpixie.so loaded successfully");
        } catch (Throwable t) {
            Log.e("ZephyrMC", "Failed to load libpixie.so", t);
        }
    }

    public Muffin(Context context) {
        Start(context);
    }

    public static void Start(final Context context) {
        // In-process mode: libpixie is loaded, hooks are active in JNI_OnLoad.
        // No floating service or WindowManager overlay needed!
        Log.i("ZephyrMC", "Zephyr In-Process Mod Engine started");
    }
}
