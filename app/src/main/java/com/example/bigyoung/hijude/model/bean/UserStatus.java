package com.example.bigyoung.hijude.model.bean;

/**
 * Created by BigYoung on 2017/5/12.
 */

public class UserStatus {
    private User mUser;//用户
    private int curStatus;//用户当前状态

    public UserStatus() {
    }

    public UserStatus(User user, int curStatus) {
        mUser = user;
        this.curStatus = curStatus;
    }

    public int getCurStatus() {
        return curStatus;
    }

    public void setCurStatus(int curStatus) {
        this.curStatus = curStatus;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }
}
