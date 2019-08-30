package com.tw.wechat.event;

/**
 * 软键盘状态切换回调接口
 */
public interface OnKeyboardStateChangeListener {
    void onKeyboardChange(int keyboardHeight, boolean isVisible);
}