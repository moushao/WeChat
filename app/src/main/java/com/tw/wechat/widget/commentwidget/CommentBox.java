package com.tw.wechat.widget.commentwidget;


import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tw.wechat.R;
import com.tw.wechat.entity.Comment;
import com.tw.wechat.entity.Comment;
import com.tw.wechat.utils.StringUtil;
import com.tw.wechat.utils.UIHelper;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by 大灯泡 on 2016/12/8.
 * <p>
 * 评论输入框
 */

// FIXME: 2016/12/13 跟别的控件耦合度较高，后期考虑优化
public class CommentBox extends FrameLayout {

    private EditText mInputContent;
    private TextView mSend;
    private OnCommentSendClickListener onCommentSendClickListener;

    private boolean isShowing;

    //草稿
    private String draftString;

    private Comment commentInfo;
    private Long statusID;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({CommentType.TYPE_CREATE, CommentType.TYPE_REPLY})
    public @interface CommentType {
        //评论
        int TYPE_CREATE = 0x10;
        //回复
        int TYPE_REPLY = 0x11;
    }


    public CommentBox(Context context) {
        this(context, null);
    }

    public CommentBox(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommentBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.widget_comment_box, this);
        mInputContent = (EditText) findViewById(R.id.ed_comment_content);
        mSend = (TextView) findViewById(R.id.btn_send);
        mSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCommentSendClickListener != null)
                    onCommentSendClickListener.onCommentSendClick(
                            v,
                            statusID,
                            commentInfo == null ? null : commentInfo,
                            mInputContent.getText().toString().trim());
            }
        });
        setVisibility(GONE);
    }

    public void showCommentBox(@NonNull Long statusID, @Nullable Comment commentInfo) {
//        if (TextUtils.isEmpty(statusID))
//            return;
        if (isShowing)
            return;
        this.isShowing = true;
        this.commentInfo = commentInfo;
        //对不同的回复动作执行不同的
        if (this.commentInfo != null) {
            mInputContent.setHint("回复 " + commentInfo.getSender().getNick() + ":");
        } else {
            mInputContent.setHint("评论");
        }
        //对于同一条动态恢复草稿，否则不恢复
        if (TextUtils.equals(statusID+"", this.statusID+"") && StringUtil.noEmpty(draftString)) {
            mInputContent.setText(draftString);
            mInputContent.setSelection(draftString.length());
        } else {
            mInputContent.setText(null);
        }
        setMomentid(statusID);
        setVisibility(VISIBLE);
        UIHelper.showInputMethod(mInputContent, 150);
    }

    public void dismissCommentBox(boolean clearDraft) {
        if (!isShowing)
            return;
        this.isShowing = false;
        if (!clearDraft) {
            this.draftString = mInputContent.getText().toString().trim();
        } else {
            clearDraft();
        }
        UIHelper.hideInputMethod(mInputContent);
        setVisibility(GONE);
    }

    /**
     * 切换评论框的状态
     *
     * @param statusID
     * @param commentInfo
     * @param clearDraft  是否清除草稿
     */
    public void toggleCommentBox(@NonNull long statusID, @Nullable Comment commentInfo, boolean clearDraft) {
        if (isShowing) {
            dismissCommentBox(clearDraft);
        } else {
            showCommentBox(statusID, commentInfo);
        }
    }

    public boolean isShowing() {
        return isShowing;
    }

    public void clearDraft() {
        this.draftString = null;
    }


    public Long getMomentid() {
        return statusID;
    }

    public void setMomentid(Long statusID) {
        this.statusID = statusID;
    }

    public Comment getCommentInfo() {
        return commentInfo;
    }

    public void setCommentInfo(Comment commentInfo) {
        this.commentInfo = commentInfo;
    }

    public boolean isReply() {
        return commentInfo != null;
    }

    @CommentType
    public int getCommentType() {
        return commentInfo == null ? CommentType.TYPE_CREATE : CommentType.TYPE_REPLY;
    }

    @Override
    protected void onDetachedFromWindow() {
        dismissCommentBox(true);
        super.onDetachedFromWindow();
    }

    public OnCommentSendClickListener getOnCommentSendClickListener() {
        return onCommentSendClickListener;
    }

    public void setOnCommentSendClickListener(OnCommentSendClickListener onCommentSendClickListener) {
        this.onCommentSendClickListener = onCommentSendClickListener;
    }

    public interface OnCommentSendClickListener {
        void onCommentSendClick(View v, Long statusID, Comment commentInfo, String commentContent);
    }
}
