package com.cby.processkeeplive.one;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.cby.processkeeplive.util.AppUtils;

public class AccountHelper {
    //authenticator.xml 中配置 的accountType值
    private static final String ACCOUNT_TYPE = "com.cby.processkeeplive";


    /**
     * 添加Account，需要"android.permission.GET_ACCOUNTS"权限
     *
     * @param context
     */
    @SuppressLint("MissingPermission")
    public static void addAccount(Context context) {
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        Account[] accountsType = accountManager.getAccountsByType(ACCOUNT_TYPE);
        if (accountsType.length > 0) {
            Log.e("cby", "账户已经存在");
            return;
        }
        //给这个账户类型添加账户 cby cby007
        Account account = new Account("cby", ACCOUNT_TYPE);
        //需要"android.permission.AUTHENTICATE_ACCOUNTS"权限
        accountManager.addAccountExplicitly(account, "cby007", new Bundle());
    }

    /**
     * 设置账户同步，即告知系统我们需要系统为我们来进行账户同步，只有设置了之后系统才会自动去
     * 触发SyncAdapter#onPerformSync方法
     */
    public static void autoSyncAccount() {
        Account account = new Account("cby", ACCOUNT_TYPE);
        //设置可同步
        ContentResolver.setIsSyncable(account, AppUtils.getDefault().getPackageName() + ".provider", 2);
        //设置自动同步
        ContentResolver.setSyncAutomatically(account, AppUtils.getDefault().getPackageName() + ".provider", true);
        //设置同步周期参考值，不受开发者控制完全由系统决定何时同步，测试下来最长等了差不多几十分钟才同步一次，不同系统表现不同
        ContentResolver.addPeriodicSync(account, AppUtils.getDefault().getPackageName() + ".provider", new Bundle(), 1);

    }
}
