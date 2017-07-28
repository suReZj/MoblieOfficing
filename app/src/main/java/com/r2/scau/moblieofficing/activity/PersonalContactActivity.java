package com.r2.scau.moblieofficing.activity;

import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;

import com.bigkoo.quicksidebar.QuickSideBarTipsView;
import com.bigkoo.quicksidebar.QuickSideBarView;
import com.bigkoo.quicksidebar.listener.OnQuickSideBarTouchListener;
import com.github.promeg.pinyinhelper.Pinyin;
import com.r2.scau.moblieofficing.R;
import com.r2.scau.moblieofficing.adapter.ContactAdapter;
import com.r2.scau.moblieofficing.bean.Contact;
import com.r2.scau.moblieofficing.widge.EDCToolBar;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PersonalContactActivity extends BaseActivity implements OnQuickSideBarTouchListener {


    private ContactAdapter adapter;
    private EDCToolBar mToolBar;
    private RecyclerView mRecyclerView;
    private SearchView mSearchView;
    private List<Contact> mContactList = new ArrayList<>();
    private HashMap<String, Integer> letters = new HashMap<>();
    private QuickSideBarView mQuickSideBarView;
    private QuickSideBarTipsView mQuickSideBarTipsView;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_personal_contact);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_personal_contact);
        mSearchView = (SearchView)findViewById(R.id.sv_personal_contact);
        mQuickSideBarView = (QuickSideBarView) findViewById(R.id.quickSideBarView);
        mQuickSideBarTipsView = (QuickSideBarTipsView) findViewById(R.id.quickSideBarTipsView);

        mToolBar = (EDCToolBar) findViewById(R.id.toolbar_contact_personal);
        mToolBar.getLeftButton().setOnClickListener(this);

        mSearchView.setIconifiedByDefault(false);
        mSearchView.setQueryHint("请输入搜索内容");

    }

    @Override
    protected void initData() {
        mContactList.clear();

        getContact();
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
        mQuickSideBarView.setOnQuickSideBarTouchListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_leftButton:
                finish();
                break;
        }
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


    /**
     * 作者：邓嘉进
     * 获取联系人信息
     */

    public void getContact() {
        Cursor cursor = null;
        try {

            Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            // 这里是获取联系人表的电话里的信息  包括：名字，名字拼音，联系人id,电话号码；
            // 然后在根据"sort-key"排序
            cursor = getContentResolver().query(
                    uri,
                    new String[]{"display_name", "sort_key", "contact_id",
                            "data1"}, null, null, "sort_key");

            if (cursor.moveToFirst()) {
                do {
                    Contact contact = new Contact();
                    String contact_phone = cursor
                            .getString(cursor
                                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    String name = cursor.getString(0);
                    String sortKey = getSortKey(cursor.getString(1));
                    int contact_id = cursor
                            .getInt(cursor
                                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                    contact.setName(name);
                    contact.setFirstLetter(sortKey);
                    contact.setPhone(contact_phone);
                    //contact.setContact_id(contact_id);
                    if (name != null)
                        mContactList.add(contact);
                } while (cursor.moveToNext());
                // c = cursor;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getSortKey(String sortKeyString) {
        String key = Pinyin.toPinyin(sortKeyString.charAt(0)).substring(0, 1).toUpperCase();
        Log.e(sortKeyString, key);
        if (key.matches("[A-Z]")) {
            return key;
        }
        return "#";
    }

}
