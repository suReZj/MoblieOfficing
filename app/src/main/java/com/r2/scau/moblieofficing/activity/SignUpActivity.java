package com.r2.scau.moblieofficing.activity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.r2.scau.moblieofficing.Contants;
import com.r2.scau.moblieofficing.R;
import com.r2.scau.moblieofficing.event.SignUpFinishEvent;
import com.r2.scau.moblieofficing.retrofit.ISignBiz;
import com.r2.scau.moblieofficing.smack.SmackListenerManager;
import com.r2.scau.moblieofficing.smack.SmackManager;
import com.r2.scau.moblieofficing.untils.MathUtil;
import com.r2.scau.moblieofficing.untils.OkHttpUntil;
import com.r2.scau.moblieofficing.untils.RetrofitUntil;
import com.r2.scau.moblieofficing.untils.UserUntil;
import com.r2.scau.moblieofficing.widge.CustomVideoView;
import com.r2.scau.moblieofficing.widge.popview.PopField;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;

public class SignUpActivity extends BaseActivity {


    private Button signUpBtn;
    private Handler mHandler;
    private EditText nameET;
    private EditText phoneET;
    private EditText passwordET;
    private EditText verCodeET;
    private String verCodeKey;
    private String password;
    private String sessionID;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private ImageView verCodeImageView;
    public static final int VERCODE = 1;


    // 注册特效
    private PopField mPopField;

    //创建播放视频的控件对象
    private CustomVideoView videoview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void initView() {
        //取消标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);
        mPopField = PopField.attach2Window(this);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case VERCODE:
                        Log.e("handle", "set");
                        Glide.with(SignUpActivity.this)
                                .load((byte[]) msg.obj)
                                .skipMemoryCache(true)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .into(verCodeImageView);
                        break;
                    default:
                        break;
                }
            }
        };
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

        signUpBtn = (Button) findViewById(R.id.btn_sign_up);
        verCodeET = (EditText) findViewById(R.id.input_ver_code);
        nameET = (EditText) findViewById(R.id.input_name_sign_up);
        phoneET = (EditText) findViewById(R.id.input_phone_sign_up);
        verCodeImageView = (ImageView) findViewById(R.id.iv_ver_code);
        passwordET = (EditText) findViewById(R.id.input_password_sign_up);

        getVerCode();


    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        signUpBtn.setOnClickListener(this);
    }

    public void getVerCode() {
        Log.e("getVerCode", "fail");
        verCodeKey = String.valueOf(System.currentTimeMillis()) + MathUtil.getRandom620(20);
        verCodeKey = MathUtil.getMD5(verCodeKey);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Contants.SERVER_IP + "/open/")
                .build();
        ISignBiz signBiz = retrofit.create(ISignBiz.class);
        retrofit2.Call<ResponseBody> call = signBiz.getVerCode(verCodeKey);
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    Log.e("getVerCode", response.toString());
                    Message msg = Message.obtain();
                    msg.what = VERCODE;
                    msg.obj = response.body().bytes();
                    mHandler.sendMessage(msg);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Log.e("getVerCode", "fail");
            }
        });
    }

    public void loginOpenFire(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                SmackManager smack = SmackManager.getInstance();
                smack.login(UserUntil.phone, password);
                try {
                    SmackListenerManager.addGlobalListener();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }


    public void signUp() {
        String name = nameET.getText().toString();
        String phone = phoneET.getText().toString();
        String verCode = verCodeET.getText().toString();
        Log.e("verCode", verCode);
        password = passwordET.getText().toString();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Contants.SERVER_IP + "/u/")
                .build();
        ISignBiz signBiz = retrofit.create(ISignBiz.class);
        retrofit2.Call<ResponseBody> call = signBiz.signUp(name, phone, password, verCode, verCodeKey);
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    String str = response.body().string();
                    Log.e("signUp", str);
                    if (str.contains("注册成功")) {
                        Headers headers = response.headers();
                        Log.d("info_headers", "header " + headers);
                        List<String> cookies = headers.values("Set-Cookie");
                        String session = cookies.get(0);
                        Log.d("info_cookies", "onResponse-size: " + cookies);
                        sessionID = session.substring(0, session.indexOf(";"));
                        Log.i("info_s", "session is  :" + sessionID);
                        OkHttpUntil.loginSessionID = sessionID;
                        UserUntil.phone = phoneET.getText().toString();
                        RetrofitUntil.type = Contants.SIGN_UP_GET_DATA;
                        RetrofitUntil.getUserInfo();
                        loginOpenFire();
                    } else {
                        getVerCode();
                        Log.e("signUp", str);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Log.e("signUp", "fail");
                getVerCode();
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSignUpFinishEvent(SignUpFinishEvent finishEvent) {
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_sign_up:
                signUp();
                LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View addView = layoutInflater.inflate(R.layout.sampletextview, null);
                final Button signUpBtn2 = (Button) addView.findViewById(R.id.btn_login2);
                signUpBtn2.setText("立即注册");
                signUpBtn2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        signUp();
                    }
                });
                mPopField.popView(signUpBtn, addView, true);
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
