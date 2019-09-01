package com.tw.wechat.event;

import com.tw.wechat.entity.Tweet;
import com.tw.wechat.entity.User;

import java.util.List;

/**
 * 类名: {@link VCCallBack}
 * <br/> 功能描述: vc模式,Model层和View层之间的回调借口
 */
public interface VCCallBack {
    /**
     * 成功获取User后的回调
     *
     * @param user 获取到的User
     */
    void getUserSuccess(User user);

    /**
     * 成功获取Tweet列表的回调
     *
     * @param tweets 获取到的tweet列表
     */
    void getTweetsSuccess(List<Tweet> tweets);

    /**
     * 加载失败的回调
     *
     * @param message 失败信息
     */
    public void failed(String message);
}