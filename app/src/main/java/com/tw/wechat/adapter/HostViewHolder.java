package com.tw.wechat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tw.wechat.R;
import com.tw.wechat.entity.User;
import com.tw.wechat.utils.ImageLoadManager;
import com.tw.wechat.utils.ToastUtils;

/**
 * 类名: {@link HostViewHolder}
 * <br/> 功能描述:照片墙的holder
 * <br/> 作者: MouShao
 * <br/> 时间: 2019/8/30
 */
public class HostViewHolder {
    private View rootView;
    private ImageView friend_wall_pic;
    private ImageView friend_avatar;
    private TextView hostId;
    private Context mContext;

    public HostViewHolder(Context context) {
        this.mContext = context;
        this.rootView = LayoutInflater.from(context).inflate(R.layout.circle_host_header, null);
        this.hostId = (TextView) rootView.findViewById(R.id.host_id);
        this.friend_wall_pic = (ImageView) rootView.findViewById(R.id.friend_wall_pic);
        this.friend_avatar = (ImageView) rootView.findViewById(R.id.friend_avatar);
        friend_wall_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.showToast(mContext, "照片墙被点击了");
            }
        });
    }

    /**
     * 绑定用户信息到照片墙
     *
     * @param user
     */
    public void loadHostData(User user) {
        if (user == null)
            return;
        ImageLoadManager.INSTANCE.loadImageWithRadius(friend_avatar, user.getAvatar(), R.drawable.pic_default_head, 20);
        ImageLoadManager.INSTANCE.loadImage(friend_wall_pic, user.getProfileImage(), R.mipmap.ic_launcher, R.drawable.pic_default_wall_pic);
        hostId.setText(user.getNick());
    }

    public View getView() {
        return rootView;
    }

}