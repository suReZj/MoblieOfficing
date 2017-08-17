package com.r2.scau.moblieofficing.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by dell88 on 2017/8/14 0014.
 */

public class GsonOmImage {
    @SerializedName("fid")
    @Expose
    private String fid;

    @SerializedName("filename")
    @Expose
    private String filename;

    @SerializedName("filepath")
    @Expose
    private String filepath;

    @SerializedName("filecreatedata")
    @Expose
    private String filecreatedata;

    @SerializedName("omid")
    @Expose
    private Long omid;

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getFilecreatedata() {
        return filecreatedata;
    }

    public void setFilecreatedata(String filecreatedata) {
        this.filecreatedata = filecreatedata;
    }

    public Long getOmid() {
        return omid;
    }

    public void setOmid(Long omid) {
        this.omid = omid;
    }
}
