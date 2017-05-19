package com.example.bigyoung.hijude.utils;

/**
 * Created by BigYoung on 2017/4/26.
 * 操作url
 */

public class FixUrl {
    public static String fixUrlForImg(String url){
        return Constants.HOST_URL+"/image?name="+url;
    }
}
