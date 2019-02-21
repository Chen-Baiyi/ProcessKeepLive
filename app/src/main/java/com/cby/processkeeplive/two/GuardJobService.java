package com.cby.processkeeplive.two;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.cby.processkeeplive.three.LocalService;
import com.cby.processkeeplive.three.RemoteService;
import com.cby.processkeeplive.util.Constant;

import java.util.List;

@SuppressLint({"NewApi", "Registered"})
public class GuardJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters params) {
        Log.e(Constant.TAG, "开启job");
        //如果7.0以上 轮询
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            startGuardJob(this);
        }
        boolean isLocalRun = isServiceWork(this, LocalService.class.getName());
        boolean isRemoteRun = isServiceWork(this, RemoteService.class.getName());
        if (!isLocalRun || !isRemoteRun) {
            LocalService.start(this);
            RemoteService.start(this);
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    @SuppressLint("MissingPermission")
    public static void startGuardJob(Context context) {
        if (context != null) {
            JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            // setPersisted 在设备重启依然执行
            JobInfo.Builder builder = new JobInfo.Builder(10, new ComponentName(context
                    .getPackageName(), GuardJobService.class
                    .getName())).setPersisted(true);
            //小于7.0
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                // 每隔1s 执行一次 job
                builder.setPeriodic(1_000);
            } else {
                // 延迟执行任务
                builder.setMinimumLatency(1_000);
            }
            jobScheduler.schedule(builder.build());
        }
    }

    // 判断服务是否正在运行
    public boolean isServiceWork(Context mContext, String serviceName) {
        boolean isWork = false;
        ActivityManager manager = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceInfoList = manager.getRunningServices(100);
        if (serviceInfoList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < serviceInfoList.size(); i++) {
            String serviceClassName = serviceInfoList.get(i).service.getClassName();
            if (serviceClassName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }
}
