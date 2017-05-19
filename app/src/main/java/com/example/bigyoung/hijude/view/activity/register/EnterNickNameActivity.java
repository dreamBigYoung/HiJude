package com.example.bigyoung.hijude.view.activity.register;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.bigyoung.hijude.R;
import com.example.bigyoung.hijude.base.MyBaseActivity;
import com.example.bigyoung.hijude.wrap.SimpleTextWatcher;

import butterknife.ButterKnife;

/**
 * Created by BigYoung on 2017/5/9.
 */

public class EnterNickNameActivity extends MyBaseActivity {
    TextInputEditText mEtNickname;
    Button mBtNext;
    private String mCurrentName;
    public static final String mNickname = "nickname";;

    @Override
    public View getContentView(ViewGroup parent) {
        View inflate = LayoutInflater.from(EnterNickNameActivity.this).inflate(R.layout.activity_enter_nick_name, parent, false);
        mEtNickname = ButterKnife.findById(inflate, R.id.et_nickname);
        mBtNext = ButterKnife.findById(inflate, R.id.bt_next);
        initEvent();
        return inflate;
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        mEtNickname.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                mCurrentName = mEtNickname.getText().toString();
                mBtNext.setEnabled(!TextUtils.isEmpty(mCurrentName));
            }
        });
        mIbBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSureExitDialog();
            }
        });
        mBtNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(EnterNickNameActivity.this,EnterPersonalInfoActivity.class);
                it.putExtra(mNickname,mCurrentName);
                startActivity(it);
            }
        });
    }

    /**
     * 显示退出提示对话框
     */
    private void showSureExitDialog() {
        new AlertDialog.Builder(EnterNickNameActivity.this)
                .setTitle("提示")
                .setMessage("确定要退出注册吗")
                .setPositiveButton("朕肯定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EnterNickNameActivity.this.finish();
                    }
                })
                .setNegativeButton("朕要三思", null)
                .show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            showSureExitDialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
