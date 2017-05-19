package com.example.bigyoung.hijude.presenter.activity.splash.impl;

import android.content.Context;
import android.text.TextUtils;

import com.example.bigyoung.hijude.model.bean.User;
import com.example.bigyoung.hijude.presenter.activity.login.LoginActivityPresenter;
import com.example.bigyoung.hijude.presenter.activity.login.impl.LoginActivityPresenterImpl;
import com.example.bigyoung.hijude.presenter.activity.splash.SplashLoginPresenter;
import com.example.bigyoung.hijude.utils.DataCacheUtils;
import com.example.bigyoung.hijude.utils.EMUtils;
import com.example.bigyoung.hijude.utils.MyToast;
import com.example.bigyoung.hijude.view.activity.login.LoginActivity;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by BigYoung on 2017/5/13.
 */

public class SplashLoginPresenterImpl implements SplashLoginPresenter {

    private final Context mContext;
    private AfterQueryListener mAfterQueryListener;

    public SplashLoginPresenterImpl(Context context) {
        mContext=context;
    }

    public SplashLoginPresenterImpl(AfterQueryListener afterQueryListener, Context context) {
        mAfterQueryListener = afterQueryListener;
        mContext = context;
    }

    @Override
    public void queryUserInfo() {
        LoginActivityPresenter loginPre=new LoginActivityPresenterImpl(mContext);
        String currentUser = EMClient.getInstance().getCurrentUser();
        if(TextUtils.isEmpty(currentUser)) {
            DataCacheUtils.setmCurUser(null);
            return;
        }
        loginPre.queryUesrInfo(currentUser);
    }

    @Override
    public void queryFriends() {
        User curUser=DataCacheUtils.getmCurUser();
        if(curUser!=null&&TextUtils.isEmpty(curUser.getUsername())==false){
            EMClient.getInstance().contactManager().aysncGetAllContactsFromServer(new EMValueCallBack<List<String>>() {
                @Override
                public void onSuccess(List<String> strings) {
                    DataCacheUtils.setFriendsNameList(strings);
                    //查询好友的详细信息
                    findAllFriendDetail();
                }

                @Override
                public void onError(int i, String s) {
                    MyToast.show(mContext,"网络异常");
                }
            });
        }
    }

    /**
     * 查询所有的好友的详细信息
     */
    private void findAllFriendDetail(){
        BmobQuery<User> bmobQuery = new BmobQuery<User>();
        bmobQuery.addWhereContainedIn("username",DataCacheUtils.getFriendsNameList());
        bmobQuery.order("username");
        bmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if(list.size() != 0 && e == null){
                    DataCacheUtils.setFriendList(list);
                }
            }
        });
    }

    /**
     * 查询结束监听器
     */
    public interface AfterQueryListener{
        /**
         * 查询好友结束后
         */
        public void afterQueryFriends();
    }
}
