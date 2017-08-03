package com.r2.scau.moblieofficing.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 嘉进 on 19:32.
 */

public class GsonGroups {
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("listGroupId")
    @Expose
    private List<GsonGroup> listGroupId = null;

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

    public List<GsonGroup> getListGroupId() {
        return listGroupId;
    }

    public void setListGroupId(List<GsonGroup> listGroupId) {
        this.listGroupId = listGroupId;
    }
}
