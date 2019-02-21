package com.cby.processkeeplive.three;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.sax.ElementListener;
import android.support.v4.app.NotificationCompat;

@SuppressLint("Registered")
public class ForegroundService extends Service {
    private static final int SERVICE_ID = 1;

    public static void start(Context context) {
        context.startService(new Intent(context, ForegroundService.class));
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new KeepLiveService();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Build.VERSION.SDK_INT < 18) {
            // 开启前台服务
            startForeground(SERVICE_ID, new Notification());
        } else if (Build.VERSION.SDK_INT < 26) {
            // 开启前台服务
            startForeground(SERVICE_ID, new Notification());
            // 移除通知栏消息
            startService(new Intent(this, InnerService.class));
        } else {
            // 8.0 以上
            @SuppressLint("WrongConstant")
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (manager != null) {
                NotificationChannel channel = new NotificationChannel("channel", "cby", NotificationManager.IMPORTANCE_MIN);
                manager.createNotificationChannel(channel);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channel.getId());
                startForeground(SERVICE_ID, builder.build());
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public static class InnerService extends Service {
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            startForeground(SERVICE_ID, new Notification());
            stopForeground(true);
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }
    }

    private class KeepLiveService extends IKeepLiveService.Stub {

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }
    }
}
