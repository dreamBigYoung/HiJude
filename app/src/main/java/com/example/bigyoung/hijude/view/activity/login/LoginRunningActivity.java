package com.example.bigyoung.hijude.view.activity.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bigyoung.hijude.R;
import com.example.bigyoung.hijude.base.MyApplication;
import com.example.bigyoung.hijude.model.bean.UserLoginBean;
import com.example.bigyoung.hijude.presenter.activity.login.LoginActivityPresenter;
import com.example.bigyoung.hijude.presenter.activity.login.impl.LoginActivityPresenterImpl;
import com.example.bigyoung.hijude.utils.MyToast;
import com.example.bigyoung.hijude.view.activity.HomeActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by BigYoung on 2017/5/12.
 */

public class LoginRunningActivity extends Activity {
    @BindView(R.id.tv_info)
    TextView mTvInfo;
    @BindView(R.id.rl_loading)
    RelativeLayout mRlLoading;
    private UserLoginBean mLoginBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        ButterKnife.bind(this);
        //注册
        EventBus.getDefault().register(LoginRunningActivity.this);
        initView();
        initEvent();
    }
    //处理
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true, priority = 100)
    public void resultUserLogin(UserLoginBean loginBean) {
        mLoginBean = loginBean;
        LoginActivityPresenter loginPresenter=new LoginActivityPresenterImpl(LoginRunningActivity.this,new LoginActivityPresenterImpl.LoginResultListener(){
            @Override
            public void resultForLoginSuccess() {
                MyToast.show(LoginRunningActivity.this,"登录成功");
                MyApplication.getMainThreadHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        mTvInfo.setText("正在加载用户数据");
                    }
                });
            }

            @Override
            public void resultForLoadSuccess() {
                MyToast.show(LoginRunningActivity.this,"加载数据成功");
                //开启Activity,防止界面已被销毁
                startActivity(new Intent(LoginRunningActivity.this, HomeActivity.class));
                LoginRunningActivity.this.finish();
            }

            @Override
            public void resultForLoadFailed() {
                MyToast.show(LoginRunningActivity.this,"网络异常,加载数据失败");
                LoginRunningActivity.this.finish();
            }

            @Override
            public void resultForFailed() {
                MyToast.show(LoginRunningActivity.this,"登录失败");
                LoginRunningActivity.this.finish();
            }
        });
        loginPresenter.startLogin(mLoginBean);
    }
    private void initEvent() {
 /*       MyApplication.getMainThreadHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                LoginRunningActivity.this.finish();
            }
        },3000);*/
    }

    private void initView() {
        mTvInfo.setText("正在登陆");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解除注册
        EventBus.getDefault().unregister(LoginRunningActivity.this);
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
