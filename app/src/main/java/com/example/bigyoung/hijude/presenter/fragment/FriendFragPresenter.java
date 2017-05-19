package com.example.bigyoung.hijude.presenter.fragment;

/**
 * Created by BigYoung on 2017/5/15.
 */

public interface FriendFragPresenter {
    //处理新好友加入的信息
    public void resultAddFriend(String username);
    //result event of delete friend
    public void resultDeleteFriend(String username);
}
