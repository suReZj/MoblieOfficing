package com.r2.scau.moblieofficing.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.r2.scau.moblieofficing.activity.FriendsInfoActivity;
import com.r2.scau.moblieofficing.bean.Contact;
import com.r2.scau.moblieofficing.untils.ImageUtils;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

/**
 * Created by 嘉进 on 11:19.
 */

public class ContactAdapter extends ContactListAdapter<ContactAdapter.ContactViewHolder>
        implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {
    private Context mContext;

    public ContactAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
        public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.contact_item, parent, false);
            Log.e("ContactViewHolder", "Create");
            return new ContactViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ContactViewHolder holder, final int position) {
            Log.e("adapterItem", getItem(position).getName());
            Contact contact = getItem(position);
            holder.contactST.setLeftTopString(getItem(position).getName());
            holder.contactST.setLeftBottomString(getItem(position).getPhone());


            holder.contactST.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(mContext, FriendsInfoActivity.class);
                    Contact contact=getItem(position);
                    intent.putExtra("phone",contact.getPhone());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
            });

            if (contact.getPhotoURL() == null || contact.getPhotoURL().equals("")){
                holder.icon.setImageDrawable(ImageUtils.getIcon(contact.getName(), 23));
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

        class ContactViewHolder extends RecyclerView.ViewHolder{
            SuperTextView contactST;
            ImageView icon;
            public ContactViewHolder(View view){
                super(view);
                contactST = (SuperTextView) view.findViewById(R.id.st_contact);
                icon = (ImageView) view.findViewById(R.id.image);
            }

        }



}
