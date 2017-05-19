package com.example.bigyoung.hijude.presenter.activity.splash;

/**
 * Created by BigYoung on 2017/5/13.
 */

public interface SplashLoginPresenter {
    //检测app已登录的用户信息
    public void queryUserInfo();

    /**
     * 查询好友信息并保存
     */
    public void queryFriends();
}
