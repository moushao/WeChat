package com.tw.wechat.event;

import android.view.View;

public interface ItemListener<T> {
    void onItemClick(View view, int position, T mData);
}