package com.example.bigyoung.hijude.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.bigyoung.hijude.R;
import com.example.bigyoung.hijude.adapter.ContactsFragmentVPAdapter;
import com.example.bigyoung.hijude.base.MyBaseFragment;
import com.example.bigyoung.hijude.model.bean.ContactNumUpdateBean;
import com.example.bigyoung.hijude.model.bean.User;
import com.example.bigyoung.hijude.utils.DataCacheUtils;
import com.example.bigyoung.hijude.utils.FragmentFactory;
import com.example.bigyoung.hijude.view.activity.AddFriendActivity;
import com.example.bigyoung.hijude.view.fragment.contacts.AttentionFragment;
import com.example.bigyoung.hijude.view.fragment.contacts.AuthenticateFragment;
import com.example.bigyoung.hijude.view.fragment.contacts.FansFragment;
import com.example.bigyoung.hijude.view.fragment.contacts.FriendFragment;
import com.example.bigyoung.hijude.view.fragment.contacts.GroupFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by BigYoung on 2017/5/8.
 */

public class ContactFragment extends MyBaseFragment {
    private View mInflate;
    private boolean isBodyViewExit = false;//bodyView是否已存在
    private String[] contacts_tab;//子fragment的tab集合
    private int mCurrentFragPos = 0;//当前fragment对应的position
    //字节码数组
    private static Class[] clazzs = new Class[]{
            FriendFragment.class,
            AttentionFragment.class,
            FansFragment.class,
            GroupFragment.class,
            AuthenticateFragment.class
    };
    private View mBodyView;
    private ViewHolder mViewHolder;

    /**
     * 获得clazz在clazzs数组中对应的position
     *
     * @param clazz
     * @return
     */
    public static int getFragPosition(Class clazz) {
        int len = clazzs.length;
        for (int i = 0; i < len; i++) {
            if (clazzs[i].equals(clazz))
                return i;
        }
        return -1;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载tab资源
        contacts_tab = getResources().getStringArray(R.array.contacts_tab);
 /*       //注册事件
        EventBus.getDefault().register(ContactFragment.this);*/
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void resultTiltleUpdate(ContactNumUpdateBean bean) {
        dealTitle(bean.getPosition());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
/*        //解除注册
        EventBus.getDefault().unregister(ContactFragment.this);*/
    }

    @Override
    protected void changeHeadUILogined() {
        dealTitle(mCurrentFragPos);
        View headLeftLogined = getHeadLeftLogined(mHeaderRight);
        if (isBodyViewExit == false) {
            mHeaderRight.addView(headLeftLogined);
            isBodyViewExit = true;
        } else {
            headLeftLogined.setVisibility(View.VISIBLE);
        }
    }

    //获得logined状态下headright的View
    public View getHeadLeftLogined(ViewGroup parent) {
        if (mInflate == null)
            mInflate = createHeadRightLogined(parent);
        return mInflate;
    }

    /**
     * 生成logined状态下headright的View
     *
     * @param parent
     * @return
     */
    private View createHeadRightLogined(ViewGroup parent) {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.header_contacts_fragment, parent, false);
        ImageButton addBtn = (ImageButton) ButterKnife.findById(inflate, R.id.ib_add_friend);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddFriendActivity.startActivity(getContext());
            }
        });
        return inflate;
    }

    @Override
    protected String getTvTitleText() {
        return "通讯录";
    }

    @Override
    protected String getTvInfoText() {
        return "可以让附近的人互动";
    }

    @Override
    public View getBobyContentView(LayoutInflater inflater, ViewGroup parent) {
        if (mBodyView == null) {
            mBodyView = inflater.inflate(R.layout.frag_contact_body, parent, false);
            mViewHolder = new ViewHolder(mBodyView);
        }
        return mBodyView;
    }

    class ViewHolder {
        @BindView(R.id.contacts_tableLayout)
        TabLayout mContactsTableLayout;
        @BindView(R.id.contacts_vp)
        ViewPager mContactsVp;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            initView();
        }

        private void initView() {
            //设置标题
            //创建Fragment的实例
            List<Fragment> fragments = new ArrayList<>();
            for (int i = 0; i < contacts_tab.length; i++) {
                Fragment fragment = FragmentFactory.getFragment(clazzs[i]);
                Bundle bundle = new Bundle();
                bundle.putString("title", contacts_tab[i]);
                fragment.setArguments(bundle);
                fragments.add(fragment);
            }
            ContactsFragmentVPAdapter adapter = new ContactsFragmentVPAdapter(getChildFragmentManager(), fragments);
            mContactsVp.setAdapter(adapter);
            //让TabLayout和ViewPager进行关联
            mContactsTableLayout.setupWithViewPager(mContactsVp);
            //缓存fragments
            mContactsVp.setOffscreenPageLimit(clazzs.length);
            //设置监听
            mContactsVp.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    dealTitle(position);
                    //更新当前pos
                    mCurrentFragPos = position;
                }

            });
        }

    }

    //处理标题
    private void dealTitle(int position) {
        String title = contacts_tab[position];
        int num = 0;
        switch (position) {
            case 0:
                List<User> friendList = DataCacheUtils.getFriendList();
                if (friendList != null)
                    num = friendList.size();
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
        }
        setTvTitleByFormat(title, num);
    }
}
