package com.tw.wechat.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.tw.wechat.entity.Tweet;
import com.tw.wechat.event.ViewListener;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;


/**
 * Created by 大灯泡 on 2016/11/1.
 * <p>
 * 朋友圈adapter
 */

public class CircleMomentsAdapter extends BaseRecyclerViewAdapter<Tweet> {


    private SparseArray<ViewHoldernfo> viewHolderKeyArray;
    private ViewListener mListener;


    private CircleMomentsAdapter(@NonNull Context context,
                                 @NonNull List<Tweet> datas) {
        super(context, datas);
    }

    private CircleMomentsAdapter(Builder builder) {
        this(builder.context, builder.datas);
        this.viewHolderKeyArray = builder.viewHolderKeyArray;
        this.mListener = builder.mListener;
    }

    @Override
    protected int getViewType(int position, @NonNull Tweet data) {
        return 2;
    }

    @Override
    protected int getLayoutResId(int viewType) {
        return 0;
    }

    @Override
    protected CircleBaseViewHolder getViewHolder(ViewGroup parent, View rootView, int viewType) {
        ViewHoldernfo viewHoldernfo = viewHolderKeyArray.get(viewType);
        if (viewHoldernfo != null) {
            CircleBaseViewHolder circleBaseViewHolder = createCircleViewHolder(context, parent, viewHoldernfo);
            if (circleBaseViewHolder != null) {
                circleBaseViewHolder.setEventListener(mListener);
            }
            return circleBaseViewHolder;
        }
        return null;
    }


    public static final class Builder<T> {
        private Context context;
        private SparseArray<ViewHoldernfo> viewHolderKeyArray = new SparseArray<>();
        private List<T> datas;
        private ViewListener mListener;


        public Builder(Context context) {
            this.context = context;
            datas = new ArrayList<>();
        }

        public Builder<T> addType(Class<? extends CircleBaseViewHolder> viewHolderClass,
                                  int viewType,
                                  int layoutResId) {
            final ViewHoldernfo info = new ViewHoldernfo();
            info.holderClass = viewHolderClass;
            info.viewType = viewType;
            info.layoutResID = layoutResId;
            viewHolderKeyArray.put(viewType, info);
            return this;
        }

        public Builder<T> setListener(ViewListener listener) {
            mListener = listener;
            return this;
        }

        public Builder<T> setData(List<T> datas) {
            this.datas = datas;
            return this;
        }

        public CircleMomentsAdapter build() {
            return new CircleMomentsAdapter(this);
        }

    }


    /**
     * vh的信息类
     */
    private static final class ViewHoldernfo {
        Class<? extends CircleBaseViewHolder> holderClass;
        int viewType;
        int layoutResID;
    }

    private CircleBaseViewHolder createCircleViewHolder(Context context, ViewGroup viewGroup, ViewHoldernfo
            viewHoldernfo) {
        if (viewHoldernfo == null) {
            throw new NullPointerException("木有这个viewholder信息哦");
        }
        Class<? extends CircleBaseViewHolder> className = viewHoldernfo.holderClass;
        Constructor constructor = null;
        try {
            constructor = className.getConstructor(Context.class, ViewGroup.class, int.class);
            return (CircleBaseViewHolder) constructor.newInstance(context, viewGroup, viewHoldernfo.layoutResID);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException
                | NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isListEmpty(List<?> datas) {
        return datas == null || datas.size() <= 0;
    }

}
