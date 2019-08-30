package com.tw.wechat.entity;

import java.security.Key;

import android.text.TextUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.ArrayList;
import java.util.List;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

import com.tw.wechat.dao.DaoSession;
import com.tw.wechat.dao.CommentDao;
import com.tw.wechat.dao.PhotoDao;
import com.tw.wechat.dao.UserDao;
import com.tw.wechat.dao.TweetDao;

/**
 * 类名: {@link Tweet}
 * <br/> 功能描述:朋友圈的实体类
 * <br/> 作者: MouShao
 * <br/> 时间: 2019/8/30
 * <br/> 最后修改者:
 * <br/> 最后修改内容:
 */
@Entity
public class Tweet {
    /**
     * tweet的id,新的位置
     */
    @Id
    private Long tweetId;

    /**
     * 网络加载下来的原始位置
     */
    public int prePosition;
    /**
     * tweet内容
     */
    private String content;
    /**
     * 发送人的id,用于数据库存储
     */
    private Long senderID;

    /**
     * 发送人
     */
    @ToOne(joinProperty = "senderID")
    @Keep
    private User sender;

    /**
     * 图片列表
     */
    @ToMany(referencedJoinProperty = "tweetId")
    @Keep
    private List<Photo> images;

    /**
     * 评论列表
     */
    @ToMany(referencedJoinProperty = "tweetId")
    @Keep
    private List<Comment> comments;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 725714356)
    private transient TweetDao myDao;

    @Generated(hash = 181437354)
    public Tweet(Long tweetId, int prePosition, String content, Long senderID) {
        this.tweetId = tweetId;
        this.prePosition = prePosition;
        this.content = content;
        this.senderID = senderID;
    }

    @Generated(hash = 2016926840)
    public Tweet() {
    }

    @Generated(hash = 880682693)
    private transient Long sender__resolvedKey;

    public Long getSenderID() {
        return senderID;
    }

    public void setSenderID(Long senderID) {
        this.senderID = senderID;
    }

    public Long getTweetId() {
        return tweetId;
    }

    public void setTweetId(Long tweetId) {
        this.tweetId = tweetId;
    }

    public int getPrePosition() {
        return prePosition;
    }

    public void setPrePosition(int prePosition) {
        this.prePosition = prePosition;
    }

    public String getContent() {
        return content == null ? "" : content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Keep
    public User getSender() {
        return sender;
    }

    @Keep
    public void setSender(User sender) {
        this.sender = sender;
    }

    public void setSenderID(long senderID) {
        this.senderID = senderID;
    }

    @Keep
    public List<Photo> getImages() {
        if (images == null) {
            images = new ArrayList<>();
        }
        return images;
    }

    @Keep
    public void setImages(List<Photo> images) {
        this.images = images;
    }

    @Keep
    public List<Comment> getComments() {
        if (comments == null) {
            comments = new ArrayList<>();
        }
        return comments;
    }

    @Keep
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    /**
     * 判断当前Twitter是否合格
     */
    public boolean isQualified() {
        if (TextUtils.isEmpty(content) && (images == null || images.size() == 0))
            return false;
        return true;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 604059028)
    public synchronized void resetImages() {
        images = null;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 249603048)
    public synchronized void resetComments() {
        comments = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1275107269)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTweetDao() : null;
    }
}
