package com.r2.scau.moblieofficing.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.r2.scau.moblieofficing.R;
import com.r2.scau.moblieofficing.gson.GsonNotice;

import java.util.List;

/**
 * Created by 嘉进 on 10:24.
 */

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.NoticeHolder> {

    private Context context;
    private List<GsonNotice> noticeList;
    private List<String> creatorList;


    public NoticeAdapter(Context context, List<GsonNotice> noticeList, List<String> creator) {
        this.context = context;
        this.noticeList = noticeList;
        this.creatorList = creator;
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
        holder.title.setText("标题：" + noticeList.get(position).getAtitle());
        holder.authorAndTime.setText("来自："+ creatorList.get(position));
        holder.content.setText("内容：" + noticeList.get(position).getAcontent());
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

    public void addAll(List<GsonNotice> notices, List<String> creators){
        if (notices != null && creators != null){
            noticeList.clear();
            notices.addAll(notices);
            creatorList.clear();
            creatorList.addAll(creators);
            notifyDataSetChanged();
        }
    }


}
