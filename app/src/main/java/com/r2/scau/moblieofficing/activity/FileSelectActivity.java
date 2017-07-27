package com.r2.scau.moblieofficing.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.r2.scau.moblieofficing.R;

/**
 * Created by EdwinCheng on 2017/7/25.
 */

public class FileSelectActivity extends BaseActivity {
    private Button personalFile,sharedFile;
    private Bundle bundle;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_filemanager);

        personalFile = (Button) findViewById(R.id.filemanager_myfileBtn);
        sharedFile = (Button) findViewById(R.id.filemanager_sharedfileBtn);
    }

    @Override
    protected void initData() {
        bundle = new Bundle();
    }

    @Override
    protected void initListener() {
        personalFile.setOnClickListener(FileSelectActivity.this);
        sharedFile.setOnClickListener(FileSelectActivity.this);
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
