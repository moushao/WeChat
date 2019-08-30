package com.tw.wechat.utils;

import android.app.Activity;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.tw.wechat.common.MyApplication;
import com.tw.wechat.event.OnKeyboardStateChangeListener;
import com.tw.wechat.widget.commentwidget.KeyboardControlManager;

public class AndroidBug5497Workaround {
    boolean preVisible = false;
    private OnKeyboardStateChangeListener mListener;

    public static void assistActivity(Activity activity, OnKeyboardStateChangeListener listener) {
        new AndroidBug5497Workaround(activity, listener);
    }

    private View mChildOfContent;
    private int usableHeightPrevious;
    private FrameLayout.LayoutParams frameLayoutParams;

    private AndroidBug5497Workaround(Activity activity, OnKeyboardStateChangeListener listener) {
        mListener = listener;
        FrameLayout content = (FrameLayout) activity.findViewById(android.R.id.content);
        mChildOfContent = content.getChildAt(0);
        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                possiblyResizeChildOfContent();
            }
        });
        frameLayoutParams = (FrameLayout.LayoutParams) mChildOfContent.getLayoutParams();
    }

    private void possiblyResizeChildOfContent() {
        int usableHeightNow = computeUsableHeight();
        if (usableHeightNow != usableHeightPrevious) {
            int usableHeightSansKeyboard = mChildOfContent.getRootView().getHeight();
            int heightDifference = usableHeightSansKeyboard - usableHeightNow;
            if (heightDifference > (usableHeightSansKeyboard / 4)) {
                // keyboard probably just became visible
                frameLayoutParams.height = usableHeightSansKeyboard - heightDifference;
            } else {
                // keyboard probably just became hidden
                frameLayoutParams.height = usableHeightSansKeyboard;
            }
            mChildOfContent.requestLayout();
            usableHeightPrevious = usableHeightNow;
        }
    }

    private int computeUsableHeight() {
        Rect r = new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(r);
        return (r.bottom);
    }

    private void onChangeed(int height, boolean visiable) {
        //if (mListener != null)
        //    mListener.onKeyboardChange(height, visiable);
    }

    public void setListener(OnKeyboardStateChangeListener listener) {
        mListener = listener;
    }
}