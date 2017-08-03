package com.r2.scau.moblieofficing.activity;


import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.r2.scau.moblieofficing.Contants;
import com.r2.scau.moblieofficing.R;
import com.r2.scau.moblieofficing.bean.Contact;
import com.r2.scau.moblieofficing.gson.GsonFriend;
import com.r2.scau.moblieofficing.gson.GsonFriends;
import com.r2.scau.moblieofficing.gson.GsonUsers;
import com.r2.scau.moblieofficing.retrofit.IFriendBiz;
import com.r2.scau.moblieofficing.retrofit.ILoginBiz;
import com.r2.scau.moblieofficing.untils.FistLetterUntil;
import com.r2.scau.moblieofficing.untils.MathUtil;
import com.r2.scau.moblieofficing.untils.OkHttpUntil;
import com.r2.scau.moblieofficing.untils.SharedPrefUtil;
import com.r2.scau.moblieofficing.untils.UserUntil;
import com.r2.scau.moblieofficing.widge.CustomVideoView;
import com.r2.scau.moblieofficing.widge.popview.PopField;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends BaseActivity {

    private EditText userET;
    private EditText passwordET;
    private Button loginBtn;
    private TextView sigUpTV;
    private String loginSessionID;

    // 登录特效
    private PopField mPopField;

    //创建播放视频的控件对象
    private CustomVideoView videoview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        //取消标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        mPopField = PopField.attach2Window(this);

        userET = (EditText) findViewById(R.id.input_user_login);
        passwordET = (EditText) findViewById(R.id.input_password_login);
        loginBtn = (Button) findViewById(R.id.btn_login);
        sigUpTV = (TextView) findViewById(R.id.link_signup);
        //加载视频资源控件
        videoview = (CustomVideoView) findViewById(R.id.videoview);
        //设置播放加载路径
        videoview.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video));
        //播放
        videoview.start();
        //循环播放
        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoview.start();
            }
        });


    }

    @Override
    protected void initData() {
        userET.setText((CharSequence) SharedPrefUtil.getInstance().get(Contants.SP_LOGIN_USER_PHONE_KEY,""));
        passwordET.setText((CharSequence) SharedPrefUtil.getInstance().get(Contants.SP_LOGIN_PASSWORD_KEY,""));

    }

    @Override
    protected void initListener() {
        loginBtn.setOnClickListener(this);
        sigUpTV.setOnClickListener(this);
    }


    public void login() {
        final String user = userET.getText().toString();
        final String password = passwordET.getText().toString();
        String passwordMD5 = user + "#" + password;
        passwordMD5 = MathUtil.getMD5(passwordMD5);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Contants.SERVER_BASE_URL + "u/")
                .build();
        ILoginBiz loginBiz = retrofit.create(ILoginBiz.class);
        Call<ResponseBody> call = loginBiz.login(user, passwordMD5, true);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String str = response.body().string();
                    Log.e("login", str);
                    if (str.contains("登陆成功")) {
                        SharedPrefUtil.getInstance().put(Contants.SP_LOGIN_USER_PHONE_KEY, user);
                        SharedPrefUtil.getInstance().put(Contants.SP_LOGIN_PASSWORD_KEY, password);
                        Headers headers = response.headers();
                        Log.d("info_headers", "header " + headers);
                        List<String> cookies = headers.values("Set-Cookie");
                        String session = cookies.get(0);
                        Log.d("info_cookies", "onResponse-size: " + cookies);
                        loginSessionID = session.substring(0, session.indexOf(";"));
                        Log.i("info_s", "session is  :" + loginSessionID);
                        OkHttpUntil.loginSessionID = loginSessionID;
                        UserUntil.phone = userET.getText().toString();
                        getUserInfo();
                        getFriend();
                    }else {
                        Log.e("login", str);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("login", "fail");
            }
        });
    }

    public void changePassword() {
        //step 1: 同样的需要创建一个OkHttpClick对象
        //step 2: 创建  FormBody.Builder
        /*FormBody formBody = new FormBody.Builder()
                .add("pswd", "123456")
                .add("newPswd", "12345678")
                .build();


        //step 3: 创建请求
        Request request = new Request.Builder().url("http://192.168.13.23:8080/user/mobileUpdatePswd.shtml")
                .addHeader("cookie", loginSessionID)
                .post(formBody)
                .build();

        //step 4： 建立联系 创建Call对象
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // TODO: 17-1-4  请求失败
                Log.e("changePswd", "fail");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // TODO: 17-1-4 请求成功
                Log.e("changePswd", response.body().string());
            }
        });*/
    }


    public void getUserInfo(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Contants.SERVER_BASE_URL + "group/")
                .callFactory(OkHttpUntil.getInstance())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ILoginBiz iLoginBiz = retrofit.create(ILoginBiz.class);
        Call<GsonUsers> call = iLoginBiz.getUserInfo(UserUntil.phone);
        call.enqueue(new Callback<GsonUsers>() {
            @Override
            public void onResponse(Call<GsonUsers> call, Response<GsonUsers> response) {
                GsonUsers gsonUsers = response.body();
                if(gsonUsers.getCode() == 200){
                    Log.e("getUser", "success");
                    UserUntil.gsonUser = gsonUsers.getUserInfo();

                }else {
                    Log.e("getUser", "fail");
                }
            }

            @Override
            public void onFailure(Call<GsonUsers> call, Throwable t) {
                Log.e("getUser", "fail");
            }
        });
    }

    public void getFriend() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Contants.SERVER_BASE_URL + "/user/")
                .callFactory(OkHttpUntil.getInstance())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IFriendBiz iFriendBiz = retrofit.create(IFriendBiz.class);
        Call<GsonFriends> call = iFriendBiz.getFriend(UserUntil.phone);
        call.enqueue(new Callback<GsonFriends>() {
            @Override
            public void onResponse(Call<GsonFriends> call, Response<GsonFriends> response) {
                GsonFriends gsonFriends = response.body();
                if (gsonFriends.getCode() == 200) {
                    List<GsonFriend> friendList = gsonFriends.getListFriends();
                    ArrayList<Contact> contacts = new ArrayList<Contact>();
                    for (GsonFriend myFriend : friendList) {
                        Contact contact = new Contact();
                        String name = myFriend.getNickname();
                        contact.setPhone(myFriend.getUserPhone());
                        contact.setName(name);
                        contact.setFirstLetter(FistLetterUntil.getSortKey(name));
                        contacts.add(contact);
                    }
                    UserUntil.friendList = contacts;
                } else {
                    Log.e("getFriend", gsonFriends.getMsg());
                }
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<GsonFriends> call, Throwable t) {
                Log.e("getFriend", "fail");
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                Log.e("jason", "点击登录按钮");
                login();
                LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View addView = layoutInflater.inflate(R.layout.sampletextview, null);
                final Button loginBtn2 = (Button) addView.findViewById(R.id.btn_login2);
                loginBtn2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        login();
                    }
                });
                mPopField.popView(loginBtn, addView, true);

                break;
            case R.id.link_signup:
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                break;
        }
    }

    //返回重启加载
    @Override
    protected void onRestart() {
        //加载视频资源控件
        videoview = (CustomVideoView) findViewById(R.id.videoview);
        //设置播放加载路径
        videoview.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video));
        //播放
        videoview.start();
        //循环播放
        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoview.start();
            }
        });
        super.onRestart();
    }

    //防止锁屏或者切出的时候，音乐在播放
    @Override
    protected void onStop() {
        videoview.stopPlayback();
        super.onStop();
    }
}
