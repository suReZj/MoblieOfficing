package com.r2.scau.moblieofficing.bean;

import org.jivesoftware.smack.packet.Message;
import org.litepal.crud.DataSupport;

/**
 * Created by dell88 on 2017/8/2 0002.
 */

public class MultiChatMessage extends DataSupport{
    private String msgId;
    private String chatRoom;

    public void setChatRoom(String chatRoom) {
        this.chatRoom = chatRoom;
    }

    public String getChatRoom() {

        return chatRoom;
    }

    public MultiChatMessage(String msgId, String chatRoom) {
        this.msgId = msgId;
        this.chatRoom = chatRoom;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getMsgId() {

        return msgId;
    }
}
