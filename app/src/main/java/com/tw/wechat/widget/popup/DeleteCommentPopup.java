package com.tw.wechat.widget.popup;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.tw.wechat.R;
import com.tw.wechat.entity.Comment;

import razerdp.basepopup.BasePopupWindow;

/**
 * 类名: {@link DeleteCommentPopup}
 * <br/> 功能描述:删除评论的popup
 */
public class DeleteCommentPopup extends BasePopupWindow implements View.OnClickListener {

    private TextView mDel;
    private TextView mCancel;
    private int commentPosition;
    private Comment commentInfo;

    public DeleteCommentPopup(Activity context) {
        super(context);
        setBackground(0);
        setBlurBackgroundEnable(false);

        mDel = (TextView) findViewById(R.id.delete);
        mCancel = (TextView) findViewById(R.id.cancel);

        setViewClickListener(this, mDel, mCancel);
    }

    public void showPopupWindow(Comment commentInfo, int commentPosition) {
        if (commentInfo == null)
            return;
        this.commentInfo = commentInfo;
        this.commentPosition = commentPosition;
        super.showPopupWindow();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.delete:
                if (mDeleteCommentClickListener != null) {
                    mDeleteCommentClickListener.onDelClick(commentInfo, commentPosition);
                }
                dismiss();
                break;
            case R.id.cancel:
                dismiss();
                break;
            default:
                break;
        }
    }

    private OnDeleteCommentClickListener mDeleteCommentClickListener;

    public OnDeleteCommentClickListener getOnDeleteCommentClickListener() {
        return mDeleteCommentClickListener;
    }

    public void setOnDeleteCommentClickListener(OnDeleteCommentClickListener deleteCommentClickListener) {
        mDeleteCommentClickListener = deleteCommentClickListener;
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_delete_comment);
    }

    public interface OnDeleteCommentClickListener {
        void onDelClick(Comment commentInfo, int commentPosition);
    }
}
