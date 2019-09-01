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
 * 类名: {@link ImageLoadManager}
 * <br/> 功能描述:图片加载辅助类
 * <br/> 作者: MouShao
 * <br/> 时间: 2019/8/30
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
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadImageWithRadius(final ImageView imageView, String imgUrl, int thumbDrawable, int radius) {
        try {
            Glide.with(getImageContext(imageView))
                    .load(imgUrl)
                    .apply(new RequestOptions()).transform(new GlideRoundTransform(getImageContext(imageView), radius))
                    .thumbnail(loadTransform(imageView.getContext(), thumbDrawable, radius))
                    .into(imageView);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private Context getImageContext(@Nullable ImageView imageView) {
        return imageView.getContext();
    }
}
