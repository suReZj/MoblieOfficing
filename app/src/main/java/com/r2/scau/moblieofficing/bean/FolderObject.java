package com.r2.scau.moblieofficing.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EdwinCheng on 2017/7/24.
 */

public class FolderObject extends BaseFile {
    private List<BaseFile> fileList ;

    public FolderObject() {
        if (fileList == null ){
            fileList = new ArrayList<>();
        }
    }

    public List<BaseFile> getFileList() {
        return fileList;
    }

    public void setFileList(List<BaseFile> fileList) {
        this.fileList = fileList;
    }
}
