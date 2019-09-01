package com.tw.wechat.utils;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;

import java.lang.ref.WeakReference;

/**
 * 类名: {@link KeyboardControlManager}
 * <br/> 功能描述:软键盘弹出与隐藏的监听辅助类
 */
public class KeyboardControlManager {
    private OnKeyboardStateChangeListener onKeyboardStateChangeListener;
    private WeakReference<Activity> act;

    private KeyboardControlManager(Activity act, OnKeyboardStateChangeListener onKeyboardStateChangeListener) {
        this.act = new WeakReference<Activity>(act);
        this.onKeyboardStateChangeListener = onKeyboardStateChangeListener;
    }


    public void observerKeyboardVisibleChangeInternal() {
        if (onKeyboardStateChangeListener == null)
            return;
        Activity activity = act.get();
        if (activity == null)
            return;
        setOnKeyboardStateChangeListener(onKeyboardStateChangeListener);
        final View decorView = activity.getWindow().getDecorView();
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            int preKeyboardHeight = -1;
            Rect rect;
            boolean preVisible = false;

            @Override
            public void onGlobalLayout() {
                rect = new Rect();
                decorView.getWindowVisibleDisplayFrame(rect);
                int displayHeight = rect.height();
                int windowHeight = decorView.getHeight();
                int keyboardHeight = windowHeight - displayHeight;
                if (preKeyboardHeight != keyboardHeight) {
                    //判定可见区域与原来的window区域占比是否小于0.75,小于意味着键盘弹出来了。
                    boolean isVisible = (displayHeight * 1.0f / windowHeight * 1.0f) < 0.75f;
                    if (isVisible != preVisible) {
                        onKeyboardStateChangeListener.onKeyboardChange(keyboardHeight, isVisible);
                        preVisible = isVisible;

                    }
                }
                preKeyboardHeight = keyboardHeight;
            }
        });
    }

    public static void observerKeyboardVisibleChange(Activity act, OnKeyboardStateChangeListener onKeyboardStateChangeListener) {
        new KeyboardControlManager(act, onKeyboardStateChangeListener).observerKeyboardVisibleChangeInternal();

    }


    public void setOnKeyboardStateChangeListener(OnKeyboardStateChangeListener onKeyboardStateChangeListener) {
        this.onKeyboardStateChangeListener = onKeyboardStateChangeListener;
    }

    /**
     * 软键盘状态切换回调接口
     */
    public interface OnKeyboardStateChangeListener {
        void onKeyboardChange(int keyboardHeight, boolean isVisible);
    }
}
