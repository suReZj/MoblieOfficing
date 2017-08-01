package com.r2.scau.moblieofficing.fragement;


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
import com.r2.scau.moblieofficing.event.MessageEvent;
import com.r2.scau.moblieofficing.smack.SmackListenerManager;
import com.r2.scau.moblieofficing.smack.SmackManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;
import org.reactivestreams.Subscriber;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.toolbar_message_menu);
        toolbar.setTitle("消息");
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.scan) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            smack = SmackManager.getInstance();
                            smack.login("test1", "test1");
//                    Chat mChat = smack.createChat("test3@192.168.13.30");
                            SmackListenerManager.addGlobalListener();
                        }
                    }).start();
                }return true;
            }
        });
        setHasOptionsMenu(true);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        refreshData();



        recyclerView.setItemAnimator(new DefaultItemAnimator());
        registerForContextMenu(recyclerView);

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
        Log.e("creat","creat");
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
            chatRecord.updateAll("mchatjid=? and mfriendusername=?", chatRecord.getmChatJid(), chatRecord.getmFriendUsername());
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
//        for(int i=0;i<message_adapter.getItemCount();i++){
//            if(message_adapter.getMessageList().get(i).getFriendUsername().equals(message.getFriendUsername())){
//                chatRecord=message_adapter.getMessageList().get(i);
//            }
//            return  chatRecord;
//        }
//        if (mMap.containsKey(message.getFriendUsername())) {
//            chatRecord = message_adapter.getMessageList().get(mMap.get(message.getFriendUsername()));
//        } else {
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
//        message_adapter.add(chatRecord, 0);
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
                chatRecord=message_adapter.getMessageList().get(message_adapter.getmPosition());
                if(chatRecord.isSaved()){
                    chatRecord.setSetTopFlag(true);
                    chatRecord.save();
                    refreshData();
                }
                break;
            case deleteBtn:
//                message_adapter.deleteMsg();
                chatRecord=message_adapter.getMessageList().get(message_adapter.getmPosition());
                if(chatRecord.isSaved()){
//                    chatRecord.setSetTopFlag(true);
//                    chatRecord.save();
//                    refreshData();
                    if (chatRecord.ismIsMulti()){
                        flagOfMulti="1";
                    }else {
                        flagOfMulti="0";
                    }
                    DataSupport.deleteAll(ChatMessage.class,"mfriendusername=? and mismulti=?",chatRecord.getmFriendUsername(),flagOfMulti);
                    chatRecord.delete();
                    refreshData();
                    Log.e("存在","存在");
                }
                break;
            case deleteTopBtn:
                chatRecord=message_adapter.getMessageList().get(message_adapter.getmPosition());
                if(chatRecord.isSaved()){
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
//        if (item.getItemId() == R.id.scan) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    smack = SmackManager.getInstance();
//                    smack.login("test1", "test1");
////                    Chat mChat = smack.createChat("test3@192.168.13.30");
//                    SmackListenerManager.addGlobalListener();
//                }
//            }).start();
//        }
        if (item.getItemId() == R.id.multiChat) {
            LitePal.getDatabase();
        }

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
                .where("settopflag=?","1")
                .order("mchattime desc")
                .find(ChatRecord.class));
        newList=new ArrayList<>(DataSupport.where("mmeusername=?", whereClause)
                .where("settopflag=?","0")
                .order("mchattime desc")
                .find(ChatRecord.class));
        msgList.addAll(newList);
        message_adapter = new MessageAdapter(getContext(), msgList);
        recyclerView.setAdapter(message_adapter);
    }


}
