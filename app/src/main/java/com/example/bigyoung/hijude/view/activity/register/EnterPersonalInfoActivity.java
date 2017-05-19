package com.example.bigyoung.hijude.view.activity.register;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.bigyoung.hijude.R;
import com.example.bigyoung.hijude.base.MyBaseActivity;
import com.example.bigyoung.hijude.model.bean.User;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.lljjcoder.citypickerview.widget.CityPicker;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by BigYoung on 2017/5/10.
 */

public class EnterPersonalInfoActivity extends MyBaseActivity {

    private String mStringExtra;//用户昵称
    private final int mPickPicCode = 100;
    private final int mCropPicCode = 200;
    private Uri mPicUri;
    private ViewHolder mViewHolder;
    public static String USER_ICON = "user_icon" + ".jpg";//用户头像图片的文件名
    private DatePickerDialog datePickerDialog;//日期选择器
    private User mUser;

    @Override
    public View getContentView(ViewGroup parent) {
        mStringExtra = getIntent().getStringExtra(EnterNickNameActivity.mNickname);
        createUserIconName(mStringExtra);
        View bodyView = LayoutInflater.from(EnterPersonalInfoActivity.this).inflate(R.layout.activity_personal_info, parent, false);
        mViewHolder = new ViewHolder(bodyView);
        return bodyView;
    }
    //创造userInco的图片名
    private void createUserIconName(String iconName){
        USER_ICON=iconName+ ".jpg";
    }
    class ViewHolder {
        @BindView(R.id.iv_icon)
        ImageView mIvIcon;
        @BindView(R.id.tv_nickname)
        TextView mTvNickname;
        @BindView(R.id.tv_birthday)
        TextView mTvBirthday;
        @BindView(R.id.tv_home)
        TextView mTvHome;
        @BindView(R.id.rb_male)
        RadioButton mRbMale;
        @BindView(R.id.rb_female)
        RadioButton mRbFemale;
        @BindView(R.id.rg_sex)
        RadioGroup mRgSex;
        @BindView(R.id.bt_next)
        Button mBtNext;
        @BindView(R.id.activity_personal_info)
        LinearLayout mActivityPersonalInfo;
        CityPicker mCityPicker;
        private final MyOnclickListener mMyOnclickListener;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            mMyOnclickListener = new MyOnclickListener();
            mTvNickname.setText(mStringExtra);
            initEvent();
        }

        //初始化事件
        private void initEvent() {
            mIvIcon.setOnClickListener(mMyOnclickListener);
            mTvBirthday.setOnClickListener(mMyOnclickListener);
            mTvHome.setOnClickListener(mMyOnclickListener);
            mRbMale.setOnClickListener(mMyOnclickListener);
            mRbFemale.setOnClickListener(mMyOnclickListener);
            mBtNext.setOnClickListener(mMyOnclickListener);
        }

