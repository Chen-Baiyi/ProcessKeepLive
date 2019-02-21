package com.cby.processkeeplive.one;

import android.accounts.Account;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by cmo on 2018/8/19  22:35
 * 用于执行账户同步，当系统执行账户同步时则会自动拉活所在的进程,不需要手动配置好之后，系统会自动绑定并调起
 */

@SuppressLint("Registered")
public class SyncService extends Service {
    private SyncAdapter mSyncAdapter;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mSyncAdapter.getSyncAdapterBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mSyncAdapter = new SyncAdapter(getApplicationContext(), true);
    }

    static class SyncAdapter extends AbstractThreadedSyncAdapter {

        public SyncAdapter(Context context, boolean autoInitialize) {
            super(context, autoInitialize);
        }

        @Override
        public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
            //todo 账户同步 工作
            Log.e("cby","同步账户");
            //与互联网 或者 本地数据库同步账户
        }
    }
}
