package com.example.bigyoung.hijude.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.bigyoung.hijude.R;
import com.example.bigyoung.hijude.utils.FragmentFactory;
import com.example.bigyoung.hijude.utils.LogUtils;
import com.example.bigyoung.hijude.view.fragment.ContactFragment;
import com.example.bigyoung.hijude.view.fragment.ConversationFragment;
import com.example.bigyoung.hijude.view.fragment.LiveFragment;
import com.example.bigyoung.hijude.view.fragment.NearByFragment;
import com.example.bigyoung.hijude.view.fragment.PersonalFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.contaier)
    FrameLayout mContaier;
    @BindView(R.id.bottomNavigationBar)
    BottomNavigationBar mBottomNavigationBar;
    @BindView(R.id.activity_home)
    LinearLayout mActivityHome;

    private int mCurrentFragPos=0;//记录当前选中的fragment

    private  Class[] fragArray=new Class[]{
            NearByFragment.class,
            LiveFragment.class,
            ConversationFragment.class,
            ContactFragment.class,
            PersonalFragment.class
    };
    //底部导航条图片资源
    int[] barIcons = new int[]{
            R.drawable.ic_nav_nearby_active,
            R.drawable.ic_nav_live_active,
            R.drawable.ic_nav_conversation_active,
            R.drawable.ic_nav_contacts_active,
            R.drawable.ic_nav_personal_active
    };
    private BadgeItem badgeItem;//悬浮在imageButton上的显示图案

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        LogUtils.d("welcome to summerner's rift");
        initView();
        initBottomNavigationBar();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        //选中默认fragment
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.contaier, FragmentFactory.getFragment(fragArray[0]),mCurrentFragPos+"");
        transaction.commit();
    }
    //初始化底部导航条
    private void initBottomNavigationBar() {
        //底部导航的标签
        String[] barLabels = getResources().getStringArray(R.array.bottombarlabel);

        badgeItem = new BadgeItem()
                .setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL);//设置位置（靠顶部|水平居中）
        badgeItem.hide();//默认隐藏

        mBottomNavigationBar
                .setBarBackgroundColor(R.color.bottombarcolor)//设置底部导航条的颜色
                .addItem(new BottomNavigationItem(barIcons[0],barLabels[0]))//添加tab
                .addItem(new BottomNavigationItem(barIcons[1],barLabels[1]))
                .addItem(new BottomNavigationItem(barIcons[2],barLabels[2]).setBadgeItem(badgeItem))
                .addItem(new BottomNavigationItem(barIcons[3],barLabels[3]))
                .addItem(new BottomNavigationItem(barIcons[4],barLabels[4]))
                .setActiveColor(R.color.activecolor)//设置选中的颜色
                .setInActiveColor(R.color.inactivecolor)//设置未选中该的颜色
                .setFirstSelectedPosition(0)//让第一个item选中
                .setMode(BottomNavigationBar.MODE_FIXED)//设置为混合模式
                .initialise();//初始化

        //给Tab设置点击监听
        mBottomNavigationBar.setTabSelectedListener(new MyTabSelectedListener());
    }

    private class MyTabSelectedListener implements BottomNavigationBar.OnTabSelectedListener{

        //tab选中调用（显示Fragment）
        @Override
        public void onTabSelected(int position) {
//            MyLogger.i("onTabSelected:"+position);
            //1 通过FragmentManager获取Fragment 2 如果不存在，就通过FragmentFactory创建 3 如果存在，显示
            mCurrentFragPos=position;
            FragmentManager fm = getSupportFragmentManager();
            Fragment fragment = fm.findFragmentByTag(position + "");
            FragmentTransaction ft = fm.beginTransaction();
            if(fragment == null){
                //添加
                ft.add(R.id.contaier,FragmentFactory.getFragment(fragArray[position]),position+"");
            }else{
                //显示
                ft.show(fragment);
            }
            ft.commit();
        }

        //之前选中的tab未被选中调用(隐藏Fragment)
        @Override
        public void onTabUnselected(int position) {
//            MyLogger.i("onTabUnselected:"+position);
            FragmentManager fm = getSupportFragmentManager();
            Fragment fragment = fm.findFragmentByTag(position + "");
            FragmentTransaction ft = fm.beginTransaction();
            ft.hide(fragment);
            ft.commit();
        }

        @Override
        public void onTabReselected(int position) {
//            MyLogger.i("onTabReselected:"+position);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
       /* if(keyCode == KeyEvent.KEYCODE_BACK){
            //把当前的Activity移至到后台
            //是否为任务栈的根   false:只能够处理点击App图标进入的Activity(Splash)  true:根对所有的Activity
            moveTaskToBack(false);
            return true;//自己处理后退键的行为
        }*/
        return super.onKeyDown(keyCode, event);
    }
}
