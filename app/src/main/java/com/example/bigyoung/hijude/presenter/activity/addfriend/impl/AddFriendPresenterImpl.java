package com.example.bigyoung.hijude.presenter.activity.addfriend.impl;

import com.example.bigyoung.hijude.model.bean.User;
import com.example.bigyoung.hijude.presenter.activity.addfriend.AddFriendPresenter;
import com.example.bigyoung.hijude.utils.DataCacheUtils;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by BigYoung on 2017/5/14.
 */

public class AddFriendPresenterImpl implements AddFriendPresenter {
    private AfterFindListener mAfterFindListener;
    private final String mUsername = "username";
    ;
    private List<String> mFriendNameList;

    public AddFriendPresenterImpl(AfterFindListener afterFindListener) {
        mAfterFindListener = afterFindListener;
    }

    public void setAfterFindListener(AfterFindListener afterFindListener) {
        mAfterFindListener = afterFindListener;
    }

    @Override
    public void findFriend(String keyWord) {
        BmobQuery<User> bmobQuery = new BmobQuery<User>();
        //按照关键字搜索
        bmobQuery.addWhereContains(mUsername, keyWord);
        //去除已经是好友的成员
        mFriendNameList = DataCacheUtils.getFriendsNameList();
        if (mFriendNameList != null)
            bmobQuery.addWhereNotContainedIn(mUsername, mFriendNameList);
        //去除自身
        bmobQuery.addWhereNotEqualTo(mUsername, DataCacheUtils.getmCurUser().getUsername());
        //按照名字排序
        bmobQuery.order(mUsername);

        bmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                User user;
                if (list != null && list.size() != 0 && e == null) {
                    //获取到了数据
                    if (mAfterFindListener != null) {
                        mAfterFindListener.findSuccess(list);
                    }

                } else {
                    //获取失败
                    if (mAfterFindListener != null) {
                        mAfterFindListener.findFailed();
                    }
                }
            }
        });
    }

    //findFriend
    public interface AfterFindListener {
        //查找成功
        public void findSuccess(List<User> userList);

        //查找失败
        public void findFailed();
    }
}
