package com.r2.scau.moblieofficing.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 嘉进 on 20:15.
 */

public class GsonNotices {
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("announcementList")
    @Expose
    private List<GsonNotice> announcementList = null;

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

    public List<GsonNotice> getAnnouncementList() {
        return announcementList;
    }

    public void setAnnouncementList(List<GsonNotice> announcementList) {
        this.announcementList = announcementList;
    }
}
