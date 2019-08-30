package com.tw.wechat.widget.pullryc;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;

import com.tw.wechat.event.OnRefreshListener;

import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.tw.wechat.R;
import com.tw.wechat.utils.AnimUtils;
import com.tw.wechat.utils.LogUtil;
import com.tw.wechat.utils.UIHelper;
import com.tw.wechat.utils.ViewOffsetHelper;
import com.tw.wechat.widget.pullryc.wrapperadapter.FixedViewInfo;
import com.tw.wechat.widget.pullryc.wrapperadapter.HeaderViewWrapperAdapter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import me.everything.android.ui.overscroll.IOverScrollDecor;
import me.everything.android.ui.overscroll.IOverScrollUpdateListener;
import me.everything.android.ui.overscroll.VerticalOverScrollBounceEffectDecorator;
import me.everything.android.ui.overscroll.adapters.RecyclerViewOverScrollDecorAdapter;

import static me.everything.android.ui.overscroll.IOverScrollState.STATE_BOUNCE_BACK;


/**
 * 专为朋友圈项目定制的下拉recyclerview
 * 原因在于：
 * 1 - 目前而言，git上大多数的rv都是用的swiperefreshlayout
 * 2 - 大多数rv都不支持下拉后收回下拉头部的
 * 3 - 因为大多数的rv都支持太多功能了，显得有点重，并且有些想要的回调我们都没法拿到
 * 目标：
 * 【基本要求】因为相当于定制，只为本项目服务，因此不考虑通用性，更多考虑扩展性
 * <p>
 * <p>
 * 1 - 下拉和上拉支持回调
 * 2 - 跟iOS朋友圈下拉头部一样，支持头部在刷新时的回弹
 * 3 - 滑动到底部自动加载更多
 * 4 - addHeaderView。
 * <p>
 * 使用的库：overscroll-decor(https://github.com/EverythingMe/overscroll-decor)
 */

public class CircleRecyclerView extends FrameLayout {
    private static final String TAG = "CircleRecyclerView";

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({Status.DEFAULT, Status.REFRESHING})
    @interface Status {
        int DEFAULT = 0;
        int REFRESHING = 1;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({Mode.FROM_START, Mode.FROM_BOTTOM})
    @interface Mode {
        int FROM_START = 0;
        int FROM_BOTTOM = 1;
    }

    @Status
    private int currentStatus;

    @Mode
    private int pullMode;


    //甜甜圈刷新的观察者
    private InnerRefreshIconObserver iconObserver;

    //刷新的回调监听
    private OnRefreshListener onRefreshListener;

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ImageView refreshIcon;

    private int refreshPosition;
    private PullRefreshFooter footerView;


    public CircleRecyclerView(Context context) {
        this(context, null);
    }

    public CircleRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @SuppressLint("WrongConstant")
    private void init(Context context) {
        if (isInEditMode())
            return;
        GradientDrawable background = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{0xff323232, 0xff323232, 0xffffffff, 0xffffffff});
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(background);
        }

