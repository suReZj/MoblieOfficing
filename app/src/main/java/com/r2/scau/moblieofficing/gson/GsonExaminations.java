package com.r2.scau.moblieofficing.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by dell88 on 2017/8/14 0014.
 */

public class GsonExaminations {
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("officeManage")
    @Expose
    private GsonExamination officeManage;

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

    public GsonExamination getOfficeManage() {
        return officeManage;
    }

    public void setOfficeManage(GsonExamination officeManage) {
        this.officeManage = officeManage;
    }
}
