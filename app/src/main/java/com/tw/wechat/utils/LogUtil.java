package com.tw.wechat.utils;

import android.util.Log;

import com.tw.wechat.BuildConfig;


public class LogUtil {
    /**
     * 截断输出日志,解决字符串太长打印不完整
     *
     * @param msg
     */
    public static void e(String msg) {
        e("", msg);
    }

    public static void e(String tag, String msg) {
        if (!BuildConfig.DEBUG)
            return;
        tag = "WeChat_" + tag;
        if (tag == null || tag.length() == 0 || msg == null || msg.length() == 0)
            return;
        int segmentSize = 1 * 1024;
        long length = msg.length();
        if (length <= segmentSize) {// 长度小于等于限制直接打印
            Log.e(tag, msg);
        } else {
            while (msg.length() > segmentSize) {// 循环分段打印日志
                String logContent = msg.substring(0, segmentSize);
                msg = msg.substring(segmentSize);
                Log.e(tag, logContent);
            }
            Log.e(tag, msg);// 打印剩余日志    
        }
    }
}
