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
import com.r2.scau.moblieofficing.event.GroupJoinEvent;
import com.r2.scau.moblieofficing.gson.GsonGroup;
import com.r2.scau.moblieofficing.untils.UserUntil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 嘉进 on 15:35.
 */

public class GroupJoinFragment extends Fragment {

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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    public void initView(){
        List<GsonGroup> groupListJoin = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        adapter = new GroupAdapter(mContext, groupListJoin);
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.rv_group);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);
        rv.setItemAnimator(new DefaultItemAnimator());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGroupJoinEvent(GroupJoinEvent event) {
        adapter.addAll(UserUntil.joinGroupList);
    }


}
