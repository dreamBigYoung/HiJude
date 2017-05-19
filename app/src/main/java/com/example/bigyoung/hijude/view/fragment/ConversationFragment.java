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

public class ConversationFragment extends MyBaseFragment {

    @Override
    protected void changeHeadUILogined() {
        setTvTitleByFormat("消息",0);
    }

    @Override
    protected String getTvTitleText() {
        return "会话";
    }

    @Override
    protected String getTvInfoText() {
        return "可以让附近的人发收消息";
    }

    @Override
    public View getBobyContentView(LayoutInflater inflater,ViewGroup parent) {
        View bodyView = inflater.inflate(R.layout.frag_conversation_body, parent, false);
        return bodyView;
    }
}
