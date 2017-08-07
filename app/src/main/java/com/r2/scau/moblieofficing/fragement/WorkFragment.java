package com.r2.scau.moblieofficing.fragement;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.r2.scau.moblieofficing.Contants;
import com.r2.scau.moblieofficing.R;
import com.r2.scau.moblieofficing.activity.FieldWorkActivity;
import com.r2.scau.moblieofficing.activity.FileTypeSelectActivity;
import com.r2.scau.moblieofficing.activity.ReportActivity;
import com.r2.scau.moblieofficing.activity.SignOfficeActivity;
import com.r2.scau.moblieofficing.untils.ImageUtils;

/**
 * Created by 嘉进 on 9:21.
 * 底部导航栏中工作的Fragment
 */

public class WorkFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Context mContext;
    private Toolbar mToolbar;
    private TextView titleTV;
    private ImageView dateReportBtn;
    private ImageView weekReportBtn;
    private ImageView monthReportBtn;

    /** Create by edwincheng in 2017/08/05 */
    private ImageView leaveBtn;
    private ImageView gooutBtn;
    private ImageView busniess_trip;
    private ImageView work_overtimeBtn;
    private ImageView clouddiskBtn;
    private ImageView signOfficeBtn;
    private ImageView videoMeetingBtn;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_work, container, false);
        mContext = getActivity();
        initView();
        return view;
    }

    public void initView(){
        titleTV = (TextView) view.findViewById(R.id.toolbar_title);
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        dateReportBtn = (ImageView) view.findViewById(R.id.btn_day_report);
        weekReportBtn = (ImageView) view.findViewById(R.id.btn_week_report);
        monthReportBtn = (ImageView) view.findViewById(R.id.btn_month_report);
        leaveBtn = (ImageView) view.findViewById(R.id.btn_leave);
        gooutBtn = (ImageView) view.findViewById(R.id.btn_goout);
        busniess_trip = (ImageView) view.findViewById(R.id.btn_business_trip);
        work_overtimeBtn = (ImageView) view.findViewById(R.id.btn_work_overtime);
        clouddiskBtn = (ImageView) view.findViewById(R.id.btn_cloud_disk);
        signOfficeBtn = (ImageView) view.findViewById(R.id.btn_sign_in);
        videoMeetingBtn = (ImageView) view.findViewById(R.id.btn_video_meeting);

        ImageUtils.setUserRectImageIcon(mContext, dateReportBtn, "日");
        ImageUtils.setUserRectImageIcon(mContext, weekReportBtn, "周");
        ImageUtils.setUserRectImageIcon(mContext, monthReportBtn, "月");


        dateReportBtn.setOnClickListener(this);
        weekReportBtn.setOnClickListener(this);
        monthReportBtn.setOnClickListener(this);
        leaveBtn.setOnClickListener(this);
        gooutBtn.setOnClickListener(this);
        busniess_trip.setOnClickListener(this);
        work_overtimeBtn.setOnClickListener(this);
        clouddiskBtn.setOnClickListener(this);
        signOfficeBtn.setOnClickListener(this);
        videoMeetingBtn.setOnClickListener(this);


        mToolbar.setTitle("");
        titleTV.setText("工作");
    }

    public void openActivity(int type){
        Intent intent = new Intent(mContext, ReportActivity.class);
        intent.putExtra("type", type);
        startActivity(intent);
    }

    public void openFieldWorkActivity(int type){
        Intent intent = new Intent(mContext, FieldWorkActivity.class);
        intent.putExtra("fieldworkType", type);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.btn_day_report:
                openActivity(Contants.OPEN_DAY_REPORTY);
                break;
            case R.id.btn_week_report:
                openActivity(Contants.OPEN_WEEK_REPORT);
                break;
            case R.id.btn_month_report:
                openActivity(Contants.OPEN_MONTH_REPORT);
                break;
            case R.id.btn_leave:
                openFieldWorkActivity(Contants.FIELDWORK.OPEN_LEAVE);
                break;
            case R.id.btn_goout:
                openFieldWorkActivity(Contants.FIELDWORK.OPEN_GO_OUT);
                break;
            case R.id.btn_business_trip:
                openFieldWorkActivity(Contants.FIELDWORK.OPEN_TRAVEL);
                break;
            case R.id.btn_work_overtime:
                openFieldWorkActivity(Contants.FIELDWORK.OPEN_OVERTIME);
                break;
            case R.id.btn_cloud_disk:
                Intent intent = new Intent(getActivity(), FileTypeSelectActivity.class);
                startActivity(intent);
                break;

            case R.id.btn_sign_in:
                requestLocationPermission();
                break;
            case R.id.btn_video_meeting:
                Intent intent2video = new Intent(getActivity(), io.agora.openvcall.ui.MainActivity.class);
                startActivity(intent2video);
                break;
            default:
                break;
        }
    }

    private void requestLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // 申请权限
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            } else {
                // 已经申请权限
                Intent intent2 = new Intent(getActivity(), SignOfficeActivity.class);
                startActivity(intent2);
            }
        } else {
            Intent intent2 = new Intent(getActivity(), SignOfficeActivity.class);
            startActivity(intent2);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 申请权限成功
                    Intent intent2 = new Intent(getActivity(), SignOfficeActivity.class);
                    startActivity(intent2);
                } else {
                    // 用户勾选了不再询问，提示用户手动打开权限
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                        Toast.makeText(getActivity(), "相机权限已经被禁止", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

        }

    }



}
