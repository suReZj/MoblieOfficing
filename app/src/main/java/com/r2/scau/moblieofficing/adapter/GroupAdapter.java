package com.r2.scau.moblieofficing.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.allen.library.SuperTextView;
import com.r2.scau.moblieofficing.R;
import com.r2.scau.moblieofficing.activity.GroupInfoActivity;
import com.r2.scau.moblieofficing.activity.SendNoticeActivity;
import com.r2.scau.moblieofficing.gson.GsonGroup;
import com.r2.scau.moblieofficing.untils.ImageUtils;

import java.util.Collection;
import java.util.List;

/**
 * Created by 嘉进 on 19:51.
 */

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {

    private int type;
    private Context context;
    private List<GsonGroup> groupList;

    public GroupAdapter(Context context, List<GsonGroup> groupList, int type) {
        this.context = context;
        this.groupList = groupList;
        this.type = type;
    }

    @Override
    public GroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.template_group, parent,
                false);
        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GroupViewHolder holder, final int position) {
        final GsonGroup group = groupList.get(position);
        holder.groupST.setLeftString(group.getGname());
        holder.icon.setImageDrawable(ImageUtils.getIcon(group.getGname(), 32));
        if (type == 0) {
            holder.groupST.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
                @Override
                public void onSuperTextViewClick() {
                    openSendNoticeActivity(groupList.get(position).getGid());
                }
            });
        }
        if (type == 1) {
            holder.groupST.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener(){
                @Override
                public void onSuperTextViewClick() {
                    Intent intent=new Intent(context, GroupInfoActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("Id",groupList.get(position).getGid());
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    public static class GroupViewHolder extends RecyclerView.ViewHolder {
        SuperTextView groupST;
        ImageView icon;

        public GroupViewHolder(View view) {
            super(view);
            groupST = (SuperTextView) view.findViewById(R.id.st_group);
            icon = (ImageView) view.findViewById(R.id.circle_image);
        }
    }

    public void openSendNoticeActivity(int id) {
        Intent intent = new Intent(context, SendNoticeActivity.class);
        intent.putExtra("groupId", id);
        context.startActivity(intent);
    }

    public void add(GsonGroup object) {
        groupList.add(object);
        notifyDataSetChanged();
    }

    public void addAll(Collection<? extends GsonGroup> collection) {
        if (collection != null) {
            groupList.clear();
            groupList.addAll(collection);
            notifyDataSetChanged();
        }
    }
}
