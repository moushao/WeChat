package com.tw.wechat.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.tw.wechat.R;
import com.tw.wechat.entity.Comment;
import com.tw.wechat.entity.Tweet;
import com.tw.wechat.photo.SimpleObjectPool;
import com.tw.wechat.utils.ImageLoadManager;
import com.tw.wechat.utils.LogUtil;
import com.tw.wechat.utils.UIHelper;
import com.tw.wechat.widget.ClickShowMoreLayout;
import com.tw.wechat.widget.PraiseWidget;
import com.tw.wechat.widget.commentwidget.CommentWidget;
import com.tw.wechat.widget.popup.CommentPopup;
import com.tw.wechat.widget.popup.DeleteCommentPopup;

import java.util.List;

import androidx.annotation.NonNull;


/**
 * Created by 大灯泡 on 2016/11/1.
 * <p>
 * 朋友圈基本item
 */
public abstract class CircleBaseViewHolder extends BaseRecyclerViewHolder<Tweet> implements
        BaseMomentVH<Tweet>, ViewGroup.OnHierarchyChangeListener {
    protected Context mContext;

    //头部
    protected ImageView avatar;
    protected TextView nick;
    protected ClickShowMoreLayout userText;

    //底部
    protected TextView createTime;
    protected TextView deleteRelease;
    protected ImageView commentImage;
    protected FrameLayout menuButton;
    protected LinearLayout commentAndPraiseLayout;
    protected PraiseWidget praiseWidget;
    protected View line;
    protected LinearLayout commentLayout;

    //内容区
    protected RelativeLayout contentLayout;

    //评论区的view对象池
    private static final SimpleObjectPool<CommentWidget> COMMENT_TEXT_POOL = new SimpleObjectPool<CommentWidget>(35);

    private CommentPopup commentPopup;
    private DeleteCommentPopup deleteCommentPopup;

    private int itemPosition; //当条朋友圈的位置
    private Tweet momentsInfo;


    public CircleBaseViewHolder(Context context, ViewGroup viewGroup, int layoutResId) {
        super(context, viewGroup, layoutResId);
        mContext = context;
        onFindView(itemView);

        //header
        avatar = (ImageView) findView(avatar, R.id.avatar);
        nick = (TextView) findView(nick, R.id.nick);
        userText = (ClickShowMoreLayout) findView(userText, R.id.item_text_field);

        //bottom
        createTime = (TextView) findView(createTime, R.id.create_time);
        deleteRelease = (TextView) itemView.findViewById(R.id.delete_release);

        // deleteRelease = (TextView) findView(deleteRelease, R.id.delete_release);
        commentImage = (ImageView) findView(commentImage, R.id.menu_img);
        menuButton = (FrameLayout) findView(menuButton, R.id.menu_button);
        commentAndPraiseLayout = (LinearLayout) findView(commentAndPraiseLayout, R.id.comment_praise_layout);
        praiseWidget = (PraiseWidget) findView(praiseWidget, R.id.praise);
        line = findView(line, R.id.divider);
        commentLayout = (LinearLayout) findView(commentLayout, R.id.comment_layout);
        //content
        contentLayout = (RelativeLayout) findView(contentLayout, R.id.content);

        if (commentPopup == null) {
            commentPopup = new CommentPopup((Activity) getContext());
            commentPopup.setOnCommentPopupClickListener(onCommentPopupClickListener);
        }
        if (deleteCommentPopup == null) {
            deleteCommentPopup = new DeleteCommentPopup((Activity) getContext());
        }
        deleteRelease.setText("删除");


    }


    @Override
    public void onBindData(Tweet data, int position) {
        if (data == null) {
            LogUtil.e("数据是空的！！！！");
            findView(userText, R.id.item_text_field);
            userText.setText("这个动态的数据是空的。。。。OMG");
            return;
        }
        this.momentsInfo = data;
        this.itemPosition = position;
        //通用数据绑定
        onBindMutualDataToViews(data);
        //点击事件
        menuButton.setOnClickListener(onMenuButtonClickListener);
        menuButton.setTag(R.id.momentinfo_data_tag_id, data);
        //if (userID == momentsInfo.getUserID()) {
        //    deleteRelease.setOnClickListener(deleteClick);
        //    deleteRelease.setVisibility(View.VISIBLE);
        //} else {
        //    deleteRelease.setVisibility(View.INVISIBLE);
        //}
        //传递到子类
        onBindDataToView(data, position, getViewType());
    }

    private void onBindMutualDataToViews(Tweet data) {
        //header
        ImageLoadManager.INSTANCE.loadImage(avatar, data.getSender().getAvatar());

        nick.setText(data.getSender().getNick());
        if (TextUtils.isEmpty(data.getContent())) {
            userText.setVisibility(View.GONE);
        } else {
            userText.setText(data.getContent());
        }
        boolean needCommentData = addComment(data.getComments());
        commentLayout.setVisibility(needCommentData ? View.VISIBLE : View.GONE);
        //bottom
        //createTime.setText(TimeUtil.getTimeString(Long.valueOf(data.getSdate())));
        //boolean needPraiseData = addLikes(data.getLikeList());
        //praiseWidget.setVisibility(needPraiseData ? View.VISIBLE : View.GONE);
        //line.setVisibility(needPraiseData && needCommentData ? View.VISIBLE : View.GONE);
        //commentAndPraiseLayout.setVisibility(needCommentData || needPraiseData ? View.VISIBLE : View.GONE);
    }

    private CommentPopup.OnCommentPopupClickListener onCommentPopupClickListener = new CommentPopup
            .OnCommentPopupClickListener() {
        @Override
        public void onLikeClick(View v, @NonNull Tweet info, boolean hasLiked, Long friendPraiseID) {

        }

        @Override
        public void onCommentClick(View v, @NonNull Tweet info) {
        }
    };
    private int commentLeftAndPaddintRight = UIHelper.dipToPx(8f);
    private int commentTopAndPaddintBottom = UIHelper.dipToPx(3f);

    /**
     * 添加评论
     *
     * @param commentList
     * @return ture=显示评论，false=不显示评论
     */
    private boolean addComment(List<Comment> commentList) {
        if (commentList == null || commentList.size() == 0) {
            return false;
        }
        final int childCount = commentLayout.getChildCount();
        if (childCount < commentList.size()) {
            //当前的view少于list的长度，则补充相差的view
            int subCount = commentList.size() - childCount;
            for (int i = 0; i < subCount; i++) {
                CommentWidget commentWidget = COMMENT_TEXT_POOL.get();
                if (commentWidget == null) {
                    commentWidget = new CommentWidget(getContext(), i);
                    commentWidget.setPadding(commentLeftAndPaddintRight, commentTopAndPaddintBottom,
                            commentLeftAndPaddintRight, commentTopAndPaddintBottom);
                    commentWidget.setLineSpacing(4, 1);
                }
                commentWidget.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable
                        .common_selector));
                commentWidget.setOnClickListener(onCommentWidgetClickListener);
                commentWidget.setOnLongClickListener(onCommentLongClickListener);
                commentLayout.addView(commentWidget);
            }
        } else if (childCount > commentList.size()) {
            //当前的view的数目比list的长度大，则减去对应的view
            commentLayout.removeViews(commentList.size(), childCount - commentList.size());
        }
        //绑定数据
        for (int n = 0; n < commentList.size(); n++) {
            CommentWidget commentWidget = (CommentWidget) commentLayout.getChildAt(n);
            if (commentWidget != null)
                commentWidget.setCommentText(commentList.get(n));
        }
        return true;
    }

    private View.OnLongClickListener onCommentLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    };
    private View.OnClickListener onCommentWidgetClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!(v instanceof CommentWidget))
                return;
            Comment commentInfo = ((CommentWidget) v).getData();
            if (commentInfo == null)
                return;
            deleteCommentPopup.showPopupWindow(commentInfo, ((CommentWidget) v).getCommentPosition());
        }
    };


    @Override
    public void onChildViewAdded(View parent, View child) {

    }

    @Override
    public void onChildViewRemoved(View parent, View child) {
        if (child instanceof CommentWidget)
            COMMENT_TEXT_POOL.put((CommentWidget) child);
    }


    private View.OnClickListener onMenuButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Tweet info = (Tweet) v.getTag(R.id.momentinfo_data_tag_id);
            if (info != null) {
                commentPopup.updateMomentInfo(info);
                commentPopup.showPopupWindow(commentImage);
            }
        }
    };


    /**
     * ============  tools method block
     */


    protected final View findView(View view, int resid) {
        if (resid > 0 && itemView != null && view == null) {
            return itemView.findViewById(resid);
        }
        return view;
    }


}
