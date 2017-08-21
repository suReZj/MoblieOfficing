package com.r2.scau.moblieofficing.fragement;


import android.Manifest;
import android.app.SearchManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.r2.scau.moblieofficing.Contants;
import com.r2.scau.moblieofficing.R;
import com.r2.scau.moblieofficing.activity.AddFriendActivity;
import com.r2.scau.moblieofficing.activity.EditGroupActivity;
import com.r2.scau.moblieofficing.activity.FileTypeSelectActivity;
import com.r2.scau.moblieofficing.activity.FriendsInfoActivity;
import com.r2.scau.moblieofficing.activity.GroupInfoActivity;
import com.r2.scau.moblieofficing.adapter.MessageAdapter;
import com.r2.scau.moblieofficing.bean.ChatMessage;
import com.r2.scau.moblieofficing.bean.ChatRecord;
import com.r2.scau.moblieofficing.event.MessageEvent;
import com.r2.scau.moblieofficing.smack.SmackManager;
import com.r2.scau.moblieofficing.untils.ToastUtils;
import com.r2.scau.moblieofficing.untils.UserUntil;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import okhttp3.OkHttpClient;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.SEARCH_SERVICE;


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
    private OkHttpClient okHttpClient = new OkHttpClient();
    private SearchView searchView;

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar_message_fragment);
        toolbar.inflateMenu(R.menu.toolbar_message_menu);
        toolbar.setTitle("消息");
        setHasOptionsMenu(true);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        refreshData();

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        registerForContextMenu(recyclerView);

        searchView = (SearchView) view.findViewById(R.id.searchView);
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(false);//添加提交按钮，监听在OnQueryTextListener的onQueryTextSubmit响应
        searchView.setIconified(false);//输入框内icon不显示
        searchView.clearFocus();//禁止弹出输入法，在某些情况下有需要
