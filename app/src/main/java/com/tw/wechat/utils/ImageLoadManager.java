package com.tw.wechat.utils;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.tw.wechat.R;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;


/**
 * Created by 大灯泡 on 2016/11/1.
 * <p>
 * 图片加载
 */

public enum ImageLoadManager {
    INSTANCE;
    private Context mContext;
    RoundedCorners mCorners = new RoundedCorners(30);
    private RequestOptions options = new RequestOptions()
            .placeholder(R.drawable.login_head)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .error(R.mipmap.ic_launcher)
            .priority(Priority.HIGH)
            .transform(mCorners);
    private RequestOptions optionsHead = new RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.login_head)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .error(R.drawable.login_head)
            .priority(Priority.HIGH);

    public void clearMemory(Context context) {
        Glide.get(context).clearMemory();
    }

    private RequestBuilder<Drawable> loadTransform(Context context, @DrawableRes int placeholderId, int radius) {
        return Glide.with(context)
                .load(placeholderId)
                .apply(new RequestOptions().centerCrop()
                        .transform(new RoundedCorners(radius)));

    }

    public void loadImage(final ImageView imageView, String imgUrl) {
        int radius = 20;
        try {
            imgUrl = "https://upload.jianshu.io/users/upload_avatars/927828/1cb87269-4e94-4369-ac57-e2beedf4975a" +
                    ".png?imageMogr2/auto-orient/strip|imageView2/1/w/80/h/80";
            Glide.with(getImageContext(imageView))
                    .load(imgUrl)
                    .apply(new RequestOptions().transform(new RoundedCorners(radius)))
                    .thumbnail(loadTransform(imageView.getContext(), R.mipmap.ic_launcher, radius))
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadImageErrorHead(ImageView imageView, String imgUrl) {
        try {
            Glide.with(getImageContext(imageView)).load(imgUrl).apply(optionsHead).into(imageView);
        } catch (Exception e) {

        }
    }

    public void loadImage(Context context, ImageView imageView, String imgUrl) {
        try {
            Glide.with(context).load(imgUrl).apply(options).into(imageView);
        } catch (Exception e) {

        }
    }

    public void loadImage(ImageView imageView, int placeHolderColor, String imgUrl) {
        Glide.with(getImageContext(imageView)).load(imgUrl).apply(options).into(imageView);

    }

    public void loadImageDontAnimate(ImageView imageView, String imgUrl) {
        Glide.with(getImageContext(imageView)).load(imgUrl).apply(options).into(imageView);

    }

    public void loadImage(ImageView imageView, String imgUrl, int width, int height) {
        options.override(width, height);
    }


    private Context getImageContext(@Nullable ImageView imageView) {
        return imageView.getContext();
    }
}
