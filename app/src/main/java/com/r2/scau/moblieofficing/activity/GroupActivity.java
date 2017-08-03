package com.r2.scau.moblieofficing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.r2.scau.moblieofficing.R;
import com.r2.scau.moblieofficing.adapter.ViewPagerAdapter;
import com.r2.scau.moblieofficing.fragement.GroupCreateFragment;
import com.r2.scau.moblieofficing.fragement.GroupJoinFragment;

import java.util.ArrayList;
import java.util.List;

public class GroupActivity extends BaseActivity {
    private Toolbar toolbar;
    private TextView titleTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_group);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        titleTV = (TextView) findViewById(R.id.toolbar_title);

        titleTV.setText("我的群组");
        toolbar.setTitle("");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViewPage();
    }

    public void initViewPage(){
        ViewPager viewPage = (ViewPager) findViewById(R.id.vp_group);
        TabLayout tableLayout = (TabLayout) findViewById(R.id.tabLayout_group);

        List<Fragment> fragmentList = new ArrayList<>();
        List<String> titleList = new ArrayList<>();
        GroupCreateFragment createFragment = new GroupCreateFragment();
        GroupJoinFragment joinFragment = new GroupJoinFragment();
        fragmentList.add(createFragment);
        fragmentList.add(joinFragment);

        titleList.add("我创建的");
        titleList.add("我加入的");

        tableLayout.setTabMode(TabLayout.MODE_FIXED);
        tableLayout.addTab(tableLayout.newTab().setText(titleList.get(0)));
        tableLayout.addTab(tableLayout.newTab().setText(titleList.get(1)));

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentList, titleList);
        viewPage.setAdapter(adapter);
        tableLayout.setupWithViewPager(viewPage);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toobar_group_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            case R.id.menu_group:
                Intent intent = new Intent(GroupActivity.this, EditGroupActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {

    }
}
