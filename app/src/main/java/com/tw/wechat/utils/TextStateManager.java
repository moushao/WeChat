package com.tw.wechat.utils;

import java.util.HashMap;

/**
 * 类名: {@link TextStateManager}
 * <br/> 功能描述:单例,用于保存文字展开或者收起来的状态
 * <br/> 作者: MouShao
 * <br/> 时间: 2019/8/31
 * <br/> 最后修改者:
 * <br/> 最后修改内容:
 */
public class TextStateManager {
    public static TextStateManager INSTANCE = new TextStateManager();
    private HashMap<Integer, Integer> stateMap = new HashMap();

    public void put(int key, int state) {
        stateMap.put(key, state);
    }

    public int get(int key) {
        try {
            return stateMap.get(key);
        } catch (Exception e) {
            return 0;
        }
    }

    public void clear() {
        stateMap.clear();
    }
}
