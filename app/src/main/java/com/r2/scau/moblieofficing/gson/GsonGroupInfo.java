package com.r2.scau.moblieofficing.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by dell88 on 2017/8/6 0006.
 */

public class GsonGroupInfo {
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("groupId")
    @Expose
    private Integer groupId;
    @SerializedName("groupName")
    @Expose
    private String groupName;
    @SerializedName("createdUserId")
    @Expose
    private Integer createdUserId;

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public Integer getCreatedUserId() {
        return createdUserId;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setCreatedUserId(Integer createdUserId) {
        this.createdUserId = createdUserId;
    }
}
