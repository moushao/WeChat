package com.tw.wechat.adapter;

import android.content.Context;
import android.view.View;

import com.tw.wechat.event.ItemListener;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Moushao on 2017/8/23.
 */

public class VBaseHolder<T> extends RecyclerView.ViewHolder {
    public ItemListener mListener;
    public Context mContext;
    public View mView;
    public T mData;
    public int position;
    public int mCount;


    public VBaseHolder(View itemView) {
        super(itemView);
        mView = itemView;
        init();
    }

    public void init() {
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null)
                    mListener.onItemClick(view, position, mData);
            }
        });
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public void setListener(ItemListener listener) {
        mListener = listener;
    }

    public void setData(int position, T mData) {
        this.mData = mData;
        this.position = position;
    }

    public void setCount(int count) {
        this.mCount = count;
    }

}