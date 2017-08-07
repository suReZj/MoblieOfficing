package com.r2.scau.moblieofficing.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by EdwinCheng on 2017/7/31.
 */

public class GsonFileJsonBean {

    @SerializedName("totalSize")
    @Expose
    private long  totalSize;
    @SerializedName("FILES")
    @Expose
    private List<String> files = null;
    @SerializedName("DIR")
    @Expose
    private List<String> folders = null;

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }

    public List<String> getFolders() {
        return folders;
    }

    public void setFolders(List<String> folders) {
        this.folders = folders;
    }
}
