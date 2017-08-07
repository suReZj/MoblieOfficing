package com.r2.scau.moblieofficing.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by dell88 on 2017/8/6 0006.
 */

public class GsonMember {
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("userphone")
    @Expose
    private String userphone;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userphone;
    }

    public void setUserPhone(String userPhone) {
        this.userphone = userPhone;
    }
}
