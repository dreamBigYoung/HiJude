package com.example.bigyoung.hijude.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bigyoung.hijude.R;
import com.example.bigyoung.hijude.model.bean.User;
import com.example.bigyoung.hijude.utils.ImageViewUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by BigYoung on 2017/5/14.
 */

public class SearchFroAddFriendAdapter extends RecyclerView.Adapter {
    private final Context mContext;
    private List<User> mUserList;
    private AddFriendButtonClickListener mAddFriendButtonClickListener;

    public SearchFroAddFriendAdapter(Context context) {
        this(context, null);
    }

    public SearchFroAddFriendAdapter(Context context, List<User> userList) {
        mContext = context;
        mUserList = userList;
    }

    public void setAddFriendButtonClickListener(AddFriendButtonClickListener addFriendButtonClickListener) {
        mAddFriendButtonClickListener = addFriendButtonClickListener;
        //更新item，click事件需要对应的position
        notifyDataSetChanged();
    }

    /**
     * 更新userList
     *
     * @param userList
     */
    public void updateUserList(List<User> userList) {
        mUserList = userList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_search_friend, parent, false);
        MyViewHolder holder = new MyViewHolder(inflate);
        holder.mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAddFriendButtonClickListener != null) {
                    User user = mUserList.get((int) v.getTag());
                    mAddFriendButtonClickListener.onClickListener(user);
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        User user = mUserList.get(position);
        myViewHolder.mTvTime.setText(user.getCreatedAt());
        myViewHolder.mTvUsername.setText(user.getNickname());
        //响应事件后可获取数据
        myViewHolder.mBtnAdd.setTag(position);
        ImageViewUtils.showImage(mContext, user.getIcon().getUrl(), R.drawable.avatar3, myViewHolder.mIvAvatar);
    }

    @Override
    public int getItemCount() {
        if (mUserList != null)
            return mUserList.size();
        return 0;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_avatar)
        ImageView mIvAvatar;
        @BindView(R.id.tv_username)
        TextView mTvUsername;
        @BindView(R.id.tv_time)
        TextView mTvTime;
        @BindView(R.id.btn_add)
        Button mBtnAdd;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    //添加好友点击监听器
    public interface AddFriendButtonClickListener {
        public void onClickListener(User user);
    }
}
