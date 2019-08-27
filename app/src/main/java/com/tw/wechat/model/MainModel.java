package com.tw.wechat.model;

import com.tw.wechat.entity.Tweet;
import com.tw.wechat.entity.User;
import com.tw.wechat.event.CallBack;
import com.tw.wechat.retrofit.RetrofitManager;
import com.tw.wechat.utils.LogUtil;

import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainModel {
    /**
     *
     */
    public void getUser(final CallBack callBack) {
        RetrofitManager.getInstance()
                .getApi()
                .getUserInfo()
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
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Tweet>>() {
                    @Override
                    public void accept(List<Tweet> tweets) throws Exception {
                        LogUtil.e("tweets get success");
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
