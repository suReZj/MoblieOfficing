package com.r2.scau.moblieofficing.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.allen.library.SuperTextView;
import com.r2.scau.moblieofficing.R;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

/**
 * Created by 嘉进 on 11:19.
 */

public class ContactAdapter extends ContactListAdapter<ContactAdapter.ContactViewHolder>
        implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {

        @Override
        public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.contact_item, parent, false);
            Log.e("ContactViewHolder", "Create");
            return new ContactViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ContactViewHolder holder, int position) {
            Log.e("adapterItem", getItem(position).getName());
            holder.contactST.setLeftTopString(getItem(position).getName());
            holder.contactST.setLeftBottomString(getItem(position).getPhone());
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
            public ContactViewHolder(View view){
                super(view);
                contactST = (SuperTextView) view.findViewById(R.id.st_contact);
            }

        }



}
