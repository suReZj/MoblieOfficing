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
import com.r2.scau.moblieofficing.activity.ChatActivity;
import com.r2.scau.moblieofficing.bean.ChatRecord;
import com.r2.scau.moblieofficing.bean.Contact;
import com.r2.scau.moblieofficing.smack.SmackManager;
import com.r2.scau.moblieofficing.untils.DateUtil;
import com.r2.scau.moblieofficing.untils.SharedPrefUtil;
import com.r2.scau.moblieofficing.untils.UserUntil;
import com.r2.scau.moblieofficing.untils.ImageUtils;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import org.greenrobot.eventbus.EventBus;
import org.litepal.crud.DataSupport;

import java.util.List;
import java.util.UUID;

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
            holder.contactST.setLeftTopString(getItem(position).getName());
            holder.contactST.setLeftBottomString(getItem(position).getPhone());


            holder.contactST.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(mContext, ChatActivity.class);
                    Contact contact=new Contact();
                    contact=getItem(position);
                    ChatRecord record;
                    List<ChatRecord> chatRecords = DataSupport.where("mfriendusername=?", contact.getPhone()).find(ChatRecord.class);
                    if(chatRecords.size()==0){
                        record = new ChatRecord();
                        record.setUuid(UUID.randomUUID().toString());
                        record.setmFriendUsername(contact.getPhone());
                        record.setmFriendNickname(contact.getName());
                        record.setmMeUsername(UserUntil.gsonUser.getUserPhone());
                        record.setmMeNickname(UserUntil.gsonUser.getNickname());
                        record.setmChatTime(DateUtil.currentDatetime());
                        record.setmIsMulti(false);
                        record.setmChatJid(SmackManager.getInstance().getChatJid(contact.getPhone()));
                        record.save();
                    }else {
                        record = chatRecords.get(0);
                    }
                    EventBus.getDefault().post(record);
                    intent.putExtra("chatrecord", record);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
            });

            ImageUtils.setUserImageIcon(mContext, holder.icon, getItem(position).getName());

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
