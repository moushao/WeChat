package com.tw.wechat.widget.commentwidget;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.view.View;
import android.widget.Toast;

import com.tw.wechat.entity.Comment;
import com.tw.wechat.entity.Sender;
import com.tw.wechat.utils.DimenUtil;
import com.tw.wechat.widget.ClickableSpanEx;

import androidx.annotation.NonNull;


/**
 * Created by 大灯泡 on 2016/2/23.
 * 评论点击事件
 */
public class CommentClick extends ClickableSpanEx {
    private Context mContext;
    private int textSize;
    private Comment mUserInfo;

    private CommentClick() {
    }

    private CommentClick(Builder builder) {
        super(builder.color, builder.clickEventColor);
        mContext = builder.mContext;
        mUserInfo = builder.mUserInfo;
        this.textSize = builder.textSize;
    }

    @Override
    public void onClick(View widget) {
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setTextSize(textSize);
        ds.setTypeface(Typeface.DEFAULT_BOLD);
    }

    public static class Builder {
        private int color;
        private Context mContext;
        private int textSize = 16;
        private Comment mUserInfo;
        private int clickEventColor;

        public Builder(Context context, @NonNull Comment info) {
            mContext = context;
            mUserInfo = info;
        }

        public Builder setTextSize(int textSize) {
            this.textSize = (int) DimenUtil.sp2px(textSize);
            return this;
        }

        public Builder setColor(int color) {
            this.color = color;
            return this;
        }

        public Builder setClickEventColor(int color) {
            this.clickEventColor = color;
            return this;
        }

        public CommentClick build() {
            return new CommentClick(this);
        }
    }
}
