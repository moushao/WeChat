package com.tw.wechat.widget.popup;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tw.wechat.R;
import com.tw.wechat.entity.Tweet;
import com.tw.wechat.utils.ToastUtils;
import com.tw.wechat.utils.UIHelper;

import java.util.List;

import androidx.annotation.NonNull;
import razerdp.basepopup.BasePopupWindow;

/**
 * 类名: {@link CommentPopup}
 * <br/> 功能描述:朋友圈点赞、添加评论的Pop
 */
public class CommentPopup extends BasePopupWindow implements View.OnClickListener {

    private ImageView mLikeView;
    private TextView mLikeText;

    private RelativeLayout mLikeClikcLayout;
    private RelativeLayout mCommentClickLayout;

    private Tweet mTweet;

    private WeakHandler handler;
    private ScaleAnimation mScaleAnimation;

    private OnCommentPopupClickListener mOnCommentPopupClickListener;

    //是否已经点赞
    private boolean hasLiked;
    private long friendPraiseID;

    public CommentPopup(Activity context) {
        super(context, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        handler = new WeakHandler();
        setBackground(0);
        mLikeView = (ImageView) findViewById(R.id.iv_like);
        mLikeText = (TextView) findViewById(R.id.tv_like);

        mLikeClikcLayout = (RelativeLayout) findViewById(R.id.item_like);
        mCommentClickLayout = (RelativeLayout) findViewById(R.id.item_comment);

        mLikeClikcLayout.setOnClickListener(this);
        mCommentClickLayout.setOnClickListener(this);

        buildAnima();
    }

    private void buildAnima() {
        mScaleAnimation = new ScaleAnimation(1f, 2.5f, 1f, 2.5f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        mScaleAnimation.setDuration(300);
        mScaleAnimation.setInterpolator(new SpringInterPolator());
        mScaleAnimation.setFillAfter(false);

        mScaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dismiss();
                    }
                }, 150);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public void showPopupWindow(View v) {
        setOffsetX(-getWidth() - 10);
        setOffsetY(-getHeight() * 2 / 3);
        super.showPopupWindow(v);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_like:
                if (mOnCommentPopupClickListener != null) {
                    mOnCommentPopupClickListener.onLikeClick(v, mTweet, hasLiked, friendPraiseID);
                    mLikeView.clearAnimation();
                    mLikeView.startAnimation(mScaleAnimation);
                }
                ToastUtils.showToast(mLikeText.getContext(), "点了个赞");
                break;
            case R.id.item_comment:
                if (mOnCommentPopupClickListener != null) {
                    mOnCommentPopupClickListener.onCommentClick(v, mTweet);
                    dismissWithOutAnimate();
                }
                break;
        }
    }
    //=============================================================Getter/Setter

    public OnCommentPopupClickListener getOnCommentPopupClickListener() {
        return mOnCommentPopupClickListener;
    }

    public void setOnCommentPopupClickListener(OnCommentPopupClickListener onCommentPopupClickListener) {
        mOnCommentPopupClickListener = onCommentPopupClickListener;
    }

    // 以下为可选代码（非必须实现）
    // 返回作用于PopupWindow的show和dismiss动画，本库提供了默认的几款动画，这里可以自由实现
    @Override
    protected Animation onCreateShowAnimation() {
        return getDefaultScaleAnimation(true);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getDefaultScaleAnimation(false);
    }

    public void updateMomentInfo(@NonNull Tweet info) {
        this.mTweet = info;
        hasLiked = false;
        //if (!isListEmpty(info.getLikeList())) {
        //    for (ThumbsUp likesInfo : info.getLikeList()) {
        //        if (TextUtils.equals(likesInfo.getLikeID() + "", UserHelper.getInstance().getLogUser().getUserInfoID()
        //                + "")) {
        //            friendPraiseID = likesInfo.getFriendPraiseID();
        //            hasLiked = true;
        //            break;
        //        }
        //    }
        //}
        mLikeText.setText(hasLiked ? "取消" : "赞");

    }

    public boolean isListEmpty(List<?> datas) {
        return datas == null || datas.size() <= 0;
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_comment);
    }

    //=============================================================InterFace
    public interface OnCommentPopupClickListener {
        void onLikeClick(View v, @NonNull Tweet info, boolean hasLiked, Long friendPraiseID);

        void onCommentClick(View v, @NonNull Tweet info);
    }

    static class SpringInterPolator extends LinearInterpolator {

        public SpringInterPolator() {
        }


        @Override
        public float getInterpolation(float input) {
            return (float) Math.sin(input * Math.PI);
        }
    }
}
