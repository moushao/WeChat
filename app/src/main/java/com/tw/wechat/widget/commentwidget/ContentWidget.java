package com.tw.wechat.widget.commentwidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tw.wechat.R;
import com.tw.wechat.entity.Tweet;
import com.tw.wechat.utils.LogUtil;
import com.tw.wechat.utils.TextStateManager;
import com.tw.wechat.utils.UIHelper;


/**
 * 类名: {@link ContentWidget}
 * <br/> 功能描述:点击展开更多,注意holder的view缓存,导致数据混乱
 */
public class ContentWidget extends LinearLayout implements View.OnClickListener {


    public static final int CLOSE = -1;
    public static final int OPEN = 1;

    private TextView mTextView;
    private TextView mClickToShow;

    private int textColor;
    private int textSize;

    private int showLine;
    private String clickText;

    private boolean hasMore;

    public ContentWidget(Context context) {
        this(context, null);
    }

    public ContentWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ContentWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ContentWidget);
        textColor = a.getColor(R.styleable.ContentWidget_text_color, 0xff1a1a1a);
        textSize = a.getDimensionPixelSize(R.styleable.ContentWidget_text_size, 14);
        showLine = a.getInt(R.styleable.ContentWidget_show_line, 8);
        clickText = a.getString(R.styleable.ContentWidget_click_text);
        if (TextUtils.isEmpty(clickText))
            clickText = "全文";
        a.recycle();
        initView(context);

    }

    private void initView(Context context) {
        mTextView = new TextView(context);
        mClickToShow = new TextView(context);

        mTextView.setTextSize(textSize);
        mTextView.setTextColor(textColor);
        mTextView.setMaxLines(showLine);

        mClickToShow.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_bg_comment_more_tx));
        mClickToShow.setTextSize(textSize);
        mClickToShow.setTextColor(getResources().getColor(R.color.color_nick));
        mClickToShow.setText(clickText);

        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = UIHelper.dipToPx(10f);
        mClickToShow.setLayoutParams(params);
        mClickToShow.setOnClickListener(this);

        setOrientation(VERTICAL);
        addView(mTextView);
        addView(mClickToShow);
    }

    @Override
    public void onClick(View v) {
        boolean needOpen = TextUtils.equals(((TextView) v).getText().toString(), clickText);
        int state = needOpen ? OPEN : CLOSE;
        setState(state);
        int hasCode = getText().hashCode();
        if (hasCode != 0)
            TextStateManager.INSTANCE.put(getText().hashCode(), state);
    }


    public void setState(int state) {
        switch (state) {
            case CLOSE:
                mTextView.setMaxLines(showLine);
                mClickToShow.setText(clickText);
                break;
            case OPEN:
                mTextView.setMaxLines(Integer.MAX_VALUE);
                mClickToShow.setText("收起");
                break;
        }
    }


    public void setText(Tweet tweet) {
        if (tweet.getPrePosition() == 21) {
            LogUtil.e("发送到");
        }
        int hasCode = tweet.getContent().hashCode();
        if (hasCode == 0) {
            mTextView.setText("");
            mClickToShow.setVisibility(GONE);
            return;
        }
        int state = TextStateManager.INSTANCE.get(hasCode);
        String str = tweet.getContent();
        if (state == 0 || state == CLOSE) {//没有保存过状态,则直接设置文字并保存状态
            mTextView.setText(str);
            mTextView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    hasMore = mTextView.getLineCount() > showLine;
                    mClickToShow.setVisibility(hasMore ? VISIBLE : GONE);
                    mTextView.getViewTreeObserver().removeOnPreDrawListener(this);
                    return true;
                }
            });
            TextStateManager.INSTANCE.put(hasCode, CLOSE);
        } else if (state == OPEN) {//保存过状态,且状态是1
            mTextView.setMaxLines(Integer.MAX_VALUE);
            mTextView.setText(str);
            mClickToShow.setText("收起");
            mClickToShow.setVisibility(VISIBLE);
        }


    }

    public CharSequence getText() {
        return mTextView.getText();
    }

}
