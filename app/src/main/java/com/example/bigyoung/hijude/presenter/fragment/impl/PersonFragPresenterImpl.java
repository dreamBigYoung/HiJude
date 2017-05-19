package com.example.bigyoung.hijude.presenter.fragment.impl;

import com.example.bigyoung.hijude.presenter.fragment.PersonFragPresenter;
import com.example.bigyoung.hijude.utils.DataCacheUtils;
import com.example.bigyoung.hijude.wrap.SimpleEMCallBack;
import com.hyphenate.chat.EMClient;

/**
 * Created by BigYoung on 2017/5/13.
 */

public class PersonFragPresenterImpl implements PersonFragPresenter {

    private final OperationAfterLogout mOperationAfterLogout;

    public PersonFragPresenterImpl(OperationAfterLogout operationAfterLogout) {
        mOperationAfterLogout =operationAfterLogout;
    }

    @Override
    public void logout() {
        EMClient.getInstance().logout(true,new SimpleEMCallBack(){
            @Override
            public void onSuccess() {
                super.onSuccess();
                if(mOperationAfterLogout!=null){
                    mOperationAfterLogout.afterLogoutResult();
                }
            }

            @Override
            public void onError(int i, String s) {
                super.onError(i, s);
            }
        });
    }

    public interface OperationAfterLogout{
        /**
         *退出登陆之后的操作
         */
        public void afterLogoutResult();

    }
}
