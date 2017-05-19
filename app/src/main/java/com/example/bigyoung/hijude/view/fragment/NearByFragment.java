package com.example.bigyoung.hijude.view.fragment;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bigyoung.hijude.R;
import com.example.bigyoung.hijude.base.MyBaseFragment;

import butterknife.ButterKnife;

/**
 * Created by BigYoung on 2017/5/8.
 */

public class NearByFragment extends MyBaseFragment {

    private View mInflate;
    private boolean headLeftLoginedStatus = false;//是否已存在,默认false

    @Override
    protected void changeHeadUILogined() {
        mIbBack.setVisibility(View.GONE);
        mTvTitle.setVisibility(View.GONE);
        View headLeftLogined = getHeadLeftLogined(mHeaderLeft);
        if (headLeftLoginedStatus == true)
            headLeftLogined.setVisibility(View.VISIBLE);
        else {
            mHeaderLeft.addView(headLeftLogined);
            headLeftLogined.setVisibility(View.VISIBLE);
            headLeftLoginedStatus = true;
        }

    }

    //获得logined状态下headleft的View
    public View getHeadLeftLogined(ViewGroup parent) {
        if (mInflate == null) {
            mInflate = createHeadLeftLogined(parent);
        }
        return mInflate;
    }

    @Override
    public void changeHeadUIUnLogined() {
        super.changeHeadUIUnLogined();
        /*mHeaderLeft.removeView(getHeadLeftLogined(mHeaderLeft));*/
        View headLeftLogined = getHeadLeftLogined(mHeaderLeft);
        headLeftLogined.setVisibility(View.GONE);
    }

    /**
     * create view of head right for logined status
     *
     * @param parent
     * @return
     */
    @NonNull
    private View createHeadLeftLogined(ViewGroup parent) {
/*
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.nearby_header, null, false);
*/
        View inflate = LinearLayout.inflate(getContext(), R.layout.nearby_header, null);
        TabLayout tabLayout = (TabLayout) ButterKnife.findById(inflate, R.id.nearby_tablayout);
        String[] stringArray = getResources().getStringArray(R.array.near_by);
        for (int i = 0; i < stringArray.length; i++) {
            TextView tv = new TextView(getContext());
            tv.setText(stringArray[i]);
            tv.setTextSize(20);
            tv.setGravity(Gravity.CENTER);
            if (i == 0) {
                tv.setTextColor(Color.BLACK);
            } else {
                tv.setTextColor(Color.DKGRAY);
            }
            tabLayout.addTab(tabLayout.newTab().setCustomView(tv));
        }
        tabLayout.setOnTabSelectedListener(new MyTabSelectedListener());
        return inflate;
    }

    //tab选择器监听器
    private class MyTabSelectedListener implements TabLayout.OnTabSelectedListener {

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            TextView view = (TextView) tab.getCustomView();
            view.setTextColor(Color.BLACK);
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            TextView view = (TextView) tab.getCustomView();
            view.setTextColor(Color.DKGRAY);
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    }

    @Override
    protected String getTvTitleText() {
        return "附近";
    }

    @Override
    protected String getTvInfoText() {
        return "附近摇一摇";
    }

    @Override
    public View getBobyContentView(LayoutInflater inflater, ViewGroup parent) {
        View bodyView = inflater.inflate(R.layout.frag_nearby_body, parent, false);
        return bodyView;
    }
}
