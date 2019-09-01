package com.tw.wechat.adapter;

import android.view.View;

import androidx.annotation.NonNull;

/**
 * 抽象出的vh接口,用于子类的数据传递
 */

public interface BaseMomentVH<T> {

    /**
     * 找到view
     */
    void onFindView(@NonNull View rootView);

    /**
     * 绑定数据
     *
     * @param data     数据源
     * @param position 位置
     * @param viewType 数据类型
     */
    void onBindDataToView(@NonNull final T data, int position, int viewType);
}
