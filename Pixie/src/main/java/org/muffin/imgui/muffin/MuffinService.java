package org.muffin.imgui.muffin;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MuffinService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
