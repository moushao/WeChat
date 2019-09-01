package com.tw.wechat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tw.wechat.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


/**
 * 抽象的adapter
 */

public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseRecyclerViewHolder<T>> {
    protected Context context;
    protected List<T> mData;
    protected LayoutInflater mInflater;


    public BaseRecyclerViewAdapter(@NonNull Context context, @NonNull List<T> datas) {
        this.context = context;
        this.mData = datas;
        if (datas != null) {
            this.mData = new ArrayList<>(datas);
        } else {
            this.mData = new ArrayList<>();
        }
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        return getViewType(position, mData.get(position));
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseRecyclerViewHolder holder = null;
        if (getLayoutResId(viewType) != 0) {
            View rootView = mInflater.inflate(getLayoutResId(viewType), parent, false);
            holder = getViewHolder(parent, rootView, viewType);
        } else {
            holder = getViewHolder(parent, null, viewType);
        }
        //点击事件实现
        setUpItemEvent(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        T data = mData.get(position);
        holder.itemView.setTag(R.id.recycler_view_tag, data);
        holder.onBindData(data, position);
        onBindData(holder, data, position);
    }

    private void setUpItemEvent(final BaseRecyclerViewHolder holder) {
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public void updateData(List<T> datas) {
        if (this.mData != null) {
            this.mData.clear();
            this.mData.addAll(datas);
        } else {
            this.mData = datas;
        }
        notifyDataSetChanged();
    }

    public void addMore(List<T> datas) {
        if (datas != null && datas.size() > 0) {
            this.mData.addAll(datas);
            notifyDataSetChanged();
        }
    }

    public List<T> getData() {
        return mData;
    }

    protected abstract int getViewType(int position, @NonNull T data);

    protected abstract int getLayoutResId(int viewType);

    protected abstract BaseRecyclerViewHolder getViewHolder(ViewGroup parent, View rootView, int viewType);

    protected void onBindData(BaseRecyclerViewHolder<T> holder, T data, int position) {

    }


    @Override
    public long getItemId(int position) {
        return position;
    }
}
