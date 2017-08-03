package com.r2.scau.moblieofficing.activity;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.r2.scau.moblieofficing.R;

public class SearchPhoneActivity extends BaseActivity {

    private Toolbar toolbar;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_search_phone);
        toolbar = (Toolbar) findViewById(R.id.toolbar_search_phone);
        toolbar.setTitle("");

        setSupportActionBar(toolbar);
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
