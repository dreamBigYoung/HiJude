package com.example.bigyoung.hijude.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bigyoung.hijude.R;
import com.example.bigyoung.hijude.model.bean.User;
import com.example.bigyoung.hijude.utils.ImageViewUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by BigYoung on 2017/5/15.
 */

public class FriendListAdapter extends RecyclerView.Adapter {
    private List<User> mFriendList;
    private Context mContext;
    private ItemLongClickListener mItemLongClickListener;
    private ItemOnClickListener mItemOnClickListener;

    public FriendListAdapter(Context context) {
        mContext = context;
    }

    public FriendListAdapter(Context context, List<User> friendList) {
        this.mFriendList = friendList;
        mContext = context;
    }

    public void setItemOnClickListener(ItemOnClickListener itemOnClickListener) {
        mItemOnClickListener = itemOnClickListener;
    }

    public void setItemLongClickListener(ItemLongClickListener itemLongClickListener) {
        mItemLongClickListener = itemLongClickListener;
    }

    /**
     * update mFriendList
     *
     * @param friendList
     */
    public void setFriendList(List<User> friendList) {
        this.mFriendList = friendList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_friend, parent, false);
        return new MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        User user = mFriendList.get(position);
        myViewHolder.mTvUsername.setText(user.getNickname());
        ImageViewUtils.showImage(mContext, user.getIcon().getUrl(), R.drawable.avatar3, myViewHolder.mIvAvatar);
        myViewHolder.mView.setOnClickListener(getOnClickListener(position));
        myViewHolder.mView.setOnLongClickListener(getOnLongClickListener(position));
    }

    @Override
    public int getItemCount() {
        if (mFriendList != null)
            return mFriendList.size();
        return 0;
    }

    private View.OnClickListener getOnClickListener(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemOnClickListener != null) {
                    mItemOnClickListener.onClickListener(mFriendList.get(position));
                }
            }
        };
    }

    private View.OnLongClickListener getOnLongClickListener(final int position) {
        return new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                if (mItemLongClickListener != null) {
                    mItemLongClickListener.onLongClickListener(mFriendList.get(position));
                }
                return true;//means the event has been consumed
            }
        };
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_avatar)
        ImageView mIvAvatar;
        @BindView(R.id.tv_username)
        TextView mTvUsername;
        View mView;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            mView = view;
        }
    }

    /**
     * 条目长按监听器
     */
    public interface ItemLongClickListener {
        public void onLongClickListener(User user);
    }

    /**
     * 条目点击监听器
     */
    public interface ItemOnClickListener {
        public void onClickListener(User user);
    }
}
