package com.r2.scau.moblieofficing.bean;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * Created by 张子健 on 2017/7/23 0023.
 */

public class chat_message_Bean {
    private Drawable icon;//头像
    private String msg;//聊天信息
    public chat_message_Bean(Drawable image, String msgs){
        this.icon=image;
        this.msg=msgs;
    }

    public Drawable getIcon() {
        return icon;
    }

    public String getMsg() {
        return msg;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
