package com.r2.scau.moblieofficing.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.r2.scau.moblieofficing.R;
import com.r2.scau.moblieofficing.activity.OnItemClickLitener;
import com.r2.scau.moblieofficing.utils.Contacts;
import com.r2.scau.moblieofficing.widge.BottomView;

import java.io.File;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by EdwinCheng on 2017/7/24.
 *
 * 文件管理中的recyclerView适配器
 */

public class FileManagerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context mContext;
    private List<File> fileList;
    private DateFormat dateFormat;
    private BottomView mButtomView;
    private int fileType;

    private OnItemClickLitener mOnItemClickLitener;
    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.popwindow_delete:
                    Toast.makeText(mContext, "删除按钮", Toast.LENGTH_SHORT).show();
                    doDelete();
                    break;
                case R.id.popwindow_shareto:
                    Toast.makeText(mContext, "分享按钮", Toast.LENGTH_SHORT).show();

                    break;
                case R.id.popwindow_rename:
                    Toast.makeText(mContext, "重命名", Toast.LENGTH_SHORT).show();

                    break;
                case R.id.popwindow_move:
                    Toast.makeText(mContext, "移动", Toast.LENGTH_SHORT).show();

                    break;
                default:

                    break;
            }
        }
    };

    private void doDelete(){

    }

    public FileManagerAdapter(Context mContext,List<File> fileList) {
        super();

        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        this.mContext = mContext;
        this.fileList = fileList;
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
            if (fileList.get(position).isDirectory()){
                fileViewHolder.file_image.setImageURI("res://com.r2.scau.moblieofficing/" + R.mipmap.ic_folder);
                fileViewHolder.file_size.setText(R.string.folder);
            }else {
                fileViewHolder.file_image.setImageURI("res://com.r2.scau.moblieofficing/" + R.mipmap.ic_file);
                fileViewHolder.file_size.setText(generateSize(fileList.get(position)));
            }
            fileViewHolder.file_name.setText(fileList.get(position).getName());
            fileViewHolder.file_date.setText(dateFormat.format(new Date(fileList.get(position).lastModified())));

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
                    if (fileList.get(position).isDirectory()){
                        fileType = Contacts.FILEMANAGER.FOLDER_TYPE;
                    }else{
                        fileType = Contacts.FILEMANAGER.FILE_TYPE;
                    }
                    mButtomView = new BottomView((Activity) mContext,fileType,fileList.get(position).getName(),clickListener);
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
    public static String generateSize(File file) {
        if (file.isFile()) {
            long result = file.length();
            long gb = 2 << 29;
            long mb = 2 << 19;
            long kb = 2 << 9;
            if (result < kb) {
                return result + "B";
            } else if (result >= kb && result < mb) {
                return String.format("%.2fKB", result / (double) kb);
            } else if (result >= mb && result < gb) {
                return String.format("%.2fMB", result / (double) mb);
            } else if (result >= gb) {
                return String.format("%.2fGB", result / (double) gb);
            }
        }
        return null;
    }

    public File[] setFileList(List<File> fileList){
        this.fileList = fileList;

        this.notifyDataSetChanged();
        File[] files = new File[fileList.size()];

        for (int i=0;i<files.length;i++){
            files[i] = fileList.get(i);
        }
        return files;
    }

    public File[] setFileList(){
        File[] files = new File[fileList.size()];
        for (int i=0;i<files.length;i++){
            files[i] = fileList.get(i);
        }
        return files;
    }

    /**
     * Created by EdwinCheng on 2017/7/24.
     *
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
