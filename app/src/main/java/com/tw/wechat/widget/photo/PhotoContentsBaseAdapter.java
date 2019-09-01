package com.tw.wechat.widget.photo;

import android.view.ViewGroup;
import android.widget.ImageView;

import com.tw.wechat.widget.photo.observer.PhotoAdapterObservable;
import com.tw.wechat.widget.photo.observer.PhotoBaseDataObserver;

import androidx.annotation.NonNull;


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