        if (recyclerView == null) {
            recyclerView = (RecyclerView) LayoutInflater.from(context).inflate(R.layout.view_recyclerview, this, false);
            recyclerView.setBackgroundColor(Color.WHITE);
            linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);
        }
        //取消默认item变更动画
        recyclerView.setItemAnimator(null);

        if (refreshIcon == null) {
            refreshIcon = new ImageView(context);
            refreshIcon.setBackgroundColor(Color.TRANSPARENT);
            refreshIcon.setImageResource(R.drawable.icon_rotate);
            refreshIcon.setVisibility(GONE);
        }
        LayoutParams iconParam = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(recyclerView, RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT);
        iconParam.leftMargin = UIHelper.dipToPx(12);
        addView(refreshIcon, iconParam);
        refreshPosition = UIHelper.dipToPx(90);
        iconObserver = new InnerRefreshIconObserver(refreshIcon, refreshPosition);
        footerView = new PullRefreshFooter(getContext());
        addFooterView(footerView);
        recyclerView.addOnScrollListener(onScrollListener);
    }

    @Override
    protected void onFinishInflate() {
        if (getChildCount() > 2) {
            throw new IllegalStateException("咳咳，不能超过两个view哦");
        }
        super.onFinishInflate();
    }

    private void setCurrentStatus(@Status int status) {
        this.currentStatus = status;
    }

    public void complete() {
        if (pullMode == Mode.FROM_START && iconObserver != null) {
            iconObserver.catchResetEvent();
        }
        if (pullMode == Mode.FROM_BOTTOM && footerView != null) {
            footerView.onFinish();
        }
        setCurrentStatus(Status.DEFAULT);
        LogUtil.e("pullMode", pullMode == Mode.FROM_START ? "FROM_START" : "FROM_BOTTOM");
    }

    public void autoRefresh() {
        if (currentStatus == Status.REFRESHING || pullMode == Mode.FROM_BOTTOM || iconObserver == null || onRefreshListener == null)
            return;
        pullMode = Mode.FROM_START;
        setCurrentStatus(Status.REFRESHING);
        //iconObserver.autoRefresh();
        onRefreshListener.onRefresh();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (onPreDispatchTouchListener != null) {
            onPreDispatchTouchListener.onPreTouch(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    //------------------------------------------get/set-----------------------------------------------

    public OnRefreshListener getOnRefreshListener() {
        return onRefreshListener;
    }

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    public OnPreDispatchTouchListener getOnPreDispatchTouchListener() {
        return onPreDispatchTouchListener;
    }

    public void setOnPreDispatchTouchListener(OnPreDispatchTouchListener onPreDispatchTouchListener) {
        this.onPreDispatchTouchListener = onPreDispatchTouchListener;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        if (adapter != null) {
            if (mHeaderViewInfos.size() > 0 || mFooterViewInfos.size() > 0) {
                recyclerView.setAdapter(wrapHeaderRecyclerViewAdapterInternal(adapter, mHeaderViewInfos,
                        mFooterViewInfos));
            } else {
                recyclerView.setAdapter(adapter);
            }
        }
        initOverScroll();
    }

    private void initOverScroll() {
        IOverScrollDecor decor = new VerticalOverScrollBounceEffectDecorator(new RecyclerViewOverScrollDecorAdapter(
                recyclerView), 2f, 1f, 2f);

        decor.setOverScrollUpdateListener(new IOverScrollUpdateListener() {
            @Override
            public void onOverScrollUpdate(IOverScrollDecor decor, int state, float offset) {
                if (offset > 0) {
                    if (currentStatus == Status.REFRESHING)
                        return;
                    iconObserver.catchPullEvent(offset);
                    if (offset >= refreshPosition && state == STATE_BOUNCE_BACK) {
                        if (currentStatus != Status.REFRESHING) {
                            setCurrentStatus(Status.REFRESHING);
                            if (onRefreshListener != null) {
                                Log.i(TAG, "refresh");
                                onRefreshListener.onRefresh();
                            }
                            pullMode = Mode.FROM_START;
                            iconObserver.catchRefreshEvent();
                        }
                    }
                } else if (offset < 0) {
                    //底部的overscroll
                }
            }
        });
    }

    /**
     * 判断recyclerview是否滑到底部
     * <p>
     * 原理：判断滑过的距离加上屏幕上的显示的区域是否比整个控件高度高
     *
     * @return
     */
    public boolean isScrollToBottom() {
        return recyclerView != null && recyclerView.computeVerticalScrollExtent() + recyclerView
                .computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange();
    }

    /**
     * scroll listener
     */
    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            scrollSize = scrollSize + dy;
            if (isScrollToBottom() && currentStatus != Status.REFRESHING) {
                footerView.onRefreshing();
                pullMode = Mode.FROM_BOTTOM;
                setCurrentStatus(Status.REFRESHING);
                onRefreshListener.onLoadMore();
            }

            if (scrollSize <= refreshPosition) {
                refreshIcon.offsetTopAndBottom(-dy);
            }
        }
    };

    int scrollSize;

    /**
     * 刷新Icon的动作观察者
     */

    private static class InnerRefreshIconObserver {
        private ViewOffsetHelper viewOffsetHelper;
        private ImageView refreshIcon;
        private final int refreshPosition;
        private RotateAnimation rotateAnimation;
        private ValueAnimator mValueAnimator;

        InnerRefreshIconObserver(ImageView refreshIcon, int refreshPosition) {
            this.refreshIcon = refreshIcon;
            this.refreshPosition = refreshPosition;

            viewOffsetHelper = new ViewOffsetHelper(refreshIcon);

            rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation
                    .RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setDuration(1000);
            rotateAnimation.setInterpolator(new LinearInterpolator());
            rotateAnimation.setRepeatCount(Animation.INFINITE);
            rotateAnimation.setFillBefore(true);

        }

        void catchPullEvent(float offset) {
            if (checkHacIcon()) {
                refreshIcon.setRotation(-offset * 2);
                if (offset >= refreshPosition) {
                    offset = refreshPosition;
                }
                viewOffsetHelper.absoluteOffsetTopAndBottom((int) offset);
                adjustRefreshIconPosition();
            }
        }

        /**
         * 调整icon的位置界限
         */
        private void adjustRefreshIconPosition() {
            if (refreshIcon.getY() < 0) {
                refreshIcon.offsetTopAndBottom(Math.abs(refreshIcon.getTop()));
            } else if (refreshIcon.getY() > refreshPosition) {
                refreshIcon.offsetTopAndBottom(-(refreshIcon.getTop() - refreshPosition));
            }
        }

        void catchRefreshEvent() {
            if (checkHacIcon()) {
                refreshIcon.clearAnimation();
                if (refreshIcon.getTop() < refreshPosition) {
                    viewOffsetHelper.absoluteOffsetTopAndBottom(refreshPosition);
                }
                refreshIcon.startAnimation(rotateAnimation);
            }
        }

        void catchResetEvent() {
            if (mValueAnimator == null) {
                mValueAnimator = ValueAnimator.ofFloat(refreshPosition, 0);
                mValueAnimator.setInterpolator(new LinearInterpolator());
                mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float result = (float) animation.getAnimatedValue();
                        catchPullEvent(result);
                    }
                });
                mValueAnimator.addListener(new AnimUtils.SimpleAnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        refreshIcon.clearAnimation();
                    }
                });
                mValueAnimator.setDuration(540);
            }

            refreshIcon.post(new Runnable() {
                @Override
                public void run() {
                    mValueAnimator.start();
                }
            });
        }

        private boolean checkHacIcon() {
            return refreshIcon != null;
        }

        void autoRefresh() {
            ValueAnimator animator = ValueAnimator.ofFloat(0, refreshPosition);
            animator.setInterpolator(new LinearInterpolator());
            animator.setDuration(540);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float result = (float) animation.getAnimatedValue();
                    catchPullEvent(result);
                }
            });
            animator.addListener(new AnimUtils.SimpleAnimatorListener() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    catchRefreshEvent();
                }
            });
            animator.start();
        }
    }


    //------------------------------------------分割线-----------------------------------------------
    //------------------------------------------分割线-----------------------------------------------
    //------------------------------------------分割线-----------------------------------------------

    /**
     * 以下为recyclerview 的headeradapter实现方案
     * <p>
     * 以Listview的headerView和footerView为模板做出的recyclerview的header和footer
     */
    private ArrayList<FixedViewInfo> mHeaderViewInfos = new ArrayList<>();
    private ArrayList<FixedViewInfo> mFooterViewInfos = new ArrayList<>();

    /**
     * 不完美解决方法：添加一个header，则从-2开始减1
     * header:-2~-98
     */
    private static final int ITEM_VIEW_TYPE_HEADER_START = -2;
    /**
     * 不完美解决方法：添加一个header，则从-99开始减1
     * footer:-99~-无穷
     */
    private static final int ITEM_VIEW_TYPE_FOOTER_START = -99;

    public void addHeaderView(View headerView) {
        final FixedViewInfo info = new FixedViewInfo(headerView, ITEM_VIEW_TYPE_HEADER_START - mHeaderViewInfos.size());
        if (mHeaderViewInfos.size() == Math.abs(ITEM_VIEW_TYPE_FOOTER_START - ITEM_VIEW_TYPE_HEADER_START)) {
            mHeaderViewInfos.remove(mHeaderViewInfos.size() - 1);
        }
        if (checkFixedViewInfoNotAdded(info, mHeaderViewInfos)) {
            mHeaderViewInfos.add(info);
        }
        checkAndNotifyWrappedViewAdd(recyclerView.getAdapter(), info, true);

    }

    private void checkAndNotifyWrappedViewAdd(RecyclerView.Adapter adapter, FixedViewInfo info, boolean isHeader) {
        //header和footer只能再setAdapter前使用，如果是set了之后再用，为何不add普通的viewholder而非要Headr或者footer呢
        if (adapter != null) {
            if (!(adapter instanceof HeaderViewWrapperAdapter)) {
                adapter = wrapHeaderRecyclerViewAdapterInternal(adapter);
                if (isHeader) {
                    adapter.notifyItemInserted(((HeaderViewWrapperAdapter) adapter).findHeaderPosition(info.view));
                } else {
                    adapter.notifyItemInserted(((HeaderViewWrapperAdapter) adapter).findFooterPosition(info.view));
                }
            }
        }
    }

    public void addFooterView(View footerView) {
        final FixedViewInfo info = new FixedViewInfo(footerView, ITEM_VIEW_TYPE_FOOTER_START - mFooterViewInfos.size());
        if (checkFixedViewInfoNotAdded(info, mFooterViewInfos)) {
            mFooterViewInfos.add(info);
        }
        checkAndNotifyWrappedViewAdd(recyclerView.getAdapter(), info, false);
    }

    private boolean checkFixedViewInfoNotAdded(FixedViewInfo info, List<FixedViewInfo> infoList) {
        boolean result = true;
        for (FixedViewInfo fixedViewInfo : infoList) {
            if (fixedViewInfo.view == info.view) {
                result = false;
                break;
            }
        }
        return result;
    }


    protected HeaderViewWrapperAdapter wrapHeaderRecyclerViewAdapterInternal(@NonNull RecyclerView.Adapter
                                                                                     mWrappedAdapter,
                                                                             ArrayList<FixedViewInfo> mHeaderViewInfos,
                                                                             ArrayList<FixedViewInfo>
                                                                                     mFooterViewInfos) {
        return new HeaderViewWrapperAdapter(recyclerView, mWrappedAdapter, mHeaderViewInfos, mFooterViewInfos);

    }

    protected HeaderViewWrapperAdapter wrapHeaderRecyclerViewAdapterInternal(@NonNull RecyclerView.Adapter
                                                                                     mWrappedAdapter) {
        return wrapHeaderRecyclerViewAdapterInternal(mWrappedAdapter, mHeaderViewInfos, mFooterViewInfos);

    }


    //------------------------------------------interface-----------------------------------------------
    private OnPreDispatchTouchListener onPreDispatchTouchListener;

    public interface OnPreDispatchTouchListener {
        boolean onPreTouch(MotionEvent ev);
    }


}
