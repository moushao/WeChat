package com.tw.wechat.model;


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
import io.reactivex.Observable;

public class MainModel {
    private static MainModel INSTANCE;

    public static MainModel getInstance() {
        if (INSTANCE == null) {
            synchronized (MainModel.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MainModel();
                }
            }
        }
        return INSTANCE;
    }

    /**
     *
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
                            getUserFromServer(callBack);
                        } else {
                            callBack.getUserSuccess(user);
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtil.e(throwable.getLocalizedMessage());
                    }
                });
    }

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
                        saveUser(user, 1);
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

    public void getTweets(final int offset, final CallBack callBack) {
        Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(ObservableEmitter emitter) throws Exception {

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
                    }
                });
    }

    private List<Tweet> loadFullTweets(List<Tweet> tweets) {
        for (Tweet tw : tweets) {
            Long senderID = tw.getSenderID();
            User user = DaoManager.getInstance().getSession()
                    .getUserDao().queryBuilder()
                    //.where(UserDao.Properties.Type.eq(2))
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
                        //.where(UserDao.Properties.Type.eq(3))
                        .where(UserDao.Properties.Id.eq(comment.getSenderID())).build().unique();
                comment.setSender(commentUser);
            }
            tw.setSender(user);
            tw.setImages(images);
            tw.setComments(comments);
        }
        return tweets;
    }

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
                    }
                });
    }

    private List<Tweet> praiseAndSaveTweets(String result) throws IOException {
        List<Tweet> tweets = new ArrayList<>();
        if (TextUtils.isEmpty(result))
            return tweets;
        Type type = new TypeToken<List<Tweet>>() {
        }.getType();
        ArrayList<Tweet> fromJson2 = new Gson().fromJson(result, type);
        for (int i = 0; i < fromJson2.size(); i++) {
            Tweet tw = fromJson2.get(i);
            if (tw.isQualified()/* && tweets.size() <2*/) {
                tw.prePosition = i;
                //tw.setComments(new ArrayList<Comment>());
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
     * //一条Tweet包含以下属性
     * //      原始原json位置;内容content,图片列表images,发布人sender,评论列表comments
     * //      评论列表包含评论content
     * //保存数据库:
     * //    1:ToOne   先保存注解对象,再保存主对象
     * //    3:ToMany  先保存主对象,再保存注解对象
     * //综上所述:保存一条含有ToOne和ToMany的主对象,应该先保存ToOne注解对象,再保存主对象,最后保存ToMany注解对象
     */
    private void saveTweet(Tweet tw) {
        long senderID = saveUser(tw.getSender(), 2);
        tw.setSenderID(senderID);
        saveImages(tw);
        saveComments(tw);
        DaoManager.getInstance().getSession().insert(tw);
    }

    private void saveComments(Tweet tw) {
        List<Comment> comments = tw.getComments();
        if (comments == null || comments.size() == 0)
            return;
        for (int i = 0; i < comments.size(); i++) {
            Comment comment = comments.get(i);
            comment.setTweetId(tw.getTweetId());
            User user = comment.getSender();
            long rowID = saveUser(user, 3);
            comment.setSenderID(rowID);
            DaoManager.getInstance().getSession().getCommentDao().insert(comment);
        }
    }

    private void saveImages(Tweet tw) {
        List<Photo> photos = tw.getImages();
        if (photos == null || photos.size() == 0)
            return;
        for (Photo photo : photos) {
            photo.setTweetId(tw.getTweetId());
            DaoManager.getInstance().getSession().getPhotoDao().insert(photo);
        }
    }

    private Long saveUser(User user, int userType) {
        Long rowID = new Long(0);
        if (user == null)
            return rowID;
        User hasUser = DaoManager.getInstance().getSession().getUserDao()
                .queryBuilder()
                .where(UserDao.Properties.Username.eq(user.getUsername()))
                .build().unique();
        if (hasUser != null)
            return hasUser.getId();
        rowID = DaoManager.getInstance().getSession().getUserDao().insert(user);
        LogUtil.e("rowID", rowID + "");
        return rowID;
    }
}
