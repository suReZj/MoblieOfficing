package com.r2.scau.moblieofficing.fragement;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.r2.scau.moblieofficing.Contants;
import com.r2.scau.moblieofficing.R;
import com.r2.scau.moblieofficing.activity.SelectGroupActivity;
import com.r2.scau.moblieofficing.adapter.NoticeAdapter;
import com.r2.scau.moblieofficing.event.RefreshNoticeEvent;
import com.r2.scau.moblieofficing.gson.GsonGroup;
import com.r2.scau.moblieofficing.gson.GsonNotice;
import com.r2.scau.moblieofficing.gson.GsonNotices;
import com.r2.scau.moblieofficing.retrofit.INoticeBiz;
import com.r2.scau.moblieofficing.untils.OkHttpUntil;
import com.r2.scau.moblieofficing.untils.UserUntil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 嘉进 on 9:21.
 * 底部导航栏中公告的Fragment
 */

public class NoticeFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Context mContext;
    private Toolbar mToolbar;
    private TextView titleTV;
    private Handler mHandler;
    private NoticeAdapter adapter;
    private List<GsonNotice> notices = new ArrayList<>();
    private List<String> creators = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notice, container, false);
        mContext = getActivity();
        initView();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case Contants.REFLASH_NOTICE:
                        adapter.addAll(notices, creators);
                        break;
                }
            }
        };
        return view;
    }

    public void initView() {

        titleTV = (TextView) view.findViewById(R.id.toolbar_title);
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mToolbar.inflateMenu(R.menu.toobar_notice_menu);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_notice:
                        Intent intent = new Intent(mContext, SelectGroupActivity.class);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });
        setHasOptionsMenu(true);
        mToolbar.setTitle("");
        titleTV.setText("公告");

        getNotices();
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        adapter = new NoticeAdapter(mContext, notices, creators);
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.rv_unread_notice);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);
        rv.setItemAnimator(new DefaultItemAnimator());


    }

    public void getNotices() {
        List<GsonGroup> groups = UserUntil.groupList;

        int size = groups.size();
        for (int i = 0; i < size; i++) {
            final GsonGroup group = groups.get(i);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Contants.SERVER_IP + "/group/")
                    .callFactory(OkHttpUntil.getInstance())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            INoticeBiz iNoticeBiz = retrofit.create(INoticeBiz.class);
            Call<GsonNotices> call = iNoticeBiz.getNotice(UserUntil.phone, (long) group.getGid());
            call.enqueue(new Callback<GsonNotices>() {
                @Override
                public void onResponse(Call<GsonNotices> call, Response<GsonNotices> response) {
                    if (response.body() != null && response.body().getCode() == 200) {
                        notices.addAll(response.body().getAnnouncementList());
                        for (int i = 0; i  < notices.size(); i++){
                            creators.add(group.getGname());
                        }
                        Log.e("notice", notices.size() + "");
                    }else {
                        Log.e("notice", response.toString());
                    }
                }

                @Override
                public void onFailure(Call<GsonNotices> call, Throwable t) {
                    Log.e("notice", t.toString());
                }
            });

        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshNoticeEvent(RefreshNoticeEvent event) {
        //向其他人发起聊天时接收到的事件
        getNotices();
        adapter.addAll(notices, creators);
    }

}