        private class MyOnclickListener implements View.OnClickListener {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.iv_icon:
                        pickPictureFromSystemGallery();
                        break;
                    case R.id.tv_birthday:
                        showSelectDateDialog();
                        break;
                    case R.id.tv_home:
                        showSelectHomeDialog();
                        break;
                    case R.id.rb_male:
                        changeSexCheckState(true);
                        break;
                    case R.id.rb_female:
                        changeSexCheckState(false);
                        break;
                    case R.id.bt_next:
                        prepareToNext();
                        break;
                }
            }
        }

        /**
         * 准备跳转到下一个activity
         */
        private void prepareToNext() {
            new AlertDialog.Builder(EnterPersonalInfoActivity.this)
                    .setTitle("提示")
                    .setMessage("性别选定后将无法修改")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            fixUserInfo();
                            EventBus.getDefault().postSticky(mUser);
                            Intent it = new Intent(EnterPersonalInfoActivity.this, RegistFinalActivity.class);
                            startActivity(it);
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
        }

        /**
         * 封装用户填入的信息
         */
        private void fixUserInfo() {
            mUser = new User();
            mUser.setNickname(mStringExtra);
            File file = new File(getFilesDir(), USER_ICON);
            BmobFile icon = new BmobFile(file);
            mUser.setIcon(icon);
            mUser.setBrithday(mTvBirthday.getText().toString());
            mUser.setHome(mTvHome.getText().toString());
            mUser.setSex(mRbMale.isChecked() == true ? 0 : 1);
        }

        //设置性别状态
        private void changeSexCheckState(boolean butStatus) {
            mRbMale.setChecked(butStatus);
            mRbFemale.setChecked(!butStatus);
            changeNextButtonState();
        }

        //显示地址选择View
        private void showSelectHomeDialog() {
            if (mCityPicker == null) {
                mCityPicker = new CityPicker.Builder(EnterPersonalInfoActivity.this)//城市选择器
                        .title("选择家乡")//设置标题
                        .textSize(20)//滚轮文字的大小
                        .titleBackgroundColor("#b9b7b8")//设置标题文字的颜色
                        .onlyShowProvinceAndCity(true)//只显示省和城市
                        .cancelTextColor("#FF4081")//取消文本的颜色
                        .confirTextColor("#FF4081")//确定文本的颜色
                        .province("湖南省")//设置缺省的省
                        .city("常德市")//设置缺省的市
                        .district("无")
                        .textColor(Color.parseColor("#000000"))//滚轮文字的颜色
                        .provinceCyclic(true)//省份滚轮是否循环显示
                        .cityCyclic(false)//城市滚轮是否循环显示
                        .districtCyclic(false)//地区（县）滚轮是否循环显示
                        .visibleItemsCount(7)//滚轮显示的item个数
                        .itemPadding(10)//滚轮item间距
                        .build();
                //确定选择监听
                mCityPicker.setOnCityItemClickListener(new MyOnCityItemClickListener());
            }
            mCityPicker.show();
        }

        //地址选择监听器
        private class MyOnCityItemClickListener implements CityPicker.OnCityItemClickListener {
            @Override
            public void onSelected(String... citySelected) {
                String city = citySelected[0] + "-" + citySelected[1];
                mTvHome.setText(city);
                changeNextButtonState();
            }
        }

        //弹出选择日期的对话框
        private void showSelectDateDialog() {
            if (datePickerDialog == null) {
                //日历对象
                Calendar calendar = Calendar.getInstance();
                //日期选择对话框
                datePickerDialog = DatePickerDialog.newInstance(new MyOnDateSetListener(),//日期选择监听
                        calendar.get(Calendar.YEAR),//年
                        calendar.get(Calendar.MONTH),//月
                        calendar.get(Calendar.DAY_OF_MONTH),//日
                        false);//是否震动
                datePickerDialog.setYearRange(1985, 2028);//设置年的范围
            }
            //显示
            datePickerDialog.show(getSupportFragmentManager(), "DATEPICKER_TAG");

        }

        //日期选择器监听器
        private class MyOnDateSetListener implements DatePickerDialog.OnDateSetListener {
            @Override
            public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
                //把选择的日期显示在TextView
                mTvBirthday.setText(year + "-" + month + "-" + day);
                changeNextButtonState();
            }
        }

        //检索所有变量信息,修改下一步的状态
        private void changeNextButtonState() {
            //头像
            Drawable drawable = mIvIcon.getDrawable();
            if (drawable == null) {
                mBtNext.setEnabled(false);
                return;
            }
            //生日
            String birthday = mTvBirthday.getText().toString();
            if (TextUtils.isEmpty(birthday)) {
                mBtNext.setEnabled(false);
                return;
            }
            //家乡
            String home = mTvHome.getText().toString();
            if (TextUtils.isEmpty(home)) {
                mBtNext.setEnabled(false);
                return;
            }
            //性别
            if (!(mRbMale.isChecked() || mRbFemale.isChecked())) {
                mBtNext.setEnabled(false);
                return;
            }
            mBtNext.setEnabled(true);
        }
    }

    //从系统的相册中获取一张图片
    private void pickPictureFromSystemGallery() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, mPickPicCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case mPickPicCode:
                if (data != null) {
                    mPicUri = data.getData();
                    crop(mPicUri);
                }
                break;
            case mCropPicCode:
                if (data != null) {
                    Bitmap bitmap = data.getParcelableExtra("data");
                    if (bitmap != null) {
                        //显示裁减后的图片
                        mViewHolder.mIvIcon.setImageBitmap(bitmap);
                        mViewHolder.changeNextButtonState();
                        //保存图片到sd卡
                        try {
                            FileOutputStream fos = openFileOutput(USER_ICON, Context.MODE_PRIVATE);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
        }
    }

    /*
* 剪切图片
*/
    private void crop(Uri uri) {
        if (uri == null)
            return;
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);

        intent.putExtra("outputFormat", "JPEG");// 图片格式
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
        startActivityForResult(intent, mCropPicCode);
    }
}
