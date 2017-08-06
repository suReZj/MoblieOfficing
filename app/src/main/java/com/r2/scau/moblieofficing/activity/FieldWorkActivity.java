package com.r2.scau.moblieofficing.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allen.library.SuperTextView;
import com.r2.scau.moblieofficing.Contants;
import com.r2.scau.moblieofficing.R;
import com.r2.scau.moblieofficing.adapter.GridAdapter;
import com.r2.scau.moblieofficing.bean.Contact;
import com.r2.scau.moblieofficing.untils.OkHttpUntil;
import com.r2.scau.moblieofficing.untils.SoftHideKeyBoardUtil;
import com.r2.scau.moblieofficing.untils.ToastUtils;
import com.r2.scau.moblieofficing.untils.UserUntil;
import com.r2.scau.moblieofficing.widge.TimePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by EdwinCheng on 2017/8/5.
 */

public class FieldWorkActivity extends BaseActivity implements TimePickerDialog.TimePickerDialogInterface {
    private static final String TAG = "FieldWorkActivity";

    private static final int THING_LEAVE = 1;       //事假
    private static final int YEAR_LEAVE = 2;        //年假
    private static final int CHANGE_REST = 3;       //调休
    private static final int SICK_LEAVE = 4;        //病假
    private static final int MARRIAGE_LEAVE = 5;    //婚假
    private static final int MATERNITY_LEAVE = 6;   //产假
    private static final int OFFICIAL_LEAVE = 7;    //例假
    private static final int FUNERAL_LEAVE = 8;     //丧假

    private static final int START_DATE = 11;     //开始日期
    private static final int START_TIME = 12;     //开始时间
    private static final int END_DATE = 13;     //结束日期
    private static final int END_TIME = 14;     //结束时间

    private Intent intent;
    private Bundle bundle;

    private Toolbar mToolBar;
    private TextView titleTV;

    //代表从上一个页面传进来的参数
    private int fieldworkType;

    private LinearLayout travel_layout;
    private Button submitBtn;
    private SuperTextView leavetype, startdate, starttime, enddate, endtime, duration , photo;
    private EditText location_input, reason_input;

    private GridAdapter fieldworkAdapter;
    private List<Contact> mContactList;
    private Handler handler;
    private Message message;
    private OkHttpClient okHttpClient;
    private TimePickerDialog timePickerDialog;

    private String omType = null;       //大类型
    private String omSubType = null;    //小类型
    private String startDateString = null;
    private String startTimeString = null;
    private String endDateString = null;
    private String endTimeString = null;
    private String omReasonString = null;
    private String photoString = null;

    private long officeManageId = -1 ;     //返回的事务id

