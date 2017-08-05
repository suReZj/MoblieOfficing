package com.r2.scau.moblieofficing.fragement;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.r2.scau.moblieofficing.R;
import com.r2.scau.moblieofficing.adapter.GroupAdapter;
import com.r2.scau.moblieofficing.gson.GsonGroup;
import com.r2.scau.moblieofficing.untils.UserUntil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 嘉进 on 15:35.
 */

public class GroupCreateFragment extends Fragment{
    private View view;
    private Context mContext;
    private GroupAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.e("groupCreate", "create");
        view = inflater.inflate(R.layout.fragment_group, container, false);
        mContext = getActivity();
        initView();
        return view;
    }

    public void initView(){
        List<GsonGroup> groupList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        groupList = UserUntil.createGroupList;
        adapter = new GroupAdapter(mContext, groupList);
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.rv_group);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);
        rv.setItemAnimator(new DefaultItemAnimator());
    }



}
