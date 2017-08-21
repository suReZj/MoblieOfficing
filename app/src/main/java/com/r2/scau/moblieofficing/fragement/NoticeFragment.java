package com.r2.scau.moblieofficing.fragement;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.r2.scau.moblieofficing.gson.GsonNotice;
import com.r2.scau.moblieofficing.gson.GsonNotices;
import com.r2.scau.moblieofficing.gson.GsonUser;
import com.r2.scau.moblieofficing.gson.GsonUsers;
import com.r2.scau.moblieofficing.retrofit.IFriendInfoByIdBiz;
import com.r2.scau.moblieofficing.retrofit.INoticeBiz;
import com.r2.scau.moblieofficing.untils.OkHttpUntil;
import com.r2.scau.moblieofficing.untils.UserUntil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.r2.scau.moblieofficing.Contants.REFLASH_NOTICE_FIRST;
import static com.r2.scau.moblieofficing.Contants.REFLASH_NOTICE_MORE;
import static com.r2.scau.moblieofficing.Contants.getInfo;

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
    private Integer startPort = 0;
    private NoticeAdapter adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
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
                    case REFLASH_NOTICE_FIRST:
                        Log.e(creators.toString(), "REFLASH_NOTICE_FIRST");
                        adapter.removeALL();
                        adapter.addAll(notices, creators);
                        break;
                    case REFLASH_NOTICE_MORE:
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
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_notice);

        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_dark, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_green_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){

            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                mSwipeRefreshLayout.setEnabled(false);
                getNotices(0, Contants.REFLASH_NOTICE_FIRST);
            }
        });

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

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        adapter = new NoticeAdapter(mContext, notices, creators);
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.rv_unread_notice);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);
        rv.setItemAnimator(new DefaultItemAnimator());
        getNotices(0, Contants.REFLASH_NOTICE_FIRST);

    }

    public void getNotices(int n, final int type) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Contants.SERVER_IP + "/group/")
                .callFactory(OkHttpUntil.getInstance())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        INoticeBiz iNoticeBiz = retrofit.create(INoticeBiz.class);
        Call<GsonNotices> call = iNoticeBiz.getNotice(UserUntil.phone, n);
        call.enqueue(new Callback<GsonNotices>() {
            @Override
            public void onResponse(Call<GsonNotices> call, Response<GsonNotices> response) {
                if (response.body().getCode() == 200){
                    notices = response.body().getAnnouncementList();
                    startPort = response.body().getStartPos();
                    getCreater(type);
                }
            }

            @Override
            public void onFailure(Call<GsonNotices> call, Throwable t) {

            }
        });

    }


    public void getCreater(final int type){
        int n = notices.size();
        for (int i = 0; i < n; i++){
            final GsonNotice notice = notices.get(i);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Contants.SERVER_IP + getInfo + "/")
                    .callFactory(OkHttpUntil.getInstance())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            IFriendInfoByIdBiz iFriendInfoByIdBiz = retrofit.create(IFriendInfoByIdBiz.class);
            iFriendInfoByIdBiz.getInfoById(notice.getAcreateduserid())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<GsonUsers>() {
                        private Disposable disposable;

                        @Override
                        public void onSubscribe(@NonNull Disposable d) {
                            disposable = d;
                        }

                        @Override
                        public void onNext(@NonNull GsonUsers gsonUsers) {
                            if (gsonUsers.getCode() == 200) {
                                GsonUser gsonUser = gsonUsers.getUserInfo();
                                creators.add(gsonUser.getNickname());
                                if(creators.size() == notices.size()){
                                    if(type == Contants.REFLASH_NOTICE_FIRST){
                                        Message msg = Message.obtain();
                                        msg.what = Contants.REFLASH_NOTICE_FIRST;
                                        mHandler.sendMessage(msg);
                                    }



                                    if(type == Contants.REFLASH_NOTICE_MORE){
                                        Message msg = Message.obtain();
                                        msg.what = Contants.REFLASH_NOTICE_MORE;
                                        mHandler.sendMessage(msg);
                                    }

                                }
                                Log.d("getCreaterInfo", "success");
                            }
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            disposable.dispose();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });

        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }

   /* @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshNoticeEvent(RefreshNoticeEvent event) {
        //向其他人发起聊天时接收到的事件
        getNotices();
        adapter.addAll(notices, creators);
    }*/

}
