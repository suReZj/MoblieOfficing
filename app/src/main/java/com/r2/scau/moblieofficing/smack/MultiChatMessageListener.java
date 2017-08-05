package com.r2.scau.moblieofficing.smack;



import android.util.Log;

import com.r2.scau.moblieofficing.bean.ChatMessage;
import com.r2.scau.moblieofficing.bean.ChatRecord;
import com.r2.scau.moblieofficing.bean.MultiChatMessage;
import com.r2.scau.moblieofficing.event.MessageEvent;
import com.r2.scau.moblieofficing.untils.UserUntil;

import org.greenrobot.eventbus.EventBus;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * 多人聊天消息监听
 * @author: laohu on 2017/1/24
 * @site: http://ittiger.cn
 */
public class MultiChatMessageListener implements MessageListener {
    private static final String PATTERN = "[a-zA-Z0-9_]+@";
//    private String mMeNickName = LoginHelper.getUser().getNickname();
//    private String mMeUserName = LoginHelper.getUser().getUsername();
    private String mMeNickName = UserUntil.gsonUser.getNickname();
    private String mMeUserName = UserUntil.gsonUser.getUserPhone();
    @Override
    public void processMessage(Message message) {

        //不会收到自己发送过来的消息
        Log.e("message",message.toString());
        String from = message.getFrom();//消息发送人，格式:老胡创建的群@conference.121.42.13.79/老胡     --> 老胡发送的
        String to = message.getTo();//消息接收人(当前登陆用户)，格式:zhangsan@121.42.13.79/Smack
        String id=message.getStanzaId();
        Matcher matcherTo = Pattern.compile(PATTERN).matcher(to);
//        Log.e(from,to);
        String[] fromUsers = from.split("/");
        String friendUserName = fromUsers[0];//老胡创建的群@conference.121.42.13.79
        String friendNickName = fromUsers[1];//发送人的昵称，用于聊天窗口中显示
        JSONObject json = null;
        String sendUser=null;
        try {
            json = new JSONObject(message.getBody());
            sendUser = json.optString(ChatMessage.KEY_MULTI_CHAT_SEND_USER);
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        ArrayList<MultiChatMessage> multiMsgList = new ArrayList<>(DataSupport.where("chatroom=? and msgid=?",friendUserName,id).find(MultiChatMessage.class));
//        if(multiMsgList.size()==0){
//            MultiChatMessage newMultiMsg=new MultiChatMessage(id,friendUserName);
//            newMultiMsg.save();
//        }else {
//            return;
//        }
        ArrayList<ChatMessage> multiMsg=new ArrayList<>(DataSupport.where("msgid=?",id).find(ChatMessage.class));
        if(multiMsg.size()!=0){
            return;
        }

        if((matcherTo.find())&&(!sendUser.equals(mMeUserName))) {//判断是不是自己发送的

            try {
                 fromUsers = from.split("/");
                 friendUserName = fromUsers[0];//老胡创建的群@conference.121.42.13.79
                 friendNickName = fromUsers[1];//发送人的昵称，用于聊天窗口中显示


                ChatMessage chatMessage = new ChatMessage(0, false);
                chatMessage.setFriendUsername(friendUserName);
                chatMessage.setFriendNickname(friendNickName);
                chatMessage.setMeUsername(mMeUserName);
                chatMessage.setMeNickname(mMeNickName);
                chatMessage.setContent(json.optString(ChatMessage.KEY_MESSAGE_CONTENT));
                chatMessage.setMultiUserName(json.optString(ChatMessage.KEY_MULTI_CHAT_SEND_USER));

                sendUser = json.optString(ChatMessage.KEY_MULTI_CHAT_SEND_USER);
                chatMessage.setMeSend(mMeUserName.equals(sendUser));
                chatMessage.setMulti(true);
                chatMessage.setMsgID(id);
                ArrayList<ChatRecord> chatMessageList = new ArrayList<>(DataSupport.where("mfriendusername=?", friendUserName).find(ChatRecord.class));
                if(chatMessageList.size()!=0){
                    chatMessage.setUuid(chatMessageList.get(0).getUuid());
                }else {
                    chatMessage.setUuid(UUID.randomUUID().toString());
                }
//                chatMessage.save();
                EventBus.getDefault().post(new MessageEvent(chatMessage));
                Log.e("发送的消息格式不正确","发送的消息格式不正确");
            } catch (Exception e) {
                Log.e("发送的消息格式不正确",e.toString());
            }
        } else {
            Log.e("发送人格式不正确","发送人格式不正确");
        }
    }
}
