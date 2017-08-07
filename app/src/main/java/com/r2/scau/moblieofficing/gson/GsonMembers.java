package com.r2.scau.moblieofficing.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dell88 on 2017/8/6 0006.
 */

public class GsonMembers {
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("groupContactUserEntityList")
    @Expose
    private List<GsonMember> groupContactUserEntityList = null;

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

    public List<GsonMember> getGroupContactUserEntityList() {
        return groupContactUserEntityList;
    }

    public void setGroupContactUserEntityList(List<GsonMember> groupContactUserEntityList) {
        this.groupContactUserEntityList = groupContactUserEntityList;
    }
}
