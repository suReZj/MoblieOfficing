package com.r2.scau.moblieofficing.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by 嘉进 on 10:23.
 */

public class GsonCreateReport {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("dailyReportId")
    @Expose
    private Integer dailyReportId;

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

    public Integer getDailyReportId() {
        return dailyReportId;
    }

    public void setDailyReportId(Integer dailyReportId) {
        this.dailyReportId = dailyReportId;
    }

}
