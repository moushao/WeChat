package com.tw.wechat.photo;

import android.view.ViewGroup;
import android.widget.ImageView;

import com.tw.wechat.photo.observer.PhotoAdapterObservable;
import com.tw.wechat.photo.observer.PhotoBaseDataObserver;

import androidx.annotation.NonNull;


/**
 * Created by 大灯泡 on 2016/11/9.
 */

public abstract class PhotoContentsBaseAdapter {


    private PhotoAdapterObservable mObservable = new PhotoAdapterObservable();


    public void registerDataSetObserver(PhotoBaseDataObserver observer) {
        mObservable.registerObserver(observer);

    }

    public void unregisterDataSetObserver(PhotoBaseDataObserver observer) {
        mObservable.unregisterObserver(observer);
    }

    public void notifyDataChanged() {
        mObservable.notifyChanged();
        mObservable.notifyInvalidated();
    }


    public abstract ImageView onCreateView(ImageView convertView, ViewGroup parent, int position);

    public abstract void onBindData(int position, @NonNull ImageView convertView);

    public abstract int getCount();
}
