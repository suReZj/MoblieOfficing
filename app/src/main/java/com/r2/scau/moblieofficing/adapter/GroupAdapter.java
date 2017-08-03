package com.r2.scau.moblieofficing.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.allen.library.SuperTextView;
import com.r2.scau.moblieofficing.R;
import com.r2.scau.moblieofficing.gson.GsonGroup;

import java.util.Collection;
import java.util.List;

/**
 * Created by 嘉进 on 19:51.
 */

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {

    private Context context;
    private List<GsonGroup> groupList;

    public GroupAdapter(Context context, List<GsonGroup> groupList) {
        this.context = context;
        this.groupList = groupList;
    }

    @Override
    public GroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.template_group, parent,
                false);
        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GroupViewHolder holder, int position) {
        holder.groupST.setLeftString(groupList.get(position).getGname());
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    public static class GroupViewHolder extends RecyclerView.ViewHolder{
        SuperTextView groupST;
        public GroupViewHolder(View view){
            super(view);
            groupST = (SuperTextView) view.findViewById(R.id.st_group);
        }
    }

    public void add(GsonGroup object){
        groupList.add(object);
        notifyDataSetChanged();
    }

    public void addAll(Collection<? extends GsonGroup> collection){
        if (collection != null) {
            groupList.addAll(collection);
            notifyDataSetChanged();
        }
    }
}
