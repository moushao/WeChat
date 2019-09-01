package com.tw.wechat.widget.photo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.tw.wechat.R;


/**
 * 类名: {@link PhotoWidget}
 * <br/> 功能描述:朋友圈的imageview，包含点击动作,去除蒙层
 */
public class PhotoWidget extends ImageView {
    private static final String TAG = "ForceClickImageView";
    //前景层
    private Drawable mForegroundDrawable;
    private Rect mCachedBounds = new Rect();

    public PhotoWidget(Context context) {
        this(context, null);
    }

    public PhotoWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PhotoWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, true);
        setFocusable(true);
    }

    /**
     * 初始化
     */
    private void init(Context context, AttributeSet attrs, boolean needDefaultForceGroundColor) {
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PhotoWidget);
        mForegroundDrawable = a.getDrawable(R.styleable.PhotoWidget_foregroundColor);
        if (mForegroundDrawable instanceof ColorDrawable || (attrs == null && needDefaultForceGroundColor)) {
            int foreGroundColor = a.getColor(R.styleable.PhotoWidget_foregroundColor, 0x00000000);
            mForegroundDrawable = new StateListDrawable();
            ColorDrawable forceDrawable = new ColorDrawable(foreGroundColor);
            ColorDrawable normalDrawable = new ColorDrawable(Color.TRANSPARENT);
            ((StateListDrawable) mForegroundDrawable).addState(new int[]{android.R.attr.state_pressed}, forceDrawable);
            ((StateListDrawable) mForegroundDrawable).addState(new int[]{android.R.attr.state_focused}, forceDrawable);
            ((StateListDrawable) mForegroundDrawable).addState(new int[]{android.R.attr.state_enabled}, normalDrawable);
            ((StateListDrawable) mForegroundDrawable).addState(new int[]{}, normalDrawable);
        }
        if (mForegroundDrawable != null) {
            mForegroundDrawable.setCallback(this);
        }
        a.recycle();
    }


    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (mForegroundDrawable != null && mForegroundDrawable.isStateful()) {
            mForegroundDrawable.setState(getDrawableState());
            invalidate();
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mForegroundDrawable != null) {
            if (getDrawable() != null) {
                mForegroundDrawable.setBounds(getDrawable().getBounds());
            } else {
                mForegroundDrawable.setBounds(mCachedBounds);
            }
            mForegroundDrawable.draw(canvas);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mForegroundDrawable != null)
            mCachedBounds.set(0, 0, w, h);
    }

}
