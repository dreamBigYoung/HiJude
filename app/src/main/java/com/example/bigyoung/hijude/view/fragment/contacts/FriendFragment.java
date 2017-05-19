package com.example.bigyoung.hijude.view.fragment.contacts;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;


import com.example.bigyoung.hijude.adapter.FriendListAdapter;
import com.example.bigyoung.hijude.base.BaseContactsFragment;
import com.example.bigyoung.hijude.base.MyApplication;
import com.example.bigyoung.hijude.model.bean.ContactNumUpdateBean;
import com.example.bigyoung.hijude.model.bean.FriendEventBean;
import com.example.bigyoung.hijude.model.bean.User;
import com.example.bigyoung.hijude.presenter.fragment.FriendFragPresenter;
import com.example.bigyoung.hijude.presenter.fragment.impl.FriendFragPresenterImpl;
import com.example.bigyoung.hijude.utils.DataCacheUtils;
import com.example.bigyoung.hijude.utils.MyToast;
import com.example.bigyoung.hijude.view.activity.TalkingActivity;
import com.example.bigyoung.hijude.view.fragment.ContactFragment;
import com.example.bigyoung.hijude.wrap.SimpleEMCallBack;
import com.hyphenate.chat.EMClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Apple on 2016/12/7.
 */

public class FriendFragment extends BaseContactsFragment {


    private AfterAlloNewFriendListener mAfterAlloNewFriendListener;
    private FriendListAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注册事件
        EventBus.getDefault().register(FriendFragment.this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //解除注册
        EventBus.getDefault().unregister(FriendFragment.this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void resultFriendEvent(FriendEventBean bean) {
        FriendFragPresenter fragPresenter = new FriendFragPresenterImpl(new FriendFragPresenterImpl.AddNewFriendListener() {
            @Override
            public void afterAddNewFriend() {
                refreshList();
            }

            @Override
            public void afterDeleteNewFriend() {
                refreshList();
            }
        });
        switch (bean.getEventType()) {
            case FriendEventBean.ADD:
                fragPresenter.resultAddFriend(bean.getUsername());
                break;
            case FriendEventBean.DELETE:
                fragPresenter.resultDeleteFriend(bean.getUsername());
                break;
        }

    }

    /**
     * 刷新列表显示及title
     */
    private void refreshList() {
        if (mAdapter != null) {
            mAdapter.setFriendList(DataCacheUtils.getFriendList());
            mAdapter.notifyDataSetChanged();
            //通知标题改变
            EventBus.getDefault().post(new ContactNumUpdateBean(ContactFragment.getFragPosition(FriendFragment.class)));
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new FriendListAdapter(getContext(), DataCacheUtils.getFriendList());
        mAdapter.setItemLongClickListener(new FriendListAdapter.ItemLongClickListener() {
            @Override
            public void onLongClickListener(User user) {
                final User myUser=user;
                new AlertDialog.Builder(getContext())
                        .setTitle("删除")
                        .setMessage("确定删除"+user.getNickname()+"吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //向服务器端发送请求删除好友
                                EMClient.getInstance().contactManager().aysncDeleteContact(myUser.getUsername(),new SimpleEMCallBack());
                            }
                        })
                        .setNegativeButton("取消",null)
                        .show();
            }
        });
        mAdapter.setItemOnClickListener(new FriendListAdapter.ItemOnClickListener() {
            @Override
            public void onClickListener(User user) {
                MyToast.show(getContext(),"waht the fuck");
                TalkingActivity.startActivity(getContext());
            }
        });
        setAdapter(mAdapter);
    }

    /**
     * 允许新好友加入监听器
     */
    public interface AfterAlloNewFriendListener {
        public void allowNewAddedListener();
    }

    public void setAfterAlloNewFriendListener(AfterAlloNewFriendListener alloNewFriendListener) {
        mAfterAlloNewFriendListener = alloNewFriendListener;
    }
}
