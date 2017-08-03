package com.r2.scau.moblieofficing.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.r2.scau.moblieofficing.smack.SmackManager;

import java.util.UUID;





public class ChatUser implements Parcelable {
    /**
     *
     */

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


    public ChatUser() {

    }

    /**
     * 单人聊天时使用
     *
     * @param friendUsername
     * @param friendNickname
     */
    public ChatUser(String friendUsername, String friendNickname) {

        this();
        this.uuid = UUID.randomUUID().toString();
        mFriendUsername = friendUsername;
        mFriendNickname = friendNickname;


//        mChatJid = SmackManager.getInstance().getChatJid(mFriendUsername);
//        mFileJid = SmackManager.getInstance().getFileTransferJid(mFriendUsername);

//        User user = LoginHelper.getUser();
//        mMeUsername = user.getUsername();
//        mMeNickname = user.getNickname();
    }

    /**
     * 创建群聊时使用
     *
     * @param friendUsername
     * @param friendNickname
     * @param isMulti
     */
    public ChatUser(String friendUsername, String friendNickname, boolean isMulti) {

        this();
        if(isMulti == false) {
            throw new IllegalArgumentException("multi chat the argument isMulti must be true");
        }
        this.uuid = UUID.randomUUID().toString();
        mFriendUsername = friendUsername;
        mFriendNickname = friendNickname;
        mIsMulti = isMulti;

        mChatJid = friendUsername;

//        User user = LoginHelper.getUser();
//        mMeUsername = user.getUsername();
//        mMeNickname = user.getNickname();
    }

    public String getUuid() {

        return uuid;
    }

    public void setUuid(String uuid) {

        this.uuid = uuid;
    }

    public String getFriendUsername() {

        return mFriendUsername;
    }

    public void setFriendUsername(String friendUsername) {

        mFriendUsername = friendUsername;
    }

    public String getFriendNickname() {

        return mFriendNickname;
    }

    public void setFriendNickname(String friendNickname) {

        mFriendNickname = friendNickname;
    }

    public String getMeUsername() {

        return mMeUsername;
    }

    public void setMeUsername(String meUsername) {

        mMeUsername = meUsername;
    }

    public String getMeNickname() {

        return mMeNickname;
    }

    public void setMeNickname(String meNickname) {

        mMeNickname = meNickname;
    }

    public String getChatJid() {

        return mChatJid;
    }

    public void setChatJid(String chatJid) {

        mChatJid = chatJid;
    }

//    public String getFileJid() {
//
//        return mFileJid;
//    }
//
//    public void setFileJid(String fileJid) {
//
//        mFileJid = fileJid;
//    }

    public boolean isMulti() {

        return mIsMulti;
    }

    public void setMulti(boolean multi) {

        mIsMulti = multi;
    }


    @Override
    public int describeContents() {

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(this.uuid);
        dest.writeString(this.mFriendUsername);
        dest.writeString(this.mFriendNickname);
        dest.writeString(this.mMeUsername);
        dest.writeString(this.mMeNickname);
        dest.writeString(this.mChatJid);
//        dest.writeString(this.mFileJid);
        dest.writeByte(this.mIsMulti ? (byte) 1 : (byte) 0);
    }

    protected ChatUser(Parcel in) {

        this.uuid = in.readString();
        this.mFriendUsername = in.readString();
        this.mFriendNickname = in.readString();
        this.mMeUsername = in.readString();
        this.mMeNickname = in.readString();
        this.mChatJid = in.readString();
//        this.mFileJid = in.readString();
        this.mIsMulti = in.readByte() != 0;
    }

    public static final Creator<ChatUser> CREATOR = new Creator<ChatUser>() {
        @Override
        public ChatUser createFromParcel(Parcel source) {

            return new ChatUser(source);
        }

        @Override
        public ChatUser[] newArray(int size) {

            return new ChatUser[size];
        }
    };
}
