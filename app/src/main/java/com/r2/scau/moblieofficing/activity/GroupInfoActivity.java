package com.r2.scau.moblieofficing.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allen.library.SuperTextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.r2.scau.moblieofficing.Contants;
import com.r2.scau.moblieofficing.R;
import com.r2.scau.moblieofficing.adapter.groupMemberAdapter;
import com.r2.scau.moblieofficing.bean.MultiChatRoom;
import com.r2.scau.moblieofficing.bean.groupMember;
import com.r2.scau.moblieofficing.gson.GsonGroupInfo;
import com.r2.scau.moblieofficing.gson.GsonGroupQRCode;
import com.r2.scau.moblieofficing.gson.GsonMember;
import com.r2.scau.moblieofficing.gson.GsonMembers;
import com.r2.scau.moblieofficing.gson.GsonQRCode;
import com.r2.scau.moblieofficing.gson.GsonUser;
import com.r2.scau.moblieofficing.gson.GsonUsers;
import com.r2.scau.moblieofficing.retrofit.IFriendInfoByIdBiz;
import com.r2.scau.moblieofficing.retrofit.IFriendInfoByPhoneBiz;
import com.r2.scau.moblieofficing.retrofit.IGroupInfoBiz;
import com.r2.scau.moblieofficing.retrofit.IGroupQRCodeBiz;
import com.r2.scau.moblieofficing.retrofit.IMembersBiz;
import com.r2.scau.moblieofficing.retrofit.IQRCodeBiz;
import com.r2.scau.moblieofficing.smack.SmackManager;
import com.r2.scau.moblieofficing.smack.SmackMultiChatManager;
import com.r2.scau.moblieofficing.untils.DensityUtil;
import com.r2.scau.moblieofficing.untils.ImageUtils;
import com.r2.scau.moblieofficing.untils.OkHttpUntil;
import com.r2.scau.moblieofficing.untils.RetrofitUntil;
import com.r2.scau.moblieofficing.untils.UserUntil;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.FormBody;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.r2.scau.moblieofficing.Contants.PHOTO_SERVER_IP;
import static com.r2.scau.moblieofficing.Contants.SERVER_IP;
import static com.r2.scau.moblieofficing.Contants.file_Server;
import static com.r2.scau.moblieofficing.Contants.getInfo;
import static com.r2.scau.moblieofficing.Contants.joinGroup;
import static com.r2.scau.moblieofficing.untils.OkHttpUntil.okHttpClient;

/**
 * Created by dell88 on 2017/8/6 0006.
 */

