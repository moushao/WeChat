package com.tw.wechat.entity;

/**
 * 类名: {@link User}
 * <br/> 功能描述:用户实体类,包含用户信息
 * <br/> 作者: MouShao
 * <br/> 时间: 2019/8/27
 * <br/> 最后修改者:
 * <br/> 最后修改内容:
 */
public class User extends Sender {

    /**
     * profile-image
     */
    private String profileImage;

    public String getProfile() {
        return profileImage == null ? "" : profileImage;
    }

    public void setProfile(String profile) {
        this.profileImage = profileImage;
    }
}
