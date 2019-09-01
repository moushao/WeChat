package com.tw.wechat.entity;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToOne;

import com.tw.wechat.dao.DaoSession;
import com.tw.wechat.dao.UserDao;
import com.tw.wechat.dao.CommentDao;

/**
 * 类名: {@link Comment}
 * <br/> 功能描述:朋友圈评论的实体类
 */

@Entity
public class Comment {
    /**
     * 此条评论的ID,用于数据库存储
     */
    @Id(autoincrement = true)
    private Long commentID;

    /**
     * tweetId,属于那一条tweet,用于数据库存储
     */
    private Long tweetId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评论者的ID
     */
    private Long senderID;

    /**
     * 评论的人
     */
    @ToOne(joinProperty = "senderID")
    @Keep
    private User sender;


    @Keep
    public Comment(User sender, String content) {
        this.content = content;
        this.sender = sender;
    }


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


    public void setCommentId(Long commentID) {
        this.commentID = commentID;
    }

    public Long getCommentID() {
        return commentID;
    }

    public void setCommentID(Long commentID) {
        this.commentID = commentID;
    }

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1903578761)
    private transient CommentDao myDao;

    @Generated(hash = 880682693)
    private transient Long sender__resolvedKey;

    @Generated(hash = 1409508996)
    public Comment(Long commentID, Long tweetId, String content, Long senderID) {
        this.commentID = commentID;
        this.tweetId = tweetId;
        this.content = content;
        this.senderID = senderID;
    }

    @Generated(hash = 1669165771)
    public Comment() {
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
    @Generated(hash = 2038262053)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getCommentDao() : null;
    }

}
