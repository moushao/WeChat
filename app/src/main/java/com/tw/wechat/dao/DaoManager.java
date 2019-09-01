package com.tw.wechat.dao;

import android.content.Context;

/**
 * 类名: {@link DaoManager}
 * <br/> 功能描述:数据库操作的管理类,方便统一获取Session
 * <br/> 作者: MouShao
 * <br/> 时间: 2019/9/1
 */
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

    /**
     * 清楚所有本地数据
     */
    public void deleteAll() {
        DaoManager.getInstance().getSession().getUserDao().deleteAll();
        DaoManager.getInstance().getSession().getTweetDao().deleteAll();
        DaoManager.getInstance().getSession().getPhotoDao().deleteAll();
        DaoManager.getInstance().getSession().getCommentDao().deleteAll();
    }
}
