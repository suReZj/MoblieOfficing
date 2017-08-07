package com.r2.scau.moblieofficing.bean;

/**
 * Created by dell88 on 2017/8/6 0006.
 */

public class groupMember {
    private String userPhone;
    private String nickName;
    private String iconPath;

    public groupMember(){

    }
    public groupMember(String userPhone, String nickName, String iconPath) {
        this.userPhone = userPhone;
        this.nickName = nickName;
        this.iconPath = iconPath;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }
}
