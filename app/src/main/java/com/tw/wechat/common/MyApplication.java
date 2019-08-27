package com.tw.wechat.common;

import android.app.Application;
import android.app.NotificationManager;

/**
 * 类名: MyApplication
 * <br/> 功能描述:
 * <br/> 作者: MouTao
 * <br/> 时间: 2017/6/21
 */

public class MyApplication extends Application {
    private static MyApplication mMyApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        mMyApplication = this;
    }

    public static MyApplication getApplication() {
        return mMyApplication;
    }
}


