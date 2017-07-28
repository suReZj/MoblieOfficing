package com.r2.scau.moblieofficing.fragement;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import java.util.ArrayList;
import java.util.HashMap;


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
    private HashMap<String, Integer> mMap = new HashMap<>();//聊天用户的用户名与用户聊天记录Position的映射关系
    private LinearLayoutManager mLayoutManager;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
//        initMsg();


        message_adapter = new MessageAdapter(view.getContext(), msgList);
//        message_adapter.setItemClickListener(new OnRecyclerViewOnClickListener() {
//            @Override
//            public void OnItemClick(View v, int position) {
//
//                Intent intent = new Intent(getActivity(), ChatActivity.class);
//                getActivity().startActivity(intent);
//                Log.d("Aaaa","aaaa");
//            }
//        });
        recyclerView.setAdapter(message_adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        registerForContextMenu(recyclerView);
//        connectButton=(Button)view.findViewById(R.id.first);
//        connectButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SmackManager smack=SmackManager.getInstance();
//                smack.login("12345678900","12345678");
//            }
//        });


        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        message_adapter = null;
//        mMap.clear();
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
        Log.d("chatRecord","chatRecord");
        if (isRemoving() || message_adapter == null) {
            Log.d("chatRecord1","chatRecord1");
            return;
        }

        ChatRecord chatRecord = getChatRecord(message.getChatMessage());
//        chatRecord.setSetTopFlag(false);
        if (chatRecord == null) {//还没有创建此朋友的聊天记录
            Log.d("chatRecord2","chatRecord2");
            chatRecord = new ChatRecord(message.getChatMessage());
            chatRecord.setSetTopFlag(false);
            addChatRecord(chatRecord);
        } else {
            Log.d("chatRecord3","chatRecord3");
            chatRecord.setChatTime(message.getChatMessage().getDatetime());
            chatRecord.setLastMessage(message.getChatMessage().getContent());
            chatRecord.setSetTopFlag(false);
            chatRecord.updateUnReadMessageCount();
            message_adapter.update(chatRecord);
//            DBHelper.getInstance().getSQLiteDB().update(chatRecord);//更新数据库中的记录
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
                if (chatRecord.getMeUsername().equals(message.getMeUsername()) &&
                        chatRecord.getFriendUsername().equals(message.getFriendUsername())) {
//                    mMap.put(message.getFriendUsername(), i);
                    break;
                } else {
                    chatRecord = null;
                }
        }
//    }
        return chatRecord;
    }

    private void addChatRecord(ChatRecord chatRecord) {
        Log.d("add","add");
//        if (message_adapter != null) {
            message_adapter.add(chatRecord, 0);
//            msgList.add(chatRecord);
//        }else {
//            msgList.add(chatRecord);
//            message_adapter = new MessageAdapter(getActivity().getApplicationContext(), msgList);
////        }
//        DBHelper.getInstance().getSQLiteDB().save(chatRecord);
        mLayoutManager.scrollToPosition(0);
//        for (String key : mMap.keySet()) {//创建新的聊天记录之后，需要将之前的映射关系进行更新
//            mMap.put(key, mMap.get(key) + 1);
//            Log.d("key",key);
//        }
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
                message_adapter.setMessageTop();
                break;
            case deleteBtn:
                message_adapter.deleteMsg();
//                mMap.remove(message_adapter.getMessageList().get(message_adapter.getmPosition()).getFriendUsername());
                break;
            case deleteTopBtn:
                message_adapter.deleteTopMsg();
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
            new Thread(new Runnable() {
                @Override
                public void run() {
                    smack = SmackManager.getInstance();
                    smack.login("test", "test");
//                    Chat mChat = smack.createChat("test1@192.168.13.30");
                    SmackListenerManager.addGlobalListener();
                }
            }).start();
        }

        return true;
    }

    private void initMsg() {
        ChatUser chatUser = new ChatUser("sure", "sure");
        chatUser.setMeUsername("子健");
        for (int i = 0; i < 10; i++) {
            ChatRecord msg = new ChatRecord(chatUser);
            msg.setLastMessage("aaaaaaaaaaaaaaa");
            msgList.add(msg);
        }

    }


}
