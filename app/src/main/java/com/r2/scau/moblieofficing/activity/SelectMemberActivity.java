package com.r2.scau.moblieofficing.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.quicksidebar.QuickSideBarTipsView;
import com.bigkoo.quicksidebar.QuickSideBarView;
import com.r2.scau.moblieofficing.R;
import com.r2.scau.moblieofficing.adapter.ContactAdapter;
import com.r2.scau.moblieofficing.bean.Contact;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.r2.scau.moblieofficing.untils.UserUntil.friendList;

public class SelectMemberActivity extends BaseActivity {

    private TextView mTitleTV;
    private Toolbar mToolbar;
    private ContactAdapter adapter;
    private RecyclerView mRecyclerView;
    private List<Contact> mContactList;
    private HashMap<String, Integer> letters = new HashMap<>();
    private QuickSideBarView mQuickSideBarView;
    private QuickSideBarTipsView mQuickSideBarTipsView;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_contact);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_personal_contact);
        mQuickSideBarView = (QuickSideBarView) findViewById(R.id.qsbv_personal);
        mQuickSideBarTipsView = (QuickSideBarTipsView) findViewById(R.id.qsbtv_personal);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTitleTV = (TextView) findViewById(R.id.toolbar_title);

        mToolbar.setTitle("");
        mTitleTV.setText("选择群成员");

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {
        mContactList = friendList;
        ArrayList<String> customLetters = new ArrayList<>();
        int position = 0;
        for (Contact contact : mContactList) {
            String letter = contact.getFirstLetter();
            if (!letters.containsKey(letter)) {
                letters.put(letter, position);
                customLetters.add(letter);
            }
            position++;
        }

        initRV(customLetters);
    }

    public void initRV(ArrayList<String> customLetters) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);

        adapter = new ContactAdapter();
        mQuickSideBarView.setLetters(customLetters);
        adapter.addAll(mContactList);

        // Add the sticky headers decoration
        StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(adapter);

        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(headersDecor);
    }

    @Override
    protected void initListener() {

    }

    @Override
    public void onClick(View view) {

    }
}
