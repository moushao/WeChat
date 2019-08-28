package com.tw.wechat.entity;

import java.util.ArrayList;
import java.util.List;

public class Tweet {
    public int position;
    private String content;
    private List<Photo> images;
    private Sender sender;
    private List<Comment> comments;

    public String getContent() {
        return content == null ? "" : content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Photo> getImages() {
        if (images == null) {
            return new ArrayList<>();
        }
        return images;
    }

    public void setImages(List<Photo> images) {
        this.images = images;
    }

    public Sender getSender() {
        return sender;
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }

    public List<Comment> getComments() {
        if (comments == null) {
            comments = new ArrayList<>();
        }
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public boolean isQualified() {
        if (content == null && (images == null || images.size() == 0))
            return false;
        return true;
    }
}
