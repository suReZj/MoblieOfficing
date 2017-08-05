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
import com.r2.scau.moblieofficing.event.GroupJoinEvent;
import com.r2.scau.moblieofficing.gson.GsonGroup;
import com.r2.scau.moblieofficing.gson.GsonGroups;
import com.r2.scau.moblieofficing.retrofit.IGroupBiz;
import com.r2.scau.moblieofficing.Contants;
import com.r2.scau.moblieofficing.untils.OkHttpUntil;
import com.r2.scau.moblieofficing.untils.UserUntil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.r2.scau.moblieofficing.Contants.GET_GROUP_CREATE;


/**
 * Created by 嘉进 on 15:35.
 */

public class GroupCreateFragment extends Fragment{
    private View view;
    private Context mContext;
    private Handler mHandler;
    private GroupAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.e("groupCreate", "create");
        view = inflater.inflate(R.layout.fragment_group, container, false);
        mContext = getActivity();
        initView();
        getGroupInfo();
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case GET_GROUP_CREATE:
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
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.rv_group);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);
        rv.setItemAnimator(new DefaultItemAnimator());
    }

    public void getGroupInfo(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Contants.SERVER_IP + "/group/")
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
                    List<GsonGroup> createGroupList = new ArrayList<GsonGroup>();
                    List<GsonGroup> joinGroupList = new ArrayList<GsonGroup>();
                    List<GsonGroup> groupList = response.body().getListGroupId();
                    for(GsonGroup gsonGroup : groupList){
                        Log.e(gsonGroup.getGcreateduserid() + "", UserUntil.gsonUser.getId()+ "");
                        if(gsonGroup.getGcreateduserid() == UserUntil.gsonUser.getId()){
                            createGroupList.add(gsonGroup);
                        }else {
                            joinGroupList.add(gsonGroup);
                        }
                    }
                    UserUntil.createGroupList = createGroupList;
                    UserUntil.joinGroupList = joinGroupList;
                    EventBus.getDefault().post(new GroupJoinEvent());
                    Message msg = Message.obtain();
                    msg.what = GET_GROUP_CREATE;
                    msg.obj = createGroupList;
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
