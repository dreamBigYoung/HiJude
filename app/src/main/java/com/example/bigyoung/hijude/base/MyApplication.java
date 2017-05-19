package com.example.bigyoung.hijude.base;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.example.bigyoung.hijude.model.bean.FriendEventBean;
import com.example.bigyoung.hijude.utils.LogUtils;
import com.example.bigyoung.hijude.utils.MyToast;
import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import cn.bmob.v3.Bmob;

/**

 * 描述	      易错点:需要在清单文件里面进行配置
 */
public class MyApplication extends Application {

    private static Context mContext;
    private static Handler mMainThreadHandler;
    private static int mMainThreadId;
    //用于存储联网返回的json string
    private static Map<String,String> jsonStringCacheMap=new HashMap<String,String>();

    public static Map<String, String> getJsonStringCacheMap() {
        return jsonStringCacheMap;
    }

    /**
     * 得到上下文
     *
     * @return
     */
    public static Context getContext() {
        return mContext;
    }

    /**
     * 得到主线程里面的创建的一个hanlder
     *
     * @return
     */
    public static Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }

    /**
     * 得到主线程的线程id
     *
     * @return
     */
    public static int getMainThreadId() {
        return mMainThreadId;
    }

    @Override
    public void onCreate() {//程序的入口方法
        //上下文
        mContext = getApplicationContext();

        //主线程的Handler
        mMainThreadHandler = new Handler();

        //主线程的线程id
        mMainThreadId = android.os.Process.myTid();
        /**
         myTid:Thread
         myPid:Process
         myUid:User
         */
        super.onCreate();
        init();
    }

    private void init() {
        initBmob();
        initEM();
        setListener();
    }

    /**
     * 设置app监听器
     */
    private void setListener() {
        //监听联系人相关
        setContactListener();
    }
    //设置联系人监听
    private void setContactListener() {
        EMClient.getInstance().contactManager().setContactListener(new EMContactListener() {

            @Override
            public void onContactAdded(String s) {
                EventBus.getDefault().post(new FriendEventBean(s,FriendEventBean.ADD));
            }

            @Override
            public void onContactDeleted(String s) {
                EventBus.getDefault().post(new FriendEventBean(s,FriendEventBean.DELETE));
            }

            @Override
            public void onContactInvited(String s, String s1) {
                //LogUtils.d("what the fuck",s+"--"+s1);
                MyToast.show(mContext,s+"--"+s1);
            }

            @Override
            public void onContactAgreed(String s) {

            }

            @Override
            public void onContactRefused(String s) {

            }
        });
    }

    //初始化bomb
    private void initBmob() {
        Bmob.initialize(this, "f215cf4637caf50a86ce30a78516f5bc");
    }

    //初始化环信的SDK
    private void initEM() {
        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        //false :接收到好友请求需要手动的同意或者拒绝
        //true:接收到好友请求,直接添加
        options.setAcceptInvitationAlways(true);
        //初始化
        EMClient.getInstance().init(this, options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);
    }
}
