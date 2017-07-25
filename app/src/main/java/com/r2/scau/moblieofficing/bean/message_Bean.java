package com.r2.scau.moblieofficing.bean;

/**
 * Created by 张子健 on 2017/7/20 0020.
 */

public class message_Bean {
    private int icon;//消息图标
    private String Msg_Title;//消息标题
    private String Msg_content;//消息内容
    private String Msg_time;//消息时间
    private Boolean setTopFlag;//置顶flag


    public message_Bean(int imageView, String title, String content, String time,Boolean flag) {
        this.icon = imageView;
        this.Msg_Title = title;
        this.Msg_content = content;
        this.Msg_time = time;
        this.setTopFlag=flag;
    }

    public int getIcon() {
        return icon;
    }

    public String getMsg_Title() {
        return Msg_Title;
    }

    public String getMsg_content() {
        return Msg_content;
    }

    public String getMsg_time() {
        return Msg_time;
    }

    public Boolean getSetTopFlag() {
        return setTopFlag;
    }

    public void setSetTopFlag(Boolean setTopFlag) {
        this.setTopFlag = setTopFlag;
    }
}
