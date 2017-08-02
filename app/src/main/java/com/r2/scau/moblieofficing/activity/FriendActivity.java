package com.r2.scau.moblieofficing.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.quicksidebar.QuickSideBarTipsView;
import com.bigkoo.quicksidebar.QuickSideBarView;
import com.bigkoo.quicksidebar.listener.OnQuickSideBarTouchListener;
import com.github.promeg.pinyinhelper.Pinyin;
import com.r2.scau.moblieofficing.R;
import com.r2.scau.moblieofficing.adapter.ContactAdapter;
import com.r2.scau.moblieofficing.bean.Contact;
import com.r2.scau.moblieofficing.gson.GsonFriend;
import com.r2.scau.moblieofficing.retrofit.IFriendBiz;
import com.r2.scau.moblieofficing.untils.OkHttpUntil;
import com.r2.scau.moblieofficing.untils.UserUntil;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FriendActivity extends BaseActivity implements OnQuickSideBarTouchListener {

    private Toolbar toolbar;
    private TextView titleTV;
    private SearchView mSearchView;
    private ContactAdapter adapter;
    private Handler handler;
    private RecyclerView mRecyclerView;
    private List<Contact> mContactList = new ArrayList<>();
    private HashMap<String, Integer> letters = new HashMap<>();
    private QuickSideBarView mQuickSideBarView;
    private QuickSideBarTipsView mQuickSideBarTipsView;


    @Override
    protected void initView() {
        setContentView(R.layout.activity_personal_contact);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        List<Contact> contacts = new ArrayList<Contact>();
                        contacts = (List<Contact>) msg.obj;
                        ArrayList<String> customLetters = new ArrayList<>();
                        int position = 0;
                        for (Contact contact : contacts) {
                            String letter = contact.getFirstLetter();
                            if (!letters.containsKey(letter)) {
                                letters.put(letter, position);
                                customLetters.add(letter);
                            }
                            position++;
                        }
                        mQuickSideBarView.setLetters(customLetters);
                        //mQuickSideBarView.invalidate();

                        adapter.addAll(contacts);
                        adapter.notifyDataSetChanged();
                        break;
                }
            }
        };
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_personal_contact);
        mSearchView = (SearchView) findViewById(R.id.sv_personal_contact);
        mQuickSideBarView = (QuickSideBarView) findViewById(R.id.qsbv_personal);
        mQuickSideBarTipsView = (QuickSideBarTipsView) findViewById(R.id.qsbtv_personal);
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
        getFriend();
        initRV();
    }



    public void initRV() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);

        adapter = new ContactAdapter();
        adapter.addAll(mContactList);

        // Add the sticky headers decoration
        StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(adapter);

        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(headersDecor);
    }

    public void getFriend() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.13.57:8089/user/")
                .callFactory(OkHttpUntil.okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IFriendBiz iFriendBiz = retrofit.create(IFriendBiz.class);
        Call<GsonFriend> call = iFriendBiz.getFriend(UserUntil.phone);
        call.enqueue(new Callback<GsonFriend>() {
            @Override
            public void onResponse(Call<GsonFriend> call, Response<GsonFriend> response) {
                GsonFriend gsonFriend = response.body();
                if (gsonFriend.getCode() == 200) {
                    List<String> friendList = gsonFriend.getListFriendUserphone();
                    ArrayList<Contact> contacts = new ArrayList<Contact>();
                    for (String str : friendList) {
                        Contact contact = new Contact();
                        contact.setPhone(str);
                        contact.setName(str);
                        contact.setFirstLetter(getSortKey(str));
                        contacts.add(contact);
                    }
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = contacts;
                    handler.sendMessage(msg);
                } else {
                    Log.e("getFriend", "fail");
                }
            }

            @Override
            public void onFailure(Call<GsonFriend> call, Throwable t) {
                Log.e("getFriend", "fail");
            }
        });
    }

    @Override
    protected void initListener() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
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


    private static String getSortKey(String sortKeyString) {
        String key = Pinyin.toPinyin(sortKeyString.charAt(0)).substring(0, 1).toUpperCase();
        Log.e(sortKeyString, key);
        if (key.matches("[A-Z]")) {
            return key;
        }
        return "#";
    }


}
