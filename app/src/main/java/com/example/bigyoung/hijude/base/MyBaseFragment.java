package com.example.bigyoung.hijude.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bigyoung.hijude.R;
import com.example.bigyoung.hijude.model.bean.UserStatus;
import com.example.bigyoung.hijude.utils.DataCacheUtils;
import com.example.bigyoung.hijude.utils.EMUtils;
import com.example.bigyoung.hijude.view.activity.login.LoginActivity;
import com.example.bigyoung.hijude.view.activity.register.EnterNickNameActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by BigYoung on 2017/5/8.
 */

public abstract class MyBaseFragment extends Fragment {
    @BindView(R.id.ib_back)
    public ImageButton mIbBack;
    @BindView(R.id.tv_title)
    public TextView mTvTitle;
    @BindView(R.id.header_left)
    public LinearLayout mHeaderLeft;
    @BindView(R.id.header_right)
    public LinearLayout mHeaderRight;
    @BindView(R.id.iv_empty)
    ImageView mIvEmpty;
    @BindView(R.id.tv_info)
    TextView mTvInfo;
    @BindView(R.id.bt_register)
    Button mBtRegister;
    @BindView(R.id.bt_login)
    Button mBtLogin;
    @BindView(R.id.empty_ll)
    LinearLayout mEmptyLl;
    @BindView(R.id.content)
    FrameLayout mContent;
    Unbinder unbinder;
    //当前登录状态
    public static final int UNLOGIN = 0;
    public static final int LOGINED = 1;
    private int currentStatus = LOGINED;

    private LayoutInflater mInflater;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View baseView = inflater.inflate(R.layout.base_fragment, container, false);
        unbinder = ButterKnife.bind(this, baseView);
        mInflater = inflater;
        initEvent();
        return baseView;
    }

    private void checkingLoginStatus() {
        currentStatus=(DataCacheUtils.getmCurUser()!=null?LOGINED:UNLOGIN);
    }

    @Override
    public void onResume() {
        super.onResume();
        checkingLoginStatus();
        refreashView();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(MyBaseFragment.this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(MyBaseFragment.this);
    }

  @Subscribe(threadMode = ThreadMode.MAIN)
    public void resultUserMessage(UserStatus userStatus) {
        currentStatus = userStatus.getCurStatus();
        refreashView();
    }

    //刷新头布局
    private void refreashHeadView() {
        switch (currentStatus) {
            case LOGINED:
                changeHeadUILogined();
                break;
            case UNLOGIN:
                changeHeadUIUnLogined();
                break;
        }
    }

    /**
     * 依照字符模版设置标题
     * @param name
     * @param num
     */
    public void setTvTitleByFormat(String name,int num){
        String title = String.format(getString(R.string.format_title), name, num);
        mTvTitle.setText(title);
    }
    /**
     * 未登录UI
     */
    public void changeHeadUIUnLogined() {
        mIbBack.setVisibility(View.GONE);
        mTvInfo.setText(getTvInfoText());
        mTvTitle.setText(getTvTitleText());

    }

    /**
     * 登陆成功后UI的展示
     */
    protected abstract void changeHeadUILogined();

    /**
     * 初始化控件的事件
     */
    private void initEvent() {
        mBtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //MyToast.show(MyApplication.getContext(),"click is register");
                Intent it = new Intent(getActivity(), EnterNickNameActivity.class);
                startActivity(it);
            }
        });
        mBtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳入登陆界面
                LoginActivity.startLoginActivity(getContext());
            }
        });
    }

    /**
     * 初始化view
     */
    private void refreashView() {
        refreashHeadView();
        refreashBodyContent();
    }

    /**
     * 获得tvInfo的text
     *
     * @return
     */
    protected abstract String getTvTitleText();

    /**
     * 获得tvInfo的text
     *
     * @return
     */
    protected abstract String getTvInfoText();

    /**
     * 决定主内容显示
     */
    private void refreashBodyContent() {
        switch (getCurrentState()) {
            case UNLOGIN:
                //显示未登录状态下的界面
                mEmptyLl.setVisibility(View.VISIBLE);
                mContent.removeAllViews();
                break;
            case LOGINED:
                mEmptyLl.setVisibility(View.GONE);
                mContent.removeAllViews();
                mContent.addView(getBobyContentView(mInflater, mContent));
                break;
        }
    }

    /**
     * 获得显示内容
     *
     * @param parent
     * @return
     */
    public abstract View getBobyContentView(LayoutInflater inflater, ViewGroup parent);


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * 获得当前用户状态
     *
     * @return
     */
    public int getCurrentState() {
        return currentStatus;
    }

}
