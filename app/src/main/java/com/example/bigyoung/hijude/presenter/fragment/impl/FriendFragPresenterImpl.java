package com.example.bigyoung.hijude.presenter.fragment.impl;

import com.example.bigyoung.hijude.base.MyApplication;
import com.example.bigyoung.hijude.model.bean.User;
import com.example.bigyoung.hijude.presenter.fragment.FriendFragPresenter;
import com.example.bigyoung.hijude.utils.DataCacheUtils;
import com.example.bigyoung.hijude.wrap.SimpleEMCallBack;
import com.hyphenate.chat.EMClient;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by BigYoung on 2017/5/15.
 */

public class FriendFragPresenterImpl implements FriendFragPresenter {
    public AddNewFriendListener mAddNewFriendListener;

    public FriendFragPresenterImpl() {
    }

    public FriendFragPresenterImpl(AddNewFriendListener addNewFriendListener) {
        mAddNewFriendListener = addNewFriendListener;
    }

    @Override
    public void resultAddFriend(final String username) {
        BmobQuery<User> bmobQuery = new BmobQuery<User>();
        bmobQuery.addWhereEqualTo("username", username);
        bmobQuery.findObjects(new FindListener<User>() {
            User user;
            @Override
            public void done(List<User> list, BmobException e) {
                if(list!=null&&list.size()!=0&&e==null){
                    user=list.get(0);
                }else{
                    user=new User();
                    user.setUsername(username);
                }
                //添加查询结果
                DataCacheUtils.addUser(user);
                if(mAddNewFriendListener!=null){
                    mAddNewFriendListener.afterAddNewFriend();
                }
            }
        });
    }

    @Override
    public void resultDeleteFriend(String username) {
        //本地删除好友
        DataCacheUtils.deleteUser(username);
        //
        if(mAddNewFriendListener!=null){
            mAddNewFriendListener.afterDeleteNewFriend();
        }
    }

    /**
     * add new friend listener
     */
    public interface AddNewFriendListener{
        //the operation after add new friend
        public void afterAddNewFriend();
        //the operation after delete friend
        public void afterDeleteNewFriend();
    }
    public void setAddNewFriendListener(AddNewFriendListener listener){
        mAddNewFriendListener=listener;
    }
}
