package com.tw.wechat.widget.pullryc;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.tw.wechat.R;


/**
 * 类名: {@link PullRefreshFooter}
 * <br/> 功能描述:RecyclerView的加载更多footView
 */
public class PullRefreshFooter extends FrameLayout {

    private RotateAnimation rotateAnimation;
    private ImageView loadingView;

    public PullRefreshFooter(Context context) {
        this(context, null);
    }

    public PullRefreshFooter(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullRefreshFooter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.view_ptr_footer, this);
        loadingView = (ImageView) findViewById(R.id.iv_loading);
        rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(600);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        setVisibility(GONE);
    }


    public void onRefreshing() {
        setVisibility(VISIBLE);
        loadingView.startAnimation(rotateAnimation);
    }

    public void onFinish() {
        Animation animation = new AlphaAnimation(1F, 0F);
        animation.setDuration(400);
        startAnimation(animation);
        setVisibility(GONE);
        loadingView.clearAnimation();
    }


}
