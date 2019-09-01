package com.tw.wechat.retrofit;


import io.reactivex.Observable;
import okhttp3.ResponseBody;
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
    /**
     * 网络加载用户信息
     *
     * @return json结果
     */
    @GET("user/jsmith/")
    Observable<ResponseBody> getUserInfo();

    /**
     * 网络加载tweet列表
     *
     * @return json结果
     */
    @GET("user/jsmith/tweets/")
    Observable<ResponseBody> getTweets();
}
