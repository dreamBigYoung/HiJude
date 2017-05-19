package com.example.bigyoung.hijude.utils;

/**
 */
public class Constants {
    /*
    LogUtils.LEVEL_ALL:打开日志(显示所有的日志输出)
    LogUtils.LEVEL_OFF:关闭日志(屏蔽所有的日志输出)
     */
    public static final int DEBUGLEVEL = LogUtils.LEVEL_ALL;
    //file文件的有效时间
    public static final Long SavingFilePeriod=5 * 60 * 1000L;

    //tomcat 服务器地址
    public static final String HOST_URL="http://192.168.14.38:8080/GooglePlayServer";
    //home 分支
    public static final String HOME_CATE=HOST_URL+"/home";
    //game 分支
    public static final String GAME_CATE=HOST_URL+"/game";
    //app 分支
    public static final String APP_CATE=HOST_URL+"/app";
    //subject 分支
    public static final String SUBJECT_CATE=HOST_URL+"/subject";
    //App detail 分支
    public static final String APPDETAIL_CATE=HOST_URL+"/detail";

    public static final int PAGE_SIZE=20;

}
