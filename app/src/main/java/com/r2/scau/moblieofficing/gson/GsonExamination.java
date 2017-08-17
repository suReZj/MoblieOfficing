package com.r2.scau.moblieofficing.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by dell88 on 2017/8/14 0014.
 */

public class GsonExamination {
    @SerializedName("omid")
    @Expose
    private String omid;

    @SerializedName("userphone")
    @Expose
    private String userphone;

    @SerializedName("omtype")
    @Expose
    private String omtype;

    @SerializedName("omsubtype")
    @Expose
    private String omsubtype;

    @SerializedName("omstarttime")
    @Expose
    private String omstarttime;

    @SerializedName("omendtime")
    @Expose
    private String omendtime;

    @SerializedName("omreason")
    @Expose
    private String omreason;

    @SerializedName("isapproval")
    @Expose
    private Boolean isapproval;

    public String getOmid() {
        return omid;
    }

    public void setOmid(String omid) {
        this.omid = omid;
    }

    public String getUserphone() {
        return userphone;
    }

    public void setUserphone(String userphone) {
        this.userphone = userphone;
    }

    public String getOmtype() {
        return omtype;
    }

    public void setOmtype(String omtype) {
        this.omtype = omtype;
    }

    public String getOmsubtype() {
        return omsubtype;
    }

    public void setOmsubtype(String omsubtype) {
        this.omsubtype = omsubtype;
    }

    public String getOmstarttime() {
        return omstarttime;
    }

    public void setOmstarttime(String omstarttime) {
        this.omstarttime = omstarttime;
    }

    public String getOmendtime() {
        return omendtime;
    }

    public void setOmendtime(String omendtime) {
        this.omendtime = omendtime;
    }

    public String getOmreason() {
        return omreason;
    }

    public void setOmreason(String omreason) {
        this.omreason = omreason;
    }

    public Boolean getIsapproval() {
        return isapproval;
    }

    public void setIsapproval(Boolean isapproval) {
        this.isapproval = isapproval;
    }
}
