package com.example.bigyoung.hijude.view.activity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bigyoung.hijude.R;
import com.example.bigyoung.hijude.base.MyBaseActivity;

/**
 * Created by BigYoung on 2017/5/16.
 */

public class TalkingActivity extends MyBaseActivity {
    public static void startActivity(Context context){
        Intent it=new Intent(context,TalkingActivity.class);
        context.startActivity(it);
    }
    @Override
    public View getContentView(ViewGroup parent) {
        View inflate = LayoutInflater.from(TalkingActivity.this).inflate(R.layout.activity_chat, parent, false);
        return inflate;
    }
}
