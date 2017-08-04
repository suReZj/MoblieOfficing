package com.r2.scau.moblieofficing.untils;

/**
 * Created by EdwinCheng on 2017/7/24.
 * class使用的字段全部封装到这一个类里面
 * layout使用的字段就封装在string中
 */
public class Contacts {
    public static final String computer_ip = "http://192.168.13.61:8089";

    public static final String file_Server = "/fileServer";

    public static final String getDir = "/getDir.shtml";
    public static final String getGroupDir = "/getGroupDir.shtml";
    public static final String fileUpload = "/fileUpload.shtml";
    public static final String uploadGroupFile = "/uploadGroupFile.shtml";
    public static final String filedownload = "/download.shtml";
    public static final String downLoadGroupFile = "/downLoadGroupFile.shtml";
    public static final String createFile = "/CreateFile.shtml";
    public static final String createDir = "/CreateDir.shtml";
    public static final String moveFile = "/MoveFile.shtml";
    public static final String fileRename = "/fileRename.shtml";
    public static final String DeleteFile = "/DeleteFile.shtml";
    public static final String deleteGroupFile = "/deleteGroupFile.shtml";
    public static final String DeleteDir = "/DeleteDir.shtml";

    /**
     * Create by edwincheng in 2017/7/28.
     *
     * 用于"文件管理"部分的字段
     */
    public static class FILEMANAGER{
        public static final int EMPTYVIEW = -1;
        public static final int ITEM_VIEW = 1;
        public static final int TAIL_VIEW = 2;

        public static final int FILE_TYPE = 10000;
        public static final int FOLDER_TYPE = 10001;

        //14个接口 28个handler 状态的字段
        public static final int GETDIR_SUCCESS = 101;
        public static final int GETDIR_FAILURE = 102;
        public static final int FILEUPLOAD_SUCCESS = 103;
        public static final int FILEUPLOAD_FAILURE = 104;
        public static final int FILEDOWNLOAD_SUCCESS = 105;
        public static final int FILEDOWNLOAD_FAILURE = 106;
        public static final int CREATE_SUCCESS = 107;
        public static final int CREATE_FAILURE = 108;
        public static final int MOVEFILE_SUCCESS = 109;
        public static final int MOVEFILE_FAILURE = 110;
        public static final int FILERENAME_SUCCESS = 111;
        public static final int FILERENAME_FAILURE = 112;
        public static final int DELETE_SUCCESS = 113;
        public static final int DELETE_FAILURE = 114;
    }

    /**
     * requestCode
     */
    public static class RequestCode{
        public static final int RENAME = 501;
        public static final int CREATE = 502;
        public static final int UPLOAD = 503;

    }

}
