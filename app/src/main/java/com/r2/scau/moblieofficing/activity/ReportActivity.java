package com.r2.scau.moblieofficing.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.r2.scau.moblieofficing.Contants;
import com.r2.scau.moblieofficing.R;
import com.r2.scau.moblieofficing.adapter.GridAdapter;
import com.r2.scau.moblieofficing.bean.Contact;
import com.r2.scau.moblieofficing.gson.GsonCreateReport;
import com.r2.scau.moblieofficing.gson.GsonSetReportUser;
import com.r2.scau.moblieofficing.retrofit.IReportBiz;
import com.r2.scau.moblieofficing.untils.DateUtil;
import com.r2.scau.moblieofficing.untils.OkHttpUntil;
import com.r2.scau.moblieofficing.untils.UserUntil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReportActivity extends BaseActivity {

    private int type;
    private Toolbar mToolBar;
    private TextView mTitleTV;
    private EditText finishET;
    private EditText unfinishET;
    private EditText concertET;
    private EditText psET;
    private GridAdapter adapter;
    private Button commitBtn;
    private int reportType = -1;
    private List<Contact> mContactList = new ArrayList<>();

    @Override
    protected void initView() {
        setContentView(R.layout.activity_report);

        Intent intent = getIntent();
        type = intent.getIntExtra("type", -1);
        mTitleTV = (TextView) findViewById(R.id.toolbar_title);
        finishET = (EditText) findViewById(R.id.et_report_finish);
        unfinishET = (EditText) findViewById(R.id.et_report_unfinish);
        concertET = (EditText) findViewById(R.id.et_report_concert);
        psET = (EditText) findViewById(R.id.et_report_ps);
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        commitBtn = (Button) findViewById(R.id.btn_report);

        mToolBar.setTitle("");
        switch (type){
            case Contants.OPEN_DAY_REPORTY:
                mTitleTV.setText("日报");
                break;
            case Contants.OPEN_WEEK_REPORT:
                mTitleTV.setText("周报");
                break;
            case Contants.OPEN_MONTH_REPORT:
                mTitleTV.setText("月报");
                break;
        }

        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void initData() {
        initRV();
    }

    public void initRV(){
        GridLayoutManager manager = new GridLayoutManager(this, 5);
        RecyclerView rv = (RecyclerView) findViewById(R.id.grid_recycle);
        rv.setLayoutManager(manager);
        adapter = new GridAdapter(this, mContactList);
        rv.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode){
            case Contants.ACTIVIRY_SELECT_MEMBER_RETURN_RESULT:
                Log.e("onActivityResult", "getMember");
                mContactList= data.getParcelableArrayListExtra("member");
                adapter.addAll(mContactList);
                break;
            default:
                break;
        }
    }

    @Override
    protected void initListener() {
        adapter.setOnItemClickListener(new GridAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(position >= adapter.getItemCount() - 1){
                    Intent intent = new Intent(ReportActivity.this, SelectMemberActivity.class);
                    intent.putExtra("type", Contants.SELECT_MEMBER_REPORT);
                    startActivityForResult(intent, Contants.START_ACTIVIRY_SELECT_MEMBER_FOR_RESULT);
                }else {
                    adapter.delete(position);
                }
            }
        });

        commitBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.btn_report:
                String finishWork = finishET.getText().toString();
                String unfinishWork = unfinishET.getText().toString();
                String concertWork = concertET.getText().toString();
                String ps = psET.getText().toString();

                if (finishWork.equals("") && unfinishWork.equals("")
                        && concertWork.equals("") && ps.equals("")){
                    Toast toast = Toast.makeText(getApplicationContext(), "至少一项内容不能为空", Toast.LENGTH_SHORT);
                    toast.show();

                }else {
                    sendReport(finishWork, unfinishWork, concertWork, ps);
                    finish();
                }

                break;
            default:
                break;
        }
    }

    public void sendReport(String finishWork, String unfinishWork, String concertWork ,String ps){
        switch (type){
            case Contants.OPEN_DAY_REPORTY:
                reportType = 0;
                break;
            case Contants.OPEN_WEEK_REPORT:
                reportType = 1;
                break;
            case Contants.OPEN_MONTH_REPORT:
                reportType = 2;
                break;
            default:
                break;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Contants.SERVER_IP + "/report/")
                .callFactory(OkHttpUntil.getInstance())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IReportBiz iReportBiz = retrofit.create(IReportBiz.class);
        Call<GsonCreateReport> call = iReportBiz.createReport(UserUntil.phone, finishWork,
                unfinishWork, concertWork, ps, DateUtil.currentDatetime(), (long)reportType);
        call.enqueue(new Callback<GsonCreateReport>() {
            @Override
            public void onResponse(Call<GsonCreateReport> call, Response<GsonCreateReport> response) {
                Log.e("createReport", response.toString());
                GsonCreateReport report = response.body();
                Log.e("createReport", report.getCode() + "");
                if (report.getCode() == 200){
                    Log.e("createReport", "success");
                    int reportId = report.getDailyReportId();
                    for (Contact contact : mContactList){
                        setReportUser(contact.getPhone(), reportId);
                    }
                }else {
                    Log.e("createReport", "fail");
                }
            }

            @Override
            public void onFailure(Call<GsonCreateReport> call, Throwable t) {
                Log.e("createReport", "fail");
            }
        });

    }

    public void setReportUser(String phone, final int reportId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Contants.SERVER_IP + "/report/")
                .callFactory(OkHttpUntil.getInstance())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IReportBiz iReportBiz = retrofit.create(IReportBiz.class);
        Call<GsonSetReportUser> call = iReportBiz.setReportUser(phone, (long) reportId);
        Log.e("setReportUser", "setUser");
        call.enqueue(new Callback<GsonSetReportUser>() {
            @Override
            public void onResponse(Call<GsonSetReportUser> call, Response<GsonSetReportUser> response) {
                Log.e("setReportUser", response.toString());
                Log.e("setReportUser", response.body().getMsg());
            }

            @Override
            public void onFailure(Call<GsonSetReportUser> call, Throwable t) {
                Log.e("setReportUser", "fail");
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
