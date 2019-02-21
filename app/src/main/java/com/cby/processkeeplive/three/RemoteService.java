package com.cby.processkeeplive.three;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.cby.processkeeplive.util.Constant;

@SuppressLint("Registered")
public class RemoteService extends ForegroundService {

    public static void start(Context context) {
        Intent intent = new Intent(context, RemoteService.class);
        context.startService(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        bindService(new Intent(this, LocalService.class), connection, Context.BIND_IMPORTANT);
        return super.onStartCommand(intent, flags, startId);
    }

    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // 远程进程被 kill ，拉活
            Log.d(Constant.TAG, "主进程被 kill ，拉活");
            startService(new Intent(RemoteService.this, LocalService.class));
            bindService(new Intent(RemoteService.this, LocalService.class), this, Context.BIND_IMPORTANT);
        }
    };
}
