package com.r2.scau.moblieofficing.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.r2.scau.moblieofficing.untils.DateUtil;
import com.r2.scau.moblieofficing.smack.SmackManager;


public class ChatRecord extends ChatUser {
    /**
     * 最后聊天时间
     */
    private String mChatTime;
    /**
     * 朋友头像地址
     */
    private String mFriendAvatar;
    /**
     * 最后一条聊天记录
     */
    private String mLastMessage;
    /**
     * 未读消息数
     */
    private int mUnReadMessageCount;

    private Boolean setTopFlag;//设置是否置顶

    public ChatRecord(ChatUser chatUser) {

        //减少服务请求
        setFriendUsername(chatUser.getFriendUsername());
        setFriendNickname(chatUser.getFriendNickname());
        setMeNickname(chatUser.getMeNickname());
        setMeUsername(chatUser.getMeUsername());
        setChatJid(chatUser.getChatJid());
        setFileJid(chatUser.getFileJid());
        setUuid(chatUser.getUuid());
        setChatTime(DateUtil.currentDatetime());
        setMulti(chatUser.isMulti());
        setSetTopFlag(chatUser.getSetTopFlag());
    }

    public ChatRecord(ChatMessage chatMessage) {

        setFriendUsername(chatMessage.getFriendUsername());
        setMeUsername(chatMessage.getMeUsername());
        setMeNickname(chatMessage.getMeNickname());

//        if(chatMessage.isMulti()) {//群发
//            int idx = chatMessage.getFriendUsername().indexOf(Constant.MULTI_CHAT_ADDRESS_SPLIT);
//            String friendNickName = chatMessage.getFriendUsername().substring(0, idx);
//            setFriendNickname(friendNickName);//群聊记录显示群聊名称
//            setChatJid(chatMessage.getFriendUsername());
//            setMulti(chatMessage.isMulti());
//        } else {
            setFriendNickname(chatMessage.getFriendNickname());
            String chatJid = SmackManager.getInstance().getChatJid(chatMessage.getFriendUsername());
            String fileJid = SmackManager.getInstance().getFileTransferJid(chatJid);
            setChatJid(chatJid);
            setFileJid(fileJid);
//        }

        setChatTime(chatMessage.getDatetime());
        setLastMessage(chatMessage.getContent());
        setUuid(chatMessage.getUuid());
        updateUnReadMessageCount();
    }

    public String getFriendAvatar() {

        return mFriendAvatar;
    }

    public void setFriendAvatar(String friendAvatar) {

        mFriendAvatar = friendAvatar;
    }

    public String getChatTime() {

        return mChatTime == null ? DateUtil.currentDatetime() : mChatTime;
    }

    public void setChatTime(String chatTime) {

        mChatTime = chatTime;
    }

    public String getLastMessage() {

        return mLastMessage;
    }

    public void setLastMessage(String lastMessage) {

        mLastMessage = lastMessage;
    }

    public Boolean getSetTopFlag() {
        return setTopFlag;
    }

    public void setSetTopFlag(Boolean setTopFlag) {
        this.setTopFlag = setTopFlag;
    }

    public int getUnReadMessageCount() {

        return mUnReadMessageCount;
    }

    public void updateUnReadMessageCount() {

        mUnReadMessageCount += 1;
    }

    @Override
    public boolean equals(Object obj) {

        if(obj == null) {
            return false;
        }
        if(obj instanceof ChatRecord) {
            return this.getUuid().equals(((ChatRecord) obj).getUuid());
        }
        return false;
    }

    @Override
    public int describeContents() {

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        super.writeToParcel(dest, flags);
        dest.writeString(this.mChatTime);
        dest.writeString(this.mFriendAvatar);
        dest.writeString(this.mLastMessage);
        dest.writeInt(this.mUnReadMessageCount);
    }

    public ChatRecord() {

    }

    protected ChatRecord(Parcel in) {

        super(in);
        this.mChatTime = in.readString();
        this.mFriendAvatar = in.readString();
        this.mLastMessage = in.readString();
        this.mUnReadMessageCount = in.readInt();
    }

    public static final Parcelable.Creator<ChatRecord> CREATOR = new Parcelable.Creator<ChatRecord>() {
        @Override
        public ChatRecord createFromParcel(Parcel source) {

            return new ChatRecord(source);
        }

        @Override
        public ChatRecord[] newArray(int size) {

            return new ChatRecord[size];
        }
    };
}
