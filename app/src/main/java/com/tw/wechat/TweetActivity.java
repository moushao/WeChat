package com.tw.wechat;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.tw.wechat.adapter.CircleMomentsAdapter;
import com.tw.wechat.adapter.HostViewHolder;
import com.tw.wechat.adapter.MultiImageMomentsVH;
import com.tw.wechat.entity.Comment;
import com.tw.wechat.entity.Photo;
import com.tw.wechat.entity.Tweet;
import com.tw.wechat.entity.User;
import com.tw.wechat.event.CallBack;
import com.tw.wechat.event.ItemListener;
import com.tw.wechat.event.OnRefreshListener2;
import com.tw.wechat.event.ViewListener;
import com.tw.wechat.model.MainModel;
import com.tw.wechat.utils.ToastUtils;
import com.tw.wechat.widget.CircleViewHelper;
import com.tw.wechat.widget.commentwidget.CommentBox;
import com.tw.wechat.widget.commentwidget.CommentWidget;
import com.tw.wechat.widget.commentwidget.KeyboardControlMnanager;
import com.tw.wechat.widget.pullryc.CircleRecyclerView;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class TweetActivity extends AppCompatActivity implements CircleRecyclerView.OnPreDispatchTouchListener {
    CircleRecyclerView circleRecyclerView;
    private HostViewHolder hostViewHolder;
    private Context mContext;
    private CircleMomentsAdapter adapter;
    private boolean isLoadMore;
    private CommentBox commentBox;
    private CircleViewHelper mViewHelper;
    private User user;
    private int offset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        circleRecyclerView = findViewById(R.id.recycler);
        commentBox = findViewById(R.id.widget_comment);
        mContext = this;
        initWidget();
        MainModel.getInstance().getUser(callBack);
        MainModel.getInstance().getTweets(offset, callBack);
        ToastUtils.showToast(this, Build.VERSION.SDK_INT + "");
    }

    private void initWidget() {
        if (mViewHelper == null) {
            mViewHelper = new CircleViewHelper(this);
        }
        hostViewHolder = new HostViewHolder(this, new ItemListener() {
            @Override
            public void onItemClick(View view, int position, Object mData) {
                ToastUtils.showToast(mContext, "照片墙被点击了");
            }
        });

        circleRecyclerView.setOnRefreshListener(refreshListener);
        circleRecyclerView.setOnPreDispatchTouchListener(this);
        circleRecyclerView.addHeaderView(hostViewHolder.getView());

        commentBox = (CommentBox) findViewById(R.id.widget_comment);
        commentBox.setOnCommentSendClickListener(onCommentSendClickListener);

        CircleMomentsAdapter.Builder<Tweet> builder = new CircleMomentsAdapter.Builder<>(this);
        adapter = builder
                .addType(MultiImageMomentsVH.class, 2, R.layout.moments_multi_image)
                .setListener(viewListener)
                .build();
        circleRecyclerView.setAdapter(adapter);
        initKeyboardHeightObserver();
    }

    private OnRefreshListener2 refreshListener =
            new OnRefreshListener2() {
                @Override
                public void onRefresh() {
                    ToastUtils.showToast(mContext, "刷新了Tweet列表");
                    offset = 0;
                    isLoadMore = false;
                    MainModel.getInstance().getTweets(offset, callBack);
                }

                @Override
                public void onLoadMore() {
                    offset++;
                    isLoadMore = true;
                    MainModel.getInstance().getTweets(offset, callBack);
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

    private ViewListener viewListener = new ViewListener() {
        @Override
        public void toggleShowCommentBox(View view, CommentWidget commentWidget, int itemPos, Tweet tweet, Comment comment) {
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
            adapter.getDatas().get(itemPosition).getComments().remove(commentPosition);
            adapter.notifyItemChanged(itemPosition);
        }

        @Override
        public void deleteRelease(int itemPosition) {
            adapter.getDatas().remove(itemPosition);
            adapter.notifyItemChanged(itemPosition);
        }

        @Override
        public void preViewPicture(List<Photo> images, int position) {
            if (Build.VERSION.SDK_INT > 16) {
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

    private CommentBox.OnCommentSendClickListener onCommentSendClickListener = new CommentBox
            .OnCommentSendClickListener() {
        @Override
        public void onCommentSendClick(Comment commentInfo, String commentContent) {
            Comment comment = new Comment(user, commentContent);
            int itemPos = mViewHelper.getCommentItemDataPosition();
            adapter.getDatas().get(itemPos).getComments().add(comment);
            adapter.notifyItemChanged(itemPos);
            commentBox.clearDraft();
            commentBox.dismissCommentBox(true);
        }
    };

    private void initKeyboardHeightObserver() {
        //观察键盘弹出与消退
        KeyboardControlMnanager.observerKeyboardVisibleChange(this, new KeyboardControlMnanager
                .OnKeyboardStateChangeListener() {
            View anchorView;

            @Override
            public void onKeyboardChange(int keyboardHeight, boolean isVisible) {
                int commentType = commentBox.getCommentType();
                if (isVisible) {
                    //定位评论框到view
                    anchorView = mViewHelper.alignCommentBoxToView(circleRecyclerView, commentBox, commentType);
                } else {
                    //定位到底部
                    commentBox.dismissCommentBox(false);
                    mViewHelper.alignCommentBoxToViewWhenDismiss(
                            circleRecyclerView, commentBox, commentType, anchorView);
                }
            }
        });
    }

    private CallBack callBack = new CallBack() {
        @Override
        public void getUserSuccess(User data) {
            user = data;
            hostViewHolder.loadHostData(user);
            circleRecyclerView.complete();
        }

        @Override
        public void getTweetsSuccess(List<Tweet> tweets) {
            circleRecyclerView.complete();
            if (tweets.size() == 0)
                ToastUtils.showToast(mContext, "没有更多Tweet了");
            if (isLoadMore) {
                adapter.addMore(tweets);
                circleRecyclerView.complete();
            } else {
                adapter.updateData(tweets);
            }
        }

        @Override
        public void failed(String message) {
            circleRecyclerView.complete();
        }
    };


}