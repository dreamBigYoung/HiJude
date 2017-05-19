package com.example.bigyoung.hijude.utils;

import com.example.bigyoung.hijude.model.bean.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BigYoung on 2017/5/12.
 */

public class DataCacheUtils {
    private static User mCurUser;//当前用户
    private static List<String> friendsNameList;//好友姓名集合
    private static List<User> friendList;//存放好友集合

    public static User getmCurUser() {
        return mCurUser;
    }

    public static void setmCurUser(User mCurUser) {
        DataCacheUtils.mCurUser = mCurUser;
    }

    public static List<User> getFriendList() {
        return friendList;
    }

    public static void setFriendList(List<User> friendList) {
        DataCacheUtils.friendList = friendList;
    }

    /**
     * add new friend to cache
     *
     * @param user
     */
    public static void addUser(User user) {
        if (friendsNameList == null)
            friendsNameList = new ArrayList<String>();
        friendsNameList.add(user.getUsername());
        if (friendList == null) {
            friendList = new ArrayList<User>();
        }
        friendList.add(user);
    }

    /**
     * delete user from local
     *
     * @param username
     */
    public static void deleteUser(String username) {
        if (friendsNameList != null && friendsNameList.size() != 0)
            friendsNameList.remove(username);
        if (friendList != null && friendList.size() != 0) {
            for (User user : friendList) {
                if(user.getUsername().equals(username)) {
                    friendList.remove(user);
                    break;
                }
            }
        }
    }

    /**
     * 清空数据
     */
    public static void clear() {
        mCurUser = null;
        if (friendList != null)
            friendList.clear();
        if (friendsNameList != null)
            friendsNameList.clear();
    }

    public static List<String> getFriendsNameList() {
        return friendsNameList;
    }

    public static void setFriendsNameList(List<String> friendsNameList) {
        DataCacheUtils.friendsNameList = friendsNameList;
    }
}
