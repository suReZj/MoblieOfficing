package com.r2.scau.moblieofficing.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.r2.scau.moblieofficing.R;
import com.r2.scau.moblieofficing.activity.FMRenameActivity;
import com.r2.scau.moblieofficing.activity.FileListActivity;
import com.r2.scau.moblieofficing.activity.OnItemClickLitener;
import com.r2.scau.moblieofficing.bean.FileBean;
import com.r2.scau.moblieofficing.gson.GsonFileJsonBean;
import com.r2.scau.moblieofficing.untils.Contacts;
import com.r2.scau.moblieofficing.untils.OkHttpUntil;
import com.r2.scau.moblieofficing.widge.BottomView;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by EdwinCheng on 2017/7/24.
 *
 * 文件管理中的recyclerView适配器
 */

public class FileManagerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final String TAG = "ileManagerAdapter";

    private Context mContext;
    private List<FileBean> fileList;
    private BottomView mButtomView;
    private String currentDirPath;
    /**
     * Create by edwincheng in 2017/7/30.
     * 存储当前点击的底栏菜单的position值
     */
    private int bottompos;
    private Intent intent;
    private Bundle bundle;
    private String fileSelectType;

    private OkHttpClient okHttpClient;
    private FormBody formBody;
    private Request request;
    private Message message;
    private Gson gson;

    /**
     * Create by edwincheng in 2017/7/30.
     * 采用handler 进行异步处理
     */

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Contacts.FILEMANAGER.DELETE_SUCCESS:
                    getFileListFromServer(currentDirPath);
                    break;

                case Contacts.FILEMANAGER.GETDIR_SUCCESS:
                    setFileList();
                    break;
            }
        }
    };

    /**
     * Create by edwincheng in 2017/7/30.
     * 底栏的事件监听
     */
    private View.OnClickListener bottomclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.popwindow_delete:
                    doDelete();
                    break;
                case R.id.popwindow_shareto:
                    doShare();
                    break;
                case R.id.popwindow_rename:
                    doRename();
                    break;
                case R.id.popwindow_move:
                    doMove();
                    break;
            }
        }
    };

    private OnItemClickLitener mOnItemClickLitener;
    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public FileManagerAdapter(Context mContext, List<FileBean> fileList, String currentDirPath, String fileSelectType) {
        super();
        this.mContext = mContext;
        this.fileList = fileList;
        this.fileSelectType = fileSelectType;
        this.currentDirPath = currentDirPath;
        gson = new Gson();
        okHttpClient = new OkHttpClient();

    }

    @Override
    public int getItemViewType(int position) {
        if (fileList != null && fileList.size() > 0){
            if(position == fileList.size()) {
                return Contacts.FILEMANAGER.TAIL_VIEW;
            }else if(position < fileList.size()){
                return Contacts.FILEMANAGER.ITEM_VIEW;
            }
        }
        return Contacts.FILEMANAGER.EMPTYVIEW;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == Contacts.FILEMANAGER.ITEM_VIEW){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.template_fileitem, parent, false);
            return new FileViewHolder(view);
        }else if (viewType == Contacts.FILEMANAGER.TAIL_VIEW || viewType == Contacts.FILEMANAGER.EMPTYVIEW){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.template_filetail, parent, false);
            return new FileTailViewHolder(view);
        }
        return null;
    }

    /**
     * Created by EdwinCheng on 2017/7/24.
     * 在绑定数据的时候，应该判断类型：文件／文件夹
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if(holder instanceof FileViewHolder){
            final FileViewHolder fileViewHolder = (FileViewHolder) holder;
            if (fileList.get(position).getAttribute() == Contacts.FILEMANAGER.FOLDER_TYPE){
                fileViewHolder.file_image.setImageURI("res://com.r2.scau.moblieofficing/" + R.mipmap.ic_folder);
                fileViewHolder.file_size.setText(R.string.folder);
            }else {
                fileViewHolder.file_image.setImageURI("res://com.r2.scau.moblieofficing/" + R.mipmap.ic_file);
                fileViewHolder.file_size.setText(generateSize(fileList.get(position).getSize()));
            }
            fileViewHolder.file_name.setText(fileList.get(position).getName());
            fileViewHolder.file_date.setText(fileList.get(position).getLastUpdateTime());

            // 监听itemView
            if (mOnItemClickLitener != null){
                fileViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //获取到当前位置
                        int pos = fileViewHolder.getLayoutPosition();
                        mOnItemClickLitener.onItemClick(fileViewHolder.itemView, pos);
                    }
                });
                fileViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v)
                    {
                        int pos = fileViewHolder.getLayoutPosition();
                        mOnItemClickLitener.onItemLongClick(fileViewHolder.itemView, pos);
                        return false;
                    }
                });
            }

            /**
             * Created by EdwinCheng on 2017/7/25.
             * 加载更多的按钮设置监听
             */
            fileViewHolder.file_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottompos = fileViewHolder.getLayoutPosition();
                    mButtomView = new BottomView((Activity) mContext,fileList.get(bottompos).getName(),fileList.get(bottompos).getAttribute(),fileSelectType,bottomclickListener);
                    mButtomView.show();
                }
            });

        }else if(holder instanceof FileTailViewHolder){
            FileTailViewHolder fileTailViewHolder = (FileTailViewHolder) holder;
            fileTailViewHolder.file_currentUsedSpace.setText("用户测试");
            fileTailViewHolder.file_totalSpace.setText("XXX");
        }
    }

    /**
     * Created by EdwinCheng on 2017/7/24.
     * 文件大小的转换方法
     */
    public static String generateSize(long size) {
        if (size!=0) {

            DecimalFormat df = new DecimalFormat("#.00");
            if (size < 1024) {
                return df.format((double) size) + "B";
            } else if (size < 1048576) {
                return  df.format((double) size / 1024) + "KB";
            } else if (size < 1073741824) {
                return df.format((double) size / 1048576) + "MB";
            } else {
                return df.format((double) size / 1073741824) +"GB";
            }
        }
        return null;
    }

    public ArrayList<FileBean> setFileList(List<FileBean> fileList,String currentDirPath){
        this.fileList = fileList;
        this.currentDirPath = currentDirPath;
        this.notifyDataSetChanged();
        return (ArrayList) fileList;
    }

    public void setFileList(){
        setFileList(fileList,currentDirPath);
    }


    private void doDelete(){
        Toast.makeText(mContext, "删除按钮 " + bottompos , Toast.LENGTH_SHORT).show();
        if (fileSelectType.equals("personalfile")){
            String posturl = null;
            if (fileList.get(bottompos).getAttribute() == Contacts.FILEMANAGER.FOLDER_TYPE){
                posturl = Contacts.computer_ip + Contacts.file_Server + Contacts.DeleteDir;
            }else if(fileList.get(bottompos).getAttribute() == Contacts.FILEMANAGER.FILE_TYPE){
                posturl = Contacts.computer_ip + Contacts.file_Server + Contacts.DeleteFile;
            }
            Log.e(TAG, "doDelete: -----> 个人文档--删除操作 url" + posturl);
            formBody = new FormBody.Builder()
                    .add("path", currentDirPath + "/"  + fileList.get(bottompos).getName())
                    .add("userPhone", "123456789010")
                    .build();
            request = new Request.Builder().url(posturl)
                    .addHeader("cookie", OkHttpUntil.loginSessionID)
                    .post(formBody)
                    .build();
        }else if(fileSelectType.equals("sharedfile")){
            formBody = new FormBody.Builder()
                    .add("path",currentDirPath + "/"  + fileList.get(bottompos).getName())
                    .add("groupId","111")
                    .build();
            request = new Request.Builder().url(Contacts.computer_ip + Contacts.file_Server + Contacts.deleteGroupFile)
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
                    message.what = Contacts.FILEMANAGER.DELETE_SUCCESS;
                }else{
                    Log.e(TAG, "网络请求 错误  "+ response.code() + "   " + response.message() );
                    message.what = Contacts.FILEMANAGER.DELETE_FAILURE;
                }
                handler.sendMessage(message);
            }
        });
    }

    private void doShare(){
        Toast.makeText(mContext, "分享按钮"+ bottompos , Toast.LENGTH_SHORT).show();

    }

    private void doRename(){
        Toast.makeText(mContext, "重命名"+ bottompos, Toast.LENGTH_SHORT).show();
        bundle = new Bundle();
        intent = new Intent(mContext, FMRenameActivity.class);
        bundle.putString("renameType","rename");
        bundle.putString("path",currentDirPath);
        bundle.putString("filename",fileList.get(bottompos).getName());
        intent.putExtras(bundle);
        ((Activity)mContext).startActivityForResult(intent,Contacts.RequestCode.RENAME,bundle);
    }

    private void doMove(){
        Toast.makeText(mContext, "移动" + bottompos, Toast.LENGTH_SHORT).show();
    }

    private void getFileListFromServer(String path) {
        if (fileSelectType.equals("personalfile")){
            formBody = new FormBody.Builder()
                    .add("path", path)
                    .add("userPhone", "123456789010")
                    .build();
            request = new Request.Builder().url(Contacts.computer_ip + Contacts.file_Server + Contacts.getDir)
                    .addHeader("cookie", OkHttpUntil.loginSessionID)
                    .post(formBody)
                    .build();
        }else {
            formBody = new FormBody.Builder()
                    .add("groupId", "123456789010")
                    .build();
            request = new Request.Builder().url(Contacts.computer_ip + Contacts.file_Server + Contacts.getGroupDir)
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
                message = handler.obtainMessage();
                if (response.code() == 200){
                    GsonFileJsonBean fileJson = gson.fromJson(response.body().string(),GsonFileJsonBean.class);
                    fileList = FileListActivity.fileJsonToObject(fileJson);
                    message.what = Contacts.FILEMANAGER.GETDIR_SUCCESS;
                }else{
                    Log.e(TAG, "网络请求 错误  "+ response.code() + "   " + response.message() );
                    message.what = Contacts.FILEMANAGER.GETDIR_FAILURE;
                }

                handler.sendMessage(message);
            }
        });
    }

    /**
     * Created by EdwinCheng on 2017/7/24.
     * 实际size = 数组长度 + 1（加载尾部);
     */
    @Override
    public int getItemCount() {
        if(fileList != null && fileList.size() != 0)
            return (fileList.size() + 1);
        return 0;
    }

    class FileViewHolder extends RecyclerView.ViewHolder {
        public SimpleDraweeView file_image;
        public TextView file_name;
        public TextView file_size;
        public TextView file_date;
        public ImageButton file_more;

        public FileViewHolder(View itemView) {
            super(itemView);
            file_image = (SimpleDraweeView) itemView.findViewById(R.id.filelist_image);
            file_name = (TextView) itemView.findViewById(R.id.filelist_filename);
            file_size = (TextView) itemView.findViewById(R.id.filelist_filesize);
            file_date = (TextView) itemView.findViewById(R.id.filelist_filedate);
            file_more = (ImageButton) itemView.findViewById(R.id.filelist_filemore);
        }
    }

    class FileTailViewHolder extends RecyclerView.ViewHolder {
        public TextView file_currentUsedSpace;
        public TextView file_totalSpace;

        public FileTailViewHolder(View itemView) {
            super(itemView);
            this.file_currentUsedSpace = (TextView) itemView.findViewById(R.id.filelist_currentused);
            this.file_totalSpace = (TextView) itemView.findViewById(R.id.filelist_totalsize);
        }
    }
}
