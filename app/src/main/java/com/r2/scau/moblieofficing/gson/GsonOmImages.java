package com.r2.scau.moblieofficing.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dell88 on 2017/8/14 0014.
 */

public class GsonOmImages {
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("officeImageList")
    @Expose
    private List<GsonOmImage> officeImageList;

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

    public List<GsonOmImage> getOfficeImageList() {
        return officeImageList;
    }

    public void setOfficeImageList(List<GsonOmImage> officeImageList) {
        this.officeImageList = officeImageList;
    }
}
