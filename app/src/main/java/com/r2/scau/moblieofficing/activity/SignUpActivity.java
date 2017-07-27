package com.r2.scau.moblieofficing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.r2.scau.moblieofficing.R;
import com.r2.scau.moblieofficing.untils.MathUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;

import static com.r2.scau.moblieofficing.untils.OkHttpClientManager.okHttpClient;

public class SignUpActivity extends AppCompatActivity {

    private TextView login;
    private Button signUpButton;
    private Handler mHandler;
    private EditText nameET;
    private EditText phoneET;
    private EditText passwordET;
    private EditText verCodeET;
    private String verCodeKey;
    private ImageView verCodeImageView;
    public static final int VERCODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
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
        initView();
    }

    public void initView(){

        login = (TextView) findViewById(R.id.link_login);
        signUpButton = (Button) findViewById(R.id.btn_sign_up);
        verCodeET = (EditText) findViewById(R.id.input_ver_code);
        nameET = (EditText) findViewById(R.id.input_name_sign_up);
        phoneET = (EditText) findViewById(R.id.input_phone_sign_up);
        verCodeImageView = (ImageView) findViewById(R.id.iv_ver_code);
        passwordET = (EditText) findViewById(R.id.input_password_sign_up);

        getVerCode();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });
    }

    public void getVerCode(){
        verCodeKey = String.valueOf(System.currentTimeMillis()) + MathUtil.getRandom620(20);
        verCodeKey = MathUtil.getMD5(verCodeKey);

        String password = passwordET.getText().toString();

        //step 1: 同样的需要创建一个OkHttpClick对象
        //step 2: 创建  FormBody.Builder
        FormBody formBody = new FormBody.Builder()
                .add("vcodeKey", verCodeKey)
                .build();


        //step 3: 创建请求
        final Request request = new Request.Builder().url("http://192.168.13.32:8080/open/getGifCode.shtml")
                .post(formBody)
                .build();

        //step 4： 建立联系 创建Call对象
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // TODO: 17-1-4  请求失败
                Log.e("getVerCode", "fail");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // TODO: 17-1-4 请求成功
                Log.e("getVerCode", "success");
                Message msg = Message.obtain();
                msg.what = VERCODE;
                msg.obj = response.body().bytes();
                mHandler.sendMessage(msg);
            }
        });
    }

    public void signUp(){
        String name = nameET.getText().toString();
        String phone = phoneET.getText().toString();
        String verCode = verCodeET.getText().toString();
        Log.e("verCode", verCode);
        String password = passwordET.getText().toString();

        //step 1: 同样的需要创建一个OkHttpClick对象
        //step 2: 创建  FormBody.Builder
        FormBody formBody = new FormBody.Builder()
                .add("username", name)
                .add("userPhone", phone)
                .add("password", password)
                .add("vcode", verCode)
                .add("vcodeKey", verCodeKey)
                .build();


        //step 3: 创建请求
        final Request request = new Request.Builder().url("http://192.168.13.32:8080/u/mobileRegister.shtml")
                .post(formBody)
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
                if (str.contains("验证码不正确")){
                    getVerCode();
                }
            }
        });
    }

}
