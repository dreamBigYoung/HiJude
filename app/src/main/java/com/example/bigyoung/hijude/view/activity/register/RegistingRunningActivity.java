package com.example.bigyoung.hijude.view.activity.register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;

import com.example.bigyoung.hijude.R;
import com.example.bigyoung.hijude.base.MyApplication;
import com.example.bigyoung.hijude.model.bean.User;
import com.example.bigyoung.hijude.presenter.activity.splash.SplashLoginPresenter;
import com.example.bigyoung.hijude.presenter.activity.splash.impl.SplashLoginPresenterImpl;
import com.example.bigyoung.hijude.utils.DataCacheUtils;
import com.example.bigyoung.hijude.utils.MyToast;
import com.example.bigyoung.hijude.view.activity.HomeActivity;
import com.example.bigyoung.hijude.wrap.SimpleEMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by BigYoung on 2017/5/10.
 */

public class RegistingRunningActivity extends Activity {
    private User mUser;
    public static final int REGISTE_FAILED=0;//注册失败
    public static final int REGISTE_SUCCESS=1;//注册失败
    public static final int LOADING_SUCCESS=2;//登录成功

    private int currentStatus=REGISTE_FAILED;//当前状态

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注册eventBus
        EventBus.getDefault().register(RegistingRunningActivity.this);

        setContentView(R.layout.activity_loading);
        startLoading();
    }

    /**
     * 开始加载
     */
    private void startLoading() {
        uploadIcon();
    }

    //上传头像
    private void uploadIcon() {
        //上传头像，非基本数据需要以BombFile的形式上传
        mUser.getIcon().uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    uploadBaseInfo();
                } else {
                    MyToast.show(RegistingRunningActivity.this, "网络异常，注册img失败");
                    RegistingRunningActivity.this.finish();
                }
            }
        });
    }

    /**
     * 上传非bombFile类型
     */
    private void uploadBaseInfo() {
        mUser.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    createEMAccount(mUser);
                } else {
                    MyToast.show(RegistingRunningActivity.this, "注册userInfo失败");
                    RegistingRunningActivity.this.finish();
                }
            }
        });
    }

    //在环信的服务器上创建账户
    private void createEMAccount(final User user) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //该方法是一个同步的方法（有联网的操作：需要在子线程里面执行）
                    EMClient.getInstance().createAccount(user.getUsername(), user.getPassword());
                    //显示注册成功
                    MyToast.show(RegistingRunningActivity.this, "注册账号成功");
                    //登录
                    loginEMServer(mUser);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    //创建失败
                    MyToast.show(RegistingRunningActivity.this, "注册账号失败");
                    //关闭activity
                    MyApplication.getMainThreadHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            //创建成功
                            RegistingRunningActivity.this.finish();
                        }
                    });
                }
            }
        }).start();
    }

    //登录到环信的服务器
    private void loginEMServer(final User user) {
        EMClient.getInstance().login(user.getUsername(), user.getPassword(), new SimpleEMCallBack() {
            @Override
            public void onSuccess() {
                super.onSuccess();
                MyToast.show(RegistingRunningActivity.this, "登录账号成功");
                //缓存信息
                DataCacheUtils.setmCurUser(user);
                //查询好友信息
                SplashLoginPresenter splashPre=new SplashLoginPresenterImpl(new SplashLoginPresenterImpl.AfterQueryListener() {
                    @Override
                    public void afterQueryFriends() {
                        Intent it=new Intent(RegistingRunningActivity.this, HomeActivity.class);
                        startActivity(it);
                        RegistingRunningActivity.this.finish();
                    }
                }, RegistingRunningActivity.this);
                splashPre.queryFriends();
                /*EventBus.getDefault().post(new UserStatus(user, MyBaseFragment.LOGINED));*/
            }

            @Override
            public void onError(int i, String s) {
                super.onError(i, s);
                MyToast.show(RegistingRunningActivity.this, "登录账号失败,请重新登录");
               /* EventBus.getDefault().post(new UserStatus(user, MyBaseFragment.UNLOGIN));*/
                Intent it=new Intent(RegistingRunningActivity.this, HomeActivity.class);
                startActivity(it);
                RegistingRunningActivity.this.finish();
            }
        });
    }

    //处理
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true, priority = 100)
    public void resultUser(User user) {
        mUser = user;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(RegistingRunningActivity.this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            //屏蔽回退事件
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