public class GroupInfoActivity extends BaseActivity {
    private CircleImageView groupIcon;
    private TextView groupNickName;
    private RelativeLayout noticeLayout;
    private RelativeLayout groupFileLayout;
    private RelativeLayout addGroupLayout;
    private RelativeLayout groupQRCode;
    private SuperTextView groupCreater;
    private RecyclerView recyclerView;
    private groupMemberAdapter memberAdapter;
    private List<GsonMember> members;
    private List<groupMember> groupMembers=new ArrayList<>();
    private String path;
    private String QRPath;
    private String groupName;
    private String createrPhone;
    private List<MultiChatRoom> multiChatRooms;
    private Integer roomId;
    private Toolbar mToolBar;
    private TextView toolBarText;
    private TextView group_nickname;

    Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    for(int i=0;i<members.size();i++){
                        GsonMember gsonMember=members.get(i);
                        groupMember member=new groupMember();
                        member.setUserPhone(gsonMember.getUserPhone());
                        member.setNickName(gsonMember.getUserName());
                        groupMembers.add(member);
                    }
                    for (int i=0;i<groupMembers.size();i++){
                        getMemberIcon(groupMembers.get(i).getUserPhone(),i);
                    }
                    break;
                case 2:
                    GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),5);
                    recyclerView.setLayoutManager(layoutManager);
                    memberAdapter = new groupMemberAdapter(groupMembers, getApplicationContext());
                    recyclerView.setAdapter(memberAdapter);
                    break;
            }
            return false;
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_group_layout);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        groupIcon = (CircleImageView) findViewById(R.id.group_icon_circle);
        groupNickName = (TextView) findViewById(R.id.group_nickname);
        noticeLayout = (RelativeLayout) findViewById(R.id.notice_layout);
        groupFileLayout = (RelativeLayout) findViewById(R.id.file_layout);
        addGroupLayout = (RelativeLayout) findViewById(R.id.addgroup_layout);
        groupQRCode = (RelativeLayout) findViewById(R.id.group_qr_code_layout);
        groupCreater = (SuperTextView) findViewById(R.id.group_creater);
        recyclerView = (RecyclerView) findViewById(R.id.group_member);
        mToolBar = (Toolbar) findViewById(R.id.toolbar_info);
        toolBarText = (TextView) findViewById(R.id.toolbar_info_title);
        group_nickname=(TextView)findViewById(R.id.group_nickname);
        mToolBar.setTitle("");
        toolBarText.setText("详细资料");
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {
        Intent intent=getIntent();
        roomId=intent.getIntExtra("Id",0);
        getGroupInfo(roomId);
        getGroupMember(roomId);
        getQRCode(roomId);
    }

    @Override
    protected void initListener() {
//        群公告点击时间
        noticeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
//        群文件点击事件
        groupFileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
//        添加群点击事件
        addGroupLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multiChatRooms=DataSupport.findAll(MultiChatRoom.class);
                for(int i=0;i<multiChatRooms.size();i++){
                    if(multiChatRooms.get(i).getRoomId()==roomId){
                        Toast.makeText(getApplicationContext(),"你已经是本群成员",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                SmackManager.getInstance().joinChatRoom(groupName, UserUntil.gsonUser.getNickname(),null);
                addGroup();
            }
        });
//        二维码点击事件
        groupQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageDialog();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {

    }


//获取群信息
    public void getGroupInfo(Integer groupId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_IP + getInfo + "/")
                .callFactory(OkHttpUntil.getInstance())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IGroupInfoBiz iGroupInfoBiz = retrofit.create(IGroupInfoBiz.class);
        Call<GsonGroupInfo> call = iGroupInfoBiz.getGroupInfo(groupId);
        call.enqueue(new Callback<GsonGroupInfo>() {
            @Override
            public void onResponse(Call<GsonGroupInfo> call, Response<GsonGroupInfo> response) {
                GsonGroupInfo gsonGroupInfo=response.body();
                if(gsonGroupInfo.getCode()==200){
                    Integer createrId=gsonGroupInfo.getCreatedUserId();
                    groupName=gsonGroupInfo.getGroupName();
                    group_nickname.setText(groupName);
                    ImageUtils.setUserImageIcon(getApplicationContext(),groupIcon,groupName);
                    getCreaterInfo(createrId);
                    Log.e("getGroupInfo","success");
                }else {
                    Log.e("getGroupInfo fail",gsonGroupInfo.getMsg());
                }
            }

            @Override
            public void onFailure(Call<GsonGroupInfo> call, Throwable t) {
                Log.e("getInfo", "fail");
            }
        });
    }

