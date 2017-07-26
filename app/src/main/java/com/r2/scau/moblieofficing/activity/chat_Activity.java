package com.r2.scau.moblieofficing.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.r2.scau.moblieofficing.R;
import com.r2.scau.moblieofficing.adapter.chat_message_Adapter;
import com.r2.scau.moblieofficing.bean.chat_message_Bean;
import com.sqk.emojirelease.Emoji;
import com.sqk.emojirelease.FaceFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 张子健 on 2017/7/21 0021.
 */

public class chat_Activity extends AppCompatActivity implements FaceFragment.OnEmojiClickListener {
    private Button emojiBtn;//表情按钮
    private Button sendBtn;//发送按钮
    private EditText editText;//文字输入框
    private RecyclerView recyclerView;//消息recycle
    private SwipeRefreshLayout swipeRefreshLayout;//刷新layout
    private List<chat_message_Bean> chatMessageList = new ArrayList<>();//消息list
    private FaceFragment faceFragment;//表情fragment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        emojiBtn = (Button) findViewById(R.id.emoji_button);
        sendBtn = (Button) findViewById(R.id.senMsg_button);
        editText = (EditText) findViewById(R.id.chat_editText);
        recyclerView = (RecyclerView) findViewById(R.id.chat_recycler);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.chat_swipelayout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.chat_title_toolbar);

        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.toolbar_chat_title_menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        chat_message_Adapter adapter = new chat_message_Adapter(chatMessageList, this);
        recyclerView.setAdapter(adapter);


        faceFragment = FaceFragment.Instance();
        getSupportFragmentManager().beginTransaction().add(R.id.Container, faceFragment).commit();
        getSupportFragmentManager().beginTransaction().hide(faceFragment).commit();


        emojiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFaceFragment();
                closeKeyBoard();
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().length() != 0) {
                    String text = editText.getText().toString();
                    chat_message_Bean msg = new chat_message_Bean(getResources().getDrawable(R.mipmap.ic_launcher_round), text);
                    chatMessageList.add(msg);
                    recyclerView.getAdapter().notifyDataSetChanged();
                    recyclerView.scrollToPosition(chatMessageList.size() - 1);
                    editText.setText(null);
                }
            }
        });

        swipeRefreshLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideFaceFragment();
            }
        });

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideFaceFragment();
            }
        });

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    hideFaceFragment();
                }else {

                }
            }
        });


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideFaceFragment();
                closeKeyBoard();
                return false;
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.toolbar_chat_title_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

    @Override
    public void onEmojiClick(Emoji emoji) {
        if (emoji != null) {
            int index = editText.getSelectionStart();
            Editable editable = editText.getEditableText();
            if (index < 0) {
                editable.append(emoji.getContent());
            } else {
                editable.insert(index, emoji.getContent());
            }
        }
    }

    @Override
    public void onEmojiDelete() {
        String text = editText.getText().toString();
        if (text.isEmpty()) {
            return;
        }
        if ("]".equals(text.substring(text.length() - 1, text.length()))) {
            int index = text.lastIndexOf("[");
            if (index == -1) {
                int action = KeyEvent.ACTION_DOWN;
                int code = KeyEvent.KEYCODE_DEL;
                KeyEvent event = new KeyEvent(action, code);
                editText.onKeyDown(KeyEvent.KEYCODE_DEL, event);
                return;
            }
            editText.getText().delete(index, text.length());
            return;
        }
        int action = KeyEvent.ACTION_DOWN;
        int code = KeyEvent.KEYCODE_DEL;
        KeyEvent event = new KeyEvent(action, code);
        editText.onKeyDown(KeyEvent.KEYCODE_DEL, event);
    }


    //对back按钮的监听
    @Override
    public void onBackPressed() {
        if (!faceFragment.isHidden()) {
            getSupportFragmentManager().beginTransaction().hide(faceFragment).commit();
        } else {
            finish();
            super.onBackPressed();
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();
        //取消editText的选中状态
        if (editText.isFocused()) {
            editText.clearFocus();
        }
    }

    //关闭键盘
    public void closeKeyBoard() {
        //                判断输入法键盘状态，如果输入法打开，强制关闭输入法
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(emojiBtn.getWindowToken(), 0);
        }
    }

    //关闭表情facefragment
    public void hideFaceFragment() {
        getSupportFragmentManager().beginTransaction().hide(faceFragment).commit();
    }

    //显示表情facefragment
    public void showFaceFragment() {
        getSupportFragmentManager().beginTransaction().show(faceFragment).commit();
    }

}

