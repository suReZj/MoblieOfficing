package com.r2.scau.moblieofficing.activity;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.r2.scau.moblieofficing.R;
import com.r2.scau.moblieofficing.untils.OkHttpClientManager;
import com.r2.scau.moblieofficing.widge.EDCToolBar;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;

import static com.r2.scau.moblieofficing.untils.OkHttpClientManager.okHttpClient;

public class SendNoticeActivity extends BaseActivity {

    private EditText titleET;
    private EditText authorET;
    private EditText contentET;
    private EDCToolBar mEDCToolBar;


    @Override
    public void initView() {
        setContentView(R.layout.activity_send_notice);

        titleET = (EditText) findViewById(R.id.et_notice_title);
        authorET = (EditText) findViewById(R.id.et_notice_author);
        contentET = (EditText) findViewById(R.id.et_notice_content);
        mEDCToolBar = (EDCToolBar) findViewById(R.id.toolbar_send_notice);

        titleET.setSingleLine();
        authorET.setSingleLine();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        mEDCToolBar.getRightSecondButton().setOnClickListener(this);
    }


    public void send(String title, String author, String content) {
        FormBody formBody = new FormBody.Builder()
                .add("aTitle", title)
                .add("aContent", content)
                .add("aCreatedUserPhone", "12345678900")
                .add("aType", "notice1")
                .build();


        //step 3: 创建请求
        Request request = new Request.Builder().url("http://192.168.13.19:8080/permission/publicAnnouncement.shtml")
                .addHeader("cookie", OkHttpClientManager.loginSessionID)
                .post(formBody)
                .build();

        //step 4： 建立联系 创建Call对象
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // TODO: 17-1-4  请求失败
                Log.e("sendNotice", "fail");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // TODO: 17-1-4 请求成功
                Log.e("sendNotice", response.body().string());

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_rightSecondButton:
                String title = titleET.getText().toString();
                String author = authorET.getText().toString();
                String content = contentET.getText().toString();

                Log.e("notice", "send");
                if (title.equals("") || author.equals("") || content.equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "标题、作者、内容都不能为空", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "合格", Toast.LENGTH_SHORT);
                    toast.show();
                    send(title, author, content);
                }
                break;
        }
    }
}