//获取群创建人
    public void getCreaterInfo(Integer createrId){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_IP + getInfo + "/")
                .callFactory(OkHttpUntil.getInstance())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IFriendInfoByIdBiz iFriendInfoByIdBiz=retrofit.create(IFriendInfoByIdBiz.class);
        Call<GsonUsers> call = iFriendInfoByIdBiz.getInfoById(createrId);
        call.enqueue(new Callback<GsonUsers>() {
            @Override
            public void onResponse(Call<GsonUsers> call, Response<GsonUsers> response) {
                GsonUsers gsonUsers=response.body();
                if(gsonUsers.getCode()==200){
                    GsonUser gsonUser=gsonUsers.getUserInfo();
                    groupCreater.setLeftBottomString2(gsonUser.getNickname());
                    createrPhone=gsonUser.getUserPhone();
                    Log.d("getCreaterInfo","success");
                }else {
                    Log.d("getCreaterInfo fail",gsonUsers.getMsg());
                }
            }
            @Override
            public void onFailure(Call<GsonUsers> call, Throwable t) {

            }
        });
    }


    public void getGroupMember(Integer roomId){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_IP + getInfo + "/")
                .callFactory(OkHttpUntil.getInstance())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IMembersBiz iMembersBiz=retrofit.create(IMembersBiz.class);
        Call<GsonMembers> call=iMembersBiz.getMembers(roomId);
        call.enqueue(new Callback<GsonMembers>() {
            @Override
            public void onResponse(Call<GsonMembers> call, Response<GsonMembers> response) {
                GsonMembers gsonMembers=response.body();
                Log.e("gsonMembers",gsonMembers.toString());
                if(gsonMembers.getCode()==200){
                    members=gsonMembers.getGroupContactUserEntityList();
//                    memberAdapter=new groupMemberAdapter(members,getApplicationContext());
//                    recyclerView.setAdapter(memberAdapter);
                    Message msg=Message.obtain();
                    msg.what=1;
                    handler.sendMessage(msg);
                    Log.e("getGroupMember","success");
                }else {
                    Log.e("getGroupMember",gsonMembers.getMsg());
                }
            }
            @Override
            public void onFailure(Call<GsonMembers> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    public void getMemberIcon(String userPhone, final int position){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_IP + getInfo + "/")
                .callFactory(OkHttpUntil.getInstance())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IFriendInfoByPhoneBiz iFriendInfoBiz = retrofit.create(IFriendInfoByPhoneBiz.class);
        Call<GsonUsers> call = iFriendInfoBiz.getInfo(userPhone);
        call.enqueue(new Callback<GsonUsers>() {
            @Override
            public void onResponse(Call<GsonUsers> call, Response<GsonUsers> response) {
                GsonUsers gsonUsers = response.body();
                if (gsonUsers.getCode() == 200) {
                    GsonUser user = gsonUsers.getUserInfo();
                    String userName=user.getNickname();
                    if (user.getUserHeadPortrait() != null) {
                        path = user.getUserHeadPortrait().toString();
                        Log.e("icon!=null", path);
                        groupMembers.get(position).setIconPath(path);
                    } else {
                        groupMembers.get(position).setIconPath(null);
                        Log.e("icon==null", "icon==null");
                    }
                    if((groupMembers.size()-1)==position){
                        Message msg=Message.obtain();
                        msg.what=2;
                        handler.sendMessage(msg);
                    }
//                    if (userPhone.equals(UserUntil.gsonUser.getUserPhone())) {
//                        ImageUtils.setUserImageIcon(mContext, holder.memberIcon, userName);
//                    } else {
//                        if (userIconPath == null) {
//                            ImageUtils.setUserImageIcon(mContext, holder.memberIcon, userName);
//                        } else {
//                            Glide.with(mContext).load(userIconPath).into(holder.memberIcon);
//                        }
//                    }
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


    private void showImageDialog() {
//        imageView.setBackground(getDrawable(R.mipmap.test));
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_content_circle, null);
        Dialog bottomDialog = new Dialog(this, R.style.BottomDialog);
        final ImageView imageView = (ImageView) contentView.findViewById(R.id.QR_image);
        Log.e("path", QRPath);
        Glide.with(getApplicationContext()).load(PHOTO_SERVER_IP + QRPath).into(new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                imageView.setBackground(resource);
                Log.e("path", QRPath);
            }
        });
//        Glide.with(this).load(QRPath).into(imageView);
        bottomDialog.setContentView(contentView);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) contentView.getLayoutParams();
        params.width = getResources().getDisplayMetrics().widthPixels - DensityUtil.dp2px(this, 16f);
        params.bottomMargin = DensityUtil.dp2px(this, 8f);
        contentView.setLayoutParams(params);
        bottomDialog.setCanceledOnTouchOutside(true);
        bottomDialog.getWindow().setGravity(Gravity.CENTER);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        bottomDialog.show();
    }

    public void getQRCode(Integer groupId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_IP + file_Server + "/")
                .callFactory(OkHttpUntil.getInstance())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IGroupQRCodeBiz iGroupQRCodeBiz=retrofit.create(IGroupQRCodeBiz.class);
        Call<GsonGroupQRCode> call=iGroupQRCodeBiz.getGroupQR(groupId);
        call.enqueue(new Callback<GsonGroupQRCode>() {
            @Override
            public void onResponse(Call<GsonGroupQRCode> call, Response<GsonGroupQRCode> response) {
                GsonGroupQRCode gsonGroupQRCode=response.body();
                if (gsonGroupQRCode.getCode() == 200) {
                    QRPath = gsonGroupQRCode.getPath();
                    Log.e("getQR", "success");

                } else {
                    Log.e("getQR", gsonGroupQRCode.getMsg());
                }
            }

            @Override
            public void onFailure(Call<GsonGroupQRCode> call, Throwable t) {
                Log.e("getQR", t.toString());
            }
        });
    }

    public void addGroup(){
        FormBody formBody = new FormBody.Builder()
                .add("groupCreatedUserPhone", createrPhone)
                .add("groupName", groupName)
                .add("userPhone", UserUntil.gsonUser.getUserPhone())
                .build();
//                            step 3: 创建请求
        Request request = new Request.Builder().url(SERVER_IP+getInfo+joinGroup)
                .post(formBody)
                .addHeader("cookie", OkHttpUntil.loginSessionID)
                .build();

//                        step 4： 建立联系 创建Call对象
        okHttpClient.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
//                                 TODO: 17-1-4  请求失败
                Log.e("register", "fail");
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
//                                 TODO: 17-1-4 请求成功
                String str = response.body().string();
                try {
                    RetrofitUntil.type = Contants.LOGIN_IN_GET_DATA;
                    RetrofitUntil.getUserInfo();
                    RetrofitUntil.getFriend();
                    RetrofitUntil.getGroupInfo();
                    SmackMultiChatManager.bindJoinMultiChat();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.e("register", str);
            }
        });
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
}
