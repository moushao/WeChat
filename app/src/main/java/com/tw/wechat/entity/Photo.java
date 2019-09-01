package com.tw.wechat.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * 类名: {@link Photo}
 * <br/> 功能描述:朋友圈照片的实体类
 */
@Entity
public class Photo {
    /**
     * 数据存储id
     */
    @Id
    private Long Id;
    /**
     * 关联tweet的id
     */
    private Long tweetId;
    /**
     * 图片url
     */
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
