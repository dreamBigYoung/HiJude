package com.example.bigyoung.hijude.utils;

import android.widget.TextView;

/**
 * Created by BigYoung on 2017/5/12.
 */

public class TextViewUtils {
    public static String getTvContent(TextView tv){
        if(tv==null)
            return null;
        return tv.getText().toString();
    }
}
