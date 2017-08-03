package com.r2.scau.moblieofficing.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.r2.scau.moblieofficing.R;
import com.r2.scau.moblieofficing.untils.Contacts;
import com.r2.scau.moblieofficing.untils.OkHttpUntil;
import com.r2.scau.moblieofficing.untils.ToastUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by EdwinCheng on 2017/7/26.
 * 在该页面使用okhttp请求，根据返回的状态进行setresult 还是 网络不通 重新请求
 */

public class FMRenameActivity extends BaseActivity {
    private static final String TAG = "FMRenameActivity";

    private Bundle bundle;

    private Toolbar mtoolbar;
    private TextView titleTV;
    private EditText newname_editText;

    private String renameType;
    private String oldFileName;
    private String newFileName;
    private String basePath;
    private String oldPath;
    private String newPath;

    private OkHttpClient okHttpClient;
    private FormBody formBody;
    private Request request;
    private Message message;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Contacts.FILEMANAGER.FILERENAME_SUCCESS:
                    setResult(RESULT_OK);
                    FMRenameActivity.this.finish();
                    break;

                case Contacts.FILEMANAGER.CREATE_SUCCESS:
                    setResult(RESULT_OK);
                    FMRenameActivity.this.finish();
                    break;
            }
        }
    };

    @Override
    protected void initView() {
        setContentView(R.layout.activity_filemanager_rename);
        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        titleTV = (TextView) findViewById(R.id.toolbar_title);

        newname_editText = (EditText) findViewById(R.id.filemanage_rename_newname);
    }

    @Override
    protected void initData() {
        okHttpClient = new OkHttpClient();
        mtoolbar.setTitle("");
        message = handler.obtainMessage();
        bundle = getIntent().getExtras();
        oldFileName="";
        renameType = bundle.getString("renameType");
        basePath = bundle.getString("path");
        /**
         * 两种情况：1、重命名  2、新建文件夹
         */
        if (renameType.equals("rename")){
            oldFileName = bundle.getString("filename");
            newname_editText.setText(oldFileName);
            titleTV.setText(getResources().getString(R.string.rename));
            oldPath = basePath + "/" + oldFileName;
        }else if(renameType.equals("newfolder")){
            titleTV.setText(getResources().getString(R.string.newfolder));
        }
        FMRenameActivity.this.setSupportActionBar(mtoolbar);
        FMRenameActivity.this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        FMRenameActivity.this.getMenuInflater().inflate(R.menu.toolbar_filemanage_submit_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                FMRenameActivity.this.finish();
                break;

            case R.id.menu_submit:
                newFileName = newname_editText.getText().toString();
                newPath = basePath + "/" + newFileName;
                if(!newFileName.equals("") && !oldFileName.equals(newFileName)){
                    if (renameType.equals("rename")){
                        Log.e(TAG, "oldPath :  " + oldPath );
                        Log.e(TAG, "newPath :  " + newPath);

                        formBody = new FormBody.Builder()
                                .add("oldPath", oldPath)
                                .add("newPath",newPath)
                                .add("userPhone", "123456789010")
                                .build();
                        request = new Request.Builder().url(Contacts.computer_ip + Contacts.file_Server + Contacts.fileRename)
                                .addHeader("cookie", OkHttpUntil.loginSessionID)
                                .post(formBody)
                                .build();
                        okHttpClient.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                // TODO: 10-0-1  请求失败
                                Log.e(TAG,"getFileListFromServer  fail");
                            }
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                // TODO: 10-0-1 请求成功
                                message = handler.obtainMessage();
                                if (response.code() == 200){
                                    message.what = Contacts.FILEMANAGER.FILERENAME_SUCCESS;
                                }else {
                                    Log.e(TAG, "网络请求 错误  "+ response.code() + "   " + response.message() );
                                    message.what = Contacts.FILEMANAGER.FILERENAME_FAILURE;
                                }
                                handler.sendMessage(message);
                            }
                        });
                    }else if (renameType.equals("newfolder")){
                        Log.e(TAG, "basepath" + basePath + "newpath  : " +newPath);
                        formBody = new FormBody.Builder()
                                .add("path", newPath)
                                .add("userPhone", "123456789010")
                                .build();
                        request = new Request.Builder()
                                .url(Contacts.computer_ip + Contacts.file_Server + Contacts.createDir)
                                .addHeader("cookie", OkHttpUntil.loginSessionID)
                                .post(formBody)
                                .build();
                        okHttpClient.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                // TODO: 10-0-1  请求失败
                                Log.e(TAG,"getFileListFromServer  fail");
                            }
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                // TODO: 10-0-1 请求成功
                                message = handler.obtainMessage();
                                if (response.code() == 200){
                                    message.what = Contacts.FILEMANAGER.CREATE_SUCCESS;
                                }else {
                                    Log.e(TAG, "网络请求 错误  "+ response.code() + "   " + response.message() );
                                    message.what = Contacts.FILEMANAGER.CREATE_FAILURE;
                                }
                                handler.sendMessage(message);
                            }
                        });
                    }
                }else if (newFileName.equals("")){
                    ToastUtils.show(FMRenameActivity.this,"文本框为空",Toast.LENGTH_SHORT);
                }else{
                    ToastUtils.show(FMRenameActivity.this,"数据不正确",Toast.LENGTH_SHORT);
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initListener() {
    }

    @Override
    public void onClick(View v) {
    }

}
