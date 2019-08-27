package com.tw.wechat.widget;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.util.LruCache;
import android.widget.TextView;

import com.tw.wechat.R;

import java.util.List;


/**
 * Created by 大灯泡 on 2016/2/21.
 * 点赞显示控件
 */
@SuppressLint("AppCompatCustomView")
public class PraiseWidget extends TextView {
    private static final String TAG = "PraiseWidget";

    //点赞名字展示的默认颜色
    private int textColor = 0xff517fae;
    //点赞列表心心默认图标
    private int iconRes = R.drawable.icon_like;
    //默认字体大小
    private int textSize = 14;
    //默认点击背景
    private int clickBg = 0x00000000;


    //private static final LruCache<String, SpannableStringBuilderCompat> praiseCache
    //        = new LruCache<String, SpannableStringBuilderCompat>(50) {
    //    @Override
    //    protected int sizeOf(String key, SpannableStringBuilderCompat value) {
    //        return 1;
    //    }
    //};

    public PraiseWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PraiseWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PraiseWidget(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PraiseWidget);
        textColor = a.getColor(R.styleable.PraiseWidget_font_color, 0xff517fae);
        textSize = a.getDimensionPixelSize(R.styleable.PraiseWidget_font_size, 14);
        clickBg = a.getColor(R.styleable.PraiseWidget_click_bg_color, 0x00000000);
        iconRes = a.getResourceId(R.styleable.PraiseWidget_like_icon, R.drawable.icon_like_circle);
        a.recycle();
        //TODO 如果不设置，clickableSpan不能响应点击事件
        
        //this.setMovementMethod(LinkMovementMethod.getInstance());
        //setOnTouchListener(new ClickableSpanEx.ClickableSpanSelector());
        setTextSize(textSize);
    }

    @Override
    public boolean onPreDraw() {
        return false;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //praiseCache.evictAll();
        //if (praiseCache.size() == 0) {
        //    Log.d(TAG, "clear cache success!");
        //}
    }
}
