package com.tw.wechat.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class Photo {
    @Id
    private Long Id;
    private Long tweetId;
    private String url;

    @Generated(hash = 34307316)
    public Photo(Long Id, Long tweetId, String url) {
        this.Id = Id;
        this.tweetId = tweetId;
        this.url = url;
    }

    @Generated(hash = 1043664727)
    public Photo() {
    }

    public String getUrl() {
        return url == null ? "" : url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getTweetId() {
        return this.tweetId;
    }

    public void setTweetId(Long tweetId) {
        this.tweetId = tweetId;
    }

    public Long getId() {
        return this.Id;
    }

    public void setId(Long Id) {
        this.Id = Id;
    }
}
