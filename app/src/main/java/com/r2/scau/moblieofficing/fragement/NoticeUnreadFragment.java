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
import com.r2.scau.moblieofficing.adapter.NoticeAdapter;
import com.r2.scau.moblieofficing.bean.Notice;

import java.util.ArrayList;

/**
 * Created by 嘉进 on 11:12.
 */

public class NoticeUnreadFragment extends Fragment {

    private View view;
    private Context mContext;
    private ArrayList<Notice> noticeList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notice_unread, container, false);
        mContext = getActivity();
        initView();
        return view;
    }

    public void initView(){
        Log.e("unreadNotice", "initView");
        initData();
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        NoticeAdapter adapter = new NoticeAdapter(mContext, noticeList);
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.rv_unread_notice);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);
        rv.setItemAnimator(new DefaultItemAnimator());
    }

    public void initData(){
        Log.e("unreadNotice", "initData");
        Notice a = new Notice();
        a.setTitle("test");
        a.setAuthor("deng");
        a.setContent("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

        Notice b = new Notice();
        b.setTitle("test2");
        b.setAuthor("deng2");
        b.setContent("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");

        Notice c = new Notice();
        c.setTitle("test3");
        c.setAuthor("deng3");
        c.setContent("cccccccccccccccccccccccccccccccccccccccccccccccccccccccccccc");

        noticeList.add(a);
        noticeList.add(b);
        noticeList.add(c);
    }
}
