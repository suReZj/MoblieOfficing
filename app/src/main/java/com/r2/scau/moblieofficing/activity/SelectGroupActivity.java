package com.r2.scau.moblieofficing.activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.r2.scau.moblieofficing.R;
import com.r2.scau.moblieofficing.adapter.GroupAdapter;
import com.r2.scau.moblieofficing.gson.GsonGroup;
import com.r2.scau.moblieofficing.untils.UserUntil;

import java.util.ArrayList;
import java.util.List;

public class SelectGroupActivity extends BaseActivity {

    private Toolbar mToolbar;
    private TextView mTitleTV;
    private GroupAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_select_group);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTitleTV = (TextView) findViewById(R.id.toolbar_title);

        mToolbar.setTitle("");
        mTitleTV.setText("选择群组");

        List<GsonGroup> groupList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        groupList = UserUntil.createGroupList;
        adapter = new GroupAdapter(this, groupList, 0);
        RecyclerView rv = (RecyclerView) findViewById(R.id.rv_group_select);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);
        rv.setItemAnimator(new DefaultItemAnimator());


        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
