package com.r2.scau.moblieofficing.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 嘉进 on 19:48.
 */

public class GsonFriends {
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("listFriends")
    @Expose
    private List<GsonFriend> mListFriends = null;

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

    public List<GsonFriend> getListFriends() {
        return mListFriends;
    }

    public void setListFriends(List<GsonFriend> listFriends) {
        this.mListFriends = listFriends;
    }
}
