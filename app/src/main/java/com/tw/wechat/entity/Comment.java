package com.tw.wechat.entity;

public class Comment {
    private String content;
    private Sender sender;

    public Comment(Sender sender, String content) {
        this.content = content;
        this.sender = sender;
    }

    public String getContent() {
        return content == null ? "" : content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Sender getSender() {
        return sender;
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }
}
