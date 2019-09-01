package com.tw.wechat.entity;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

/**
 * 类名: {@link User}
 * <br/> 功能描述:用户实体类,包含用户信息
 */
@Entity
public class User {
    @Id
    private Long Id;
    /**
     * name
     */
    private String username;
    /**
     * nick name
     */
    private String nick;

    /**
     * avatar
     */
    private String avatar;
    /**
     * profile-image
     */
    private String profileImage;
    /**
     * 人员类型
     * 1:我自己
     */
    private int type;


    @Generated(hash = 586692638)
    public User() {
    }

    @Generated(hash = 145582840)
    public User(Long Id, String username, String nick, String avatar,
                String profileImage, int type) {
        this.Id = Id;
        this.username = username;
        this.nick = nick;
        this.avatar = avatar;
        this.profileImage = profileImage;
        this.type = type;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getUsername() {
        return username == null ? "" : username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNick() {
        return nick == null ? "" : nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getAvatar() {
        return avatar == null ? "" : avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getProfileImage() {
        return profileImage == null ? "" : profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
