package com.r2.scau.moblieofficing.activity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.r2.scau.moblieofficing.R;
import com.r2.scau.moblieofficing.retrofit.ILoginBiz;
import com.r2.scau.moblieofficing.untils.MathUtil;
import com.r2.scau.moblieofficing.untils.OkHttpUntil;
import com.r2.scau.moblieofficing.untils.UserUntil;

import java.io.IOException;
import java.util.List;

import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends BaseActivity {

    private EditText userET;
    private EditText passwordET;
    private Button loginBtn;
    private TextView sigUpTV;
    private String loginSessionID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView(){
        setContentView(R.layout.activity_login);

        userET = (EditText) findViewById(R.id.input_user_login);
        passwordET = (EditText) findViewById(R.id.input_password_login);
        loginBtn = (Button) findViewById(R.id.btn_login);
        sigUpTV = (TextView) findViewById(R.id.link_signup);
        passwordET = (EditText) findViewById(R.id.input_password_login);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        loginBtn.setOnClickListener(this);

        sigUpTV.setOnClickListener(this);
    }


    public void login(){
        //step 1: 同样的需要创建一个OkHttpClick对象
        //step 2: 创建  FormBody.Builder
        String user = userET.getText().toString();
        String password = passwordET.getText().toString();
        password = user + "#" + password;
        password = MathUtil.getMD5(password);
        UserUntil.phone = user;

        /*FormBody formBody = new FormBody.Builder()
                .add("userPhone", user)
                .add("password", password)
                .add("isRememberMe", "true")
                .build();*/


        //step 3: 创建请求
        /*Request request = new Request.Builder().url("http://192.168.13.57:8089/u/mobileLogin.shtml")
                .post(formBody)
                .build();

        //step 4： 建立联系 创建Call对象
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.e("login", "fail");
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                Log.e("login", response.body().string());
                Headers headers = response.headers();
                Log.d("info_headers", "header " + headers);
                List<String> cookies = headers.values("Set-Cookie");
                String session = cookies.get(0);
                Log.d("info_cookies", "onResponse-size: " + cookies);

                loginSessionID = session.substring(0, session.indexOf(";"));
                Log.i("info_s", "session is  :" + loginSessionID);
                OkHttpUntil.loginSessionID = loginSessionID;
            }

        });*/

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.13.57:8089/u/")
                .build();
        ILoginBiz loginBiz = retrofit.create(ILoginBiz.class);
        Call<ResponseBody> call = loginBiz.login(user, password, true);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.e("login", response.body().string());
                    Headers headers = response.headers();
                    Log.d("info_headers", "header " + headers);
                    List<String> cookies = headers.values("Set-Cookie");
                    String session = cookies.get(0);
                    Log.d("info_cookies", "onResponse-size: " + cookies);

                    loginSessionID = session.substring(0, session.indexOf(";"));
                    Log.i("info_s", "session is  :" + loginSessionID);
                    OkHttpUntil.loginSessionID = loginSessionID;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void changePassword(){
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login:
                login();
                break;
            case R.id.link_signup:
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                break;
        }
    }
}
