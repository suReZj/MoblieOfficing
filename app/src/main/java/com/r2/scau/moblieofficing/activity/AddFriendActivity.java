package com.r2.scau.moblieofficing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.allen.library.SuperTextView;
import com.r2.scau.moblieofficing.R;

public class AddFriendActivity extends BaseActivity {

    private Toolbar toolbar;
    private TextView titleTV;
    private SuperTextView searchST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_add_friend);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        titleTV = (TextView) findViewById(R.id.toolbar_title);
        searchST = (SuperTextView) findViewById(R.id.st_search_friend);

        toolbar.setTitle("");
        titleTV.setText("添加好友");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        searchST.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener(){
            @Override
            public void onSuperTextViewClick() {
                Intent intent = new Intent(AddFriendActivity.this, SearchPhoneActivity.class);
                startActivity(intent);
            }
        });
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
