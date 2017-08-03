package com.r2.scau.moblieofficing.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.r2.scau.moblieofficing.untils.DateUtil;
import com.r2.scau.moblieofficing.smack.SmackManager;

import org.litepal.crud.DataSupport;


public class ChatRecord extends DataSupport implements Parcelable{
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
    private int mUnReadMessageCount=0;

    private Boolean setTopFlag=false;//设置是否置顶
    private String uuid;
    /**
     * 聊天好友的用户名，群聊时为群聊MultiUserChat的room，即jid,格式为：老胡创建的群@conference.121.42.13.79
     */
    private String mFriendUsername;
    /**
     * 聊天好友的昵称
     */
    private String mFriendNickname;
    /**
     * 自己的用户名
     */
    private String mMeUsername;
    /**
     * 自己的昵称
     */
    private String mMeNickname;
    /**
     * 聊天JID，群聊时为群聊jid
     */
    private String mChatJid;
//    /**
//     * 聊天时文件发送JID
//     */
//    private String mFileJid;
    /**
     * 是否为群聊信息
     */
    private boolean mIsMulti = false;

    public ChatRecord(ChatUser chatUser) {

        //减少服务请求
        setmFriendUsername(chatUser.getFriendUsername());
        setmFriendNickname(chatUser.getFriendNickname());
        setmMeNickname(chatUser.getMeNickname());
        setmMeUsername(chatUser.getMeUsername());
        setmChatJid(chatUser.getChatJid());
//        setFileJid(chatUser.getFileJid());
        setUuid(chatUser.getUuid());
        setmChatTime(DateUtil.currentDatetime());
        setmIsMulti(chatUser.isMulti());
    }

    public ChatRecord(ChatMessage chatMessage) {

        setmFriendUsername(chatMessage.getFriendUsername());
        setmMeUsername(chatMessage.getMeUsername());
        setmMeNickname(chatMessage.getMeNickname());

        if(chatMessage.isMulti()) {//群发
            int idx = chatMessage.getFriendUsername().indexOf("@conference.");
            String friendNickName = chatMessage.getFriendUsername().substring(0, idx);
            setmFriendNickname(friendNickName);//群聊记录显示群聊名称
            setmChatJid(chatMessage.getFriendUsername());
            setmIsMulti(chatMessage.isMulti());
        } else {
        setmFriendNickname(chatMessage.getFriendNickname());
        String chatJid = SmackManager.getInstance().getChatJid(chatMessage.getFriendUsername());
//            String fileJid = SmackManager.getInstance().getFileTransferJid(chatJid);
        setmChatJid(chatJid);
//            setFileJid(fileJid);
        }

        setmChatTime(chatMessage.getDatetime());
        setmLastMessage(chatMessage.getContent());
        setUuid(chatMessage.getUuid());
        updateUnReadMessageCount();
    }

    public String getmChatTime() {
        return mChatTime;
    }

    public String getmFriendAvatar() {
        return mFriendAvatar;
    }

    public String getmLastMessage() {
        return mLastMessage;
    }

    public int getmUnReadMessageCount() {
        return mUnReadMessageCount;
    }

    public Boolean getSetTopFlag() {
        return setTopFlag;
    }

    public String getUuid() {
        return uuid;
    }

    public String getmFriendUsername() {
        return mFriendUsername;
    }

    public String getmFriendNickname() {
        return mFriendNickname;
    }

    public String getmMeUsername() {
        return mMeUsername;
    }

    public String getmMeNickname() {
        return mMeNickname;
    }

    public String getmChatJid() {
        return mChatJid;
    }

    public boolean ismIsMulti() {
        return mIsMulti;
    }

    public void setmChatTime(String mChatTime) {
        this.mChatTime = mChatTime;
    }

    public void setmFriendAvatar(String mFriendAvatar) {
        this.mFriendAvatar = mFriendAvatar;
    }

    public void setmLastMessage(String mLastMessage) {
        this.mLastMessage = mLastMessage;
    }

    public void setmUnReadMessageCount() {
        mUnReadMessageCount =0;
    }

    public void setSetTopFlag(Boolean setTopFlag) {
        this.setTopFlag = setTopFlag;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setmFriendUsername(String mFriendUsername) {
        this.mFriendUsername = mFriendUsername;
    }

    public void setmFriendNickname(String mFriendNickname) {
        this.mFriendNickname = mFriendNickname;
    }

    public void setmMeUsername(String mMeUsername) {
        this.mMeUsername = mMeUsername;
    }

    public void setmMeNickname(String mMeNickname) {
        this.mMeNickname = mMeNickname;
    }

    public void setmChatJid(String mChatJid) {
        this.mChatJid = mChatJid;
    }

    public void setmIsMulti(boolean mIsMulti) {
        this.mIsMulti = mIsMulti;
    }

    public void updateUnReadMessageCount() {
//        int i=Integer.parseInt(this.mUnReadMessageCount);
//        Log.d("updateUnRead",this.mUnReadMessageCount);
//        i=i+1;
//        mUnReadMessageCount=i+"";
        mUnReadMessageCount=mUnReadMessageCount+1;
    }
    public void setUnReadMsgZero(){
        mUnReadMessageCount=0;
    }

//    @Override
//    public boolean equals(Object obj) {
//
//        if (obj == null) {
//            return false;
//        }
//        if (obj instanceof ChatRecord) {
//            return this.getUuid().equals(((ChatRecord) obj).getUuid());
//        }
//        return false;
//    }


    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(this.uuid);
        dest.writeString(this.mFriendUsername);
        dest.writeString(this.mFriendNickname);
        dest.writeString(this.mMeUsername);
        dest.writeString(this.mMeNickname);
        dest.writeString(this.mChatJid);
//        dest.writeString(this.mFileJid);
        dest.writeByte(this.mIsMulti ? (byte) 1 : (byte) 0);
        dest.writeString(this.mChatTime);
        dest.writeString(this.mFriendAvatar);
        dest.writeString(this.mLastMessage);
        dest.writeInt(this.getmUnReadMessageCount());
    }

    public ChatRecord() {

    }

    protected ChatRecord(Parcel in) {

        this.uuid = in.readString();
        this.mFriendUsername = in.readString();
        this.mFriendNickname = in.readString();
        this.mMeUsername = in.readString();
        this.mMeNickname = in.readString();
        this.mChatJid = in.readString();
//        this.mFileJid = in.readString();
        this.mIsMulti = in.readByte() != 0;
        this.mChatTime = in.readString();
        this.mFriendAvatar = in.readString();
        this.mLastMessage = in.readString();
        this.mUnReadMessageCount =in.readInt();
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

    @Override
    public int describeContents() {
        return 0;
    }
}
