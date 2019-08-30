package com.tw.wechat.controller;


import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tw.wechat.dao.CommentDao;
import com.tw.wechat.dao.DaoManager;
import com.tw.wechat.dao.PhotoDao;
import com.tw.wechat.dao.UserDao;
import com.tw.wechat.entity.Comment;
import com.tw.wechat.entity.Photo;
import com.tw.wechat.entity.Tweet;
import com.tw.wechat.entity.User;
import com.tw.wechat.event.CallBack;
import com.tw.wechat.retrofit.RetrofitManager;
import com.tw.wechat.utils.LogUtil;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

import com.tw.wechat.TweetActivity;

import io.reactivex.Observable;

/**
 * 类名: {@link TweetController}
 * <br/> 功能描述:{@link TweetActivity}的controller,用于加载数据
 * <br/> 作者: MouShao
 * <br/> 时间: 2019/8/30
 */
public class TweetController {
    private static TweetController INSTANCE;

    public static TweetController getInstance() {
        if (INSTANCE == null) {
            synchronized (TweetController.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TweetController();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * <br/> 方法名称: getUser
     * <br/> 方法详述: 获取User信息
     * <br/> 参数: callBack 加载成功后的回调
     * <br/> 流程介绍: 首先异步从本地数据库加载，主线程回调，如果无数据，则通过网络请求加载，如果有，直接回调View层
     * <br/>    1:首先异步从本地数据库加载
     * <br/>    2:主线程回调，判断数据是否为空，空则网络请求加载，不为空则直接回调View层
     */
    public void getUser(final CallBack callBack) {
        Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(ObservableEmitter emitter) throws Exception {
                User user = DaoManager.getInstance().getSession().getUserDao().queryBuilder().where(UserDao.Properties.Type.eq(1)).build().unique();
                if (user == null)
                    user = new User();
                emitter.onNext(user);
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<User>() {
                    @Override
                    public void accept(User user) throws Exception {
                        if (user == null || TextUtils.isEmpty(user.getUsername())) {
                            //用户数据为空，网络加载
                            getUserFromServer(callBack);
                        } else {
                            callBack.getUserSuccess(user);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtil.e(throwable.getLocalizedMessage());
                        callBack.failed("");
                    }
                });
    }

    /**
     * <br/> 方法名称:getUserFromServer
     * <br/> 方法详述:网络加载用户信息
     * <br/> 参数: callBack 加载成功后的回调
     * <br/> 流程介绍:
     * <br/>    1:异步请求网络加载用户信息，处理异常字段，解析数据
     * <br/>    2:保存用户信息到本地数据库
     * <br/>    3:回调主线程，回调View层，界面刷新
     */
    private void getUserFromServer(final CallBack callBack) {
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
                        user.setType(1);
                        saveUser(user);
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
                        callBack.failed("没有更多的Tweet了");
                    }
                });
    }

    /**
     * <br/> 方法名称: getTweets
     * <br/> 方法详述: 加载Tweet列表
     * <br/> 参数:
     * <br/>    1:offset 偏移位置,页数
     * <br/>    2:callBack 加载成功后的回调监听
     * <br/>方法流程:
     * <br/>    1:异步加载本地数据库tweets列表,判断是否为空
     * <br/>    2:不为空则查询Tweet的发送人、内容、图片列表、和评论列表,然后进行第三步
     * <br/>    3:回调主线程,
     */
    public void getTweets(final int offset, final CallBack callBack) {
        Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(ObservableEmitter emitter) throws Exception {
                //
                List<Tweet> tweets = DaoManager.getInstance().getSession().getTweetDao()
                        .queryBuilder().offset(5 * offset).limit(5).build().list();
                if (tweets.size() != 0) {
                    tweets = loadFullTweets(tweets);
                }
                emitter.onNext(tweets);
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Tweet>>() {
                    @Override
                    public void accept(List<Tweet> tweets) throws Exception {
                        if ((tweets == null || tweets.size() == 0) && offset == 0) {
                            getTweetsFromServer(callBack);
                        } else {
                            callBack.getTweetsSuccess(tweets);
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtil.e(throwable.getLocalizedMessage());
                        callBack.failed("没有更多的Tweet了");
                    }
                });
    }

    /**
     * <br/> 方法名称: loadFullTweets
     * <br/> 方法详述: 查询tweet的评论,图片列表等信息,补全tweet信息
     * <br/> 参数: tweets 本地已加载到的tweet列表
     * <br/>方法流程:
     * <br/>    1:异步本地加载发送人、图片列表、评论列表
     * <br/>    2:回调主线程
     */
    private List<Tweet> loadFullTweets(List<Tweet> tweets) {
        for (Tweet tw : tweets) {
            Long senderID = tw.getSenderID();
            User user = DaoManager.getInstance().getSession()
                    .getUserDao().queryBuilder()
                    .where(UserDao.Properties.Id.eq(senderID)).build().unique();
            List<Photo> images = DaoManager.getInstance().getSession()
                    .getPhotoDao().queryBuilder()
                    .where(PhotoDao.Properties.TweetId.eq(tw.getTweetId())).build().list();
            List<Comment> comments = DaoManager.getInstance().getSession()
                    .getCommentDao().queryBuilder()
                    .where(CommentDao.Properties.TweetId.eq(tw.getTweetId())).build().list();
            for (Comment comment : comments) {
                User commentUser = DaoManager.getInstance().getSession()
                        .getUserDao().queryBuilder()
                        .where(UserDao.Properties.Id.eq(comment.getSenderID())).build().unique();
                comment.setSender(commentUser);
            }
            tw.setSender(user);
            tw.setImages(images);
            tw.setComments(comments);
        }
        return tweets;
    }

    /**
     * <br/> 方法名称: getTweetsFromServer
     * <br/> 方法详述: 从服务器加载
     * <br/> 参数: callBack 加载成功后的回调监听
     * <br/>方法流程:
     * <br/>    1:异步网络请求数据,解析数据并处理不需要显示的Tweet(无图片和内容)
     * <br/>    2:判断数据是否大于5条,如果大于5则只取前五条
     * <br/>    3:回调主线程,刷新界面
     */
    public void getTweetsFromServer(final CallBack callBack) {
        RetrofitManager.getInstance()
                .getApi()
                .getTweets()
                .map(new Function<ResponseBody, List<Tweet>>() {
                    @Override
                    public List<Tweet> apply(ResponseBody responseBody) throws Exception {
                        String result = responseBody.string();
                        List<Tweet> tweets = praiseAndSaveTweets(result);
                        if (tweets.size() > 4) {
                            //防止子列表操作引起主列表的变化,所以从新复制
                            List<Tweet> list = tweets.subList(0, 5);
                            return list;
                        }
                        return tweets;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Tweet>>() {
                    @Override
                    public void accept(List<Tweet> tweets) throws Exception {
                        if (tweets != null && tweets.size() > 0)
                            callBack.getTweetsSuccess(tweets);
                        else
                            callBack.failed("没有更多的Tweet了");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtil.e(throwable.getLocalizedMessage());
                        callBack.failed("");
                    }
                });
    }

    /**
     * <br/> 方法名称: praiseAndSaveTweets
     * <br/> 方法详述: 解析数据并保存到本地数据库
     * <br/> 参数: result 数据源
     * <br/>方法流程:
     * <br/>     1:解析json数据,处理空的Tweet
     * <br/>     2:保存tweet到本地数据库
     * <br/>     3:返回处理后的Tweet
     */
    private List<Tweet> praiseAndSaveTweets(String result) throws IOException {
        List<Tweet> tweets = new ArrayList<>();
        if (TextUtils.isEmpty(result))
            return tweets;
        Type type = new TypeToken<List<Tweet>>() {
        }.getType();
        ArrayList<Tweet> fromJson2 = new Gson().fromJson(result, type);
        for (int i = 0; i < fromJson2.size(); i++) {
            Tweet tw = fromJson2.get(i);
            if (tw.isQualified()) {
                tw.prePosition = i;
                tw.setTweetId(Long.valueOf(tweets.size()));
                saveTweet(tw);
                tweets.add(tw);
            }
        }
        return tweets;
    }

    /**
     * <br/> 方法名称: saveTweet
     * <br/> 方法详述: 保存Tweet
     * <br/>一条Tweet包含以下属性
     * <br/>      原始原json位置;内容content,图片列表images,发布人sender,评论列表comments
     * <br/>      评论列表包含评论content
     * <br/>保存数据库:
     * <br/>    1:ToOne   先保存注解对象,再保存主对象
     * <br/>    3:ToMany  先保存主对象,再保存注解对象
     * <br/>综上所述:保存一条含有ToOne和ToMany的主对象,应该先保存ToOne注解对象,再保存主对象,最后保存ToMany注解对象
     */
    private void saveTweet(Tweet tw) {
        long senderID = saveUser(tw.getSender());
        tw.setSenderID(senderID);
        saveImages(tw);
        saveComments(tw);
        DaoManager.getInstance().getSession().insert(tw);
    }

    /**
     * <br/> 方法名称: saveComments
     * <br/> 方法详述: 保存评论列表
     * <br/>一条评论包含以下属性
     * <br/>      评论内容
     * <br/>      评论人
     * <br/>保存数据库:
     * <br/>    1:ToOne   先保存注解对象,再保存主对象
     * <br/>    3:ToMany  先保存主对象,再保存注解对象
     * <br/>综上所述:保存一条含有ToOne和ToMany的主对象,应该先保存ToOne注解对象,再保存主对象,最后保存ToMany注解对象
     */
    private void saveComments(Tweet tw) {
        List<Comment> comments = tw.getComments();
        if (comments == null || comments.size() == 0)
            return;
        for (int i = 0; i < comments.size(); i++) {
            Comment comment = comments.get(i);
            comment.setTweetId(tw.getTweetId());
            User user = comment.getSender();
            long rowID = saveUser(user);
            comment.setSenderID(rowID);
            DaoManager.getInstance().getSession().getCommentDao().insert(comment);
        }
    }

    /**
     * <br/> 方法名称: saveImages
     * <br/> 方法详述: 保存图片列表
     */
    private void saveImages(Tweet tw) {
        List<Photo> photos = tw.getImages();
        if (photos == null || photos.size() == 0)
            return;
        for (Photo photo : photos) {
            photo.setTweetId(tw.getTweetId());
            DaoManager.getInstance().getSession().getPhotoDao().insert(photo);
        }
    }

    /**
     * <br/> 方法名称: saveUser
     * <br/> 方法详述: 保存用户到本地数据库
     * <br/> 参数: user 需要保存的用户
     */
    private Long saveUser(User user) {
        Long rowID = new Long(0);
        if (user == null)
            return rowID;
        User hasUser = DaoManager.getInstance().getSession().getUserDao()
                .queryBuilder()
                .where(UserDao.Properties.Username.eq(user.getUsername()))
                .build().unique();
        //判断当前用户信息是否已经存在，存在则直接返回rowID，不存在则保存
        if (hasUser != null)
            return hasUser.getId();
        rowID = DaoManager.getInstance().getSession().getUserDao().insert(user);
        return rowID;
    }
}
