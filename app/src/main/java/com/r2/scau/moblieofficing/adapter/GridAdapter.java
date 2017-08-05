package com.r2.scau.moblieofficing.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.r2.scau.moblieofficing.R;
import com.r2.scau.moblieofficing.bean.Contact;

import java.util.List;

/**
 * Created by 嘉进 on 15:44.
 */

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.GridViewHolder> implements View.OnClickListener {

    private Context mContext;
    private List<Contact> mContactList;
    private OnItemClickListener mOnItemClickListener = null;

    public GridAdapter(Context context, List<Contact> contactList) {
        mContext = context;
        mContactList = contactList;
    }

    @Override
    public GridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.template_add, parent,
                false);
        view.setOnClickListener(this);
        return new GridViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GridViewHolder holder, int position) {
        if (position < mContactList.size()){
            Contact contact = mContactList.get(position);
            holder.addTextView.setText(contact.getName());
            if (contact.getPhotoURL() == null){
                holder.addImageView.setImageResource(R.mipmap.ic_friend);
            }else {
                Glide.with(mContext)
                        .load(contact.getPhotoURL())
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(holder.addImageView);
            }
        }else {
            holder.addImageView.setImageResource(R.mipmap.ic_add);
            holder.addTextView.setText("添加人员");
        }
        holder.itemView.setTag(position);

    }



    @Override
    public int getItemCount() {
        return mContactList.size() + 1;
    }

    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(view,(int)view.getTag());
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public static interface OnItemClickListener {
        void onItemClick(View view , int position);
    }

    public void addAll(List<Contact> contacts){
        if (contacts != null){
            mContactList.clear();
            mContactList.addAll(contacts);
            notifyDataSetChanged();
        }
    }

    public void delete(int position){
        mContactList.remove(position);
        notifyDataSetChanged();
    }

    class GridViewHolder extends RecyclerView.ViewHolder {
        ImageView addImageView;
        TextView addTextView;
        public GridViewHolder(View view){
            super(view);
            addImageView = (ImageView) view.findViewById(R.id.iv_add);
            addTextView = (TextView) view.findViewById(R.id.tv_add);
        }
    }
}
