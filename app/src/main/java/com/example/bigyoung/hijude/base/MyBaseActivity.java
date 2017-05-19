package com.example.bigyoung.hijude.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bigyoung.hijude.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by BigYoung on 2017/5/8.
 */

public abstract class MyBaseActivity extends AppCompatActivity {

    @BindView(R.id.ib_back)
    public ImageButton mIbBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.header_left)
    LinearLayout mHeaderLeft;
    @BindView(R.id.header_right)
    LinearLayout mHeaderRight;
    @BindView(R.id.content_home)
    public FrameLayout mContentHome;

    /**
     * 初始化内容
     */
    private void initContent() {
        mContentHome.addView(getContentView(mContentHome));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        setContentView(R.layout.activity_base);
        ButterKnife.bind(this);
        initContent();
        mIbBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyBaseActivity.this.finish();
            }
        });
    }
    //设置标题内容
    public void setActivityTitle(String title){
        mTvTitle.setText(title);
    }

    /**
     * @return
     */
    public abstract View getContentView(ViewGroup parent);
}
