package com.example.bigyoung.hijude.view.activity.register;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import com.example.bigyoung.hijude.R;
import com.example.bigyoung.hijude.base.MyBaseActivity;
import com.example.bigyoung.hijude.model.bean.User;
import com.example.bigyoung.hijude.utils.MyToast;
import com.example.bigyoung.hijude.utils.ValidateUtils;
import com.example.bigyoung.hijude.wrap.SimpleTextWatcher;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by BigYoung on 2017/5/10.
 */

public class RegistFinalActivity extends MyBaseActivity {
    private User mUser;
    private ViewHolder mViewHolder;

    @Override
    public View getContentView(ViewGroup parent) {
        View bodyView = LayoutInflater.from(RegistFinalActivity.this).inflate(R.layout.activity_register_final, parent, false);
        mViewHolder = new ViewHolder(bodyView);
        return bodyView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注册eventBus
        EventBus.getDefault().register(RegistFinalActivity.this);
    }
    //处理
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void resultUser(User user) {
        mUser = user;
        //防止再次接收
        EventBus.getDefault().unregister(RegistFinalActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    class ViewHolder {
        @BindView(R.id.et_username)
        TextInputEditText mEtUsername;
        @BindView(R.id.et_pwd)
        TextInputEditText mEtPwd;
        @BindView(R.id.et_confirm_pwd)
        TextInputEditText mEtConfirmPwd;
        @BindView(R.id.bt_register)
        Button mBtRegister;
        private final MyTextWatcher mMyTextWatcher;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            mMyTextWatcher = new MyTextWatcher();
            initEvent();
        }

        private void initEvent() {
            mEtUsername.addTextChangedListener(mMyTextWatcher);
            mEtPwd.addTextChangedListener(mMyTextWatcher);
            mEtConfirmPwd.addTextChangedListener(mMyTextWatcher);
            //监听软件盘的操作
            mEtUsername.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if(actionId == EditorInfo.IME_ACTION_NEXT){
                        checkUserTextView();
                    }
                    return false;
                }
            });
            mEtUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus==false){
                        checkUserTextView();
                    }
                }
            });
            mBtRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkData() == true) {
                        startRegisting();
                    }
                }
            });
        }
        //开始注册
        private void startRegisting() {
            fixUserData();
            jumpIntoLoadingUI();

        }
        //跳入加载界面
        private void jumpIntoLoadingUI() {
            EventBus.getDefault().postSticky(mUser);
            Intent it=new Intent(RegistFinalActivity.this,RegistingRunningActivity.class);
            startActivityForResult(it,100);
        }

        //完善数据
        private void fixUserData() {
            String userName = mEtUsername.getText().toString();
            String pwd = mEtPwd.getText().toString();
            mUser.setUsername(userName);
            mUser.setPassword(pwd);
        }

        private boolean checkData() {
            //检测密码
            String pwd = mEtPwd.getText().toString();
            String confirmPwd = mEtConfirmPwd.getText().toString();
            //数据合法性
            if(ValidateUtils.validatePassword(pwd)==false){
                MyToast.show(RegistFinalActivity.this,"密码输入不符合规范");
                return false;
            }
            //密码是否一致
            if(pwd.equals(confirmPwd)==false){
                MyToast.show(RegistFinalActivity.this,"两次密码输入不一致");
                return false;
            }

            //检测用户名
            String userName = mEtUsername.getText().toString();
            if(ValidateUtils.validateUserName(userName)==false){
                MyToast.show(RegistFinalActivity.this,"用户名不规范");
                return false;
            }
            return true;
        }

        //检测文本内容，并做出提示
        private void checkUserTextView() {
            if(TextUtils.isEmpty(mEtUsername.getText().toString().trim())){
                MyToast.show(RegistFinalActivity.this,"用户名输入为空");
            }
        }

        /**
         * 监听输入文本
         */
        private class MyTextWatcher extends SimpleTextWatcher {
            @Override
            public void afterTextChanged(Editable s) {
                String userName = mEtUsername.getText().toString();
                String pwd = mEtPwd.getText().toString();
                String confirmPwd = mEtConfirmPwd.getText().toString();
                if (TextUtils.isEmpty(userName) ||
                        TextUtils.isEmpty(pwd) ||
                        TextUtils.isEmpty(confirmPwd)) {
                    mBtRegister.setEnabled(false);
                }else{
                    mBtRegister.setEnabled(true);
                }

            }
        }
    }
}
