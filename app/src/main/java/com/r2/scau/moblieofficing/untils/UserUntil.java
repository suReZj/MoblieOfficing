package com.r2.scau.moblieofficing.untils;

import com.r2.scau.moblieofficing.bean.Contact;
import com.r2.scau.moblieofficing.gson.GsonGroup;
import com.r2.scau.moblieofficing.gson.GsonUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 嘉进 on 11:48.
 */

public class UserUntil {
    public static String name;
    public static String phone;
    public static List<Contact> friendList = new ArrayList<>();
    public static GsonUser gsonUser;
    public static List<GsonGroup> createGroupList = new ArrayList<>();
    public static List<GsonGroup> joinGroupList = new ArrayList<>();
    public static List<GsonGroup> groupList = new ArrayList<>();
}
