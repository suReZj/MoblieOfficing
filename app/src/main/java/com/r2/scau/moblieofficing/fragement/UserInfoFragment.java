package com.r2.scau.moblieofficing.fragement;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.r2.scau.moblieofficing.R;
import com.r2.scau.moblieofficing.activity.LoginActivity;

/**
 * Created by 嘉进 on 9:23.
 * 底部导航栏中我的Fragment
 */

public class UserInfoFragment extends Fragment {

    private View view;
    private Context mContext;
    private Button mButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_info, container, false);
        mContext = getActivity();
        initView();
        return view;
    }

    public void initView(){
        mButton = (Button) view.findViewById(R.id.button_login);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
