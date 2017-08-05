package com.r2.scau.moblieofficing.fragement;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.r2.scau.moblieofficing.Contants;
import com.r2.scau.moblieofficing.R;
import com.r2.scau.moblieofficing.activity.ReportActivity;

/**
 * Created by 嘉进 on 9:21.
 * 底部导航栏中工作的Fragment
 */

public class WorkFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Context mContext;
    private Toolbar mToolbar;
    private TextView titleTV;
    private Button dateReportBtn;
    private Button weekReportBtn;
    private Button monthReportBtn;


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
        dateReportBtn = (Button) view.findViewById(R.id.btn_day_report);
        weekReportBtn = (Button) view.findViewById(R.id.btn_week_report);
        monthReportBtn = (Button) view.findViewById(R.id.btn_month_report);

        dateReportBtn.setOnClickListener(this);
        weekReportBtn.setOnClickListener(this);
        monthReportBtn.setOnClickListener(this);

        mToolbar.setTitle("");
        titleTV.setText("工作");
    }

    public void openActivity(int type){
        Intent intent = new Intent(mContext, ReportActivity.class);
        intent.putExtra("type", type);
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
            default:
                break;
        }
    }
}
