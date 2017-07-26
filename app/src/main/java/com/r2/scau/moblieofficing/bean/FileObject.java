package com.r2.scau.moblieofficing.bean;

/**
 * Created by EdwinCheng on 2017/7/24.
 * 为了与io的File类区分，命名后缀Object
 *
 */
public class FileObject extends BaseFile {
    private byte dataSize;

    /** 表示一个是否处于编辑状态 */
    private int editState;


    public byte getDataSize() {
        return dataSize;
    }

    public void setDataSize(byte dataSize) {
        this.dataSize = dataSize;
    }
}
