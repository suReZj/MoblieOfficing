package com.r2.scau.moblieofficing.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.r2.scau.moblieofficing.R;
import com.r2.scau.moblieofficing.activity.OnItemClickLitener;
import com.r2.scau.moblieofficing.untils.DateUtils;

import java.io.File;
import java.util.List;

/**
 * Created by EdwinCheng on 2017/8/1.
 *
 * 文件上传的时候，自定义重写的一个本地文件浏览器
 */

public class FMUploadSelectFileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<File> selectfilelists;
    private String selectFilePath;

    private OnItemClickLitener mOnItemClickLitener;
    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    public FMUploadSelectFileAdapter(Context mContext, List<File> selectfilelists){
        this.mContext = mContext;
        this.selectfilelists = selectfilelists;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.template_fileitem, parent, false);
        return new FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final FileViewHolder fileViewHolder = (FileViewHolder) holder;
        fileViewHolder.file_more.setVisibility(View.GONE);

        if (selectfilelists.get(position).isDirectory()){
            fileViewHolder.file_image.setImageURI("res://com.r2.scau.moblieofficing/" + R.mipmap.ic_folder);
            fileViewHolder.file_size.setText(R.string.folder);
        }else if (selectfilelists.get(position).isFile()){
            fileViewHolder.file_image.setImageURI("res://com.r2.scau.moblieofficing/" + R.mipmap.ic_file);
            fileViewHolder.file_size.setText(FileManagerAdapter.generateSize(selectfilelists.get(position).length()));
        }else {
            fileViewHolder.file_image.setImageURI("res://com.r2.scau.moblieofficing/" + R.mipmap.ic_systemfile);
            fileViewHolder.file_size.setText(FileManagerAdapter.generateSize(R.string.system_file));
        }
        fileViewHolder.file_name.setText(selectfilelists.get(position).getName());
        fileViewHolder.file_date.setText(DateUtils.timete(String.valueOf(selectfilelists.get(position).lastModified())));

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

    }

    public void setFileList(List<File> data) {
        this.selectfilelists = data;
        this.notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        if(selectfilelists != null && selectfilelists.size() != 0)
            return selectfilelists.size();
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
}
