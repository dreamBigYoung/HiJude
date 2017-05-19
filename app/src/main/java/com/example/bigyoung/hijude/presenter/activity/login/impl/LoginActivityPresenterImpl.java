package com.example.bigyoung.hijude.presenter.activity.login.impl;

import android.content.Context;
import android.content.Intent;

import com.example.bigyoung.hijude.base.MyBaseFragment;
import com.example.bigyoung.hijude.model.bean.User;
import com.example.bigyoung.hijude.model.bean.UserLoginBean;
import com.example.bigyoung.hijude.model.bean.UserStatus;
import com.example.bigyoung.hijude.presenter.activity.login.LoginActivityPresenter;
import com.example.bigyoung.hijude.presenter.activity.splash.SplashLoginPresenter;
import com.example.bigyoung.hijude.presenter.activity.splash.impl.SplashLoginPresenterImpl;
import com.example.bigyoung.hijude.utils.DataCacheUtils;
import com.example.bigyoung.hijude.view.activity.HomeActivity;
import com.example.bigyoung.hijude.view.activity.register.RegistingRunningActivity;
import com.example.bigyoung.hijude.wrap.SimpleEMCallBack;
import com.hyphenate.chat.EMClient;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by BigYoung on 2017/5/12.
 */

public class LoginActivityPresenterImpl implements LoginActivityPresenter {

    private final Context mContext;
    private LoginResultListener mLoginResultListener;

    public LoginActivityPresenterImpl(Context context) {
        mContext = context;
    }

    public LoginActivityPresenterImpl(Context context, LoginResultListener loginResultListener) {
        mContext = context;
        mLoginResultListener = loginResultListener;
    }

    @Override
    public void startLogin(final UserLoginBean loginBean) {
        if (loginBean == null) {
            return;
        }
        EMClient.getInstance().login(loginBean.getUsername(), loginBean.getPassword(), new SimpleEMCallBack() {
                    @Override
                    public void onSuccess() {
                        if (mLoginResultListener != null) {
                            mLoginResultListener.resultForLoginSuccess();
                        }
                        queryUesrInfo(loginBean.getUsername());
                    }

                    @Override
                    public void onError(int i, String s) {
                        if (mLoginResultListener != null) {
                            mLoginResultListener.resultForFailed();
                        }
                    }
                }
        );
    }

    //通过用户名到Bmob云数据通过username进行查询
    @Override
    public void queryUesrInfo(final String username) {
        BmobQuery<User> bmobQuery = new BmobQuery<User>();
        bmobQuery.addWhereEqualTo("username", username);
        bmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                User user;
                if (list != null && list.size() != 0 && e == null) {
                    //获取到了数据
                    user = list.get(0);
                    //把当前的用户信息缓存到内存中
                    DataCacheUtils.setmCurUser(user);
                    if (mLoginResultListener != null) {
                        mLoginResultListener.resultForLoadSuccess();
                    }
                    //查询好友信息
                    SplashLoginPresenter splashPre=new SplashLoginPresenterImpl(mContext);
                    splashPre.queryFriends();
                } else {
                    user = new User();
                    user.setUsername(username);
                    //把当前的用户信息缓存到内存中
                    DataCacheUtils.setmCurUser(user);
                    if (mLoginResultListener != null) {
                        mLoginResultListener.resultForLoadFailed();
                    }
                }

              /*  //发送消息通知主界面里面的5个Fragment，使用粘性事件,防止activity已被销毁
                EventBus.getDefault().postSticky(new UserStatus(user, MyBaseFragment.LOGINED));*/
            }
        });
    }

    public interface LoginResultListener {
        //处理登陆成功
        public void resultForLoginSuccess();

        //加载用户数据成功
        public void resultForLoadSuccess();

        //加载用户数据失败
        public void resultForLoadFailed();

        //处理登录失败
        public void resultForFailed();
    }
}
