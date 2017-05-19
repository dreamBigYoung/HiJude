package com.example.bigyoung.hijude.view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bigyoung.hijude.R;
import com.example.bigyoung.hijude.adapter.SearchFroAddFriendAdapter;
import com.example.bigyoung.hijude.base.MyBaseActivity;
import com.example.bigyoung.hijude.model.bean.User;
import com.example.bigyoung.hijude.presenter.activity.addfriend.AddFriendPresenter;
import com.example.bigyoung.hijude.presenter.activity.addfriend.impl.AddFriendPresenterImpl;
import com.example.bigyoung.hijude.utils.MyToast;
import com.example.bigyoung.hijude.utils.TextViewUtils;
import com.example.bigyoung.hijude.widget.RecycleViewDivider;
import com.example.bigyoung.hijude.wrap.SimpleEMCallBack;
import com.example.bigyoung.hijude.wrap.SimpleTextWatcher;
import com.hyphenate.chat.EMClient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by BigYoung on 2017/5/14.
 */

public class AddFriendActivity extends MyBaseActivity {

    public static void startActivity(Context context) {
        Intent it = new Intent(context, AddFriendActivity.class);
        context.startActivity(it);
    }

    @Override
    public View getContentView(ViewGroup parent) {
        View inflate = LayoutInflater.from(AddFriendActivity.this).inflate(R.layout.activity_add_friend, parent, false);
        setActivityTitle("搜索好友");
        new ViewHolder(inflate);
        return inflate;
    }

    class ViewHolder {
        @BindView(R.id.et_search_username)
        EditText mEtSearchUsername;
        @BindView(R.id.ib_search)
        ImageButton mIbSearch;
        @BindView(R.id.addfriend_rv)
        RecyclerView mAddfriendRv;
        @BindView(R.id.iv_nodata)
        ImageView mIvNodata;
        private SearchFroAddFriendAdapter mSearchFroAddFriendAdapter;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            initView();
            initEvent();
        }

        private void initView() {
            //添加布局管理器
            LinearLayoutManager manager = new LinearLayoutManager(AddFriendActivity.this);
            mAddfriendRv.setLayoutManager(manager);
            //设置item下划线
            mAddfriendRv.addItemDecoration(new RecycleViewDivider(AddFriendActivity.this, LinearLayoutManager.HORIZONTAL, 1, Color.BLACK));
            //创建adapter
            mSearchFroAddFriendAdapter = new SearchFroAddFriendAdapter(AddFriendActivity.this);
            //建立事件
            mSearchFroAddFriendAdapter.setAddFriendButtonClickListener(new SearchFroAddFriendAdapter.AddFriendButtonClickListener() {
                @Override
                public void onClickListener(User user) {
                    //弹出对话框
                    final User curUser=user;
                    final EditText editText = new EditText(AddFriendActivity.this);
                    editText.setFocusable(true);
                    new AlertDialog.Builder(AddFriendActivity.this)
                            .setMessage("加好友理由")
                            .setView(editText)
                            .setNegativeButton("取消", null)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String mess = editText.getText().toString();
                                    //添加好友
                                    EMClient.getInstance().contactManager().aysncAddContact(curUser.getUsername(), mess, new SimpleEMCallBack(){
                                        @Override
                                        public void onSuccess() {
                                            super.onSuccess();
                                            MyToast.show(AddFriendActivity.this,"发送请求成功");
                                        }

                                        @Override
                                        public void onError(int i, String s) {
                                            super.onError(i, s);
                                            MyToast.show(AddFriendActivity.this,"网络异常，发送请求失败");
                                        }
                                    });
                                }
                            })
                            .show();
                }
            });
            //建立联系
            mAddfriendRv.setAdapter(mSearchFroAddFriendAdapter);

        }

        private void initEvent() {
            mEtSearchUsername.addTextChangedListener(new SimpleTextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    String searchContent = TextViewUtils.getTvContent(mEtSearchUsername);
                    if (TextUtils.isEmpty(searchContent) == false) {
                        mIbSearch.setEnabled(true);
                    } else {
                        mIbSearch.setEnabled(false);
                    }
                }
            });
            //添加软键盘搜索监听
            mEtSearchUsername.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    String searchContent = TextViewUtils.getTvContent(mEtSearchUsername);
                    if (actionId == EditorInfo.IME_ACTION_SEARCH && TextUtils.isEmpty(searchContent) == false) {
                        doSearch();
                    }
                    return false;
                }
            });
            mIbSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doSearch();
                }
            });
        }

        //执行搜索
        private void doSearch() {
            //MyToast.show(AddFriendActivity.this, "开始搜索");
            hideSofeInputMethod();
            AddFriendPresenter frPresenter = new AddFriendPresenterImpl(new AddFriendPresenterImpl.AfterFindListener() {
                @Override
                public void findSuccess(List<User> userList) {
                    mSearchFroAddFriendAdapter.updateUserList(userList);
                    mSearchFroAddFriendAdapter.notifyDataSetChanged();
                    mIvNodata.setVisibility(View.GONE);
                }

                @Override
                public void findFailed() {
                    mSearchFroAddFriendAdapter.updateUserList(null);
                    mSearchFroAddFriendAdapter.notifyDataSetChanged();
                    mIvNodata.setVisibility(View.VISIBLE);
                }
            });
            frPresenter.findFriend(TextViewUtils.getTvContent(mEtSearchUsername));
        }

        //隐藏软键盘(软键盘是一个系统的服务)
        private void hideSofeInputMethod() {
            //获取输入法的服务
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            //从窗体上隐藏输入法
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
