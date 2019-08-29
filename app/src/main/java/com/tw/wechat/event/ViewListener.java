package com.tw.wechat.event;

import android.view.View;

import com.tw.wechat.entity.Comment;
import com.tw.wechat.entity.Photo;
import com.tw.wechat.entity.Tweet;
import com.tw.wechat.widget.commentwidget.CommentWidget;

import java.util.List;

public interface ViewListener {
    void toggleShowCommentBox(View viewHolderRootView, CommentWidget commentWidget, int itemPos, Tweet tweet, Comment comment);

    boolean isMyContent(String commentInfo);

    void deleteComment(int itemPosition, int commentPosition);

    void deleteRelease(int itemPosition);

    void preViewPicture(List<Photo> images, int position);
}
