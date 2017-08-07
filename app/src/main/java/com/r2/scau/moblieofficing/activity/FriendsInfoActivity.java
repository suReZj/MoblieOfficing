package com.r2.scau.moblieofficing.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.ContactsContract;
import android.speech.tts.TextToSpeech;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allen.library.SuperTextView;
import com.r2.scau.moblieofficing.R;
import com.r2.scau.moblieofficing.bean.ChatRecord;
import com.r2.scau.moblieofficing.bean.Contact;
import com.r2.scau.moblieofficing.gson.GsonFriend;
import com.r2.scau.moblieofficing.gson.GsonFriends;
import com.r2.scau.moblieofficing.gson.GsonUser;
import com.r2.scau.moblieofficing.gson.GsonUsers;
import com.r2.scau.moblieofficing.retrofit.IFriendInfoBiz;
import com.r2.scau.moblieofficing.smack.SmackManager;
import com.r2.scau.moblieofficing.untils.DateUtil;
import com.r2.scau.moblieofficing.untils.FistLetterUntil;
import com.r2.scau.moblieofficing.untils.OkHttpUntil;
import com.r2.scau.moblieofficing.untils.UserUntil;

import org.greenrobot.eventbus.EventBus;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import io.agora.openvcall.ui.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.r2.scau.moblieofficing.R.string.contact;

/**
 * Created by dell88 on 2017/8/4 0004.
 */

public class FriendsInfoActivity extends BaseActivity {
    private RelativeLayout chatToFriend;
    private RelativeLayout phoneToFriend;
    private RelativeLayout videoToFriend;
    private SuperTextView friendName;
    private SuperTextView friendPhone;
    private SuperTextView friendEmail;
    private Toolbar mToolBar;
    private TextView toolBarText;
    private CircleImageView userIcon;//用户头像
    private TextView userNickName;//用户昵称
    private int copyFriendName = 1;
    private int copyFriendPhone = 2;
    private int callFriendPhone = 3;
    private int addFriendPhone = 4;
    private int copyFriendEmail = 5;
    private int sendFriendEmail = 6;
    private String phone;
    private GsonUser user;
    private String userPhone="";
    private String userName="";
    private String userEmail="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_friend_info);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        chatToFriend = (RelativeLayout) findViewById(R.id.chat_layout);
        phoneToFriend = (RelativeLayout) findViewById(R.id.phone_layout);
        videoToFriend = (RelativeLayout) findViewById(R.id.video_layout);
        friendName = (SuperTextView) findViewById(R.id.friend_name);
        friendPhone = (SuperTextView) findViewById(R.id.friend_phone);
        friendEmail = (SuperTextView) findViewById(R.id.friend_email);
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        toolBarText = (TextView) findViewById(R.id.toolbar_title);
        userIcon=(CircleImageView)findViewById(R.id.friend_icon_circle);
        userNickName=(TextView)findViewById(R.id.user_nickname);
        mToolBar.setTitle("");
        toolBarText.setText("详细资料");
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        phone = intent.getStringExtra("phone");
//        Log.e("phone", phone);
        getFriendInfo(phone);
//        friendName.setLeftBottomString2(user.getNickname());
//        friendPhone.setLeftBottomString2(user.getUserPhone());
//        friendEmail.setLeftBottomString2(user.getEmail().toString());

    }

    @Override
    protected void initListener() {
        friendName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerForContextMenu(friendName);
                openContextMenu(v);
                unregisterForContextMenu(friendName);
            }
        });

        friendPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerForContextMenu(friendPhone);
                openContextMenu(v);
                unregisterForContextMenu(friendPhone);
            }
        });
        friendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerForContextMenu(friendEmail);
                openContextMenu(v);
                unregisterForContextMenu(friendEmail);
            }
        });
        chatToFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userName.equals(UserUntil.gsonUser.getUserPhone())){
                    Toast.makeText(getApplicationContext(),"不能和自己聊天",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                List<ChatRecord> chatRecords = DataSupport.where("mfriendusername=?", phone).find(ChatRecord.class);
                ChatRecord record;
                if (chatRecords.size() == 0) {
                    record = new ChatRecord();
                    record.setUuid(UUID.randomUUID().toString());
                    record.setmFriendUsername(userPhone);
                    record.setmFriendNickname(userName);
                    record.setmMeUsername(UserUntil.gsonUser.getUserPhone());
                    record.setmMeNickname(UserUntil.gsonUser.getNickname());
                    record.setmChatTime(DateUtil.currentDatetime());
                    record.setmIsMulti(false);
                    record.setmChatJid(SmackManager.getInstance().getChatJid(userPhone));
                    record.save();
                } else {
                    record = chatRecords.get(0);
                }
                EventBus.getDefault().post(record);
                intent.putExtra("chatrecord", record);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        videoToFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FriendsInfoActivity.this, io.agora.openvcall.ui.MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    //创建contextmenu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.friend_name) {
            menu.add(Menu.NONE, copyFriendName, Menu.NONE, "复制");
        }
        if (v.getId() == R.id.friend_phone) {
            menu.add(Menu.NONE, copyFriendPhone, Menu.NONE, "复制");
            menu.add(Menu.NONE, callFriendPhone, Menu.NONE, "手机电话拨打");
            menu.add(Menu.NONE, addFriendPhone, Menu.NONE, "添加到通讯录");
        }
        if (v.getId() == R.id.friend_email) {
            menu.add(Menu.NONE, copyFriendEmail, Menu.NONE, "复制");
            menu.add(Menu.NONE, sendFriendEmail, Menu.NONE, "发送邮件");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1://复制姓名
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager.setText(userName);
                break;
            case 2:
                clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager.setText(userPhone);
                break;
            case 3://手机拨打电话
                String phone = userPhone;
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case 4://添加到通讯录
                Intent addIntent = new Intent(Intent.ACTION_INSERT, Uri.withAppendedPath(Uri.parse("content://com.android.contacts"), "contacts"));
                addIntent.putExtra(android.provider.ContactsContract.Intents.Insert.PHONE, userPhone);
                startActivity(addIntent);
                break;
            case 5://复制邮箱
                clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager.setText(userEmail);
                break;
            case 6:
                Intent data = new Intent(Intent.ACTION_SENDTO);
                data.setData(Uri.parse("mailto:"+userEmail));
                startActivity(data);
                break;
        }
        return super.onContextItemSelected(item);
    }

    public void getFriendInfo(String Phone) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.13.61:8089/group/")
                .callFactory(OkHttpUntil.getInstance())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IFriendInfoBiz iFriendInfoBiz=retrofit.create(IFriendInfoBiz.class);
        Call<GsonUsers> call = iFriendInfoBiz.getInfo(Phone);
        call.enqueue(new Callback<GsonUsers>() {
            @Override
            public void onResponse(Call<GsonUsers> call, Response<GsonUsers> response) {
                GsonUsers gsonUsers = response.body();
                if (gsonUsers.getCode() == 200) {
                    user=gsonUsers.getUserInfo();
                    userPhone=user.getUserPhone();
                    userName=user.getNickname();
                    if (user.getEmail() != null){
                        userEmail = user.getEmail().toString();
                    }else {
                        userEmail = "无";
                    }
                    friendName.setLeftBottomString2(userName);
                    friendPhone.setLeftBottomString2(userPhone);
                    friendEmail.setLeftBottomString2(userEmail);
                    Log.e("getInfo", "success");
                } else {
                    Log.e("getInfo", gsonUsers.getMsg());
                }
            }
            @Override
            public void onFailure(Call<GsonUsers> call, Throwable t) {
                Log.e("getInfo", "fail");
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

}
