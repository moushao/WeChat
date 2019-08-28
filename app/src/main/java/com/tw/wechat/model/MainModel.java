package com.tw.wechat.model;


import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.tw.wechat.entity.Comment;
import com.tw.wechat.entity.Tweet;
import com.tw.wechat.entity.User;
import com.tw.wechat.event.CallBack;
import com.tw.wechat.retrofit.RetrofitManager;
import com.tw.wechat.utils.LogUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class MainModel {
    /**
     *
     */
    public void getUser(final CallBack callBack) {
        RetrofitManager.getInstance()
                .getApi()
                .getUserInfo()
                .map(new Function<ResponseBody, User>() {
                    @Override
                    public User apply(ResponseBody responseBody) throws Exception {
                        String result = responseBody.string();
                        LogUtil.e(result);
                        Type type = new TypeToken<User>() {
                        }.getType();
                        User user = new Gson().fromJson(result.replace("profile-image", "profileImage"), type);
                        return user;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<User>() {
                    @Override
                    public void accept(User user) throws Exception {
                        callBack.getUserSuccess(user);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtil.e(throwable.getLocalizedMessage());
                    }
                });
    }

    public void getTweets(final CallBack callBack) {
        RetrofitManager.getInstance()
                .getApi()
                .getTweets()
                .map(new Function<ResponseBody, List<Tweet>>() {
                    @Override
                    public List<Tweet> apply(ResponseBody responseBody) throws Exception {
                        String result = responseBody.string();
                        Type type = new TypeToken<List<Tweet>>() {
                        }.getType();
                        ArrayList<Tweet> fromJson2 = new Gson().fromJson(result, type);
                        List<Tweet> tweets = new ArrayList<>();
                        for (int i = 0; i < fromJson2.size(); i++) {
                            Tweet tw = fromJson2.get(i);
                            if (tw.isQualified()/* && tweets.size() <2*/) {
                                tw.position = i;
                                //tw.setComments(new ArrayList<Comment>());
                                tweets.add(tw);
                            }
                        }
                        LogUtil.e("size", fromJson2.size() + ":" + tweets.size());
                        return tweets;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Tweet>>() {
                    @Override
                    public void accept(List<Tweet> tweets) throws Exception {
                        callBack.getTweetsSuccess(tweets);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtil.e(throwable.getLocalizedMessage());
                    }
                });
    }
}
