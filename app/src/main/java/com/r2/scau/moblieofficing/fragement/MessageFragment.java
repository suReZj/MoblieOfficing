package com.r2.scau.moblieofficing.fragement;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.r2.scau.moblieofficing.R;
import com.r2.scau.moblieofficing.activity.ChatActivity;
import com.r2.scau.moblieofficing.adapter.MessageAdapter;
import com.r2.scau.moblieofficing.bean.ChatMessage;
import com.r2.scau.moblieofficing.bean.ChatRecord;
import com.r2.scau.moblieofficing.bean.ChatUser;
import com.r2.scau.moblieofficing.bean.MultiChatRoom;
import com.r2.scau.moblieofficing.event.MessageEvent;
import com.r2.scau.moblieofficing.smack.SmackListenerManager;
import com.r2.scau.moblieofficing.smack.SmackManager;
import com.r2.scau.moblieofficing.smack.SmackMultiChatManager;
import com.r2.scau.moblieofficing.untils.DateUtil;

import com.r2.scau.moblieofficing.untils.OkHttpUntil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import org.jivesoftware.smackx.muc.MultiUserChat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;
import org.litepal.crud.DataSupport;
import org.reactivestreams.Subscriber;

import java.io.IOException;
import java.util.ArrayList;

import java.util.List;
import java.util.UUID;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import okhttp3.Response;




/**
 * Created by 嘉进 on 9:20.
 * 底部导航栏中消息的Fragment
 */

public class MessageFragment extends Fragment {
    private ArrayList<ChatRecord> msgList = new ArrayList<>();
    private MessageAdapter message_adapter;
    final private int setTopBtn = 0;
    final private int deleteBtn = 1;
    final private int deleteTopBtn = 2;
    private SmackManager smack;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView recyclerView;
    private ChatRecord chatRecord;
    private ArrayList<ChatRecord> newList;
    private String flagOfMulti;
    private ArrayList<String> roomNameList;
    private OkHttpClient  okHttpClient = new OkHttpClient();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.toolbar_message_menu);
        toolbar.setTitle("消息");
        setHasOptionsMenu(true);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        refreshData();

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        registerForContextMenu(recyclerView);



        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getItemId() == R.id.scan) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            smack = SmackManager.getInstance();
                            smack.login("sure3", "sure3");
