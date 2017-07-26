package com.r2.scau.moblieofficing.activity;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.r2.scau.moblieofficing.R;
import com.r2.scau.moblieofficing.adapter.FileManagerAdapter;
import com.r2.scau.moblieofficing.untils.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by EdwinCheng on 2017/7/24.
 *
 */

public class FileList_Activity extends BaseActivity {

    private static final String TAG = "FileList_Activity";
    private RecyclerView fileListRecycler;
    private LinearLayoutManager linearLayoutManager;
    private FileManagerAdapter fileManagerAdapter;
    private Button filelist_editBtn,filelist_newfolderBtn;
    private List<File> fileList;

    private File[] files;
    private String rootPath;
    private Stack<String> currentPathStack;
    private long lastBackPressed = 0;

    private Bundle bundle;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_filelist);

        fileListRecycler = (RecyclerView) findViewById(R.id.filelist_recycler);
        filelist_editBtn = (Button) findViewById(R.id.filelist_editBtn);
        filelist_newfolderBtn = (Button) findViewById(R.id.filelist_newfolderBtn);
    }

    @Override
    protected void initData() {

        if (fileList == null){
            fileList = new ArrayList<>();
            currentPathStack = new Stack<>();
            linearLayoutManager = new LinearLayoutManager(FileList_Activity.this);
        }
        rootPath = Environment.getExternalStorageDirectory().toString();

        files = Environment.getExternalStorageDirectory().listFiles();

        /**
         * Created by EdwinCheng on 2017/7/25.
         * 将根路径push进栈
         * 并将file[]赋值给arraylist
         */
        currentPathStack.push(rootPath);
        for(File f : files){
            fileList.add(f);
        }

        initRecycler();

    }

    private void initRecycler(){
        fileManagerAdapter = new FileManagerAdapter(FileList_Activity.this,fileList);
        fileListRecycler.setLayoutManager(linearLayoutManager);
        fileListRecycler.setAdapter(fileManagerAdapter);
        this.fileManagerAdapter.setOnItemClickLitener(new OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                File file = files[position];
                //对文件的处理
                if (file.isFile()){


                }else {
                    /**
                     * 如果是文件夹
                     * 清除列表数据
                     * 获得目录中的内容，计入列表中
                     * 适配器通知数据集改变
                     */
                    currentPathStack.push("/" + file.getName());
                    showChange(getPathString());
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
                ToastUtils.show(FileList_Activity.this,"长点击position:" +position, Toast.LENGTH_SHORT);
            }
        });
    }

    @Override
    protected void initListener() {
        filelist_editBtn.setOnClickListener(FileList_Activity.this);
        filelist_newfolderBtn.setOnClickListener(FileList_Activity.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.filelist_editBtn:
                break;
            case R.id.filelist_newfolderBtn:
                break;
        }
    }

    //显示改变data之后的文件数据列表
    private void showChange(String path) {
        files = new File(path).listFiles();

        fileList.clear();
        for (File f : files) {
            fileList.add(f);
        }
        files =  fileManagerAdapter.setFileList(fileList);
    }

    //显示当前路径
    private String getPathString(){
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
        if (currentPathStack.peek() == rootPath){
            FileList_Activity.this.finish();

        }else{
            currentPathStack.pop();
            showChange(getPathString());
        }
    }
}
