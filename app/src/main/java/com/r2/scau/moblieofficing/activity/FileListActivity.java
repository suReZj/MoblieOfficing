package com.r2.scau.moblieofficing.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.r2.scau.moblieofficing.Contants;
import com.r2.scau.moblieofficing.R;
import com.r2.scau.moblieofficing.adapter.FileManagerAdapter;
import com.r2.scau.moblieofficing.bean.FileBean;
import com.r2.scau.moblieofficing.gson.GsonFileJsonBean;
import com.r2.scau.moblieofficing.untils.DateUtils;
import com.r2.scau.moblieofficing.untils.OkHttpUntil;
import com.r2.scau.moblieofficing.untils.ToastUtils;
import com.r2.scau.moblieofficing.untils.UserUntil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by EdwinCheng on 2017/7/24.
 *
 */

public class FileListActivity extends BaseActivity {

    private static final String TAG = "FileListActivity";
    private RecyclerView fileListRecycler;
    private LinearLayoutManager linearLayoutManager;
    private FileManagerAdapter fileManagerAdapter;
    private LinearLayout secondActionBar;
    private Button filelist_editBtn, filelist_newfolderBtn;
    private Toolbar mtoolbar;
    private TextView titleTV;
    private GsonFileJsonBean fileJson;
    private List<FileBean> fileList;
    private String rootPath;
    private Stack<String> currentPathStack;
    private long lastBackPressed = 0;
    private int bottompos;
    /**
     * initState为true时候，是需要初始化的,代表你是第一次加载Adapter
     */
    private boolean initState;
    private String fileSelectType;

    private Intent intent;
    private Bundle bundle;
    private Handler handler;
    private Gson gson;

    private OkHttpClient okHttpClient;
    private FormBody formBody;
    private Request request;
    private Message message;
    private View.OnClickListener bottomclickListener;
    private static final int MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE = 1;

