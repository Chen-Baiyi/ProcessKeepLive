package com.cby.processkeeplive.four;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class KeepLiveReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            // 屏幕熄灭，开启 KeepLiveActivity
            KeepLiveManager.getInstance().start(context);
        } else {
            KeepLiveManager.getInstance().stop(context);
        }
    }
}