    @Override
    protected void initView() {
        setContentView(R.layout.activity_fieldwork_manager);
        SoftHideKeyBoardUtil.assistActivity(this);

        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        titleTV = (TextView) findViewById(R.id.toolbar_title);
        submitBtn = (Button) findViewById(R.id.fieldwork_submitBtn);

        leavetype = (SuperTextView) findViewById(R.id.fieldwork_leavetype);
        startdate = (SuperTextView) findViewById(R.id.fieldwork_startdate);
        starttime = (SuperTextView) findViewById(R.id.fieldwork_starttime);
        enddate = (SuperTextView) findViewById( R.id.fieldwork_enddate);
        endtime = (SuperTextView) findViewById(R.id.fieldwork_endtime);
        duration = (SuperTextView) findViewById(R.id.fieldwork_duration);
        photo = (SuperTextView) findViewById(R.id.fieldwork_photo);

        location_input = (EditText) findViewById(R.id.fieldwork_travellocation_input);
        reason_input = (EditText) findViewById(R.id.fieldwork_reason_input);

        travel_layout = (LinearLayout) findViewById(R.id.fieldwork_travel_layout);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case Contants.FIELDWORK.CREATEOM_SUCCESS:
                        for (Contact contact : mContactList){
                            setomBoss(contact.getPhone(),officeManageId);
                        }
                        break;
                }
            }
        };
    }

    @Override
    protected void initData() {
        if (mContactList == null) {
            mContactList = new ArrayList<>();
        }

            intent = new Intent();
            bundle = getIntent().getExtras();
            timePickerDialog = new TimePickerDialog(FieldWorkActivity.this);

            mToolBar.setTitle("");
            initRV();
            setSupportActionBar(mToolBar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        fieldworkType = bundle.getInt("fieldworkType");
        switch (fieldworkType) {
            case Contants.FIELDWORK.OPEN_LEAVE:
                omType = getResources().getString(R.string.leave);

                titleTV.setText(getResources().getString(R.string.leave));
                leavetype.setVisibility(View.VISIBLE);
                travel_layout.setVisibility(View.GONE);
                break;

            case Contants.FIELDWORK.OPEN_GO_OUT:
                omType = getResources().getString(R.string.go_out);

                titleTV.setText(getResources().getString(R.string.go_out));
                break;

            case Contants.FIELDWORK.OPEN_TRAVEL:
                omType = getResources().getString(R.string.business_trip);

                titleTV.setText(getResources().getString(R.string.business_trip));
                travel_layout.setVisibility(View.VISIBLE);
                leavetype.setVisibility(View.GONE);
                break;

            case Contants.FIELDWORK.OPEN_OVERTIME:
                omType = getResources().getString(R.string.work_overtime);

                titleTV.setText(getResources().getString(R.string.work_overtime));
                break;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                FieldWorkActivity.this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void initRV() {
        GridLayoutManager manager = new GridLayoutManager(this, 5);
        RecyclerView rv = (RecyclerView) findViewById(R.id.grid_recycle);
        rv.setLayoutManager(manager);
        fieldworkAdapter = new GridAdapter(this, mContactList);
        rv.setAdapter(fieldworkAdapter);
    }

    @Override
    protected void initListener() {
        submitBtn.setOnClickListener(this);

        fieldworkAdapter.setOnItemClickListener(new GridAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position >= fieldworkAdapter.getItemCount() - 1) {
                    intent.setClass(FieldWorkActivity.this, SelectMemberActivity.class);
                    intent.putExtra("type", Contants.SELECT_MEMBER_REPORT);
                    startActivityForResult(intent, Contants.START_ACTIVIRY_SELECT_MEMBER_FOR_RESULT);
                } else {
                    fieldworkAdapter.delete(position);
                }
            }
        });

        //请假的点击事件
        if (fieldworkType == Contants.FIELDWORK.OPEN_LEAVE) {
            leavetype.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
                @Override
                public void onSuperTextViewClick() {
                    registerForContextMenu(leavetype);
                    openContextMenu(leavetype);
                    unregisterForContextMenu(leavetype);
                }
            });
        }

        startdate.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener(){
            @Override
            public void onSuperTextViewClick() {
                timePickerDialog.setTimetype(START_DATE);
                timePickerDialog.showDatePickerDialog();
            }
        });

        starttime.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onSuperTextViewClick() {
                timePickerDialog.setTimetype(START_TIME);
                timePickerDialog.showTimePickerDialog();
            }
        });

        enddate.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener(){
            @Override
            public void onSuperTextViewClick() {
                timePickerDialog.setTimetype(END_DATE);
                timePickerDialog.showDatePickerDialog();
            }
        });

        endtime.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onSuperTextViewClick() {
                timePickerDialog.setTimetype(END_TIME);
                timePickerDialog.showTimePickerDialog();
            }
        });

        photo.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener(){
            @Override
            public void onSuperTextViewClick() {
                //访问图库
                intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, Contants.RequestCode.OPEN_SYSTEM_ALBUM);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //提交按钮
            case R.id.fieldwork_submitBtn:
                if (okHttpClient == null){
                    okHttpClient = new OkHttpClient();
                }
                if (fieldworkType == Contants.FIELDWORK.OPEN_LEAVE){

                }else if (fieldworkType == Contants.FIELDWORK.OPEN_GO_OUT){

                }else if (fieldworkType == Contants.FIELDWORK.OPEN_TRAVEL){
                    //当选择为出差时候，小类型写入出差地点
                    omSubType = location_input.getText().toString();
                }else if (fieldworkType == Contants.FIELDWORK.OPEN_OVERTIME){

                }

                omReasonString = reason_input.getText().toString();
                if (omType ==null || startDateString == null || startTimeString == null || endDateString == null
                        ||endTimeString == null){
                    ToastUtils.show(FieldWorkActivity.this,"至少一项为空",Toast.LENGTH_SHORT);
                }else{
                    FormBody formBody = new FormBody.Builder()
                            .add("userPhone", UserUntil.gsonUser.getUserPhone())
                            .add("omType",omType)
                            .add("omSubType",omSubType)
                            .add("omStartTime",startDateString + " " + startTimeString + ":00")
                            .add("omEndTime",endDateString + " " + endTimeString +":00")
                            .add("omReason",omReasonString)
                            .build();
                    Request request = new Request.Builder().url(Contants.SERVER_IP + Contants.OfficeManage + Contants.createOfficeThing)
                            .addHeader("cookie", OkHttpUntil.loginSessionID)
                            .post(formBody)
                            .build();

                    okHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            // TODO: 10-0-1  请求失败
                            Log.e(TAG, "请求创建事务  fail");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            // TODO: 10-0-1 请求成功
                            message = handler.obtainMessage();
                            if (response.code() == 200){
                                JSONObject jsonObj = null;
                                try {
                                    jsonObj = new JSONObject(response.body().string());
                                    officeManageId = jsonObj.getLong("officeManageId");

                                    message.what = Contants.FIELDWORK.CREATEOM_SUCCESS;

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }else{
                                Log.e(TAG, "网络请求 错误  "+ response.code() + "   " + response.message() );
                                message.what = Contants.FIELDWORK.CREATEOM_FAILURE;
                            }
                            handler.sendMessage(message);
                        }
                    });

                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case Contants.ACTIVIRY_SELECT_MEMBER_RETURN_RESULT:
                Log.e("onActivityResult", "getMember");
                mContactList = data.getParcelableArrayListExtra("member");
                fieldworkAdapter.addAll(mContactList);
                break;

            case Contants.RequestCode.OPEN_SYSTEM_ALBUM:
                Log.e(TAG, "调取系统相册"+ data.toString());

                Uri selectImageUri = intent.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectImageUri, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                //获得路径
                photoString = cursor.getString(columnIndex);
                cursor.close();


                Log.e(TAG, "选取图片的路径" + photoString);
                break;

            default:
                break;
        }
    }

    //时间选择器 ----------点击确定
    @Override
    public void positiveListener() {
        int timetype = timePickerDialog.getTimetype();
        if (timetype == START_DATE || timetype == END_DATE) {
            int mYear = timePickerDialog.getYear();
            int mMonth = timePickerDialog.getMonth();
            int mDay = timePickerDialog.getDay();

            if (timetype == START_DATE) {
                startDateString = mYear + "-" + mMonth + "-" + mDay;
                startdate.setRightString(startDateString);
            } else if (timetype == END_DATE) {
                endDateString = mYear + "-" + mMonth + "-" + mDay;
                enddate.setRightString(endDateString);
            }

        } else if (timetype == START_TIME || timetype == END_TIME){
            int mHour = timePickerDialog.getHour();
            int mMinute = timePickerDialog.getMinute();

            if (timetype == START_TIME) {
                startTimeString = mHour + ":" + mMinute + ":00";
                starttime.setRightString(startTimeString);
            } else if (timetype == END_TIME) {
                endTimeString = mHour + ":" + mMinute + ":00";
                endtime.setRightString(endTimeString);
            }
        }
    }


    //时间选择器 ----------点击取消
    @Override
    public void negativeListener() {
        ToastUtils.show(FieldWorkActivity.this,"用户取消", Toast.LENGTH_SHORT);
    }

    //创建contextmenu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.fieldwork_leavetype) {
            menu.add(Menu.NONE,THING_LEAVE,Menu.NONE,getResources().getString(R.string.thing_leave));
            menu.add(Menu.NONE,YEAR_LEAVE,Menu.NONE,getResources().getString(R.string.year_leave));
            menu.add(Menu.NONE,CHANGE_REST,Menu.NONE,getResources().getString(R.string.change_rest));
            menu.add(Menu.NONE,SICK_LEAVE,Menu.NONE,getResources().getString(R.string.sick_leave));
            menu.add(Menu.NONE,MARRIAGE_LEAVE,Menu.NONE,getResources().getString(R.string.marriage_leave));
            menu.add(Menu.NONE,MATERNITY_LEAVE,Menu.NONE,getResources().getString(R.string.maternity_leave));
            menu.add(Menu.NONE,OFFICIAL_LEAVE,Menu.NONE,getResources().getString(R.string.official_leave));
            menu.add(Menu.NONE,FUNERAL_LEAVE,Menu.NONE,getResources().getString(R.string.funeral_leave));
        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case THING_LEAVE:
                omSubType = getResources().getString(R.string.thing_leave);
                leavetype.setRightString(omSubType);
                break;
            case YEAR_LEAVE:
                omSubType = getResources().getString(R.string.year_leave);
                leavetype.setRightString(omSubType);
                break;
            case CHANGE_REST:
                omSubType = getResources().getString(R.string.change_rest);
                leavetype.setRightString(omSubType);
                break;
            case SICK_LEAVE:
                omSubType = getResources().getString(R.string.sick_leave);
                leavetype.setRightString(omSubType);
                break;
            case MARRIAGE_LEAVE:
                omSubType = getResources().getString(R.string.marriage_leave);
                leavetype.setRightString(omSubType);
                break;
            case MATERNITY_LEAVE:
                omSubType= getResources().getString(R.string.maternity_leave);
                leavetype.setRightString(omSubType);
                break;
            case OFFICIAL_LEAVE:
                omSubType = getResources().getString(R.string.official_leave);
                leavetype.setRightString(omSubType);
                break;
            case FUNERAL_LEAVE:
                omSubType = getResources().getString(R.string.funeral_leave);
                leavetype.setRightString(omSubType);
                break;
        }

        return true;
    }

    public void setomBoss(final String omBossPhone, final long officeManageId){
        FormBody formBody = new FormBody.Builder()
                .add("userPhone", UserUntil.gsonUser.getUserPhone())
                .add("approvalUserPhone",omBossPhone)
                .add("uomType","a")
                .add("officeManageId", String.valueOf(officeManageId))
                .build();
        Request request = new Request.Builder().url(Contants.SERVER_IP + Contants.OfficeManage + Contants.createOfficeThing)
                .addHeader("cookie", OkHttpUntil.loginSessionID)
                .post(formBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // TODO: 10-0-1  请求失败
                Log.e(TAG, "请求创建事务  fail");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // TODO: 10-0-1 请求成功
                message = handler.obtainMessage();
                if (response.code() == 200){
                    Log.e(TAG, "omaddbossid:" + omBossPhone + "state:" + response.body().string() );
                    message.what = Contants.FIELDWORK.OMADDBOSS_SUCCESS;
                }else{
                    Log.e(TAG, "网络请求 错误  "+ response.code() + "   " + response.message() );
                    message.what = Contants.FIELDWORK.OMADDBOSS_FAILURE;
                }
                handler.sendMessage(message);
            }
        });
    }
}
