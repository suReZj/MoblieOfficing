package com.r2.scau.moblieofficing.smack;

import android.util.Log;


import com.r2.scau.moblieofficing.bean.ChatMessage;
import com.r2.scau.moblieofficing.event.MessageEvent;
import com.r2.scau.moblieofficing.gson.GsonUser;
import com.r2.scau.moblieofficing.gson.GsonUsers;
import com.r2.scau.moblieofficing.retrofit.IFriendInfoByPhoneBiz;
import com.r2.scau.moblieofficing.untils.OkHttpUntil;
import com.r2.scau.moblieofficing.untils.UserUntil;

import org.greenrobot.eventbus.EventBus;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.r2.scau.moblieofficing.Contants.SERVER_IP;
import static com.r2.scau.moblieofficing.Contants.getInfo;


/**
 * Smack普通消息监听处理
 *
 * @author: laohu on 2017/1/18
 * @site: http://ittiger.cn
 */
public class SmackChatManagerListener implements ChatManagerListener {
    private static final String PATTERN = "[a-zA-Z0-9_]+@";
    private String mMeNickName = UserUntil.gsonUser.getNickname();

    @Override
    public void chatCreated(Chat chat, boolean createdLocally) {

        chat.addMessageListener(new ChatMessageListener() {

            @Override
            public void processMessage(Chat chat, Message message) {
                //不会收到自己发送过来的消息
                Log.d("get Message", message.toString());
                String from = message.getFrom();//消息发送人，格式:laohu@171.17.100.201/Smack
                if (from.contains("@conference.")) {
                    return;
                }
                String to = message.getTo();//消息接收人(当前登陆用户)，格式:laohu@171.17.100.201/Smack
                Matcher matcherFrom = Pattern.compile(PATTERN).matcher(from);
                Matcher matcherTo = Pattern.compile(PATTERN).matcher(to);
                String id=message.getStanzaId();

                ArrayList<ChatMessage> multiMsg=new ArrayList<>(DataSupport.where("msgid=?",id).find(ChatMessage.class));
                if(multiMsg.size()!=0){
                    return;
                }
                if (matcherFrom.find() && matcherTo.find()) {
                    try {
//                        String id=message.getStanzaId();
                        String fromUser = matcherFrom.group(0);
                        fromUser = fromUser.substring(0, fromUser.length() - 1);//去掉@
                        String toUser = matcherTo.group(0);
                        toUser = toUser.substring(0, toUser.length() - 1);//去掉@
                        JSONObject json = new JSONObject(message.getBody());

                        final ChatMessage chatMessage = new ChatMessage(1, false);
                        chatMessage.setFriendUsername(fromUser);
                        chatMessage.setFriendNickname(json.optString(ChatMessage.KEY_FROM_NICKNAME));
                        chatMessage.setMeUsername(toUser);
                        chatMessage.setMeNickname(mMeNickName);
                        chatMessage.setContent(json.optString(ChatMessage.KEY_MESSAGE_CONTENT));
                        chatMessage.setMulti(false);
                        chatMessage.setMsgID(id);
//                        chatMessage.save();
                        Log.e("sendMessage", "send");
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(SERVER_IP + getInfo + "/")
                                .callFactory(OkHttpUntil.getInstance())
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
                        IFriendInfoByPhoneBiz iFriendInfoBiz = retrofit.create(IFriendInfoByPhoneBiz.class);
                        Call<GsonUsers> call = iFriendInfoBiz.getInfo(chatMessage.getFriendUsername());
                        call.enqueue(new Callback<GsonUsers>() {
                            @Override
                            public void onResponse(Call<GsonUsers> call, Response<GsonUsers> response) {
                                GsonUsers gsonUsers = response.body();
                                if (gsonUsers.getCode() == 200) {
                                    GsonUser user = gsonUsers.getUserInfo();
                                    if (user.getUserHeadPortrait() != null) {
                                        chatMessage.setIconPath(user.getUserHeadPortrait().toString());
                                        Log.e("icon!=null", chatMessage.getIconPath());
                                    } else {
                                        Log.e("icon==null", "icon==null");
                                    }
                                    Log.e("getIcon", "success");
                                } else {
                                    Log.e("getIcon", gsonUsers.getMsg());
                                }
                                EventBus.getDefault().post(new MessageEvent(chatMessage));
                            }
                            @Override
                            public void onFailure(Call<GsonUsers> call, Throwable t) {
                                Log.e("getIcon", "fail");
                            }
                        });
                    } catch (Exception e) {
                        Log.d("get Message", "发送的消息格式不正确");
                    }
                } else {
                    Log.d("get Message", "发送人格式不正确");
                }
            }
        });
    }
}
