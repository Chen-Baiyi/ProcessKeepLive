package com.cby.processkeeplive.four;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

@SuppressLint("Registered")
public class KeepLiveActivity extends AppCompatActivity {

    public static void launch(Context context) {
        Intent intent = new Intent(context, KeepLiveActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();

        // 设置左上角
        window.setGravity(Gravity.TOP | Gravity.LEFT);
        WindowManager.LayoutParams params = window.getAttributes();
        // 坐标
        params.x = 0;
        params.y = 0;
        // 设置 1 像素
        params.width = 1;
        params.height = 1;
        window.setAttributes(params);

        KeepLiveManager.getInstance().setKeepLiveActivity(this);
    }
}
