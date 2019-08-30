package com.tw.wechat.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tw.wechat.R;
import com.tw.wechat.event.OnRecyclerViewItemClickListener;
import com.tw.wechat.event.OnRecyclerViewLongItemClickListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by 大灯泡 on 2016/7/20.
 * <p>
 * 抽象的adapter
 */

public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseRecyclerViewHolder<T>> {

    private static final String TAG = "BaseRecyclerViewAdapter";
    protected Context context;
    protected List<T> mData;
    protected LayoutInflater mInflater;

    private OnRecyclerViewItemClickListener<T> onRecyclerViewItemClickListener;
    private OnRecyclerViewLongItemClickListener<T> onRecyclerViewLongItemClickListener;

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
        if (onRecyclerViewItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //这个获取位置的方法，防止添加删除导致位置不变
                    int layoutPosition = holder.getAdapterPosition();
                    onRecyclerViewItemClickListener.onItemClick(holder.itemView, layoutPosition, mData.get(layoutPosition));
                }
            });
        }
        if (onRecyclerViewLongItemClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int layoutPosition = holder.getAdapterPosition();
                    onRecyclerViewLongItemClickListener.onItemLongClick(holder.itemView, layoutPosition, mData.get(layoutPosition));
                    return false;
                }
            });
        }
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

    public void addData(int pos, @NonNull T data) {
        if (mData != null) {
            mData.add(pos, data);
            notifyItemInserted(pos);
        }
    }

    public void addData(@NonNull T data) {
        if (mData != null) {
            mData.add(data);
            notifyItemInserted(mData.size() - 1);
        }
    }

    public void deleteData(int pos) {
        if (mData != null && mData.size() > pos) {
            mData.remove(pos);
            notifyItemRemoved(pos);
        }
    }

    public T findData(int pos) {
        if (pos < 0 || pos > mData.size()) {
            return null;
        }
        return mData.get(pos);
    }

    protected abstract int getViewType(int position, @NonNull T data);

    protected abstract int getLayoutResId(int viewType);

    protected abstract BaseRecyclerViewHolder getViewHolder(ViewGroup parent, View rootView, int viewType);

    protected void onBindData(BaseRecyclerViewHolder<T> holder, T data, int position) {

    }

    public OnRecyclerViewItemClickListener<T> getOnRecyclerViewItemClickListener() {
        return onRecyclerViewItemClickListener;
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener<T> onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    public OnRecyclerViewLongItemClickListener<T> getOnRecyclerViewLongItemClickListener() {
        return onRecyclerViewLongItemClickListener;
    }

    public void setOnRecyclerViewLongItemClickListener(OnRecyclerViewLongItemClickListener<T> onRecyclerViewLongItemClickListener) {
        this.onRecyclerViewLongItemClickListener = onRecyclerViewLongItemClickListener;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
