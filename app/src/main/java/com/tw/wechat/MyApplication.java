package com.tw.wechat;

import android.app.Application;

import com.luck.picture.lib.PictureSelectorActivity;
import com.tw.wechat.dao.DaoManager;
import com.tw.wechat.utils.LogUtil;

import me.jessyan.autosize.AutoSizeConfig;
import me.jessyan.autosize.external.ExternalAdaptInfo;
import me.jessyan.autosize.unit.Subunits;

/**
 * 类名: MyApplication
 * <br/> 功能描述:Application全局管理类,用于数据库的初始化、界面适配的初始化
 * <br/> 作者: MouTao
 * <br/> 时间: 2017/6/21
 */

public class MyApplication extends Application {
    private static MyApplication mMyApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        mMyApplication = this;
        initAutoSize();
        DaoManager.initDao(this);
    }

    public static MyApplication getApplication() {
        return mMyApplication;
    }

    /**
     * 以下是 AndroidAutoSize 可以自定义的参数, {@link AutoSizeConfig} 的每个方法的注释都写的很详细
     * 使用前请一定记得跳进源码，查看方法的注释, 下面的注释只是简单描述!!!
     */
    public void initAutoSize() {
        AutoSizeConfig.getInstance()
                .getUnitsManager()
                .setSupportDP(false)
                .setSupportSP(false)
                .setSupportSubunits(Subunits.MM);
        AutoSizeConfig.getInstance()
                .setCustomFragment(true);
        AutoSizeConfig.getInstance().getExternalAdaptManager()
                .addExternalAdaptInfoOfActivity(PictureSelectorActivity.class, new ExternalAdaptInfo(true, 400));
        LogUtil.e("AutoSizeConfig", AutoSizeConfig.getInstance().getDesignWidthInDp() + ":" + AutoSizeConfig.getInstance().getDesignHeightInDp());
    }
}


