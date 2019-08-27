package com.tw.wechat.event;

import com.tw.wechat.entity.Tweet;
import com.tw.wechat.entity.User;

import java.util.List;

/**
 * 类名: {@link CallBack}
 * <br/> 功能描述: mvp模式,解析层和原型层之间的回调借口
 * <br/> 作者: MouTao
 * <br/> 时间: 2017/5/16
 * <br/> 最后修改者:
 * <br/> 最后修改内容:
 */
public interface CallBack {

    void getUserSuccess(User user);

    void getTweetsSuccess(List<Tweet> tweets);

    public void failed(String message);
}