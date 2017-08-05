package com.r2.scau.moblieofficing.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.r2.scau.moblieofficing.untils.DateUtil;

import org.litepal.crud.DataSupport;

import java.util.Date;
import java.util.UUID;


public class ChatMessage extends DataSupport implements Parcelable {
    public static final String KEY_FROM_NICKNAME = "fromNickName";
    public static final String KEY_MESSAGE_CONTENT = "messageContent";
    public static final String KEY_MULTI_CHAT_SEND_USER = "multiChatSendUser";
    private String msgID;
    /**
     *
     */
    private String uuid;
    /**
     * 消息内容
     */
    private String mContent;
    /**
     * 消息类型
     */
    private int mMessageType;
    /**
     * 聊天好友的用户名,群聊时为群聊的jid,格式为：老胡创建的群@conference.121.42.13.79
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
     * 消息发送接收的时间
     */
    private String mDatetime;
    /**
     * 当前消息是否是自己发出的
     */
    private boolean mIsMeSend;

    private String multiUserName=null;

    public String getMultiUserName() {
        return multiUserName;
    }

    public void setMultiUserName(String multiUserName) {
        this.multiUserName = multiUserName;
    }
    //    /**
//     * 接收的图片或语音路径
//     */
//    private String mFilePath;
//    /**
//     * 文件加载状态
//     */
//    private int mFileLoadState = FileLoadState.STATE_LOAD_START.value();


    /**
     * 是否为群聊记录
     */
    private boolean mIsMulti = false;

    public ChatMessage() {

    }
    public String getMsgID() {
        return msgID;
    }

    public void setMsgID(String msgID) {
        this.msgID = msgID;
    }

    public ChatMessage(int messageType, boolean isMeSend) {

        mMessageType = messageType;
        mIsMeSend = isMeSend;

        this.uuid = UUID.randomUUID().toString();
        this.mDatetime = DateUtil.formatDatetime(new Date());
    }

    public String getContent() {

        return mContent;
    }

    public void setContent(String content) {

        mContent = content;
    }

    public int getMessageType() {

        return mMessageType;
    }

    public void setMessageType(int messageType) {

        mMessageType = messageType;
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

    public String getDatetime() {

        return mDatetime;
    }

    public void setDatetime(String datetime) {

        mDatetime = datetime;
    }

    public boolean isMeSend() {

        return mIsMeSend;
    }

    public void setMeSend(boolean meSend) {

        mIsMeSend = meSend;
    }

//    public String getFilePath() {
//
//        return mFilePath;
//    }
//
//    public void setFilePath(String filePath) {
//
//        mFilePath = filePath;
//    }
//
//    public int getFileLoadState() {
//
//        return mFileLoadState;
//    }
//
//    public void setFileLoadState(int fileLoadState) {
//
//        mFileLoadState = fileLoadState;
//    }

    public String getUuid() {

        return uuid;
    }

    public void setUuid(String uuid) {

        this.uuid = uuid;
    }

    public boolean isMulti() {

        return mIsMulti;
    }

    public void setMulti(boolean multi) {

        mIsMulti = multi;
    }

    @Override
    public boolean equals(Object o) {

        if (o == null) {
            return false;
        }
        if (o instanceof ChatMessage) {
            return uuid.equals(((ChatMessage) o).uuid);
        }
        return false;
    }


    @Override
    public int describeContents() {

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(this.uuid);
        dest.writeString(this.mContent);
        dest.writeInt(this.mMessageType);
        dest.writeString(this.mFriendUsername);
        dest.writeString(this.mFriendNickname);
        dest.writeString(this.mMeUsername);
        dest.writeString(this.mMeNickname);
        dest.writeString(this.mDatetime);
        dest.writeByte(this.mIsMeSend ? (byte) 1 : (byte) 0);
//        dest.writeString(this.mFilePath);
//        dest.writeInt(this.mFileLoadState);
        dest.writeByte(this.mIsMulti ? (byte) 1 : (byte) 0);
    }

    protected ChatMessage(Parcel in) {

        this.uuid = in.readString();
        this.mContent = in.readString();
        this.mMessageType = in.readInt();
        this.mFriendUsername = in.readString();
        this.mFriendNickname = in.readString();
        this.mMeUsername = in.readString();
        this.mMeNickname = in.readString();
        this.mDatetime = in.readString();
        this.mIsMeSend = in.readByte() != 0;
//        this.mFilePath = in.readString();
//        this.mFileLoadState = in.readInt();
        this.mIsMulti = in.readByte() != 0;
    }

    public static final Creator<ChatMessage> CREATOR = new Creator<ChatMessage>() {
        @Override
        public ChatMessage createFromParcel(Parcel source) {

            return new ChatMessage(source);
        }

        @Override
        public ChatMessage[] newArray(int size) {

            return new ChatMessage[size];
        }
    };
}
