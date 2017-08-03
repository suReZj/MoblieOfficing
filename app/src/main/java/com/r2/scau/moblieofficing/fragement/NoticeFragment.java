package com.r2.scau.moblieofficing.fragement;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.r2.scau.moblieofficing.R;
import com.r2.scau.moblieofficing.activity.SendNoticeActivity;
import com.r2.scau.moblieofficing.adapter.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 嘉进 on 9:21.
 * 底部导航栏中公告的Fragment
 */

public class NoticeFragment extends Fragment implements View.OnClickListener{

    private View view;
    private Context mContext;
    private Toolbar mToolbar;
    private Button sendNoticeBtn;
    private TextView titleTV;
    private TextView readTV;
    private TextView unReadTV;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notice, container, false);
        mContext = getActivity();
        initView();
        return view;
    }

    public void initView() {

        titleTV = (TextView) view.findViewById(R.id.toolbar_title);
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mToolbar.inflateMenu(R.menu.toobar_notice_menu);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_notice:
                        Intent intent = new Intent(mContext, SendNoticeActivity.class);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });
        setHasOptionsMenu(true);

        mToolbar.setTitle("");
        titleTV.setText("公告");
        initViewPage();

    }

    public void initViewPage(){
        ViewPager viewPage = (ViewPager) view.findViewById(R.id.vp_notice);
        TabLayout tableLayout = (TabLayout) view.findViewById(R.id.tabLayout_notice);

        List<Fragment> fragmentList = new ArrayList<>();
        List<String> titleList = new ArrayList<>();
        NoticeUnreadFragment unreadFragment = new NoticeUnreadFragment();
        NoticeReadFragment readFragment = new NoticeReadFragment();
        fragmentList.add(unreadFragment);
        fragmentList.add(readFragment);

        titleList.add("未读");
        titleList.add("已读");

        tableLayout.setTabMode(TabLayout.MODE_FIXED);
        tableLayout.addTab(tableLayout.newTab().setText(titleList.get(0)));
        tableLayout.addTab(tableLayout.newTab().setText(titleList.get(1)));

        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(), fragmentList, titleList);
        viewPage.setAdapter(adapter);
        tableLayout.setupWithViewPager(viewPage);
    }

   /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.toobar_notice_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_notice:
                Intent intent = new Intent(mContext, SendNoticeActivity.class);
                startActivity(intent);
                break;
        }

        return true;
    }*/

    @Override
    public void onClick(View view) {
        switch (view.getId()){

        }
    }
}
