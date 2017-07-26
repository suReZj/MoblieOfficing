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
import android.widget.Toast;

import com.r2.scau.moblieofficing.R;
import com.r2.scau.moblieofficing.activity.FileSelect_Avtivity;
import com.r2.scau.moblieofficing.activity.LoginActivity;
import com.r2.scau.moblieofficing.widge.BottomView;

/**
 * Created by 嘉进 on 9:23.
 * 底部导航栏中我的Fragment
 */

public class UserInfoFragment extends Fragment implements View.OnClickListener{

    private View view;
    private Context mContext;
    private Button mButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_info, container, false);
        mContext = getActivity();

        Button button = (Button) view.findViewById(R.id.filemanageBtn);

        Button button1 = (Button) view.findViewById(R.id.filemanage);

        button1.setOnClickListener(this);
        button.setOnClickListener(this);

        initView();

        return view;
    }

    private View.OnClickListener clickListener = new View.OnClickListener(){

        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.popwindow_delete:
                    Toast.makeText(getActivity(), "menu1", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.popwindow_shareto:
                    Toast.makeText(getActivity(), "menu2", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.filemanageBtn:
                BottomView menuWindow = new BottomView(getActivity(), 10000, "s",clickListener);
                menuWindow.show();
                break;

            case R.id.filemanage:
                Intent intent = new Intent(getActivity(), FileSelect_Avtivity.class);
                startActivity(intent);
                break;


        }


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
