package com.r2.scau.moblieofficing.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.r2.scau.moblieofficing.R;
import com.r2.scau.moblieofficing.adapter.ChatMessageAdapter;
import com.r2.scau.moblieofficing.bean.ChatMessage;
import com.r2.scau.moblieofficing.bean.ChatRecord;
import com.r2.scau.moblieofficing.event.MessageEvent;
import com.r2.scau.moblieofficing.smack.SmackListenerManager;
import com.r2.scau.moblieofficing.smack.SmackManager;
import com.r2.scau.moblieofficing.untils.SoftHideKeyBoardUtil;
import com.sqk.emojirelease.Emoji;
import com.sqk.emojirelease.FaceFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 张子健 on 2017/7/21 0021.
 */

public class ChatActivity extends BaseActivity implements FaceFragment.OnEmojiClickListener {
    private Button emojiBtn;//表情按钮
    private Button sendBtn;//发送按钮
    private EditText editText;//文字输入框
    private RecyclerView recyclerView;//消息recycle
    private SwipeRefreshLayout swipeRefreshLayout;//刷新layout
    private List<ChatMessage> chatMessageList = new ArrayList<>();//消息list
    private FaceFragment faceFragment;//表情fragment
    private SmackManager smack;
    private Chat mChat;
    private ChatMessageAdapter adapter;
    private TextView titleText;
    private LinearLayoutManager layoutManager;
    private Toolbar toolbar;
    private MultiUserChat mMultiUserChat;//多人聊天对象


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_chat);
        super.onCreate(savedInstanceState);
        SoftHideKeyBoardUtil.assistActivity(this);


        new Thread(new Runnable() {
            @Override
            public void run() {
                smack = SmackManager.getInstance();
                ChatRecord chatRecord = getIntent().getParcelableExtra("chatrecord");
                if (chatRecord.ismIsMulti()) {
                    mMultiUserChat = SmackManager.getInstance().getMultiChat(chatRecord.getmFriendUsername());
                    SmackListenerManager.addMultiChatMessageListener(mMultiUserChat);
                } else {
                    mChat = smack.createChat(chatRecord.getmChatJid());
                    try {
                        SmackListenerManager.addGlobalListener();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


    }

    @Override
    protected void initView() {
        emojiBtn = (Button) findViewById(R.id.emoji_button);
        sendBtn = (Button) findViewById(R.id.senMsg_button);
        editText = (EditText) findViewById(R.id.chat_editText);
        recyclerView = (RecyclerView) findViewById(R.id.chat_recycler);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.chat_swipelayout);
        toolbar = (Toolbar) findViewById(R.id.chat_toolbar);
        titleText = (TextView) findViewById(R.id.chat_toolbar_title);

        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.toolbar_chat_title_menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        refreshData();
        recyclerView.setAdapter(adapter);


        faceFragment = FaceFragment.Instance();
        getSupportFragmentManager().beginTransaction().add(R.id.Container, faceFragment).commit();
        getSupportFragmentManager().beginTransaction().hide(faceFragment).commit();

        ChatRecord chatRecord = getIntent().getParcelableExtra("chatrecord");
//        if (chatRecord)
        titleText.setText(chatRecord.getmFriendNickname());
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        emojiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyBoard();
                showFaceFragment();
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().length() != 0) {
                    ChatRecord chatRecord = getIntent().getParcelableExtra("chatrecord");
                    if (chatRecord.ismIsMulti()) {
                        String text = editText.getText().toString();
                        sendMulti(text);
                        editText.setText(null);
                    } else {
                        String text = editText.getText().toString();
                        send(text);//发送消息
                        editText.setText(null);
                    }
                }
            }
        });

        swipeRefreshLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideFaceFragment();
            }
        });

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideFaceFragment();
            }
        });

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    hideFaceFragment();
                } else {

                }
            }
        });


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideFaceFragment();
                closeKeyBoard();
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onStart() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent message) {
        Log.e("MessageEvent", "accept");
        ChatRecord chatRecord = getIntent().getParcelableExtra("chatrecord");
        if (!chatRecord.ismIsMulti()) {
            if (message.getChatMessage().getFriendNickname().equals(chatRecord.getmFriendNickname())) {
                adapter.add(message.getChatMessage());
                layoutManager.scrollToPosition(adapter.getItemCount() - 1);
            }
        }
        if (chatRecord.ismIsMulti()) {
            if ((chatRecord.getUuid()).equals(message.getChatMessage().getUuid())) {
                adapter.add(message.getChatMessage());
                layoutManager.scrollToPosition(adapter.getItemCount() - 1);
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.toolbar_chat_title_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

    @Override
    public void onEmojiClick(Emoji emoji) {
        if (emoji != null) {
            int index = editText.getSelectionStart();
            Editable editable = editText.getEditableText();
            if (index < 0) {
                editable.append(emoji.getContent());
            } else {
                editable.insert(index, emoji.getContent());
            }
        }
    }

    @Override
    public void onEmojiDelete() {
        String text = editText.getText().toString();
        if (text.isEmpty()) {
            return;
        }
        if ("]".equals(text.substring(text.length() - 1, text.length()))) {
            int index = text.lastIndexOf("[");
            if (index == -1) {
                int action = KeyEvent.ACTION_DOWN;
                int code = KeyEvent.KEYCODE_DEL;
                KeyEvent event = new KeyEvent(action, code);
                editText.onKeyDown(KeyEvent.KEYCODE_DEL, event);
                return;
            }
            editText.getText().delete(index, text.length());
            return;
        }
        int action = KeyEvent.ACTION_DOWN;
        int code = KeyEvent.KEYCODE_DEL;
        KeyEvent event = new KeyEvent(action, code);
        editText.onKeyDown(KeyEvent.KEYCODE_DEL, event);
    }


    //对back按钮的监听
    @Override
    public void onBackPressed() {
        if (!faceFragment.isHidden()) {
            getSupportFragmentManager().beginTransaction().hide(faceFragment).commit();
        } else {

            ChatRecord chatRecord = getIntent().getParcelableExtra("chatrecord");
            if (chatRecord.ismIsMulti()) {
                String whereClause = chatRecord.getmFriendUsername();
                ArrayList msgList = new ArrayList<>(DataSupport.where("mfriendusername=?", whereClause).find(ChatRecord.class));
                chatRecord = (ChatRecord) msgList.get(0);
            } else {
                String whereClause = chatRecord.getmChatJid();
                ArrayList msgList = new ArrayList<>(DataSupport.where("mchatjid=?", whereClause).find(ChatRecord.class));
                chatRecord = (ChatRecord) msgList.get(0);
            }
            if (chatRecord.isSaved()) {
                Log.e("aaaaaaa", "aaaaaaaa");
            } else {
                Log.e("bbbbbbb", "bbbbbbbb");
            }
            chatRecord.setmUnReadMessageCount();
            chatRecord.save();
//            ChatRecord chatRecord=getIntent().getParcelableExtra("chatrecord");
//            if (chatRecord.isSaved()){
//                Log.e("aaaaaaa","aaaaaaaa");
//            }else {
//                Log.e("bbbbbbb","bbbbbbbb");
//            }
            finish();
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();
        //取消editText的选中状态
        if (editText.isFocused()) {
            editText.clearFocus();
        }
    }

    //关闭键盘
    public void closeKeyBoard() {
        //                判断输入法键盘状态，如果输入法打开，强制关闭输入法
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(emojiBtn.getWindowToken(), 0);
        }
    }

    //关闭表情facefragment
    public void hideFaceFragment() {
        getSupportFragmentManager().beginTransaction().hide(faceFragment).commit();
    }

    //显示表情facefragment
    public void showFaceFragment() {
        getSupportFragmentManager().beginTransaction().show(faceFragment).commit();
    }

    public void send(final String message) {
        Observable.just(message)
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String message) {
                        try {
                            JSONObject json = new JSONObject();
                            json.put("fromNickName", "张大爷");
                            json.put("messageContent", message);
                            mChat.sendMessage(json.toString());

                            ChatMessage msg = new ChatMessage(1, true);
                            ChatRecord chatRecord = getIntent().getParcelableExtra("chatrecord");
                            msg.setFriendNickname(chatRecord.getmFriendNickname());
                            msg.setFriendUsername(chatRecord.getmFriendUsername());
                            msg.setMeUsername(chatRecord.getmMeUsername());
                            msg.setMeNickname(chatRecord.getmMeNickname());
                            msg.setContent(message);
                            msg.save();
                            EventBus.getDefault().post(new MessageEvent(msg));
                        } catch (Exception e) {
                            Log.d("send message failure", e.toString());
                        }
                    }
                });
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
        ChatRecord chatRecord = getIntent().getParcelableExtra("chatrecord");
        String whereClause = chatRecord.getmFriendUsername();
//    String[] whereArgs = {LoginHelper.getUser().getUsername()};
        if (!chatRecord.ismIsMulti()) {
            chatMessageList = new ArrayList<>(DataSupport.where("mfriendusername=?", whereClause).find(ChatMessage.class));
        } else {
            chatMessageList = new ArrayList<>(DataSupport.where("uuid=?", chatRecord.getUuid()).find(ChatMessage.class));
        }
        adapter = new ChatMessageAdapter(chatMessageList, this);
        layoutManager.scrollToPosition(adapter.getItemCount() - 1);
    }

    public void sendMulti(String message) {
        Observable.just(message)
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String message) {
                        try {
                            JSONObject json = new JSONObject();
                            json.put(ChatMessage.KEY_MESSAGE_CONTENT, message);
                            json.put(ChatMessage.KEY_MULTI_CHAT_SEND_USER, "sure3");//信息后缀必须为用户名
//                            Log.e(mChatUser.getMeUsername(),mChatUser.getMeUsername());
                            mMultiUserChat.sendMessage(json.toString());


                            ChatMessage msg = new ChatMessage(1, true);
                            ChatRecord chatRecord = getIntent().getParcelableExtra("chatrecord");
                            msg.setFriendNickname(chatRecord.getmFriendNickname());
                            msg.setFriendUsername(chatRecord.getmFriendUsername());
                            msg.setMeUsername("sure3");
                            msg.setMeNickname("张大爷");
                            msg.setContent(message);
                            msg.setMulti(true);
                            msg.setUuid(chatRecord.getUuid());
                            msg.save();
                            EventBus.getDefault().post(new MessageEvent(msg));
                        } catch (Exception e) {
                            Log.e(e.toString(), "send message failure");
                        }
                    }
                });
    }

}

