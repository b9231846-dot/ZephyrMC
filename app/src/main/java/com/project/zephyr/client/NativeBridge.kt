package com.project.zephyr.client

import android.util.Log

object NativeBridge {
    init {
        try {
            System.loadLibrary("pixie")
            Log.i("ZephyrMC", "libpixie.so loaded successfully via NativeBridge")
        } catch (t: Throwable) {
            Log.e("ZephyrMC", "Failed to load libpixie.so in NativeBridge", t)
        }
    }

    @JvmStatic
    external fun onTouchEvent(action: Int, x: Float, y: Float): Boolean
}
