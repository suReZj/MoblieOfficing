package com.r2.scau.moblieofficing.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 嘉进 on 19:48.
 */

public class GsonFriend {
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("listFriendUserphone")
    @Expose
    private List<String> listFriendUserphone = null;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<String> getListFriendUserphone() {
        return listFriendUserphone;
    }

    public void setListFriendUserphone(List<String> listFriendUserphone) {
        this.listFriendUserphone = listFriendUserphone;
    }
}
