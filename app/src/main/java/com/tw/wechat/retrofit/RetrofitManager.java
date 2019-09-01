package com.tw.wechat.retrofit;


import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * 类名: {@link RetrofitManager}
 * <br/> 功能描述:集中处理Api相关配置的Manager类
 * <br/> 作者: MouShao
 * <br/> 时间: 2016/9/8
 */
public class RetrofitManager {
    public static final String HOST_NAME = "https://thoughtworks-ios.herokuapp.com/";
    public static RetrofitManager sApiManager;
    private TweetApi mApi;

    public RetrofitManager() {

    }

    public static RetrofitManager getInstance() {
        if (sApiManager == null) {
            synchronized (RetrofitManager.class) {
                if (sApiManager == null) {
                    sApiManager = new RetrofitManager();
                }
            }
        }
        return sApiManager;
    }


    @NonNull
    public Retrofit getRetrofit() {
        OkHttpClient mClient = new OkHttpClient.Builder()
                .addInterceptor(new CustomInterceptor())
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();

        return new Retrofit.Builder()
                .client(mClient)
                .baseUrl(HOST_NAME)
                //.addConverterFactory(CustomGsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public TweetApi getApi() {
        if (mApi == null) {
            mApi = getRetrofit().create(TweetApi.class);
        }
        return mApi;
    }
}
