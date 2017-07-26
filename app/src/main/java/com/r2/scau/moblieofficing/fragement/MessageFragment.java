package com.r2.scau.moblieofficing.fragement;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.r2.scau.moblieofficing.R;
import com.r2.scau.moblieofficing.adapter.message_Adapter;
import com.r2.scau.moblieofficing.bean.message_Bean;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;

/**
 * Created by 嘉进 on 9:20.
 * 底部导航栏中消息的Fragment
 */

public class MessageFragment extends Fragment {
    private ArrayList<message_Bean> msgList=new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        RecyclerView recyclerView=(RecyclerView)view.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        initMsg();
        message_Adapter message_adapter=new message_Adapter(view.getContext(),msgList);
        recyclerView.setAdapter(message_adapter);


        recyclerView.setOnScrollListener(onScrollListener);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.toolbar_message_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

    private void initMsg(){
        for(int i=0;i<4;i++){
            message_Bean msg=new message_Bean(R.mipmap.ic_launcher_round,"成都","子健鸡鸡18cm子健鸡鸡18cm子健鸡鸡18cm子健鸡鸡18cm子健鸡鸡18cm",i+"",false);
            msgList.add(msg);
        }
        message_Bean msg=new message_Bean(R.mipmap.ic_launcher_round,"成都","子健鸡鸡18cm子健鸡鸡18cm子健鸡鸡18cm子健鸡鸡18cm子健鸡鸡18cm",4+"",true);
        msgList.add(msg);
        for(int i=5;i<10;i++){
            message_Bean msg1=new message_Bean(R.mipmap.ic_launcher_round,"成都","子健鸡鸡18cm子健鸡鸡18cm子健鸡鸡18cm子健鸡鸡18cm子健鸡鸡18cm",i+"",false);
            msgList.add(msg1);
        }
    }

    RecyclerView.OnScrollListener onScrollListener=new RecyclerView.OnScrollListener(){
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            // Could hide open views here if you wanted. //
        }
    };
}
