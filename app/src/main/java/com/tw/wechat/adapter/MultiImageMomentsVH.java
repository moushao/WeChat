package com.tw.wechat.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.tw.wechat.R;
import com.tw.wechat.entity.Photo;
import com.tw.wechat.entity.Tweet;
import com.tw.wechat.widget.photo.PhotoContents;
import com.tw.wechat.widget.photo.PhotoContentsBaseAdapter;
import com.tw.wechat.widget.photo.PhotoWidget;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;


/**
 * 类名: {@link MultiImageMomentsVH}
 * <br/> 功能描述:九宮格圖片的vh
 */
public class MultiImageMomentsVH extends CircleBaseViewHolder implements PhotoContents.OnItemClickListener {

    private PhotoContents imageContainer;
    private InnerContainerAdapter adapter;

    public MultiImageMomentsVH(Context context, ViewGroup viewGroup, int layoutResId) {
        super(context, viewGroup, layoutResId);
    }

    @Override
    public void onFindView(@NonNull View rootView) {
        imageContainer = (PhotoContents) findView(imageContainer, R.id.circle_image_container);
        if (imageContainer.getmOnItemClickListener() == null) {
            imageContainer.setmOnItemClickListener(this);
        }
    }

    @Override
    public void onBindDataToView(@NonNull Tweet data, int position, int viewType) {
        if (adapter == null) {
            adapter = new InnerContainerAdapter(getContext(), data.getImages());
            imageContainer.setAdapter(adapter);
        } else {
            adapter.updateData(data.getImages());
        }
    }


    @Override
    public void onItemClick(ImageView imageView, int position) {
        mEventListener.preViewPicture(mTweet.getImages(), position);
    }


    private static class InnerContainerAdapter extends PhotoContentsBaseAdapter {

        private RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.pic_failure)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.pic_failure)
                .priority(Priority.HIGH);
        private Context context;
        private List<Photo> datas;

        InnerContainerAdapter(Context context, List<Photo> datas) {
            this.context = context;
            this.datas = new ArrayList<>();
            this.datas.addAll(datas);
        }

        @Override
        public ImageView onCreateView(ImageView convertView, ViewGroup parent, int position) {
            if (convertView == null) {
                convertView = new PhotoWidget(context);
                convertView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
            return convertView;
        }

        @Override
        public void onBindData(int position, @NonNull ImageView convertView) {
            Glide.with(context).load(datas.get(position).getUrl()).apply(options).into(convertView);

        }

        @Override
        public int getCount() {
            return datas.size();
        }

        public void updateData(List<Photo> datas) {
            this.datas.clear();
            this.datas.addAll(datas);
            notifyDataChanged();
        }

    }
}
