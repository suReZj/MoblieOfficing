package com.r2.scau.moblieofficing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.r2.scau.moblieofficing.Contants;
import com.r2.scau.moblieofficing.R;
import com.r2.scau.moblieofficing.adapter.FileManagerAdapter;
import com.r2.scau.moblieofficing.bean.FileBean;
import com.r2.scau.moblieofficing.gson.GsonFileJsonBean;
import com.r2.scau.moblieofficing.untils.OkHttpUntil;
import com.r2.scau.moblieofficing.untils.ToastUtils;
import com.r2.scau.moblieofficing.untils.UserUntil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.r2.scau.moblieofficing.activity.FileListActivity.fileJsonToObject;

/**
 * Created by EdwinCheng on 2017/8/7.
 *
 * 文件移动的activity  ，recyclerview 适配器采用filelist的适配器
 */

public class FileMoveSelectFileActivity extends BaseActivity{
    private static final String TAG = "FileMoveSelectFile";

    private RecyclerView fileMove_Select_RecyclerView;
    private FileManagerAdapter fileMove_Select_Adapter;
    private LinearLayoutManager linearLayoutManager;

    private Toolbar mtoolbar;
    private TextView titleTV;
    private Button newfolder, movetodir;

    private GsonFileJsonBean fileJson;
    private List<FileBean> fileList;
    private String rootPath;
    private Stack<String> currentPathStack;


    private boolean initState;

    private Handler handler;
    private Gson gson;

    private OkHttpClient okHttpClient;
    private FormBody formBody;
    private Request request;
    private Message message;

    private String oldPath;
    private String filename;


