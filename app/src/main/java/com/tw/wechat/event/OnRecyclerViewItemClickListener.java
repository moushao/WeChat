package com.tw.wechat.event;

import android.view.View;

/**
 * list item的点击
 */
public interface OnRecyclerViewItemClickListener<T> {
    /**
     * 短按响应
     *
     * @param v        点击的view
     * @param position 点击item的位置
     * @param data     数据源
     */
    void onItemClick(View v, int position, T data);

    /**
     * 长按响应
     *
     * @param v        点击的view
     * @param position 点击item的位置
     * @param data     数据源
     */
    boolean onItemLongClick(View v, int position, T data);

}