//                    Chat mChat = smack.createChat("test3@192.168.13.30");
                            try {
                                SmackListenerManager.addGlobalListener();


                                //step 1: 同样的需要创建一个OkHttpClick对象
                                //step 2: 创建  FormBody.Builder
                                FormBody formBody = new FormBody.Builder()
                                        .add("userPhone", "sure3")
                                        .build();
                                //step 3: 创建请求
                                final Request request = new Request.Builder().url("http://192.168.13.57:8089/group/getAllGroupByUser.shtml")
                                        .post(formBody)
                                        .addHeader("cookie", OkHttpUntil.loginSessionID)
                                        .build();
                                //step 4： 建立联系 创建Call对象
                                okHttpClient.newCall(request).enqueue(new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        // TODO: 17-1-4  请求失败
                                        Log.e("register", "fail");
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        // TODO: 17-1-4 请求成功
                                        String str = response.body().string();
                                        Log.e("register", str);
                                    }
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
                if (item.getItemId() == R.id.multiChat) {
                    Log.e("发起群聊", "发起群聊");
                    String chatRoomName = String.format("%s", "dasdsadsadsa");
                    String reason = String.format("%s邀请你入群", "dasdsadsadsa");
                    try {
                        MultiUserChat multiUserChat = SmackManager.getInstance().createChatRoom(chatRoomName, "dasdsadsadsa", null);
                        String jid1 = SmackManager.getInstance().getFullJid("sure1");
                        multiUserChat.invite(jid1, reason);//邀请入群
                        String jid2 = SmackManager.getInstance().getFullJid("sure2");
                        multiUserChat.invite(jid2, reason);//邀请入群
                        String jid3 = SmackManager.getInstance().getFullJid("sure3");
                        multiUserChat.invite(jid3, reason);//邀请入群

                        SmackListenerManager.addMultiChatMessageListener(multiUserChat);
                        SmackMultiChatManager.saveMultiChat(multiUserChat);
                        startMultiChat(getActivity().getApplicationContext(), multiUserChat);

//                        FormBody formBody = new FormBody.Builder()
//                                .add("userPhone", "sure1")
//                                .add("groupName","dasdsadsadsa")
//                                .build();
//                        step 3: 创建请求
//                         Request request = new Request.Builder().url("http://192.168.13.57:8089/group/createGroup.shtml")
//                                .post(formBody)
//                                .addHeader("cookie", OkHttpClientManager.loginSessionID)
//                                .build();
//
//                        step 4： 建立联系 创建Call对象
//                        okHttpClient.newCall(request).enqueue(new Callback() {
//                            @Override
//                            public void onFailure(Call call, IOException e) {
//                                 TODO: 17-1-4  请求失败
//                                Log.e("register", "fail");
//                            }

//                            @Override
//                            public void onResponse(Call call, Response response) throws IOException {
//                                 TODO: 17-1-4 请求成功
//                                String str = response.body().string();
//                                Log.e("register", str);
//                            }
//                        });


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if(item.getItemId()==R.id.addFriend){
//                    FormBody formBody = new FormBody.Builder()
//                            .add("groupCreatedUserPhone", "sure1")
//                            .add("groupName","dasdsadsadsa")
//                            .add("userPhone","sure3")
//                            .build();
                    //step 3: 创建请求
//                    Request  request = new Request.Builder().url("http://192.168.13.57:8089/group/joinGroup.shtml")
//                            .post(formBody)
//                            .addHeader("cookie", OkHttpClientManager.loginSessionID)
//                            .build();
//
                    //step 4： 建立联系 创建Call对象
//                    okHttpClient.newCall(request).enqueue(new Callback() {
//                        @Override
//                        public void onFailure(Call call, IOException e) {
//                             TODO: 17-1-4  请求失败
//                            Log.e("register", "fail");
//                        }
//
//                        @Override
//                        public void onResponse(Call call, Response response) throws IOException {
                            // TODO: 17-1-4 请求成功
//                            String str = response.body().string();
//                            Log.e("register", str);
//                        }
//                    });
                }
                if (item.getItemId()==R.id.cloud){
                    String roomName="dasdsadsadsa@conference.192.168.13.57";
                    ChatRecord record;
                    List<ChatRecord> chatRecords = DataSupport.where("mfriendusername=?", roomName).find(ChatRecord.class);
                    if (chatRecords.size() == 0) {
                        Log.e("aaaaaaaaaa","bbbbbbbbbbbb");
                        record = new ChatRecord();
                        String friendUserName = roomName;
                        int idx = friendUserName.indexOf("@conference.");
                        String friendNickName = friendUserName.substring(0, idx);
                        record.setUuid(UUID.randomUUID().toString());
                        record.setmFriendUsername(friendUserName);
                        record.setmFriendNickname(friendNickName);
                        record.setmMeUsername("sure3");
                        record.setmMeNickname("sure3");
                        record.setmChatTime(DateUtil.currentDatetime());
                        record.setmIsMulti(true);
                        record.save();
                        SmackManager.getInstance().joinChatRoom("dasdsadsadsa@conference.192.168.13.57","sure3",null);
                    } else {
                        record = chatRecords.get(0);
                    }
                    EventBus.getDefault().post(record);
            }

//
                return true;
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        Log.e("creat", "creat");
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        message_adapter = null;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChatRecordEvent(ChatRecord event) {
        //向其他人发起聊天时接收到的事件
        if (isRemoving() || message_adapter == null) {
            return;
        }
        if (message_adapter.getMessageList().indexOf(event) > -1) {
            return;//已经存在此人的聊天窗口记录
        }
        addChatRecord(event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveChatMessageEvent(MessageEvent message) {
        //收到发送的消息时接收到的事件(包括别人发送的和自己发送的消息)
        Log.d("chatRecord", "chatRecord");
        if (isRemoving() || message_adapter == null) {
            return;
        }
        ChatRecord chatRecord = getChatRecord(message.getChatMessage());
//        chatRecord.setSetTopFlag(false);
        if (chatRecord == null) {//还没有创建此朋友的聊天记录
            Log.d("chatRecord1", "chatRecord1");
            chatRecord = new ChatRecord(message.getChatMessage());
            chatRecord.setSetTopFlag(false);
            addChatRecord(chatRecord);
        } else {
            Log.d("chatRecord2", "chatRecord2");
            chatRecord.setmChatTime(message.getChatMessage().getDatetime());
            chatRecord.setmLastMessage(message.getChatMessage().getContent());
            chatRecord.setSetTopFlag(false);
            if (message.getChatMessage().isMeSend()) {
//                chatRecord.updateUnReadMessageCount();
            } else {
                chatRecord.updateUnReadMessageCount();
            }
//            message_adapter.update(chatRecord);
            if (!message.getChatMessage().isMulti()) {
                chatRecord.updateAll("mchatjid=? and mfriendusername=?", chatRecord.getmChatJid(), chatRecord.getmFriendUsername());
            }else {
                chatRecord.updateAll("uuid=? and mfriendusername=?",chatRecord.getUuid(),chatRecord.getmFriendUsername());
            }
            refreshData();
        }
    }


    /**
     * 根据消息获取聊天记录窗口对象
     *
     * @param message
     * @return
     */
    private ChatRecord getChatRecord(ChatMessage message) {

        ChatRecord chatRecord = null;
        for (int i = 0; i < message_adapter.getMessageList().size(); i++) {
            chatRecord = message_adapter.getMessageList().get(i);
            if (chatRecord.getmMeUsername().equals(message.getMeUsername()) &&
                    chatRecord.getmFriendUsername().equals(message.getFriendUsername())) {
                return chatRecord;
            } else {
                chatRecord = null;
            }
        }
//    }
        return chatRecord;
    }

    private void addChatRecord(ChatRecord chatRecord) {
        chatRecord.save();
        refreshData();
        mLayoutManager.scrollToPosition(0);
    }


    //创建contextmenu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (msgList.get(message_adapter.getmPosition()).getSetTopFlag()) {
            menu.add(Menu.NONE, deleteTopBtn, Menu.NONE, "取消置顶");//groupId, itemId, order, title
            menu.add(Menu.NONE, deleteBtn, Menu.NONE, "删除");
        } else {
            menu.add(Menu.NONE, setTopBtn, Menu.NONE, "置顶");//groupId, itemId, order, title
            menu.add(Menu.NONE, deleteBtn, Menu.NONE, "删除");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case setTopBtn:
                chatRecord = message_adapter.getMessageList().get(message_adapter.getmPosition());
                if (chatRecord.isSaved()) {
                    chatRecord.setSetTopFlag(true);
                    chatRecord.save();
                    refreshData();
                }
                break;
            case deleteBtn:
//                message_adapter.deleteMsg();
                chatRecord = message_adapter.getMessageList().get(message_adapter.getmPosition());
                if (chatRecord.isSaved()) {
//                    chatRecord.setSetTopFlag(true);
//                    chatRecord.save();
//                    refreshData();
                    if (chatRecord.ismIsMulti()) {
                        flagOfMulti = "1";
                    } else {
                        flagOfMulti = "0";
                    }
                    DataSupport.deleteAll(ChatMessage.class, "mfriendusername=? and mismulti=?", chatRecord.getmFriendUsername(), flagOfMulti);
                    chatRecord.delete();
                    refreshData();
                }
                break;
            case deleteTopBtn:
                chatRecord = message_adapter.getMessageList().get(message_adapter.getmPosition());
                if (chatRecord.isSaved()) {
                    chatRecord.setSetTopFlag(false);
                    chatRecord.save();
                    refreshData();
                }
        }
        return true;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.toolbar_message_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.scan) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    smack = SmackManager.getInstance();
//                    smack.login("test1", "test1");
////                    Chat mChat = smack.createChat("test3@192.168.13.30");
//                    SmackListenerManager.addGlobalListener();
//                }
//            }).start();
        }
//        if (item.getItemId() == R.id.multiChat) {
//            Log.e("发起群聊", "发起群聊");
//            String chatRoomName = String.format("%s创建的群", "张大爷");
//            String reason = String.format("%s邀请你入群", "张大爷");
//            try {
//                MultiUserChat multiUserChat = SmackManager.getInstance().createChatRoom(chatRoomName, "张大爷", null);
//                String jid1 = SmackManager.getInstance().getFullJid("test");
//                multiUserChat.invite(jid1, reason);//邀请入群
//                String jid2 = SmackManager.getInstance().getFullJid("test3");
//                multiUserChat.invite(jid2, reason);//邀请入群
//
//                SmackListenerManager.addMultiChatMessageListener(multiUserChat);
//                SmackMultiChatManager.saveMultiChat(multiUserChat);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }

        return true;
    }


    public void refreshData() {

//    Observable.create(new Observable.OnSubscribe<List<ChatRecord>>() {
//        @Override
//        public void call(Subscriber<? super List<ChatRecord>> subscriber) {
//
//            List<ChatRecord> list = DBQueryHelper.queryChatRecord();
//            subscriber.onNext(list);
//            subscriber.onCompleted();
//        }
//    })
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .doOnError(new Action1<Throwable>() {
//                @Override
//                public void call(Throwable throwable) {
////
////                    refreshFailed();
////                    Logger.e(throwable, "get chat record failure");
//                }
//            })
//            .subscribe(new Action1<List<ChatRecord>>() {
//                @Override
//                public void call(List<ChatRecord> chatRecords) {
//
////                    mAdapter = new ChatRecordAdapter(mContext, chatRecords);
////                    mRecyclerView.setAdapter(mAdapter);
////                    refreshSuccess();
//                }
//            });


        //我的用户名
        String whereClause = "test1";
//    String[] whereArgs = {LoginHelper.getUser().getUsername()};
        msgList = new ArrayList<>(DataSupport.where("mmeusername=?", whereClause)
                .where("settopflag=?", "1")
                .order("mchattime desc")
                .find(ChatRecord.class));
        newList = new ArrayList<>(DataSupport.where("mmeusername=?", whereClause)
                .where("settopflag=?", "0")
                .order("mchattime desc")
                .find(ChatRecord.class));
        msgList.addAll(newList);
        message_adapter = new MessageAdapter(getContext(), msgList);
        recyclerView.setAdapter(message_adapter);
    }


    public void startMultiChat(Context context, MultiUserChat multiUserChat) {
        ChatRecord record;
        List<ChatRecord> chatRecords = DataSupport.where("mfriendusername=?", multiUserChat.getRoom()).find(ChatRecord.class);
        if (chatRecords.size() == 0) {
            record = new ChatRecord();
            String friendUserName = multiUserChat.getRoom();
            int idx = friendUserName.indexOf("@conference.");
            String friendNickName = friendUserName.substring(0, idx);
            record.setUuid(UUID.randomUUID().toString());
            record.setmFriendUsername(friendUserName);
            record.setmFriendNickname(friendNickName);
            record.setmMeUsername("sure3");
            record.setmMeNickname("张大爷");
            record.setmChatTime(DateUtil.currentDatetime());
            record.setmIsMulti(true);
            record.save();

        } else {
            record = chatRecords.get(0);
        }
        EventBus.getDefault().post(record);
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("chatrecord", record);
        startActivity(intent);
    }

    //单人聊天离线消息接受
    public void getUnReanMsg(){

    }


//检查离线期间加入的群聊
public void checkMultiInvite(){
    String roomName="dasdsadsadsa@conference.192.168.13.57";
    ChatRecord record;
    List<ChatRecord> chatRecords = DataSupport.where("mfriendusername=?", roomName).find(ChatRecord.class);
    if (chatRecords.size() == 0) {
        record = new ChatRecord();
        String friendUserName = roomName;
        int idx = friendUserName.indexOf("@conference.");
        String friendNickName = friendUserName.substring(0, idx);
        record.setUuid(UUID.randomUUID().toString());
        record.setmFriendUsername(friendUserName);
        record.setmFriendNickname(friendNickName);
        record.setmMeUsername("sure3");
        record.setmMeNickname("sure3");
        record.setmChatTime(DateUtil.currentDatetime());
        record.setmIsMulti(true);
        record.save();
        MultiUserChat multiChatRoom=SmackManager.getInstance().getMultiChat(roomName);
        SmackListenerManager.addMultiChatMessageListener(multiChatRoom);
        SmackManager.getInstance().joinChatRoom("dasdsadsadsa@conference.192.168.13.57","sure3",null);
    } else {
        record = chatRecords.get(0);
    }
    EventBus.getDefault().post(record);
}


}
