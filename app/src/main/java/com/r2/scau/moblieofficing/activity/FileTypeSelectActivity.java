package com.r2.scau.moblieofficing.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.r2.scau.moblieofficing.R;
import com.r2.scau.moblieofficing.adapter.FMTypeSelectAdapter;
import com.r2.scau.moblieofficing.untils.UserUntil;

/**
 * Created by EdwinCheng on 2017/7/25.
 *
 *
 */
public class FileTypeSelectActivity extends BaseActivity {
    private static final String TAG = "FileTypeSelectActivity";
    private Bundle bundle;
    private Toolbar mToolbar;
    private TextView titleTV;

    private RecyclerView fmtypeselectRecycler;
    private FMTypeSelectAdapter fmTypeSelectAdapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_filemanager);

        titleTV = (TextView) findViewById(R.id.toolbar_title);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        fmtypeselectRecycler = (RecyclerView) findViewById(R.id.filetype_select_RV);

    }


    @Override
    protected void initData() {
        bundle = new Bundle();

        mToolbar.setTitle("");
        titleTV.setText(R.string.my_disk);
        linearLayoutManager = new LinearLayoutManager(FileTypeSelectActivity.this);

        FileTypeSelectActivity.this.setSupportActionBar(mToolbar);
        FileTypeSelectActivity.this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initRecycler();

    }

    private void initRecycler() {
        fmTypeSelectAdapter = new FMTypeSelectAdapter(FileTypeSelectActivity.this, UserUntil.groupList);
        fmtypeselectRecycler.setLayoutManager(linearLayoutManager);
        fmtypeselectRecycler.setAdapter(fmTypeSelectAdapter);

        this.fmTypeSelectAdapter.setOnItemClickLitener(new OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.e(TAG, "recyclerView点击 ： " + position);

                if (position == 0){
                    Log.e(TAG, "recyclerView点击 ： " + position);
                    bundle.clear();
                    bundle.putString("intenttype","personalfile");
                    openActivity(FileListActivity.class,bundle);

                }else if (position >1 && position < UserUntil.groupList.size() + 2){
                    Log.e(TAG, "recyclerView点击 ： " + position);
                    bundle.clear();
                    bundle.putString("intenttype","shared");
                    bundle.putInt("groupId",UserUntil.groupList.get(position-2).getGid());
                    bundle.putString("groupName",UserUntil.groupList.get(position-2).getGname());
                    openActivity(FileListActivity.class,bundle);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }

        });
    }

    @Override
    protected void initListener() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                FileTypeSelectActivity.this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
        }
    }
}
