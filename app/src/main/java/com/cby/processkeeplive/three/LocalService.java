package com.cby.processkeeplive.three;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.cby.processkeeplive.four.KeepLiveManager;
import com.cby.processkeeplive.util.Constant;

@SuppressLint("Registered")
public class LocalService extends ForegroundService {

    public static void start(Context context) {
        Intent intent = new Intent(context, LocalService.class);
        context.startService(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        bindService(new Intent(this, RemoteService.class), connection, Context.BIND_IMPORTANT);
        return super.onStartCommand(intent, flags, startId);
    }

    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // 远程进程被 kill ，拉活
            Log.d(Constant.TAG, "远程进程被 kill ，拉活");
            startService(new Intent(LocalService.this, RemoteService.class));
            bindService(new Intent(LocalService.this, RemoteService.class), this, Context.BIND_IMPORTANT);
        }
    };
}
