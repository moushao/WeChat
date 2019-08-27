package com.tw.wechat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.tw.wechat.R;
import com.tw.wechat.entity.User;
import com.tw.wechat.event.ItemListener;


public class HostViewHolder {
    private View rootView;
    private ImageView friend_wall_pic;
    private ImageView friend_avatar;
    private ImageView message_avatar;
    private TextView message_detail;
    private TextView hostid;
    private ItemListener mItemListener;
    private RequestOptions options = new RequestOptions()
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .priority(Priority.HIGH);
    private Context mContext;

    public HostViewHolder(Context context, ItemListener listener) {
        this.mItemListener = listener;
        this.mContext = context;
        this.rootView = LayoutInflater.from(context).inflate(R.layout.circle_host_header, null);
        this.hostid = (TextView) rootView.findViewById(R.id.host_id);
        this.friend_wall_pic = (ImageView) rootView.findViewById(R.id.friend_wall_pic);
        this.friend_avatar = (ImageView) rootView.findViewById(R.id.friend_avatar);
        this.message_avatar = (ImageView) rootView.findViewById(R.id.message_avatar);
        this.message_detail = (TextView) rootView.findViewById(R.id.message_detail);
        friend_wall_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemListener != null)
                    mItemListener.onItemClick(null, 0, null);
            }
        });
    }

    public void loadHostData(User hostInfo) {
        if (hostInfo == null)
            return;

        Glide.with(mContext).load("https://upload.jianshu.io/users/upload_avatars/927828/1cb87269-4e94-4369-ac57-e2beedf4975a.png?imageMogr2/auto-orient/strip|imageView2/1/w/80/h/80")
                .apply(options.placeholder(R.drawable.login_head)
                        .error(R.drawable.login_head))
                .into(friend_avatar);

        Glide.with(mContext).load(hostInfo.getProfile())
                .apply(options
                        .placeholder(R.drawable.login_head)
                        .error(R.drawable.login_head))
                .into(friend_wall_pic);

        hostid.setText(hostInfo.getNick());
    }

    public View getView() {
        return rootView;
    }

}