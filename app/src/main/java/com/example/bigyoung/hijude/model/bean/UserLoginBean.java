package com.example.bigyoung.hijude.model.bean;

/**
 * Created by BigYoung on 2017/5/12.
 */

public class UserLoginBean {
    private String username;
    private String password;

    public UserLoginBean() {
    }

    public UserLoginBean(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
