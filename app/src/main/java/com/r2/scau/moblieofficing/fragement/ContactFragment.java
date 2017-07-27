package com.r2.scau.moblieofficing.fragement;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.allen.library.SuperTextView;
import com.r2.scau.moblieofficing.R;
import com.r2.scau.moblieofficing.activity.PersonalContactActivity;

/**
 * Created by 嘉进 on 9:22.
 * 底部导航栏中联系人的Fragment
 */

public class ContactFragment extends Fragment
        /* implements OnQuickSideBarTouchListener*/ {

    private View view;
    private Context mContext;
    private SuperTextView personalContactST;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.e("contactFragment", "create");
        view = inflater.inflate(R.layout.fragment_contact, container, false);
        mContext = getActivity();
        initView();
        return view;
    }

    private void initView(){

        personalContactST = (SuperTextView) view.findViewById(R.id.st_contact_personal);
        personalContactST.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener(){
            @Override
            public void onSuperTextViewClick() {
                Intent intent = new Intent(mContext, PersonalContactActivity.class);
                startActivity(intent);
            }
        });
    }

}
