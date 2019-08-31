package com.tw.wechat.widget.commentwidget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.tw.wechat.entity.Comment;

import androidx.annotation.NonNull;


/**
 * Created by 大灯泡 on 2016/2/23.
 * 评论控件
 */
public class CommentWidget extends TextView {
    private static final String TAG = "CommentWidget";
    //用户名颜色
    private int textColor = 0xff517fae;
    private static final int textSize = 14;
    private int commentPositon;

    public CommentWidget(Context context, int commentPositon) {
        this(context, commentPositon, null);
    }

    public CommentWidget(Context context, int commentPositon, AttributeSet attrs) {
        this(context, commentPositon, attrs, 0);
    }

    public CommentWidget(Context context, int commentPositon, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.commentPositon = commentPositon;

        setMovementMethod(LinkMovementMethod.getInstance());
        //setOnTouchListener(new ClickableSpanEx.ClickableSpanSelector());
        this.setHighlightColor(0x00000000);
        setTextSize(textSize);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CommentWidget(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setMovementMethod(LinkMovementMethod.getInstance());
        this.setHighlightColor(0x00000000);
        setTextSize(textSize);
    }

    public void setCommentText(Comment info, int commentPosition) {
        if (info == null)
            return;
        try {
            setTag(info);
            this.commentPositon = commentPosition;
            createCommentStringBuilder(info);
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.e(TAG, "虽然在下觉得不可能会有这个情况，但还是捕捉下吧，万一被打脸呢。。。");
        }
    }

    private void createCommentStringBuilder(@NonNull Comment info) {
        String repet = "回复:";
        if (info.getContent().contains(":"))
            repet = repet.replace(":", "");

        String text = info.getSender().getNick() + repet + info.getContent();
        SpannableStringBuilder ssb = new SpannableStringBuilder(text);
        ssb.setSpan(new ForegroundColorSpan(textColor),
                0, text.indexOf("回"),
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        ssb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, text.indexOf("回"), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //粗体 

        if (text.indexOf("复") + 1 != text.indexOf(":")) {
            ssb.setSpan(new ForegroundColorSpan(textColor),
                    text.indexOf("复") + 1,
                    text.indexOf(":"),
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            ssb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), text.indexOf("复") + 1,
                    text.indexOf(":"), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //粗体 
        }


        setText(ssb);
    }

    public Comment getData() throws ClassCastException {
        return (Comment) getTag();
    }

    public int getCommentPosition() {
        return commentPositon;
    }
}
