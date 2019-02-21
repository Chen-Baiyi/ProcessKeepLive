package com.cby.processkeeplive;

import android.app.Application;

import com.cby.processkeeplive.util.AppUtils;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        AppUtils.getDefault().init(this);
    }
}
