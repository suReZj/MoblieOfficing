package com.r2.scau.moblieofficing.bean;

import org.jivesoftware.smackx.disco.packet.DiscoverItems;
import org.litepal.crud.DataSupport;


/**
 * 当前用户加入过的群聊
 */

public class MultiChatRoom extends DataSupport{


    private String mRoomJid;
    private Integer roomId;

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public MultiChatRoom() {

    }

    public MultiChatRoom(String roomJid) {

        mRoomJid = roomJid;
    }

    public String getRoomJid() {

        return mRoomJid;
    }

    public void setRoomJid(String roomJid) {

        mRoomJid = roomJid;
    }

    @Override
    public boolean equals(Object obj) {

        if(obj instanceof DiscoverItems.Item) {
            return ((DiscoverItems.Item) obj).getEntityID().equals(mRoomJid);
        }
        if(obj instanceof MultiChatRoom) {
            return ((MultiChatRoom) obj).mRoomJid.equals(mRoomJid);
        }
        return false;
    }
}
