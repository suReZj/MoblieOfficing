package com.r2.scau.moblieofficing.bean;

/**
 * Created by EdwinCheng on 2017/7/31.
 */

public class FileBean {
    private String name;
    private long size;
    private int attribute;
    private String lastUpdateTime;

    public FileBean(String name, int size, String lastUpdateTime, int attribute) {
        this.name = name;
        this.size = size;
        this.attribute = attribute;
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getAttribute() {
        return attribute;
    }

    public void setAttribute(int attribute) {
        this.attribute = attribute;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
