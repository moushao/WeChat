package com.tw.wechat;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.tw.wechat.adapter.CircleMomentsAdapter;
import com.tw.wechat.adapter.HostViewHolder;
import com.tw.wechat.adapter.MultiImageMomentsVH;
import com.tw.wechat.entity.Comment;
import com.tw.wechat.entity.Photo;
import com.tw.wechat.entity.Tweet;
import com.tw.wechat.entity.User;
import com.tw.wechat.event.CallBack;
import com.tw.wechat.event.OnRefreshListener;
import com.tw.wechat.event.ViewListener;
import com.tw.wechat.controller.TweetController;
import com.tw.wechat.utils.TextStateManager;
import com.tw.wechat.utils.ToastUtils;
import com.tw.wechat.widget.CircleViewHelper;
import com.tw.wechat.widget.commentwidget.CommentBox;
import com.tw.wechat.widget.commentwidget.CommentWidget;
import com.tw.wechat.widget.commentwidget.KeyboardControlManager;
import com.tw.wechat.widget.pullryc.CircleRecyclerView;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TweetActivity extends AppCompatActivity implements CircleRecyclerView.OnPreDispatchTouchListener {
    @BindView(R.id.recycler) CircleRecyclerView circleRecyclerView;
    @BindView(R.id.widget_comment) CommentBox commentBox;
    private Context mContext;
    private boolean isLoadMore;
    private HostViewHolder hostViewHolder;
    private CircleMomentsAdapter adapter;
    private CircleViewHelper mViewHelper;
    //当前用户
    private User user;
    //分页查询的便宜位置
    private int offset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidget();
        TweetController.getInstance().getUser(callBack);
        TweetController.getInstance().getTweets(offset, callBack);
    }

    //初始化控件
    private void initWidget() {
        ButterKnife.bind(this);
        mContext = this;
        QMUIStatusBarHelper.translucent(this);
        initKeyboardHeightObserver();
        commentBox.setOnCommentSendClickListener(onCommentSendClickListener);
        circleRecyclerView.setOnRefreshListener(refreshListener);
        circleRecyclerView.setOnPreDispatchTouchListener(this);
        circleRecyclerView.autoRefresh();
        initAdapter();
    }

    //初始化adapter
    private void initAdapter() {
        if (mViewHelper == null) {
            mViewHelper = new CircleViewHelper(this);
        }
        hostViewHolder = new HostViewHolder(this);
        circleRecyclerView.addHeaderView(hostViewHolder.getView());
        adapter = new CircleMomentsAdapter.Builder<>(this)
                .addType(MultiImageMomentsVH.class, 2, R.layout.moments_multi_image)
                .setListener(viewListener)
                .build();
        circleRecyclerView.setAdapter(adapter);
    }

    private OnRefreshListener refreshListener =
            new OnRefreshListener() {
                @Override
                public void onRefresh() {
                    TextStateManager.INSTANCE.clear();
                    TweetController.getInstance().getUser(callBack);
                    ToastUtils.showToast(mContext, "刷新了Tweet列表");
                    offset = 0;
                    isLoadMore = false;
                    TweetController.getInstance().getTweets(offset, callBack);
                }

                @Override
                public void onLoadMore() {
                    offset++;
                    isLoadMore = true;
                    TweetController.getInstance().getTweets(offset, callBack);
                }
            };

    @Override
    public boolean onPreTouch(MotionEvent ev) {
        if (commentBox != null && commentBox.isShowing()) {
            commentBox.dismissCommentBox(false);
            return true;
        }
        return false;
    }

    //adapter、viewHolder中的常规交互时间回调
    private ViewListener viewListener = new ViewListener() {
        @Override
        public void toggleShowCommentBox(View view, CommentWidget commentWidget, int itemPos, Comment comment) {
            if (view != null) {
                mViewHelper.setCommentAnchorView(view);
            } else if (commentWidget != null) {
                mViewHelper.setCommentAnchorView(commentWidget);
            }
            mViewHelper.setCommentItemDataPosition(itemPos);
            commentBox.toggleCommentBox(comment, false);
        }

        @Override
        public boolean isMyContent(String username) {
            if (user != null)
                return user.getNick().equals(username);
            return false;
        }

        @Override
        public void deleteComment(int itemPosition, int commentPosition) {
            adapter.getData().get(itemPosition).getComments().remove(commentPosition);
            adapter.notifyItemChanged(itemPosition);
        }

        @Override
        public void deleteRelease(int itemPosition) {
            adapter.getData().remove(itemPosition);
            adapter.notifyItemChanged(itemPosition);
        }

        @Override
        public void preViewPicture(List<Photo> images, int position) {
            if (Build.VERSION.SDK_INT < 16) {
                ToastUtils.showToast(mContext, "十分抱歉,由于机子型号太小,无法预览图片");
                return;
            }
            List<LocalMedia> medias = new ArrayList<>();
            for (int i = 0; i < images.size(); i++) {
                LocalMedia media = new LocalMedia();
                media.position = i;
                media.setPath(images.get(i).getUrl());
                medias.add(media);
            }
            PictureSelector.create(TweetActivity.this).themeStyle(R.style.picture_default_style).openExternalPreview(position, medias);
        }
    };

    //评论框的发送监听,用于更新评论
    private CommentBox.OnCommentSendClickListener onCommentSendClickListener = new CommentBox
            .OnCommentSendClickListener() {
        @Override
        public void onCommentSendClick(Comment commentInfo, String commentContent) {
            Comment comment = new Comment(user, commentContent);
            int itemPos = mViewHelper.getCommentItemDataPosition();
            adapter.getData().get(itemPos).getComments().add(comment);
            adapter.notifyItemChanged(itemPos);
            commentBox.clearDraft();
            commentBox.dismissCommentBox(true);
        }
    };


    private CallBack callBack = new CallBack() {
        @Override
        public void getUserSuccess(User data) {
            user = data;
            hostViewHolder.loadHostData(user);
        }

        @Override
        public void getTweetsSuccess(List<Tweet> tweets) {
            if (tweets.size() == 0)
                ToastUtils.showToast(mContext, "没有更多Tweet了");
            circleRecyclerView.complete();
            if (isLoadMore) {
                adapter.addMore(tweets);
            } else {
                adapter.updateData(tweets);
            }
        }

        @Override
        public void failed(String message) {
            circleRecyclerView.complete();
        }
    };

    //观察键盘弹出与消退
    private void initKeyboardHeightObserver() {
        KeyboardControlManager.observerKeyboardVisibleChange(this, new KeyboardControlManager.OnKeyboardStateChangeListener() {

            View anchorView;

            @Override
            public void onKeyboardChange(int keyboardHeight, boolean isVisible) {
                int commentType = commentBox.getCommentType();
                if (isVisible) {
                    anchorView = mViewHelper.alignCommentBoxToView(circleRecyclerView, commentBox, commentType);
                } else {
                    //定位到底部
                    commentBox.dismissCommentBox(false);
                    mViewHelper.alignCommentBoxToViewWhenDismiss(circleRecyclerView, commentBox, commentType, anchorView);
                }
            }
        });
    }
}