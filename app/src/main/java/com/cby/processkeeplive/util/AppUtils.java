package com.cby.processkeeplive.util;

import android.app.Application;

public class AppUtils {
    private static AppUtils instance = null;
    private Application application;

    public static AppUtils getDefault() {
        if (instance == null) {
            synchronized (AppUtils.class) {
                if (instance == null) {
                    instance = new AppUtils();
                }
            }
        }
        return instance;
    }

    private AppUtils() {
    }

    public void init(Application application) {
        this.application = application;
    }

    public Application getApplication() {
        if (application == null) {
            throw new RuntimeException("AppUtils 未初始化");
        }
        return application;
    }

    public String getPackageName() {
        if (application == null) {
            throw new RuntimeException("AppUtils 未初始化");
        }
        return application.getPackageName();
    }
}
