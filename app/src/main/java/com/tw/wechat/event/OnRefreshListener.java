package com.tw.wechat.event;

/**
 * 下拉和上拉的回调
 */

public interface OnRefreshListener {
    void onRefresh();

    void onLoadMore();
}
