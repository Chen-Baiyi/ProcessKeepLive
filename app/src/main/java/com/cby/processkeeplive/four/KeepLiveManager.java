package com.cby.processkeeplive.four;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.lang.ref.WeakReference;

public class KeepLiveManager {
    private static final KeepLiveManager ourInstance = new KeepLiveManager();
    private WeakReference<KeepLiveActivity> reference;
    private KeepLiveReceiver receiver;

    public static KeepLiveManager getInstance() {
        return ourInstance;
    }

    private KeepLiveManager() {
    }

    public void register(Context context) {
        if (receiver == null) {
            receiver = new KeepLiveReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            filter.addAction(Intent.ACTION_SCREEN_ON);
            context.registerReceiver(receiver, filter);
        }
    }

    public void unRegister(Context context) {
        if (receiver != null) {
            context.unregisterReceiver(receiver);
        }
    }

    public void setKeepLiveActivity(KeepLiveActivity activity) {
        reference = new WeakReference<>(activity);
    }

    public void start(Context context) {
        KeepLiveActivity.launch(context);
    }

    public void stop(Context context) {
        if (reference != null && reference.get() != null) {
            reference.get().finish();
        }
    }
}
