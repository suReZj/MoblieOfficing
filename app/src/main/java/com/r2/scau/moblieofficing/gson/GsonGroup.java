package com.r2.scau.moblieofficing.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by 嘉进 on 19:32.
 */

public class GsonGroup {

    @SerializedName("gid")
    @Expose
    private Integer gid;
    @SerializedName("gname")
    @Expose
    private String gname;
    @SerializedName("gcreateduserid")
    @Expose
    private Integer gcreateduserid;

    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    public Integer getGcreateduserid() {
        return gcreateduserid;
    }

    public void setGcreateduserid(Integer gcreateduserid) {
        this.gcreateduserid = gcreateduserid;
    }
}
