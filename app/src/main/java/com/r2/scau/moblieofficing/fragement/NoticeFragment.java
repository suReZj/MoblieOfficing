package com.r2.scau.moblieofficing.fragement;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.r2.scau.moblieofficing.R;
import com.r2.scau.moblieofficing.activity.SendNoticeActivity;
import com.r2.scau.moblieofficing.widge.EDCToolBar;

/**
 * Created by 嘉进 on 9:21.
 * 底部导航栏中公告的Fragment
 */

public class NoticeFragment extends Fragment implements View.OnClickListener{

    private View view;
    private Context mContext;
    private Button sendNoticeBtn;
    private EDCToolBar mEDCToolBar;
    private TextView readTV;
    private TextView unReadTV;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notice, container, false);
        mContext = getActivity();
        initView();
        return view;
    }

    public void initView() {

        readTV = (TextView) view.findViewById(R.id.tv_notice_read);
        unReadTV = (TextView) view.findViewById(R.id.tv_notice_unread);
        mEDCToolBar = (EDCToolBar)  view.findViewById(R.id.toolbar_notice);

        readTV.setOnClickListener(this);
        unReadTV.setOnClickListener(this);
        unReadTV.setSelected(true);
        mEDCToolBar.getRightSecondButton().setOnClickListener(this);
    }

    public void selected() {
        readTV.setSelected(false);
        unReadTV.setSelected(false);
    }




    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.toolbar_rightSecondButton:
                Intent intent = new Intent(mContext, SendNoticeActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_notice_read:
                selected();
                readTV.setSelected(true);
                break;
            case R.id.tv_notice_unread:
                selected();
                unReadTV.setSelected(true);
                break;
        }
    }
}
