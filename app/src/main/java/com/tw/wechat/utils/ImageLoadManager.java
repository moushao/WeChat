package com.tw.wechat.utils;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.tw.wechat.R;

import androidx.annotation.Nullable;


/**
 * Created by 大灯泡 on 2016/11/1.
 * <p>
 * 图片加载
 */

public enum ImageLoadManager {
    INSTANCE;
    private Context mContext;
    private RequestOptions options = new RequestOptions()
            .centerCrop()
            .placeholder(R.mipmap.ic_launcher)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .error(R.mipmap.ic_launcher)
            .priority(Priority.HIGH);
    private RequestOptions optionsHead = new RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.login_head)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .error(R.drawable.login_head)
            .priority(Priority.HIGH);

    public void clearMemory(Context context) {
        Glide.get(context).clearMemory();
    }
    


    public void loadImage(ImageView imageView, String imgUrl) {
        try {
            Glide.with(getImageContext(imageView)).load(imgUrl).apply(options).into(imageView);
        } catch (Exception e) {

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
