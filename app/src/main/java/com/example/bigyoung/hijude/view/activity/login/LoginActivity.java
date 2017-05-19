package com.example.bigyoung.hijude.view.activity.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
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
import com.example.bigyoung.hijude.base.MyApplication;
import com.example.bigyoung.hijude.base.MyBaseActivity;
import com.example.bigyoung.hijude.model.bean.UserLoginBean;
import com.example.bigyoung.hijude.utils.MyToast;
import com.example.bigyoung.hijude.utils.TextViewUtils;
import com.example.bigyoung.hijude.utils.ValidateUtils;
import com.example.bigyoung.hijude.wrap.SimpleTextWatcher;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by BigYoung on 2017/5/12.
 */

public class LoginActivity extends MyBaseActivity {

    private ViewHolder mViewHolder;

    //开启activity的方法
    public static void startLoginActivity(Context context) {
        Intent it = new Intent(context, LoginActivity.class);
        context.startActivity(it);
    }

    @Override
    public View getContentView(ViewGroup parent) {
        View inflate = LayoutInflater.from(LoginActivity.this).inflate(R.layout.activity_login, parent, false);
        mViewHolder = new ViewHolder(inflate);
        return inflate;
    }

    class ViewHolder {
        @BindView(R.id.et_username)
        TextInputEditText mEtUsername;
        @BindView(R.id.et_pwd)
        TextInputEditText mEtPwd;
        @BindView(R.id.bt_login)
        Button mBtLogin;
        @BindView(R.id.tv_select_area)
        TextView mTvSelectArea;
        @BindView(R.id.tv_login_question)
        TextView mTvLoginQuestion;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            initView();
            initEvent();
        }

        /**
         * 检测数据合法性
         */
        private boolean checkData() {
            if (ValidateUtils.validateUserName(TextViewUtils.getTvContent(mEtUsername)) == false) {
                MyToast.show(LoginActivity.this, "输入用户名不规范");
                return false;
            }
            if (ValidateUtils.validatePassword(TextViewUtils.getTvContent(mEtPwd)) == false) {
                MyToast.show(LoginActivity.this, "输入密码不规范");
                return false;
            }
            return true;
        }

        private void initEvent() {
            mBtLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkData() == true) {
                        EventBus.getDefault().postSticky(new UserLoginBean(TextViewUtils.getTvContent(mEtUsername), TextViewUtils.getTvContent(mEtPwd)));
                        Intent it=new Intent(LoginActivity.this,LoginRunningActivity.class);
                        startActivity(it);
                    }
                }
            });
            //文本监听
            mEtUsername.addTextChangedListener(new MyTextWatcher());
            mEtPwd.addTextChangedListener(new MyTextWatcher());
            //软键盘监听
            mEtUsername.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_NEXT) {
                        String userName = TextViewUtils.getTvContent(mEtUsername);
                        if (TextUtils.isEmpty(userName) == true) {
                            MyToast.show(LoginActivity.this, "用户名不能为空");
                        }
                    }
                    return false;
                }
            });
        }

        private class MyTextWatcher extends SimpleTextWatcher {
            @Override
            public void afterTextChanged(Editable s) {
                String userName = mEtUsername.getText().toString();
                String pwd = mEtPwd.getText().toString();
                mBtLogin.setEnabled(!(TextUtils.isEmpty(userName) || TextUtils.isEmpty(pwd)));
            }
        }

        private void initView() {
            createTextViewUnderline(mTvLoginQuestion);
            createTextViewUnderline(mTvSelectArea);
        }

        //创建TextView的下划线
        public void createTextViewUnderline(TextView textView) {
            //设置画笔有一个下划线的标记
            textView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            //设置抗锯齿
            textView.getPaint().setAntiAlias(true);
        }
    }
}
