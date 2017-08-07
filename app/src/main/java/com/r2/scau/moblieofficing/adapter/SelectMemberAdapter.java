package com.r2.scau.moblieofficing.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.allen.library.SuperTextView;
import com.bumptech.glide.Glide;
import com.r2.scau.moblieofficing.Contants;
import com.r2.scau.moblieofficing.R;
import com.r2.scau.moblieofficing.bean.Contact;
import com.r2.scau.moblieofficing.untils.ImageUtils;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import cn.refactor.library.SmoothCheckBox;

/**
 * Created by 嘉进 on 10:55.
 */

public class SelectMemberAdapter extends ContactListAdapter<SelectMemberAdapter.ContactViewHolder>
        implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {

    private Context mContext;
    public SelectMemberAdapter (Context context){
        mContext = context;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.template_select_member, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ContactViewHolder holder, final int position) {
        Contact contact = getItem(position);
        holder.contactST.setLeftTopString(contact.getName());
        holder.contactST.setLeftBottomString(contact.getPhone());
        holder.contactST.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener(){
            @Override
            public void onSuperTextViewClick() {
                holder.smoothCheckBox.toggle();
            }
        });

        holder.smoothCheckBox.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                getItem(position).setSelect(isChecked);
            }
        });

        if (contact.getPhotoURL() == null || contact.getPhotoURL().equals("")){
            holder.icon.setImageDrawable(ImageUtils.getIcon(contact.getName(), 32));
        }else {
            Glide.with(mContext)
                    .load(Contants.PHOTO_SERVER_IP + contact.getPhotoURL())
                    .into(holder.icon);
        }

    }

    @Override
    public long getHeaderId(int position) {
        return getItem(position).getFirstLetter().charAt(0);
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_header, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        TextView textView = (TextView) holder.itemView;
        Log.e("adapterHeard", String.valueOf(getItem(position).getFirstLetter()));
        textView.setText(String.valueOf(getItem(position).getFirstLetter()));
    }

    class ContactViewHolder extends RecyclerView.ViewHolder {
        SmoothCheckBox smoothCheckBox;
        ImageView icon;
        SuperTextView contactST;

        public ContactViewHolder(View view) {
            super(view);
            contactST = (SuperTextView) view.findViewById(R.id.st_contact);
            icon = (ImageView) view.findViewById(R.id.circle_image);
            smoothCheckBox = (SmoothCheckBox) view.findViewById(R.id.checkbox_contact);
        }

    }
}