package com.r2.scau.moblieofficing.activity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.r2.scau.moblieofficing.Constants;
import com.r2.scau.moblieofficing.R;
import com.r2.scau.moblieofficing.bean.ImageIconBean;
import com.r2.scau.moblieofficing.gson.GsonUploadPortrait;
import com.r2.scau.moblieofficing.retrofit.ISignBiz;
import com.r2.scau.moblieofficing.retrofit.IUploadPortrait;
import com.r2.scau.moblieofficing.untils.Contacts;
import com.r2.scau.moblieofficing.untils.ImageUtils;
import com.r2.scau.moblieofficing.untils.MathUtil;
import com.r2.scau.moblieofficing.untils.OkHttpUntil;
import com.r2.scau.moblieofficing.untils.SharedPrefUtil;
import com.r2.scau.moblieofficing.untils.UserUntil;
import com.r2.scau.moblieofficing.widge.CustomVideoView;
import com.r2.scau.moblieofficing.widge.popview.PopField;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.R.attr.data;
import static android.R.attr.path;

public class SignUpActivity extends BaseActivity {


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


    // 注册特效
    private PopField mPopField;

    //创建播放视频的控件对象
    private CustomVideoView videoview;

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
        initialPath();
    }

    private void initialPath() {
        File file = new File(Constants.FILEPATH+"/data/portraits");
        if(!file.exists()){
            file.mkdirs();
        }
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
                .baseUrl(Constants.SERVER_BASE_URL + "open/")
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
                .baseUrl(Constants.SERVER_BASE_URL + "u/")
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
                        imageIconUpload(nameET.getText().toString()+".jpg",phoneET.getText().toString(),ImageUtils.changeDrawableToFile(
                                ImageUtils.getIcon(nameET.getText().toString(), 23),
                                Constants.FILEPATH+"/data/portraits",nameET.getText().toString()));
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
    public void imageIconUpload(String filename, String userPhone, File image){
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("fileName",filename)
                .addFormDataPart("userPhone",userPhone)
                .addFormDataPart("file",filename, RequestBody.create(null,image));

        final RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(Constants.SERVER_BASE_URL + "fileServer/uploadPortrait.shtml")
                .addHeader("cookie", OkHttpUntil.loginSessionID)
                .post(requestBody)
                .build();

        new OkHttpClient().newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
            }
            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                if (response.code() == 200){
                    ImageIconBean imageIconBean = new Gson().fromJson(response.body().string(), ImageIconBean.class);
                    SharedPrefUtil.getInstance().put(Constants.IMAGE_ICON_URL,imageIconBean.getPath());
                    Log.d("onResponse",(String) SharedPrefUtil.getInstance().get(Constants.IMAGE_ICON_URL,""));
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }});
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
