package com.r2.scau.moblieofficing.fragement;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bigkoo.quicksidebar.QuickSideBarTipsView;
import com.bigkoo.quicksidebar.QuickSideBarView;
import com.bigkoo.quicksidebar.listener.OnQuickSideBarTouchListener;
import com.r2.scau.moblieofficing.R;
import com.r2.scau.moblieofficing.adapter.ContactAdapter;
import com.r2.scau.moblieofficing.bean.Contact;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.r2.scau.moblieofficing.R.id.quickSideBarTipsView;
import static com.r2.scau.moblieofficing.R.id.quickSideBarView;
import static com.r2.scau.moblieofficing.R.id.recyclerView;

/**
 * Created by 嘉进 on 9:22.
 * 底部导航栏中联系人的Fragment
 */

public class ContactFragment extends Fragment implements OnQuickSideBarTouchListener {

    private View view;
    private Context mContext;
    private RecyclerView mRecyclerView;
    private ContactAdapter adapter;
    private List<Contact> mContactList = new ArrayList<>();
    private ContentResolver mContentResolver;
    private HashMap<String,Integer> letters = new HashMap<>();
    private QuickSideBarView mQuickSideBarView;
    private QuickSideBarTipsView mQuickSideBarTipsView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.e("contactFragment", "create");
        view = inflater.inflate(R.layout.fragment_contact, container, false);
        mContext = getActivity();
        initView();
        return view;
    }

    private void initView(){
        mRecyclerView = (RecyclerView) view.findViewById(recyclerView);
        mQuickSideBarView = (QuickSideBarView) view.findViewById(quickSideBarView);
        mQuickSideBarTipsView = (QuickSideBarTipsView) view.findViewById(quickSideBarTipsView);

        mQuickSideBarView.setOnQuickSideBarTouchListener(this);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        // Add the sticky headers decoration
        mContactList.clear();
        adapter = new ContactAdapter();

        getContact();

        ArrayList<String> customLetters = new ArrayList<>();
        int position = 0;
        for (Contact contact : mContactList){
            String letter = contact.getFirstLetter();
            if(!letters.containsKey(letter)){
                letters.put(letter, position);
                customLetters.add(letter);
            }
            position ++;
        }

        mQuickSideBarView.setLetters(customLetters);
        adapter.addAll(mContactList);
        mRecyclerView.setAdapter(adapter);
        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(adapter);
        mRecyclerView.addItemDecoration(headersDecor);

        // Add decoration for dividers between list items
       // mRecyclerView.addItemDecoration(new DividerDecoration(this));
    }

    @Override
    public void onLetterChanged(String letter, int position, float y) {
        mQuickSideBarTipsView.setText(letter, position, y);
        //有此key则获取位置并滚动到该位置
        if(letters.containsKey(letter)) {
            mRecyclerView.scrollToPosition(letters.get(letter));
        }
    }

    @Override
    public void onLetterTouching(boolean touching) {
        //可以自己加入动画效果渐显渐隐
        mQuickSideBarTipsView.setVisibility(touching? View.VISIBLE:View.INVISIBLE);
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
            cursor = mContext.getContentResolver().query(
                    uri,
                    new String[] { "display_name", "sort_key", "contact_id",
                            "data1" }, null, null, "sort_key");

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
        String key = sortKeyString.substring(0, 1).toUpperCase();
        if (key.matches("[A-Z]")) {
            return key;
        }
        return "#";
    }
}
