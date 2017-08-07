package com.r2.scau.moblieofficing.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by dell88 on 2017/8/6 0006.
 */

public class GsonQRCode {
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("path")
    @Expose
    private String path;

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getPath() {
        return path;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
