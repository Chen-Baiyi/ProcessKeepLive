package com.cby.processkeeplive;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.cby.processkeeplive.four.KeepLiveManager;
import com.cby.processkeeplive.one.AccountHelper;
import com.cby.processkeeplive.three.ForegroundService;
import com.cby.processkeeplive.three.LocalService;
import com.cby.processkeeplive.three.RemoteService;
import com.cby.processkeeplive.two.GuardJobService;
import com.cby.processkeeplive.util.Constant;
import com.tbruyelle.rxpermissions.RxPermissions;

import rx.Observer;

public class MainActivity extends AppCompatActivity {

    private RxPermissions rxPermissions;
    private long mExitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rxPermissions = new RxPermissions(this);


        // 1、账户同步
        syncAccount();
        // 2、JobService 拉活
        jobService();
        // 3、前台服务
        // ForegroundService.start(this);
        // 3、双守护进程
        doubleGuardService();
        // 4、单像素保活
        singlePx();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        KeepLiveManager.getInstance().unRegister(this);
    }

    // 1、账户同步
    private void syncAccount() {
        AccountHelper.addAccount(this);     // 添加账户
        AccountHelper.autoSyncAccount();            // 调用告知系统自动同步
    }

    // 2、JobService 拉活
    private void jobService() {
        rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onCompleted() {
                        Log.e(Constant.TAG, "rxPermissions onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(Constant.TAG, "rxPermissions onError");
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        Log.e(Constant.TAG, "rxPermissions onNext");
                        GuardJobService.startGuardJob(MainActivity.this);
                    }
                });
    }

    // 3、双守护进程
    private void doubleGuardService() {
        LocalService.start(this);
        RemoteService.start(this);
    }

    // 4、单像素保活
    private void singlePx() {
        KeepLiveManager.getInstance().register(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // TODO: 2016/8/19  二次返回退出
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次真的会退出了哦~", Toast.LENGTH_LONG).show();
                mExitTime = System.currentTimeMillis();
            } else {
                //杀死该应用进程
                try {
                    android.os.Process.killProcess(android.os.Process.myPid());
                    ActivityManager activityMgr = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
                    activityMgr.restartPackage(this.getPackageName());
                    System.exit(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