//下面是在搜索栏的字体，设置为白色，默认也是黑色
        TextView TextViewtextView=(TextView)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        TextViewtextView.setTextColor(Color.WHITE);
        TextView mSearchSrcTextView = (SearchView.SearchAutoComplete) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        mSearchSrcTextView.setHintTextColor(Color.WHITE);


        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.scan) {
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.CAMERA},
                                MY_PERMISSIONS_REQUEST_CAMERA);
                    } else {
                        openQRCodeActivity();
                    }
                }
                if (item.getItemId() == R.id.multiChat) {
                    Intent multiIntent = new Intent(getActivity().getApplicationContext(), EditGroupActivity.class);
                    startActivity(multiIntent);
                }
                if (item.getItemId() == R.id.addFriend) {
                    Intent addFriendIntent = new Intent(getActivity().getApplicationContext(), AddFriendActivity.class);
                    startActivity(addFriendIntent);
                }
                if (item.getItemId() == R.id.cloud) {
                    Intent intent = new Intent(getActivity(), FileTypeSelectActivity.class);
                    startActivity(intent);
                }

                if (item.getItemId() == R.id.searchBtn) {
                    if (searchView.getVisibility() == View.GONE) {
                        Log.e("searchView.getVisibility()==View.GONE", "searchView.getVisibility()==View.GONE");
                        item.setIcon(R.drawable.close);
                        searchView.setVisibility(View.VISIBLE);
                        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(final String query) {
                                Observable.create(new ObservableOnSubscribe<String>() {
                                    @Override
                                    public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<String> e) throws Exception {
                                        e.onNext(query);
                                    }
                                }).observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Consumer<String>() {
                                            @Override
                                            public void accept(@io.reactivex.annotations.NonNull String s) throws Exception {
                                                searchRefreshData(s);
                                            }
                                        });
                                return false;
                            }

                            @Override
                            public boolean onQueryTextChange(final String newText) {
                                Observable.create(new ObservableOnSubscribe<String>() {
                                    @Override
                                    public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<String> e) throws Exception {
                                        e.onNext(newText);
                                    }
                                }).observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Consumer<String>() {
                                            @Override
                                            public void accept(@io.reactivex.annotations.NonNull String s) throws Exception {
                                                searchRefreshData(s);
                                            }
                                        });
                                return false;
                            }
                        });
                    } else {
                        Log.e("searchView.getVisibility()==View.VISIBLE", "searchView.getVisibility()==View.VISIBLE");
                        item.setIcon(R.drawable.ic_search_white_24dp);
                        searchView.setVisibility(View.GONE);
                        refreshData();
                       TextView mSearchSrcTextView = (SearchView.SearchAutoComplete) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
                        mSearchSrcTextView.setText(null);
                    }
                }
                return true;
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(searchView.getVisibility()==View.VISIBLE){
        }else {
            refreshData();
        }
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
            Log.e("向其他人发起聊天时接收到的事件", "向其他人发起聊天时接收到的事件");
            return;
        }
        if (message_adapter.getMessageList().indexOf(event) > -1) {
            Log.e("已经存在此人的聊天窗口记录", "已经存在此人的聊天窗口记录");
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
            message.getChatMessage().save();
            chatRecord = new ChatRecord(message.getChatMessage());
            chatRecord.setSetTopFlag(false);
            chatRecord.setmFriendAvatar(message.getChatMessage().getIconPath());
            addChatRecord(chatRecord);
        } else {
            Log.d("chatRecord2", "chatRecord2");
            if (!message.getChatMessage().isMeSend()) {
                ArrayList<ChatMessage> multiMsg = new ArrayList<>(DataSupport.where("msgid=?", message.getChatMessage().getMsgID()).find(ChatMessage.class));
                if (multiMsg.size() != 0) {
                    refreshData();
                    return;
                } else {
                    message.getChatMessage().save();
                }
            }
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
                chatRecord.setmFriendAvatar(message.getChatMessage().getIconPath());
                chatRecord.updateAll("mchatjid=? and mfriendusername=?", chatRecord.getmChatJid(), chatRecord.getmFriendUsername());
            } else {
                chatRecord.setmFriendAvatar(null);
                chatRecord.updateAll("mchatjid=? and mfriendusername=?", chatRecord.getmChatJid(), chatRecord.getmFriendUsername());
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
        List<ChatRecord> list = DataSupport.findAll(ChatRecord.class);
        for (int i = 0; i < list.size(); i++) {
            chatRecord = list.get(i);
            if (chatRecord.getmFriendUsername().equals(message.getFriendUsername())) {
                return chatRecord;
            } else {
                chatRecord = null;
            }

        }

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
//                    DataSupport.deleteAll(ChatMessage.class, "mfriendusername=? and mismulti=?", chatRecord.getmFriendUsername(), flagOfMulti);
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


    public void openQRCodeActivity() {
        Intent intent = new Intent(getActivity(), CaptureActivity.class);
        startActivityForResult(intent, Contants.RequestCode.QRSCAN);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.toolbar_message_menu, menu);
    }

    public void refreshData() {
        String whereClause = UserUntil.gsonUser.getUserPhone();
        Log.e("whereClausewhereClause", whereClause);
        msgList = new ArrayList<>(DataSupport.where("mmeusername= ? and settopflag=?", whereClause, "1")
                .order("mchattime desc")
                .find(ChatRecord.class));
        newList = new ArrayList<>(DataSupport.where("mmeusername= ? and settopflag=?", whereClause, "0")
                .order("mchattime desc")
                .find(ChatRecord.class));
        msgList.addAll(newList);
        message_adapter = new MessageAdapter(getContext(), msgList);
        recyclerView.setAdapter(message_adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Contants.RequestCode.QRSCAN:
                if (resultCode == RESULT_OK) {
                    /**
                     * Create by edwincheng in 2017/08/04
                     * resultdata代表的是 二维码内部储存的信息
                     *
                     * 自己解析resultdata中的字段 然后在做相应操作
                     */
                    String resultdata = data.getStringExtra("result");
                    Log.e("二维码扫描结果", resultdata);
                    String[] resultarr = resultdata.split(":");

                    if (resultarr.length == 2) {
                        if (resultarr[0].equals("user")) {
                            //打开个人信息页面的activity

                            Bundle bundle = new Bundle();
                            Intent intent = new Intent(getActivity(), FriendsInfoActivity.class);
                            bundle.putString("phone", resultarr[1]);
                            intent.putExtras(bundle);
                            startActivity(intent);

                        } else if (resultarr[0].equals("groupId")) {
//                            打开 查看群信息 的Actiity
                            Bundle bundle = new Bundle();
                            Intent intent = new Intent(getActivity(), GroupInfoActivity.class);
                            bundle.putInt("Id", Integer.parseInt(resultarr[1]));
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } else {
                            ToastUtils.show(getActivity(), "未知二维码信息", Toast.LENGTH_SHORT);
                        }
                    } else {
                        ToastUtils.show(getActivity(), "未知的二维码信息", Toast.LENGTH_SHORT);
                    }


                } else if (resultCode == RESULT_CANCELED) {
                    Log.e("二维码扫描结果", "用户选择取消");
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("permission", "accept");
                openQRCodeActivity();
            } else {
                // Permission Denied
                Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void searchRefreshData(String seach) {
        String whereClause = UserUntil.gsonUser.getUserPhone();
        msgList = new ArrayList<>(DataSupport.where("mmeusername= ? and mfriendusername like ?", whereClause, "%" + seach + "%")
                .order("mchattime desc")
                .find(ChatRecord.class));
        Log.e("searchRefreshData", msgList.toString());
        message_adapter = new MessageAdapter(getContext(), msgList);
        recyclerView.setAdapter(message_adapter);
    }


}
