package com.example.bigyoung.hijude.model.bean;

/**
 * Created by BigYoung on 2017/5/15.
 */

public class FriendEventBean {
    public static final int ADD=0;//添加了好友事件
    public static final int DELETE=1;//删除了好友事件

    private String username;//用户类型
    private  int eventType;//事件类型

    public FriendEventBean() {
    }

    public FriendEventBean(String username, int eventType) {
        this.username = username;
        this.eventType = eventType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }
}
