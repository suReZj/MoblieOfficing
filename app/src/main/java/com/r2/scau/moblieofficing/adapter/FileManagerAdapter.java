package com.r2.scau.moblieofficing.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.r2.scau.moblieofficing.Contants;
import com.r2.scau.moblieofficing.R;
import com.r2.scau.moblieofficing.activity.OnItemClickLitener;
import com.r2.scau.moblieofficing.bean.FileBean;
import com.r2.scau.moblieofficing.widge.BottomView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by EdwinCheng on 2017/7/24.
 *
 * 文件管理中的recyclerView适配器
 */

public class FileManagerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final String TAG = "fileManagerAdapter";

    private Context mContext;
    private List<FileBean> fileList;
    private long totalSize;
    private BottomView mButtomView;
    private String currentDirPath;
    /**
     * Create by edwincheng in 2017/7/30.
     * 存储当前点击的底栏菜单的position值
     */
    private int bottompos;
    private String fileSelectType;

    private View.OnClickListener bottomclickListener;


    private OnItemClickLitener mOnItemClickLitener;
    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public FileManagerAdapter(Context mContext, long totalSize, List<FileBean> fileList, String currentDirPath, String fileSelectType, View.OnClickListener bottomclickListener) {
        super();
        this.mContext = mContext;
        this.fileList = fileList;
        this.totalSize = totalSize;
        this.fileSelectType = fileSelectType;
        this.currentDirPath = currentDirPath;
        this.bottomclickListener = bottomclickListener;

    }

    @Override
    public int getItemViewType(int position) {
        if (fileList != null && fileList.size() > 0){
            if(position == fileList.size()) {
                return Contants.FILEMANAGER.TAIL_VIEW;
            }else if(position < fileList.size()){
                return Contants.FILEMANAGER.ITEM_VIEW;
            }
        }
        return Contants.FILEMANAGER.EMPTYVIEW;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == Contants.FILEMANAGER.ITEM_VIEW){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.template_fileitem, parent, false);
            return new FileViewHolder(view);
        }else if (viewType == Contants.FILEMANAGER.TAIL_VIEW || viewType == Contants.FILEMANAGER.EMPTYVIEW){
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
            if (fileList.get(position).getAttribute() == Contants.FILEMANAGER.FOLDER_TYPE){
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
                    mButtomView = new BottomView((Activity) mContext,fileList.get(bottompos).getName(),fileList.get(bottompos).getAttribute(), bottompos, fileSelectType,bottomclickListener);
                    mButtomView.show();
                }
            });

        }else if(holder instanceof FileTailViewHolder){
            FileTailViewHolder fileTailViewHolder = (FileTailViewHolder) holder;
            fileTailViewHolder.file_currentUsedSpace.setText("文件大小:" + generateSize(totalSize));
            fileTailViewHolder.file_totalSpace.setText(",总容量2G");
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
        return "0KB";
    }

    public ArrayList<FileBean> setFileList(List<FileBean> fileList,String currentDirPath, long totalSize){
        this.fileList = fileList;
        this.currentDirPath = currentDirPath;
        this.totalSize = totalSize;

        this.notifyDataSetChanged();
        return (ArrayList) fileList;
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
