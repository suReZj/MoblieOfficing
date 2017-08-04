package com.r2.scau.moblieofficing.fragement;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.r2.scau.moblieofficing.gson.GsonGroups;
import com.r2.scau.moblieofficing.retrofit.IGroupBiz;
import com.r2.scau.moblieofficing.untils.OkHttpUntil;
import com.r2.scau.moblieofficing.untils.UserUntil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 嘉进 on 15:35.
 */

public class GroupCreateFragment extends Fragment{
    private View view;
    private Context mContext;
    private Handler mHandler;
    private GroupAdapter adapter;
    public static final int GET_GROUP = 2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.e("groupCreate", "create");
        view = inflater.inflate(R.layout.fragment_group_create, container, false);
        mContext = getActivity();
        initView();
        getGroupInfo();
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case GET_GROUP:
                        adapter.addAll((List<GsonGroup>) msg.obj);
                        break;
                    default:
                        break;
                }
            }
        };
        return view;
    }

    public void initView(){
        List<GsonGroup> groupList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        adapter = new GroupAdapter(mContext, groupList);
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.rv_group_create);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);
        rv.setItemAnimator(new DefaultItemAnimator());
    }

    public void getGroupInfo(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.13.61:8089/group/")
                .callFactory(OkHttpUntil.getInstance())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IGroupBiz iGroupBiz = retrofit.create(IGroupBiz.class);
        Call<GsonGroups> call = iGroupBiz.getGroup(UserUntil.phone);
        call.enqueue(new Callback<GsonGroups>() {
            @Override
            public void onResponse(Call<GsonGroups> call, Response<GsonGroups> response) {
                if(response.body().getCode() == 200){
                    Log.e("getGroup", "success");
                    List<GsonGroup> groupList = response.body().getListGroupId();
                    for (int i = 0; i < groupList.size(); i++){
                        Log.e(groupList.get(i).getGname(), groupList.get(i).getGid() + "");
                    }
                    Message msg = new Message();
                    msg.what = GET_GROUP;
                    msg.obj = groupList;
                    mHandler.sendMessage(msg);
                }else {
                    Log.e("getGroup", "fail");
                }
            }

            @Override
            public void onFailure(Call<GsonGroups> call, Throwable t) {
                Log.e("getGroup", "fail");
            }
        });
    }

}
