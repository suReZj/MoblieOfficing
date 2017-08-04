package com.r2.scau.moblieofficing.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.r2.scau.moblieofficing.R;

/**
 * Created by EdwinCheng on 2017/7/25.
 */
public class FileTypeSelectActivity extends BaseActivity {
    private Button personalFile,sharedFile;
    private Bundle bundle;
    private Toolbar mToolbar;
    private TextView titleTV;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_filemanager);

        personalFile = (Button) findViewById(R.id.filemanager_myfileBtn);
        sharedFile = (Button) findViewById(R.id.filemanager_sharedfileBtn);
        titleTV = (TextView) findViewById(R.id.toolbar_title);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
    }


    @Override
    protected void initData() {
        bundle = new Bundle();

        mToolbar.setTitle("");
        titleTV.setText(R.string.my_disk);
        FileTypeSelectActivity.this.setSupportActionBar(mToolbar);
        FileTypeSelectActivity.this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initListener() {
        personalFile.setOnClickListener(FileTypeSelectActivity.this);
        sharedFile.setOnClickListener(FileTypeSelectActivity.this);
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
            case R.id.filemanager_myfileBtn:
                bundle.clear();
                bundle.putString("intenttype","personalfile");
                openActivity(FileListActivity.class,bundle);
                break;

            case R.id.filemanager_sharedfileBtn:
                bundle.clear();
                bundle.putString("intenttype","sharedfile");
                openActivity(FileListActivity.class,bundle);
                break;
        }
    }
}
