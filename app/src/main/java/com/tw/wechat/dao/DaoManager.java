package com.tw.wechat.dao;

import android.content.Context;

public class DaoManager {
    private static DaoManager INSTANCE;
    public static DaoSession daoSession;

    public static DaoManager getInstance() {
        if (INSTANCE == null) {
            synchronized (DaoManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DaoManager();
                }
            }
        }
        return INSTANCE;
    }

    public static void initDao(Context context) {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context, "tw_green_dao.db", null);
        DaoMaster master = new DaoMaster(devOpenHelper.getWritableDatabase());
        daoSession = master.newSession();
    }

    public DaoSession getSession() {
        if (daoSession == null) {
            throw new RuntimeException("请先初始化GreenDao");
        }
        return daoSession;
    }
}
