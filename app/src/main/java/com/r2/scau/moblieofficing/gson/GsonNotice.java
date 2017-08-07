package com.r2.scau.moblieofficing.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by 嘉进 on 20:17.
 */

public class GsonNotice {

    @SerializedName("aid")
    @Expose
    private Integer aid;
    @SerializedName("atitle")
    @Expose
    private String atitle;
    @SerializedName("acontent")
    @Expose
    private String acontent;
    @SerializedName("atype")
    @Expose
    private String atype;
    @SerializedName("acreateduserid")
    @Expose
    private Integer acreateduserid;
    @SerializedName("gid")
    @Expose
    private Integer gid;

    public Integer getAid() {
        return aid;
    }

    public void setAid(Integer aid) {
        this.aid = aid;
    }

    public String getAtitle() {
        return atitle;
    }

    public void setAtitle(String atitle) {
        this.atitle = atitle;
    }

    public String getAcontent() {
        return acontent;
    }

    public void setAcontent(String acontent) {
        this.acontent = acontent;
    }

    public String getAtype() {
        return atype;
    }

    public void setAtype(String atype) {
        this.atype = atype;
    }

    public Integer getAcreateduserid() {
        return acreateduserid;
    }

    public void setAcreateduserid(Integer acreateduserid) {
        this.acreateduserid = acreateduserid;
    }

    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }

}