    private long groupId ;
    private String groupName = null;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_filelist);
        fileListRecycler = (RecyclerView) findViewById(R.id.filelist_recycler);
        filelist_editBtn = (Button) findViewById(R.id.filelist_editBtn);
        filelist_newfolderBtn = (Button) findViewById(R.id.filelist_newfolderBtn);
        secondActionBar = (LinearLayout) findViewById(R.id.filelist_secondbar);
        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        titleTV = (TextView) findViewById(R.id.toolbar_title);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case Contants.FILEMANAGER.GETDIR_SUCCESS:
                        //初始化状态
                        if (fileManagerAdapter == null || initState == true) {
                            initRecycler();
                            initState = false;
                        } else if (initState == false) {
                            //非初始化状态
                            fileList = fileManagerAdapter.setFileList(fileList, getPathString());
                        }
                        break;

                    case Contants.FILEMANAGER.FILEDOWNLOAD_ING:
                        Log.e(TAG, "正在下载进度：" + msg.arg1);
                        break;

                    case Contants.FILEMANAGER.FILEDOWNLOAD_SUCCESS:
                        Log.e(TAG, "下载成功");

                        break;

                    case Contants.FILEMANAGER.DELETE_SUCCESS:
                        getFileListFromServer(getPathString(),fileSelectType);
                        break;

                }
            }
        };
    }

    @Override
    protected void initData() {
        mtoolbar.setTitle("");
        FileListActivity.this.setSupportActionBar(mtoolbar);
        FileListActivity.this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        okHttpClient = new OkHttpClient();

        if (fileJson == null) {
            initState = true;
            fileJson = new GsonFileJsonBean();
            fileList = new ArrayList<>();
            currentPathStack = new Stack<>();
            intent = new Intent();
            bundle = new Bundle();
            bundle = getIntent().getExtras();
            gson = new Gson();
            linearLayoutManager = new LinearLayoutManager(FileListActivity.this);
        }

        //判断是"个人文件" 还是公共文件
        bundle = getIntent().getExtras();
        fileSelectType = bundle.getString("intenttype");

        if (fileSelectType.equals("personalfile")) {
            titleTV.setText(R.string.my_file);
        } else if (fileSelectType.equals("shared")) {
            secondActionBar.setVisibility(View.GONE);
            groupId = bundle.getInt("groupId");
            groupName = bundle.getString("groupName");
            titleTV.setText(groupName);
        }
        //默认的路径
        rootPath = "/";
        /**
         * Created by EdwinCheng on 2017/7/25.
         * 将根路径push进栈
         * 并将file[]赋值给arraylist
         */
        currentPathStack.push(rootPath);
        getFileListFromServer(getPathString(), fileSelectType);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        FileListActivity.this.getMenuInflater().inflate(R.menu.toolbar_filemanage_upload_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                FileListActivity.this.finish();
                break;

            case R.id.menu_upload:

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                        ContextCompat.checkSelfPermission(FileListActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(FileListActivity.this ,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE);
                }else {
                    openFileSelectActivity();
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void openFileSelectActivity() {
        bundle.clear();
        bundle.putString("intentType",fileSelectType);
        bundle.putString("remotePath", getPathString());

        if (fileSelectType.equals("personalfile")) {

        } else if (fileSelectType.equals("shared")) {
            bundle.putLong("groupId",groupId);
            bundle.putString("groupName",groupName);
        }

        FileListActivity.this.openActivityForResult(UploadSelectFileActivity.class, bundle, Contants.RequestCode.UPLOAD);
    }

    private void initRecycler() {
        Log.e(TAG, "initRecycler: 文件大小" + fileJson.getTotalSize() );
        fileManagerAdapter = new FileManagerAdapter(FileListActivity.this, fileJson.getTotalSize(), fileList, getPathString(), fileSelectType, bottomclickListener);
        fileListRecycler.setLayoutManager(linearLayoutManager);
        fileListRecycler.setAdapter(fileManagerAdapter);

        this.fileManagerAdapter.setOnItemClickLitener(new OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                FileBean fileBean = fileList.get(position);
                //对文件的处理
                if (fileBean.getAttribute() == Contants.FILEMANAGER.FILE_TYPE) {
                    Log.e(TAG, "点击的是文件" + fileBean.getName());
                    doDowmload(getPathString(), fileBean.getName());

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

    private void doDowmload(String currentDir, final String filename) {
        String downloadPath = currentDir + "/" + filename;
        if (fileSelectType.equals("personalfile")) {
            formBody = new FormBody.Builder()
                    .add("path", downloadPath)
                    .add("userPhone", UserUntil.gsonUser.getUserPhone())
                    .build();
            request = new Request.Builder().url(Contants.SERVER_IP + Contants.file_Server + Contants.filedownload)
                    .addHeader("cookie", OkHttpUntil.loginSessionID)
                    .post(formBody)
                    .build();
        } else {
            formBody = new FormBody.Builder()
                    .add("fileName", filename)
                    .add("groupId", UserUntil.gsonUser.getUserPhone())
                    .build();
            request = new Request.Builder().url(Contants.SERVER_IP + Contants.file_Server + Contants.downLoadGroupFile)
                    .addHeader("cookie", OkHttpUntil.loginSessionID)
                    .post(formBody)
                    .build();
        }

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // TODO: 10-0-1  请求失败
                Log.e(TAG, "getFileListFromServer  fail");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // TODO: 10-0-1 请求成功
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;

                String downloadLocalPath = Environment.getExternalStorageDirectory().toString();
                Log.e(TAG, "downloadLocalPath" + downloadLocalPath);

                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    File downloadFile = new File(downloadLocalPath, filename);
                    fos = new FileOutputStream(downloadFile);

                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        Log.e("h_bl", "progress=" + progress);
                        message = handler.obtainMessage();
                        message.what = Contants.FILEMANAGER.FILEDOWNLOAD_ING;
                        message.arg1 = progress;
                        handler.sendMessage(message);
                    }
                    fos.flush();
                    Log.e("h_bl", "文件下载成功");
                    message = handler.obtainMessage();
                    message.what = Contants.FILEMANAGER.FILEDOWNLOAD_SUCCESS;
                    handler.sendMessage(message);

                } catch (Exception e) {
                    Log.e("h_bl", "文件下载失败");
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }
            }
        });

    }

    @Override
    protected void initListener() {
        filelist_editBtn.setOnClickListener(FileListActivity.this);
        filelist_newfolderBtn.setOnClickListener(FileListActivity.this);

        /**
         * Create by edwincheng in 2017/7/30.
         * 底栏的事件监听
         */
        bottomclickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "view.tag+++++  " + v.getTag());
                bottompos = (int) v.getTag();

                switch (v.getId()) {
                    case R.id.popwindow_delete:
                        doDelete(bottompos);
                        break;
                    case R.id.popwindow_shareto:
                        doShare(bottompos);
                        break;
                    case R.id.popwindow_rename:
                        doRename(bottompos);
                        break;
                    case R.id.popwindow_move:
                        doMove(bottompos);
                        break;
                }
            }
        };

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.filelist_editBtn:
                break;
            case R.id.filelist_newfolderBtn:
                bundle.clear();
                bundle.putString("renameType", "newfolder");
                /**
                 * 应该还要传输当前的路径 ？
                 * Create by edwincheng in 2027/7/27.
                 */
                bundle.putString("path", getPathString());
                openActivityForResult(FMRenameActivity.class, bundle, Contants.RequestCode.CREATE);
                break;
        }
    }

    //显示改变data之后的文件数据列表
    //先清楚filelist，再发起网络请求，获取文件网盘目录
    private void showChange(String path) {
        fileList.clear();
        getFileListFromServer(path, fileSelectType);
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
            FileListActivity.this.finish();
        } else {
            currentPathStack.pop();
            showChange(getPathString());
        }
    }

    /**
     * Create by edwincheng in 2017/7/28.
     * <p>
     * 一个可以将文件Json转化成Object的方法
     */
    public static ArrayList<FileBean> fileJsonToObject(GsonFileJsonBean tempJsonBean) {
        ArrayList<FileBean> tempFileList = new ArrayList<>();
        for (String str : tempJsonBean.getFiles()) {
            String[] fi = str.split(";");
            tempFileList.add(new FileBean(fi[0], Integer.parseInt(fi[1]), DateUtils.timete(fi[2]), Contants.FILEMANAGER.FILE_TYPE));
        }
        for (String str : tempJsonBean.getFolders()) {
            String[] fo = str.split(";");
            tempFileList.add(new FileBean(fo[0], Integer.parseInt(fo[1]), DateUtils.timete(fo[2]), Contants.FILEMANAGER.FOLDER_TYPE));
        }
        return tempFileList;
    }


    private void doDelete(int bottompos){
        Toast.makeText(FileListActivity.this, "删除按钮 " , Toast.LENGTH_SHORT).show();
        if (fileSelectType.equals("personalfile")){
            String posturl = null;
            if (fileList.get(bottompos).getAttribute() == Contants.FILEMANAGER.FOLDER_TYPE){
                posturl = Contants.SERVER_IP + Contants.file_Server + Contants.DeleteDir;
            }else if(fileList.get(bottompos).getAttribute() == Contants.FILEMANAGER.FILE_TYPE){
                posturl = Contants.SERVER_IP + Contants.file_Server + Contants.DeleteFile;
            }
            Log.e(TAG, "doDelete: -----> 个人文档--删除操作 url" + posturl);
            formBody = new FormBody.Builder()
                    .add("path", getPathString() + "/"  + fileList.get(bottompos).getName())
                    .add("userPhone", UserUntil.gsonUser.getUserPhone())
                    .build();
            request = new Request.Builder().url(posturl)
                    .addHeader("cookie", OkHttpUntil.loginSessionID)
                    .post(formBody)
                    .build();
        }else if(fileSelectType.equals("shared")){
            formBody = new FormBody.Builder()
                    .add("path",getPathString() + "/" +fileList.get(bottompos).getName())
                    .add("groupId",String.valueOf(groupId))
                    .build();
            request = new Request.Builder().url(Contants.SERVER_IP + Contants.file_Server + Contants.deleteGroupFile)
                    .addHeader("cookie", OkHttpUntil.loginSessionID)
                    .post(formBody)
                    .build();
        }
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // TODO: 10-0-1  请求失败
                Log.e(TAG,"getFileListFromServer  fail");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // TODO: 10-0-1 请求成功
                //重新get一次当前文件目录，是在handler做操作 ？还是在这里？
                message = handler.obtainMessage();
                if (response.code() == 200){
                    message.what = Contants.FILEMANAGER.DELETE_SUCCESS;
                }else{
                    Log.e(TAG, "网络请求 错误  "+ response.code() + "   " + response.message() );
                    message.what = Contants.FILEMANAGER.DELETE_FAILURE;
                }
                handler.sendMessage(message);
            }
        });
    }

    private void doShare(int bottompos){
//        Toast.makeText(mContext, "分享按钮"+ bottompos , Toast.LENGTH_SHORT).show();

    }

    private void doRename(int bottompos){
        Toast.makeText(FileListActivity.this, "重命名", Toast.LENGTH_SHORT).show();
        bundle.clear();
        intent.setClass(FileListActivity.this, FMRenameActivity.class);
        bundle.putString("renameType","rename");
        bundle.putString("path",getPathString());
        bundle.putString("filename",fileList.get(bottompos).getName());
        intent.putExtras(bundle);
        this.startActivityForResult(intent, Contants.RequestCode.RENAME,bundle);
    }

    private void doMove(int bottompos){
        Toast.makeText(FileListActivity.this, "该动能暂未开放" , Toast.LENGTH_SHORT).show();
    }

    private void  getFileListFromServer(String path,String fileSelectType) {
        if (fileSelectType.equals("personalfile")){
            formBody = new FormBody.Builder()
                    .add("path", path)
                    .add("userPhone", UserUntil.gsonUser.getUserPhone())
                    .build();
            request = new Request.Builder().url(Contants.SERVER_IP + Contants.file_Server + Contants.getDir)
                    .addHeader("cookie", OkHttpUntil.loginSessionID)
                    .post(formBody)
                    .build();
        }else {
            formBody = new FormBody.Builder()
                    .add("groupId", String.valueOf(groupId))
                    .build();
            request = new Request.Builder().url(Contants.SERVER_IP + Contants.file_Server + Contants.getGroupDir)
                    .addHeader("cookie", OkHttpUntil.loginSessionID)
                    .post(formBody)
                    .build();
        }

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
                    Log.e(TAG, "onResponse: totalsizeaaaaaa:  " + fileJson.getTotalSize() );
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case Contants.RequestCode.RENAME:
                if(resultCode == RESULT_OK){
                    ToastUtils.show(FileListActivity.this,"重命名成功", Toast.LENGTH_SHORT);
                    showChange(getPathString());
                }else if(resultCode == RESULT_CANCELED){
                    ToastUtils.show(FileListActivity.this,"用户选择取消", Toast.LENGTH_SHORT);
                }else{

                }
                break;

            case Contants.RequestCode.CREATE:
                if(resultCode == RESULT_OK){
                    ToastUtils.show(FileListActivity.this,"新建文件成功", Toast.LENGTH_SHORT);
                    showChange(getPathString());
                }else if(resultCode == RESULT_CANCELED){
                    ToastUtils.show(FileListActivity.this,"用户选择取消", Toast.LENGTH_SHORT);
                }else{

                }
                break;

            case Contants.RequestCode.UPLOAD:
                if (resultCode == RESULT_OK){
                    ToastUtils.show(FileListActivity.this,"上传文件成功", Toast.LENGTH_SHORT);
                    showChange(getPathString());
                }else if (resultCode == RESULT_CANCELED){
                    ToastUtils.show(FileListActivity.this,"用户选择取消", Toast.LENGTH_SHORT);
                }else{

                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Log.e("permission", "accept");
                openFileSelectActivity();
            } else {
                // Permission Denied
                Toast.makeText(FileListActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
