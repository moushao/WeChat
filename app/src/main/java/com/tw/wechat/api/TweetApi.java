package com.tw.wechat.api;


import com.tw.wechat.entity.Tweet;
import com.tw.wechat.entity.User;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * 类名: {@link TweetApi}
 * <br/> 功能描述:获取数据的接口描述
 * <br/> 作者: MouShao
 * <br/> 时间: 2019/8/27
 * <br/> 最后修改者:
 * <br/> 最后修改内容:
 */
public interface TweetApi {
    @GET("user/jsmith/")
    Observable<User> getUserInfo();

    @GET("user/jsmith/tweets/")
    Observable<List<Tweet>> getTweets();
}
