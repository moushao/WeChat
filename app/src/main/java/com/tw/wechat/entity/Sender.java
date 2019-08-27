package com.tw.wechat.entity;

public class Sender {
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
}
