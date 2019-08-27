package com.tw.wechat.entity;

public class Photo {
    private String url;

    public String getUrl() {
        return url == null ? "" : url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
