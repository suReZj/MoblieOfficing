package com.r2.scau.moblieofficing.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.r2.scau.moblieofficing.R;
import com.r2.scau.moblieofficing.retrofit.ISignBiz;
import com.r2.scau.moblieofficing.untils.MathUtil;
import com.r2.scau.moblieofficing.untils.OkHttpUntil;
import com.r2.scau.moblieofficing.untils.UserUntil;

import java.util.List;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;

public class SignUpActivity extends BaseActivity {

    private TextView loginTV;
    private Button signUpBtn;
    private Handler mHandler;
    private EditText nameET;
    private EditText phoneET;
    private EditText passwordET;
    private EditText verCodeET;
    private String verCodeKey;
    private String sessionID;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private ImageView verCodeImageView;
    public static final int VERCODE = 1;


    @Override
    public void initView() {
        setContentView(R.layout.activity_sign_up);

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

        loginTV = (TextView) findViewById(R.id.link_login);
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
        loginTV.setOnClickListener(this);
        signUpBtn.setOnClickListener(this);
    }

    public void getVerCode() {
        Log.e("getVerCode", "fail");
        verCodeKey = String.valueOf(System.currentTimeMillis()) + MathUtil.getRandom620(20);
        verCodeKey = MathUtil.getMD5(verCodeKey);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.13.61:8089/open/")
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


    public void signUp() {
        String name = nameET.getText().toString();
        String phone = phoneET.getText().toString();
        String verCode = verCodeET.getText().toString();
        Log.e("verCode", verCode);
        String password = passwordET.getText().toString();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.13.61:8089/u/")
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
                        sessionID = OkHttpUntil.loginSessionID;
                        UserUntil.phone = phoneET.getText().toString();
                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                        startActivity(intent);
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


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.link_login:
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_sign_up:
                signUp();
                break;
        }
    }
}
