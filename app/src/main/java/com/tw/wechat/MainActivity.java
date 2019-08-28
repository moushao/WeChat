package com.tw.wechat;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.tw.wechat.adapter.CircleMomentsAdapter;
import com.tw.wechat.adapter.HostViewHolder;
import com.tw.wechat.adapter.MultiImageMomentsVH;
import com.tw.wechat.entity.Tweet;
import com.tw.wechat.entity.User;
import com.tw.wechat.event.CallBack;
import com.tw.wechat.event.ItemListener;
import com.tw.wechat.model.MainModel;
import com.tw.wechat.utils.ToastUtils;
import com.tw.wechat.widget.pullryc.CircleRecyclerView;
import com.tw.wechat.event.OnRefreshListener2;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements
        CircleRecyclerView.OnPreDispatchTouchListener, CallBack {
    /*  @BindView(R.id.recycler)*/ CircleRecyclerView circleRecyclerView;
    private HostViewHolder hostViewHolder;
    private Context mContext;
    private CircleMomentsAdapter adapter;
    private boolean isLoadMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        circleRecyclerView = findViewById(R.id.recycler);
        mContext = this;
        initWidget();
        MainModel mainModel = new MainModel();
        mainModel.getUser(this);
        mainModel.getTweets(this);
    }

    private void initWidget() {
        hostViewHolder = new HostViewHolder(this, new ItemListener() {
            @Override
            public void onItemClick(View view, int position, Object mData) {
                ToastUtils.showToast(mContext, "头像部分被点击了");
            }
        });

        circleRecyclerView.setOnRefreshListener(refreshListener);
        circleRecyclerView.setOnPreDispatchTouchListener(this);
        circleRecyclerView.addHeaderView(hostViewHolder.getView());
        //hostViewHolder.loadHostData(UserHelper.getInstance().getLogUser());

        //commentBox = (CommentBox) findViewById(R.id.widget_comment);
        //commentBox.setOnCommentSendClickListener(onCommentSendClickListener);

        CircleMomentsAdapter.Builder<Tweet> builder = new CircleMomentsAdapter.Builder<>(this);
        builder//.addType(EmptyMomentsVH.class, 0, R.layout.moments_empty_content)
                //        .addType(TextOnlyMomentsVH.class, 1, R.layout.moments_only_text)
                .addType(MultiImageMomentsVH.class, 2, R.layout.moments_multi_image);
        //.addType(CirccleVideoMomentsVH.class, 3, R.layout.moments_video_image)
        //.addType(WebMomentsVH.class, 4, R.layout.moments_web) 
        //.setData(momentsInfoList)
        //.setPresenter((CirclePresenter) mPresenter) ;

        adapter = builder.build();

        circleRecyclerView.setAdapter(adapter);
        //circleRecyclerView.autoRefresh();
        //initKeyboardHeightObserver();
    }

    private OnRefreshListener2 refreshListener =
            new OnRefreshListener2() {
                @Override
                public void onRefresh() {
                    isLoadMore = false;
                    ToastUtils.showToast(mContext, "onRefresh");
                    circleRecyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.showToast(mContext, "postDelayed");
                            circleRecyclerView.complete();
                        }
                    }, 5000);
                }

                @Override
                public void onLoadMore() {
                    isLoadMore = true;
                    ToastUtils.showToast(mContext, "onLoadMore");
                    circleRecyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            circleRecyclerView.complete();
                        }
                    }, 5000);
                }
            };

    @Override
    public boolean onPreTouch(MotionEvent ev) {
        return false;
    }

    @Override
    public void getUserSuccess(User user) {
        hostViewHolder.loadHostData(user);
        circleRecyclerView.complete();
    }

    @Override
    public void getTweetsSuccess(List<Tweet> tweets) {
        circleRecyclerView.complete();
        if (isLoadMore) {
            adapter.addMore(tweets);
        } else {
            adapter.updateData(tweets);
        }
    }

    @Override
    public void failed(String message) {
        circleRecyclerView.complete();
    }
}