    @Override
    protected void initView() {
        setContentView(R.layout.activity_filemove_fileselect);
        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        titleTV = (TextView) findViewById(R.id.toolbar_title);
        fileMove_Select_RecyclerView = (RecyclerView) findViewById(R.id.filemove_fileselect_recycler);
        newfolder = (Button) findViewById(R.id.filemove_fileselect_newfolder);
        movetodir = (Button) findViewById(R.id.filemove_fileselect_comfirm);


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case Contants.FILEMANAGER.GETDIR_SUCCESS:
                        //初始化状态
                        if (fileMove_Select_Adapter == null || initState == true) {
                            initRecycler();
                            initState = false;
                        } else if (initState == false) {
                            //非初始化状态
                            fileList = fileMove_Select_Adapter.setFileList(fileList, getPathString(),fileJson.getTotalSize());
                        }
                        break;

                    case Contants.FILEMANAGER.MOVEFILE_SUCCESS:
                        setResult(RESULT_OK);
                        FileMoveSelectFileActivity.this.finish();
                        break;

                    case Contants.FILEMANAGER.MOVEFILE_FAILURE:
                        ToastUtils.show(FileMoveSelectFileActivity.this,"文件移动失败", Toast.LENGTH_SHORT);
                        break;

                }
            }
        };

    }

    @Override
    protected void initData() {
        mtoolbar.setTitle("");
        titleTV.setText(R.string.select_target_dir);
        titleTV.setTextSize(12);

        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        okHttpClient = new OkHttpClient();

        oldPath = getIntent().getStringExtra("oldPath");
        filename = getIntent().getStringExtra("fileName");
        Log.e(TAG, "文件移动oldPath: -----> " + oldPath);

        if (fileJson == null) {
            initState = true;
            fileJson = new GsonFileJsonBean();
            fileList = new ArrayList<>();
            currentPathStack = new Stack<>();
            gson = new Gson();
            linearLayoutManager = new LinearLayoutManager(FileMoveSelectFileActivity.this);
        }

        rootPath = "/";

        currentPathStack.push(rootPath);

        getFileListFromServer(getPathString());
    }

    private void initRecycler() {

        fileMove_Select_Adapter = new FileManagerAdapter(FileMoveSelectFileActivity.this, fileJson.getTotalSize(), fileList, getPathString(), TAG);
        fileMove_Select_RecyclerView.setLayoutManager(linearLayoutManager);
        fileMove_Select_RecyclerView.setAdapter(fileMove_Select_Adapter);

        this.fileMove_Select_Adapter.setOnItemClickLitener(new OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                FileBean fileBean = fileList.get(position);
                //对文件的处理
                if (fileBean.getAttribute() == Contants.FILEMANAGER.FILE_TYPE) {

                } else {
                    /**
                     * 如果是文件夹
                     * 清除列表数据
                     * 获得目录中的内容，计入列表中
                     * 适配器通知数据集改变
                     */
                    Log.e(TAG, "点击的是文件夹" + fileBean.getName());

                    currentPathStack.push("/" + fileBean.getName());
                    showChange(getPathString());
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Log.e(TAG,  "长点击position:" + position);
            }
        });
    }

    //显示改变data之后的文件数据列表
    //先清楚filelist，再发起网络请求，获取文件网盘目录
    private void showChange(String path) {
        fileList.clear();
        getFileListFromServer(path);
    }

    //显示当前路径
    private String getPathString() {
        Stack<String> temp = new Stack<>();
        temp.addAll(currentPathStack);
        String result = "";
        while (temp.size() != 0) {
            result = temp.pop() + result;
        }
        return result;
    }

    @Override
    public void onBackPressed() {
        if (currentPathStack.peek() == rootPath) {
            FileMoveSelectFileActivity.this.finish();
        } else {
            currentPathStack.pop();
            showChange(getPathString());
        }
    }

    private void  getFileListFromServer(String path) {

            formBody = new FormBody.Builder()
                    .add("path", path)
                    .add("userPhone", UserUntil.gsonUser.getUserPhone())
                    .build();
            request = new Request.Builder().url(Contants.SERVER_IP + Contants.file_Server + Contants.getDir)
                    .addHeader("cookie", OkHttpUntil.loginSessionID)
                    .post(formBody)
                    .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // TODO: 10-0-1  请求失败
                Log.e(TAG, "getFileListFromServer  fail");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // TODO: 10-0-1 请求成功
                message = handler.obtainMessage();
                if (response.code() == 200){
                    fileJson = gson.fromJson(response.body().string(), GsonFileJsonBean.class);
                    fileList = fileJsonToObject(fileJson);
                    message.what = Contants.FILEMANAGER.GETDIR_SUCCESS;
                }else{
                    Log.e(TAG, "网络请求 错误  "+ response.code() + "   " + response.message() );
                    message.what = Contants.FILEMANAGER.GETDIR_FAILURE;
                }
                handler.sendMessage(message);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                FileMoveSelectFileActivity.this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void initListener() {
        newfolder.setOnClickListener(this);
        movetodir.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.filemove_fileselect_newfolder:
                Bundle bundle = new Bundle();
                bundle.putString("renameType", "newfolder");
                /**
                 * 应该还要传输当前的路径 ？
                 * Create by edwincheng in 2027/7/27.
                 */
                bundle.putString("path", getPathString());
                openActivityForResult(FMRenameActivity.class, bundle, Contants.RequestCode.CREATE);
                break;

            //确认转移到该文件夹
            case R.id.filemove_fileselect_comfirm:
                moveFile();
                break;

        }
    }

    private void moveFile() {
        Log.e(TAG, "moveFile: newfilePath: " +  getPathString() + "/" + filename);
        Log.e(TAG, "moveFile: oldfilePath: " + oldPath);

        FormBody formBody = new FormBody.Builder()
                .add("oldFilePath", oldPath)
                .add("newFilePath", (getPathString() + "/" + filename))
                .add("userPhone", UserUntil.gsonUser.getUserPhone())
                .build();
        Request request = new Request.Builder().url(Contants.SERVER_IP + Contants.file_Server + Contants.moveFile)
                .addHeader("cookie", OkHttpUntil.loginSessionID)
                .post(formBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // TODO: 10-0-1  请求失败
                Log.e(TAG, "getFileListFromServer  fail");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // TODO: 10-0-1 请求成功
                message = handler.obtainMessage();
                if (response.code() == 200){
                    message.what = Contants.FILEMANAGER.MOVEFILE_SUCCESS;
                }else{
                    Log.e(TAG, "网络请求 错误  "+ response.code() + "   " + response.message() );
                    message.what = Contants.FILEMANAGER.MOVEFILE_FAILURE;
                }
                handler.sendMessage(message);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case Contants.RequestCode.CREATE:
                if(resultCode == RESULT_OK){
                    ToastUtils.show(FileMoveSelectFileActivity.this,"新建文件成功", Toast.LENGTH_SHORT);
                    showChange(getPathString());
                }else if(resultCode == RESULT_CANCELED){
                    ToastUtils.show(FileMoveSelectFileActivity.this,"用户选择取消", Toast.LENGTH_SHORT);
                }else{

                }
                break;


        }
    }
}
