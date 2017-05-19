package com.example.bigyoung.hijude.utils;

import android.support.v4.app.Fragment;

import com.example.bigyoung.hijude.base.MyBaseFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by BigYoung on 2017/5/8.
 * 在内存中缓存fragment
 */

public final class FragmentFactory {
    public static Map<Class,Fragment> map=new HashMap<>();
    private FragmentFactory(){}

    /**
     * 获得Fragment实例
     * @param cl
     * @return
     */
    public static synchronized Fragment getFragment(Class<? extends MyBaseFragment> cl){
        Fragment fragment = map.get(cl);
        if(fragment ==null){
            try {
                fragment=cl.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return fragment;
    }

    /**
     * 清除以缓存的fragment
     */
    public static void clearMap(){
        map.clear();
    }
}
