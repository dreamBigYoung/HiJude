package com.example.bigyoung.hijude.view.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.example.bigyoung.hijude.R;
import com.example.bigyoung.hijude.base.MyApplication;
import com.example.bigyoung.hijude.base.MyBaseFragment;
import com.example.bigyoung.hijude.model.bean.UserStatus;
import com.example.bigyoung.hijude.presenter.activity.splash.SplashLoginPresenter;
import com.example.bigyoung.hijude.presenter.activity.splash.impl.SplashLoginPresenterImpl;
import com.example.bigyoung.hijude.utils.CheckPermissionUtils;
import com.example.bigyoung.hijude.utils.EMUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.Random;

/**
 * Created by BigYoung on 2017/5/8.
 */

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initEvent();
        //threadText();
    }

    private void threadText() {
        new Thread(new Runnable() {
            int i = 10;
            @Override
            public void run() {
                while (i > 0) {
                    int result = (int) (Math.random() * 2);

                    try {
                        Thread.sleep(3000L);
                        EventBus.getDefault().post(new UserStatus(null, result));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * 初始化控件事件
     */
    private void initEvent() {
        //检查权限
        String[] permissions = CheckPermissionUtils.checkPermission(this);
        if (permissions.length == 0) {
            //权限都申请了
            enterHomeActivity();
            //是否登录
        } else {
            //申请权限
            ActivityCompat.requestPermissions(this, permissions, 100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            for (int grantResult : grantResults) {
                if (grantResult == PackageManager.PERMISSION_DENIED) {
                    finish();
                    return;
                }
            }
            enterHomeActivity();
        }
    }

    //进入主界面
    private void enterHomeActivity() {
        //查询已登录的用户信息
        SplashLoginPresenter loginPresenter=new SplashLoginPresenterImpl(SplashActivity.this);
        loginPresenter.queryUserInfo();
        loginPresenter.queryFriends();
        MyApplication.getMainThreadHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent it = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(it);
                SplashActivity.this.finish();
            }
        }, 6000);
    }
}
