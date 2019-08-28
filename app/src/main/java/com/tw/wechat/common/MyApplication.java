package com.tw.wechat.common;

import android.app.Application;
import android.app.NotificationManager;

import com.tw.wechat.utils.CrashHandler;

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
        CrashHandler.getInstance().init(this);
    }

    public static MyApplication getApplication() {
        return mMyApplication;
    }
}


