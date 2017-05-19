package com.example.bigyoung.hijude.view.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bigyoung.hijude.R;
import com.example.bigyoung.hijude.base.MyBaseFragment;

/**
 * Created by BigYoung on 2017/5/8.
 */

public class LiveFragment extends MyBaseFragment {


    @Override
    protected void changeHeadUILogined() {
        mTvTitle.setText("直播半夜有福利");
        mIbBack.setVisibility(View.GONE);
    }

    @Override
    protected String getTvTitleText() {
        return "直播";
    }

    @Override
    protected String getTvInfoText() {
        return "想怎么聊都行哦";
    }

    @Override
    public View getBobyContentView(LayoutInflater inflater, ViewGroup parent) {
        View bodyView = inflater.inflate(R.layout.frag_live_body, parent, false);
        return bodyView;
    }
}
