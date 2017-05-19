package com.example.bigyoung.hijude.model.bean;

/**
 * Created by BigYoung on 2017/5/15.
 */

public class ContactNumUpdateBean {
    private int position;//待更新的tab对应的fragment

    public ContactNumUpdateBean(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
