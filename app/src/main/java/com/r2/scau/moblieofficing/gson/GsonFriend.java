package com.r2.scau.moblieofficing.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by 嘉进 on 16:34.
 */

public class GsonFriend {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("nickname")
    @Expose
    private String nickname;
    @SerializedName("email")
    @Expose
    private Object email;
    @SerializedName("pswd")
    @Expose
    private String pswd;
    @SerializedName("createTime")
    @Expose
    private Object createTime;
    @SerializedName("lastLoginTime")
    @Expose
    private Object lastLoginTime;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("userHeadPortrait")
    @Expose
    private Object userHeadPortrait;
    @SerializedName("userCompany")
    @Expose
    private Object userCompany;
    @SerializedName("userAddress")
    @Expose
    private Object userAddress;
    @SerializedName("userBirthday")
    @Expose
    private Object userBirthday;
    @SerializedName("userSex")
    @Expose
    private Object userSex;
    @SerializedName("userPhone")
    @Expose
    private String userPhone;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Object getEmail() {
        return email;
    }

    public void setEmail(Object email) {
        this.email = email;
    }

    public String getPswd() {
        return pswd;
    }

    public void setPswd(String pswd) {
        this.pswd = pswd;
    }

    public Object getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Object createTime) {
        this.createTime = createTime;
    }

    public Object getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Object lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Object getUserHeadPortrait() {
        return userHeadPortrait;
    }

    public void setUserHeadPortrait(Object userHeadPortrait) {
        this.userHeadPortrait = userHeadPortrait;
    }

    public Object getUserCompany() {
        return userCompany;
    }

    public void setUserCompany(Object userCompany) {
        this.userCompany = userCompany;
    }

    public Object getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(Object userAddress) {
        this.userAddress = userAddress;
    }

    public Object getUserBirthday() {
        return userBirthday;
    }

    public void setUserBirthday(Object userBirthday) {
        this.userBirthday = userBirthday;
    }

    public Object getUserSex() {
        return userSex;
    }

    public void setUserSex(Object userSex) {
        this.userSex = userSex;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

}
