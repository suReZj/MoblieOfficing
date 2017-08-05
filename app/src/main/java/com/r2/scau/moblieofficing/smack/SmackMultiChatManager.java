package com.r2.scau.moblieofficing.smack;


import android.util.Log;

import com.r2.scau.moblieofficing.bean.ChatRecord;
import com.r2.scau.moblieofficing.bean.MultiChatRoom;
import com.r2.scau.moblieofficing.gson.GsonGroup;
import com.r2.scau.moblieofficing.untils.DateUtil;
import com.r2.scau.moblieofficing.untils.UserUntil;

import org.greenrobot.eventbus.EventBus;
import org.jivesoftware.smackx.disco.ServiceDiscoveryManager;
import org.jivesoftware.smackx.disco.packet.DiscoverItems;
import org.jivesoftware.smackx.muc.HostedRoom;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.litepal.crud.DataSupport;
import org.reactivestreams.Subscriber;

import java.util.List;
import java.util.UUID;

import io.reactivex.Observable;


/**
 * 群聊管理类
 *
 * @author: laohu on 2017/2/3
 * @site: http://ittiger.cn
 */
public class SmackMultiChatManager {


    public static void saveMultiChat(MultiUserChat multiUserChat) {
        MultiChatRoom multiRoom = new MultiChatRoom(multiUserChat.getRoom());
        multiRoom.save();
    }

    public static void bindJoinMultiChat() throws Exception {

//        Observable.create(new Observable.OnSubscribe<List<HostedRoom>>() {
//            @Override
//            public void call(Subscriber<? super List<HostedRoom>> subscriber) {
//
//                try {
        List<HostedRoom> rooms = SmackManager.getInstance().getHostedRooms();

//                    subscriber.onNext(rooms);
//                    subscriber.onCompleted();
//                } catch (Exception e) {
//                    subscriber.onError(e);
//                }
//            }
//        })
//        .subscribeOn(Schedulers.io())
//        .observeOn(Schedulers.io())
//        .doOnError(new Action1<Throwable>() {
//            @Override
//            public void call(Throwable throwable) {
//
//                Logger.e(throwable, "bind join multi chat failure");
//            }
//        })
//        .subscribe(new Action1<List<HostedRoom>>() {
//            @Override
//            public void call(List<HostedRoom> hostedRooms) {






//        List<MultiChatRoom>  multiChats = DataSupport.findAll(MultiChatRoom.class);
//        if(multiChats!=null){
//            DataSupport.deleteAll(MultiChatRoom.class);
//        }
//        multiChats=null;
//        List<GsonGroup> groupList=UserUntil.joinGroupList;
//        for(int i=0;i<groupList.size();i++){
//            MultiChatRoom newRoom=new MultiChatRoom(groupList.get(i).getGname()+"@conference."+SmackManager.SERVER_IP);
//            newRoom.save();
//            multiChats.add(newRoom);
//        }
//        List<GsonGroup> createGroupList=UserUntil.createGroupList;
//        for(int i=0;i<createGroupList.size();i++){
//            MultiChatRoom newRoom=new MultiChatRoom(createGroupList.get(i).getGname()+"@conference."+SmackManager.SERVER_IP);
//            newRoom.save();
//            multiChats.add(newRoom);
//        }



        List<MultiChatRoom>  multiChats = DataSupport.findAll(MultiChatRoom.class);
        for (HostedRoom room : rooms) {
            ServiceDiscoveryManager discoManager = SmackManager.getInstance().getServiceDiscoveryManager();
            // 获得指定XMPP实体的项目
            // 这个例子获得与在线目录服务相关的项目
            try {
                //获得未读消息
                DiscoverItems discoItems = discoManager.discoverItems(room.getJid());
                // 获得被查询的XMPP实体的要查看的项目
                List<DiscoverItems.Item> listItems = discoItems.getItems();//获得用户创建的群聊
                if (listItems.size() > 0) {
                    for (MultiChatRoom chatRoom : multiChats) {
                        int idx = listItems.indexOf(chatRoom);
                        if (idx != -1) {
                            try {
                                MultiUserChat multiUserChat = SmackManager.getInstance().getMultiChat(chatRoom.getRoomJid());
                                multiUserChat.join(UserUntil.gsonUser.getNickname());
                                SmackListenerManager.addMultiChatMessageListener(multiUserChat);
                                ChatRecord record;
                                List<ChatRecord> chatRecords = DataSupport.where("mfriendusername=?", chatRoom.getRoomJid()).find(ChatRecord.class);
                                if (chatRecords.size() == 0) {
                                    record = new ChatRecord();
                                    String friendUserName = chatRoom.getRoomJid();
                                    int position = friendUserName.indexOf("@conference.");
                                    String friendNickName = friendUserName.substring(0, position);
                                    record.setUuid(UUID.randomUUID().toString());
                                    record.setmFriendUsername(friendUserName);
                                    record.setmFriendNickname(friendNickName);
                                    record.setmMeUsername(UserUntil.gsonUser.getUserPhone());
                                    record.setmMeNickname(UserUntil.gsonUser.getNickname());
                                    record.setmChatTime(DateUtil.currentDatetime());
                                    record.setmIsMulti(true);
                                    record.setmChatJid(friendUserName);
//                                    record.save();
                                } else {
                                    record = chatRecords.get(0);
                                }
                                Log.e("bindJoinMultiChat","bindJoinMultiChat");
//                                EventBus.getDefault().post(record);
                            } catch (Exception e) {
                                Log.e(e.toString(), "join room %s failure");
                            }
                        } else {
//                            DBHelper.getInstance().getSQLiteDB().delete(chatRoom);//服务器上没有此群聊
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }
}
