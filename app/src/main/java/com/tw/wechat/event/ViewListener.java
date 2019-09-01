package com.tw.wechat.event;

import android.view.View;

import com.tw.wechat.entity.Comment;
import com.tw.wechat.entity.Photo;
import com.tw.wechat.entity.Tweet;
import com.tw.wechat.widget.commentwidget.CommentWidget;

import java.util.List;

/**
 * 类名: {@link ViewListener}
 * <br/> 功能描述:用于Adapter中与主界面的交互回调
 */
public interface ViewListener {
    /**
     * 显示或者隐藏评论输入框
     *
     * @param viewHolderRootView：当前view
     * @param commentWidget：评论的控件
     * @param itemPos：当前view的位置
     * @param comment：评论内容
     */
    void toggleShowCommentBox(View viewHolderRootView, CommentWidget commentWidget, int itemPos, Comment comment);

    /**
     * 判断当前tweet是否是我发的
     *
     * @param commentInfo
     * @return 是否是我发的tweet
     */
    boolean isMyContent(String commentInfo);

    /**
     * 删除当前评论
     *
     * @param itemPosition    当前tweet的位置
     * @param commentPosition 当前评论的位置
     */
    void deleteComment(int itemPosition, int commentPosition);

    /**
     * 删除当前tweet
     *
     * @param itemPosition 删除位置
     */
    void deleteRelease(int itemPosition);

    /**
     * 预览照片
     *
     * @param images   需要预览的照片列表
     * @param position 选中照片的位置
     */
    void preViewPicture(List<Photo> images, int position);
}
