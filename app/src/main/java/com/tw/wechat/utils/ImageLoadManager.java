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
    RoundedCorners mCorners = new RoundedCorners(30);
    private RequestOptions options = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .priority(Priority.HIGH)
            .transform(mCorners);

    public void clearMemory(Context context) {
        Glide.get(context).clearMemory();
    }

    private RequestBuilder<Drawable> loadTransform(Context context, @DrawableRes int placeholderId, int radius) {
        return Glide.with(context)
                .load(placeholderId)
                .apply(new RequestOptions().centerCrop()
                        .transform(new RoundedCorners(radius)));

    }

    public void loadImage(final ImageView imageView, String imgUrl, int holderDrawable, int thumbDrawable) {
        try {
            options.error(thumbDrawable)
                    .placeholder(holderDrawable);
            Glide.with(getImageContext(imageView))
                    .load(imgUrl)
                    .apply(options)
                    //.thumbnail(loadTransform(imageView.getContext(), thumbDrawable, 0))
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadImageWithRadius(final ImageView imageView, String imgUrl, int thumbDrawable, int radius) {
        try {
            Glide.with(getImageContext(imageView))
                    .load(imgUrl)
                    .apply(new RequestOptions().transform(new RoundedCorners(radius)))
                    .thumbnail(loadTransform(imageView.getContext(), thumbDrawable, radius))
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
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


    private Context getImageContext(@Nullable ImageView imageView) {
        return imageView.getContext();
    }
}
