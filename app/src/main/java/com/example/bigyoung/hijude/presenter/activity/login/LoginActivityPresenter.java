package com.example.bigyoung.hijude.presenter.activity.login;

import com.example.bigyoung.hijude.model.bean.UserLoginBean;

/**
 * Created by BigYoung on 2017/5/12.
 */

public interface LoginActivityPresenter {
    /**
     * 准备登录
     * @param loginBean
     */
    public void startLogin(UserLoginBean loginBean);

    /**
     *查询用户信息
     * @param username
     */
    public void queryUesrInfo(final String username);
}
