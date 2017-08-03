package com.r2.scau.moblieofficing.fragement;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.r2.scau.moblieofficing.R;

/**
 * Created by 嘉进 on 9:21.
 * 底部导航栏中工作的Fragment
 */

public class WorkFragment extends Fragment {

    private View view;
    private Context mContext;
    private Toolbar mToolbar;
    private TextView titleTV;

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

        mToolbar.setTitle("");
        titleTV.setText("工作");
    }
}
