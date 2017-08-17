package com.r2.scau.moblieofficing.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allen.library.SuperTextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.r2.scau.moblieofficing.R;
import com.r2.scau.moblieofficing.gson.GsonExamination;
import com.r2.scau.moblieofficing.gson.GsonExaminations;
import com.r2.scau.moblieofficing.gson.GsonOmImages;
import com.r2.scau.moblieofficing.retrofit.IGetOmImage;
import com.r2.scau.moblieofficing.retrofit.IQueryOfficeThing;
import com.r2.scau.moblieofficing.untils.DateUtil;
import com.r2.scau.moblieofficing.untils.DateUtils;
import com.r2.scau.moblieofficing.untils.DensityUtil;
import com.r2.scau.moblieofficing.untils.OkHttpUntil;
import com.r2.scau.moblieofficing.untils.RetrofitUntil;
import com.r2.scau.moblieofficing.untils.UserUntil;
import com.r2.scau.moblieofficing.widge.popview.CircleImageView;

import java.io.IOException;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.FormBody;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.r2.scau.moblieofficing.Contants.OfficeManage;
import static com.r2.scau.moblieofficing.Contants.PHOTO_SERVER_IP;
import static com.r2.scau.moblieofficing.Contants.SERVER_IP;
import static com.r2.scau.moblieofficing.Contants.getInfo;
import static com.r2.scau.moblieofficing.Contants.joinGroup;
import static com.r2.scau.moblieofficing.untils.OkHttpUntil.okHttpClient;

public class ExaminationActivity extends BaseActivity {
    private Toolbar toolbar;
    private TextView toolBarText;
    private SuperTextView person;
    private SuperTextView type;
    private SuperTextView startTime;
    private SuperTextView endTime;
    private TextView reson;
    private CircleImageView imageView;
    private LinearLayout selectTrue;
    private LinearLayout selectFalse;
    private String pathImage;
    private Integer omid;
    private String userPhone;

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    getImageview(userPhone, omid);
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_examination);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_info);
        toolBarText = (TextView) findViewById(R.id.toolbar_info_title);
        person = (SuperTextView) findViewById(R.id.examination_person);
        type = (SuperTextView) findViewById(R.id.examination_type);
        startTime = (SuperTextView) findViewById(R.id.start_time);
        endTime = (SuperTextView) findViewById(R.id.end_time);
        reson = (TextView) findViewById(R.id.examination_reason);
        imageView = (CircleImageView) findViewById(R.id.examination_photo);
        selectTrue = (LinearLayout) findViewById(R.id.select_true);
        selectFalse = (LinearLayout) findViewById(R.id.select_false);
        toolbar.setTitle("");
        toolBarText.setText("审批内容");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        omid = intent.getIntExtra("omid", 0);
        checkExamination(omid);
    }

    @Override
    protected void initListener() {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageDialog();
            }
        });

        selectTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTrue("true");
                Toast.makeText(getApplicationContext(),"同意申请成功",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        selectFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTrue("false");
                Toast.makeText(getApplicationContext(),"拒绝申请成功",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
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

    private void showImageDialog() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_content_circle, null);
        Dialog bottomDialog = new Dialog(this, R.style.BottomDialog);
        final ImageView imageView = (ImageView) contentView.findViewById(R.id.QR_image);
        Glide.with(getApplicationContext()).load(PHOTO_SERVER_IP + pathImage).into(new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                imageView.setBackground(resource);
            }
        });
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

    private void selectTrue(String flag) {
        Log.e("selectTrue","selectTrue");
        FormBody formBody = new FormBody.Builder()
                .add("userPhone", userPhone)
                .add("isApproval",flag)
                .add("officeManageId",omid+"")
                .build();
//                            step 3: 创建请求
        Request request = new Request.Builder().url(SERVER_IP + OfficeManage + "/checkApproval.shtml")
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
                Log.e("register", str);
            }
        });
    }


    private void checkExamination(Integer omid) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_IP + OfficeManage + "/")
                .callFactory(OkHttpUntil.getInstance())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        IQueryOfficeThing iQueryOfficeThing = retrofit.create(IQueryOfficeThing.class);
        iQueryOfficeThing.getExaminations(omid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GsonExaminations>() {
                    private Disposable disposable;

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(@NonNull GsonExaminations gsonExaminations) {
                        if (gsonExaminations.getCode() == 200) {
                            GsonExamination gsonExamination = gsonExaminations.getOfficeManage();
                            person.setRightString(gsonExamination.getUserphone());
                            type.setRightString(gsonExamination.getOmtype());
                            startTime.setRightString(DateUtils.timete(gsonExamination.getOmstarttime()));
                            endTime.setRightString(DateUtils.timete(gsonExamination.getOmendtime()));
                            reson.setText(gsonExamination.getOmreason());
                            userPhone = gsonExamination.getUserphone();
                            Message message=Message.obtain();
                            message.what=1;
                            handler.sendMessage(message);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        disposable.dispose();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void getImageview(String userPhone,Integer omid){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_IP + OfficeManage + "/")
                .callFactory(OkHttpUntil.getInstance())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        IGetOmImage iGetOmImage=retrofit.create(IGetOmImage.class);
        iGetOmImage.getImage(userPhone,omid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GsonOmImages>() {
                    private Disposable disposable;
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable=d;
                    }

                    @Override
                    public void onNext(@NonNull GsonOmImages gsonOmImages) {
                        if(gsonOmImages.getCode()==200){
                            if(gsonOmImages.getOfficeImageList().get(0).getFilepath()!=null){
                                pathImage=gsonOmImages.getOfficeImageList().get(0).getFilepath();
                                Log.e("gsonOmImages",pathImage);
                                Glide.with(getApplicationContext()).load(PHOTO_SERVER_IP + pathImage).into(new SimpleTarget<GlideDrawable>() {
                                    @Override
                                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                        imageView.setBackground(resource);
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        disposable.dispose();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
