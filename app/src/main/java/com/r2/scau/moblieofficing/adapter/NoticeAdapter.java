package com.r2.scau.moblieofficing.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.r2.scau.moblieofficing.R;
import com.r2.scau.moblieofficing.bean.Notice;

import java.util.List;

/**
 * Created by 嘉进 on 10:24.
 */

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.NoticeHolder> {

    private Context context;
    private List<Notice> noticeList;

    public NoticeAdapter(Context context, List<Notice> noticeList){
        this.context = context;
        this.noticeList = noticeList;
    }

    @Override
    public NoticeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.template_notice, parent,
                false);
        return new NoticeHolder(view);
    }

    @Override
    public void onBindViewHolder(NoticeHolder holder, int position) {
        Log.e("unreadNotice", "setData");
        holder.title.setText(noticeList.get(position).getTitle());
        holder.authorAndTime.setText(noticeList.get(position).getAuthor());
        holder.content.setText(noticeList.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return noticeList.size();
    }

    public static class NoticeHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView authorAndTime;
        TextView content;
        public NoticeHolder(View view){
            super(view);
            title = (TextView) view.findViewById(R.id.tv_notice_title);
            authorAndTime = (TextView) view.findViewById(R.id.tv_notice_author_time);
            content = (TextView) view.findViewById(R.id.tv_notice_content);
        }
    }
}
