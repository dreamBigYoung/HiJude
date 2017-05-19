package com.example.bigyoung.hijude.view.fragment;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bigyoung.hijude.R;
import com.example.bigyoung.hijude.base.MyBaseFragment;
import com.example.bigyoung.hijude.model.bean.User;
import com.example.bigyoung.hijude.presenter.fragment.PersonFragPresenter;
import com.example.bigyoung.hijude.presenter.fragment.impl.PersonFragPresenterImpl;
import com.example.bigyoung.hijude.utils.DataCacheUtils;
import com.example.bigyoung.hijude.utils.FragmentFactory;
import com.example.bigyoung.hijude.utils.ImageViewUtils;
import com.example.bigyoung.hijude.utils.MyToast;
import com.example.bigyoung.hijude.view.activity.login.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by BigYoung on 2017/5/8.
 */

public class PersonalFragment extends MyBaseFragment {

    private ViewHolder mViewHolder;

    @Override
    protected void changeHeadUILogined() {
        mTvTitle.setText("个人");
    }

    @Override
    protected String getTvTitleText() {
        return "个人";
    }

    @Override
    protected String getTvInfoText() {
        return "可以让附近的人发现你";
    }

    @Override
    public View getBobyContentView(LayoutInflater inflater, ViewGroup parent) {
        View bodyView = inflater.inflate(R.layout.fragment_personal, parent, false);
        mViewHolder = new ViewHolder(bodyView);
        return bodyView;
    }

    class ViewHolder {
        @BindView(R.id.iv_icon)
        ImageView mIvIcon;
        @BindView(R.id.tv_nickname)
        TextView mTvNickname;
        @BindView(R.id.bt_logout)
        Button mBtLogout;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            initView();
            mBtLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(getContext())
                            .setTitle("操作")
                            .setMessage("是否退出嗨聊？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    PersonFragPresenter perFrag=new PersonFragPresenterImpl(new PersonFragPresenterImpl.OperationAfterLogout(){
                                        @Override
                                        public void afterLogoutResult() {
                                            //清除user相关信息
                                            DataCacheUtils.clear();
                                            //清理frament缓存，跳转至登录界面,关闭mainActivity
                                            FragmentFactory.clearMap();
                                            getActivity().finish();
                                            LoginActivity.startLoginActivity(getContext());
                                        }
                                    });
                                    //登出
                                    perFrag.logout();
                                }
                            })
                            .setNegativeButton("取消",null)
                            .show();
                }
            });
        }
        //初始化界面
        private void initView() {
            //获取user信息
            User user = DataCacheUtils.getmCurUser();
            if(user!=null){
                BmobFile icon = user.getIcon();
                if(icon!=null){
                    //联网加载图片
                    ImageViewUtils.showImage(getContext(),icon.getUrl(),R.drawable.avatar3,mIvIcon);
                }
                if(TextUtils.isEmpty(user.getNickname())==false){
                    mTvNickname.setText(user.getNickname());
                }else{
                    mTvNickname.setText(user.getUsername());
                }
            }
        }

    }
}
