package com.r2.scau.moblieofficing.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.quicksidebar.QuickSideBarTipsView;
import com.bigkoo.quicksidebar.QuickSideBarView;
import com.bigkoo.quicksidebar.listener.OnQuickSideBarTouchListener;
import com.r2.scau.moblieofficing.R;
import com.r2.scau.moblieofficing.adapter.ContactAdapter;
import com.r2.scau.moblieofficing.bean.Contact;
import com.r2.scau.moblieofficing.untils.UserUntil;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class FriendActivity extends BaseActivity implements OnQuickSideBarTouchListener {

    private Toolbar toolbar;
    private TextView titleTV;
    private SearchView mSearchView;
    private ContactAdapter adapter;
    private RecyclerView mRecyclerView;
    private List<Contact> mContactList = new ArrayList<>();
    private HashMap<String, Integer> letters = new HashMap<>();
    private QuickSideBarView mQuickSideBarView;
    private QuickSideBarTipsView mQuickSideBarTipsView;


    @Override
    protected void initView() {
        setContentView(R.layout.activity_contact);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_contact);
        mSearchView = (SearchView) findViewById(R.id.sv_contact);
        mQuickSideBarView = (QuickSideBarView) findViewById(R.id.qsbv);
        mQuickSideBarTipsView = (QuickSideBarTipsView) findViewById(R.id.qsbtv);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        titleTV = (TextView) findViewById(R.id.toolbar_title);

        toolbar.setTitle("");
        titleTV.setText("好友");
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setQueryHint("请输入搜索内容");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {
        mContactList.clear();

        mContactList = UserUntil.friendList;
        ArrayList<String> customLetters = new ArrayList<>();
        Collections.sort(mContactList);
        int position = 0;
        for (Contact contact : mContactList) {
            String letter = contact.getFirstLetter();
            if (!letters.containsKey(letter)) {
                letters.put(letter, position);
                customLetters.add(letter);
            }
            position++;
        }
        mQuickSideBarView.setLetters(customLetters);

        initRV();
    }



    public void initRV() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);

        adapter = new ContactAdapter(getApplicationContext());
        adapter.addAll(mContactList);

        // Add the sticky headers decoration
        StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(adapter);

        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(headersDecor);
    }

    @Override
    protected void initListener() {
        mQuickSideBarView.setOnQuickSideBarTouchListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toobar_friend_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_friend:
                Intent intent = new Intent(FriendActivity.this, AddFriendActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onLetterChanged(String letter, int position, float y) {
        mQuickSideBarTipsView.setText(letter, position, y);
        //有此key则获取位置并滚动到该位置
        if (letters.containsKey(letter)) {
            mRecyclerView.scrollToPosition(letters.get(letter));
        }
    }

    @Override
    public void onLetterTouching(boolean touching) {
        //可以自己加入动画效果渐显渐隐
        mQuickSideBarTipsView.setVisibility(touching ? View.VISIBLE : View.INVISIBLE);
    }



}
