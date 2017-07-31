package com.r2.scau.moblieofficing.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.daimajia.swipe.SwipeLayout;
import com.r2.scau.moblieofficing.untils.ChatTimeUtil;
import com.r2.scau.moblieofficing.OnRecyclerViewOnClickListener;
import com.r2.scau.moblieofficing.R;
import com.r2.scau.moblieofficing.activity.ChatActivity;
import com.r2.scau.moblieofficing.bean.ChatRecord;
import com.sqk.emojirelease.EmojiUtil;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;


/**
 * Created by 张子健 on 2017/7/20 0020.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.messageHolder> {
    private List<ChatRecord> messageList;
    private Context mContext;
    public int mPosition = 0;
    private final LayoutInflater inflater;
    private OnRecyclerViewOnClickListener mListener;

    public List<ChatRecord> getMessageList() {
        return messageList;
    }

    public MessageAdapter(Context context, List<ChatRecord> list) {
        messageList = sortMessage(list);
        mContext = context;
        this.inflater = LayoutInflater.from(context);
    }

    static class messageHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        SwipeLayout swipeLayout;
        RecyclerView recyclerView;
        ImageView icon;
        TextView chatTitle;
        TextView chatContent;
        TextView chatTime;
        TextView unRead;
        CardView cardView;


        public messageHolder(final View view) {
            super(view);
            linearLayout = (LinearLayout) view.findViewById(R.id.chatRecod);
            recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
            icon = (ImageView) view.findViewById(R.id.chat_friend_avatar);
            chatTitle = (TextView) view.findViewById(R.id.chat_friend_nickname);
            chatContent = (TextView) view.findViewById(R.id.chat_message);
            chatTime = (TextView) view.findViewById(R.id.chat_time);
            unRead = (TextView) view.findViewById(R.id.chat_message_count);
            cardView = (CardView) view;

        }

    }


    @Override
    public messageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.chat_record_item_layout, parent, false);
        final messageHolder holder = new messageHolder(view);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                ChatRecord updateRecord = messageList.get(position);
//                if(updateRecord.isSaved()){
//                    Log.d("save","save");
//                }
                updateRecord.setmUnReadMessageCount();
                updateRecord.save();
                update(updateRecord);
//                if(updateRecord.isSaved()){
//                    Log.e("save1","save1");
//                }
//                ChatRecord newChatRecord=new ChatRecord();
//                newChatRecord.setmUnReadMessageCount();
//                Log.d(updateRecord.getmChatJid(), updateRecord.getmFriendUsername());
//                Log.d("eeeeee", newChatRecord.getmUnReadMessageCount());
//                updateRecord.updateAll("mchatjid= ?", updateRecord.getmChatJid());
                startChat(mContext, updateRecord);
            }
        });
        return holder;
    }


    public int getmPosition() {
        return mPosition;
    }

    @Override
    public void onBindViewHolder(messageHolder holder, final int position) {


        ChatRecord msg = messageList.get(position);
        final Resources resources = mContext.getResources();
        Drawable drawable = resources.getDrawable(R.drawable.ic_launcher_round);


//        ((messageHolder) holder).cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                ChatRecord updateRecord=messageList.get(position);
////                updateRecord.setUnReadMsgZero();
////                updateRecord.setmUnReadMessageCount(0);
////                update(updateRecord);
////                Log.d(updateRecord.getmChatJid(),updateRecord.getmFriendUsername());
////                Log.d("eeeeee","eeeeee");
////                updateRecord.updateAll("mchatjid=? and mfriendusername=?",updateRecord.getmChatJid(), updateRecord.getmFriendUsername());
//////                startChat(mContext, position);
//            }
//        });

        ((messageHolder) holder).linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mPosition = position;
                Log.d("mmmm", mPosition + "aaaaa");
                return false;
            }
        });

//设置置顶状态
        if (messageList.get(position).getSetTopFlag()) {
            try {
                holder.cardView.setBackgroundColor(resources.getColor(R.color.notice_Catagory_gray));
                EmojiUtil.handlerEmojiText(holder.chatContent, msg.getmLastMessage(), this.mContext);
                holder.chatTitle.setText(msg.getmFriendNickname());
                holder.chatTime.setText(ChatTimeUtil.getFriendlyTimeSpanByNow(msg.getmChatTime()));
//                String messageCount = msg.getUnReadMessageCount() > 0 ? String.valueOf(msg.getUnReadMessageCount()) : "";
//                ((messageHolder) holder).unRead.setText(messageCount);
                if ((msg.getmUnReadMessageCount() > 0)&&(msg.getmUnReadMessageCount()<=99)) {
                    holder.unRead.setVisibility(View.VISIBLE);
                    holder.unRead.setText(String.valueOf(msg.getmUnReadMessageCount()));
                }if(msg.getmUnReadMessageCount()>99){
                    holder.unRead.setVisibility(View.VISIBLE);
                    holder.unRead.setText("...");
                } if(msg.getmUnReadMessageCount() < 0){
                    holder.unRead.setVisibility(View.GONE);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            try {
                holder.cardView.setBackgroundColor(resources.getColor(R.color.white));
                EmojiUtil.handlerEmojiText(holder.chatContent, msg.getmLastMessage(), this.mContext);
                holder.chatTitle.setText(msg.getmFriendNickname());
                holder.chatTime.setText(ChatTimeUtil.getFriendlyTimeSpanByNow(msg.getmChatTime()));
//                String messageCount = msg.getUnReadMessageCount() > 0 ? String.valueOf(msg.getUnReadMessageCount()) : "";
//                ((messageHolder) holder).unRead.setText(messageCount);
                if ((msg.getmUnReadMessageCount() > 0)&&(msg.getmUnReadMessageCount()<=99)) {
                    holder.unRead.setVisibility(View.VISIBLE);
                    holder.unRead.setText(String.valueOf(msg.getmUnReadMessageCount()));
                }if(msg.getmUnReadMessageCount()>99){
                    holder.unRead.setVisibility(View.VISIBLE);
                    holder.unRead.setText("...");
                } if(msg.getmUnReadMessageCount() < 0){
                    holder.unRead.setVisibility(View.GONE);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setMessageTop() {
        messageList.get(mPosition).setSetTopFlag(true);
        ChatRecord msg = messageList.get(mPosition);
        messageList.remove(mPosition);
        messageList.add(0, msg);
        notifyItemMoved(mPosition, 0);
        notifyItemRangeChanged(0, messageList.size());
//        notifyDataSetChanged();
    }

    public void deleteMsg() {
        messageList.remove(mPosition);
        notifyItemRemoved(mPosition);
//        notifyDataSetChanged();
        if (mPosition != messageList.size()) { // 如果移除的是最后一个，忽略
            notifyItemRangeChanged(mPosition, messageList.size() - mPosition);
        }
        Log.d("mmmm", mPosition + "");
    }

    public void deleteTopMsg() {
        messageList.get(mPosition).setSetTopFlag(false);
        ChatRecord msg = messageList.get(mPosition);
        messageList.remove(mPosition);
        messageList.add(messageList.size(), msg);
        notifyItemMoved(mPosition, messageList.size() - 1);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return messageList.size();
    }


    //对消息队列根据是否进行排序
    public List<ChatRecord> sortMessage(List<ChatRecord> msgs) {
        for (int i = 0; i < msgs.size(); i++) {
            if (msgs.get(i).getSetTopFlag()) {
                ChatRecord msg = msgs.get(i);
                msgs.remove(i);
                msgs.add(0, msg);
            }
        }
        return msgs;
    }

    public void startChat(Context context, ChatRecord updateRecord) {
        Intent intent = new Intent(context, ChatActivity.class);
        String whereClause = "test1";
        ArrayList msgList = new ArrayList<>(DataSupport.where("mmeusername=?", whereClause).find(ChatRecord.class));
        intent.putExtra("chatrecord", updateRecord);
//        intent.putExtra("聊天jid",messageList.get(position).getmChatJid());
//        intent.putExtra("对方username",messageList.get(position).getmFriendUsername());
//        intent.putExtra("对方nicname",messageList.get(position).getmFriendNickname());
//        intent.putExtra("我的username",messageList.get(position).getmMeUsername());
//        intent.putExtra("我的nicname",messageList.get(position).getmMeNickname());
        context.startActivity(intent);
    }

    public void update(ChatRecord item) {

        if (messageList == null) {
            messageList = new ArrayList<>();
        }
        int idx = messageList.indexOf(item);
        if (idx < 0) {
            add(item);
        } else {
            messageList.set(idx, item);
            notifyItemChanged(idx);
        }
    }

    public void add(ChatRecord item) {

        if (messageList == null) {
            messageList = new ArrayList<>(1);
        }
        int size = getItemCount();
        messageList.add(item);
        notifyDataSetChanged();
    }

    public void add(ChatRecord item, int position) {

        if (messageList == null) {
            messageList = new ArrayList<>();
        }
        messageList.add(position, item);
        notifyDataSetChanged();
    }

//    public void setTop(){
//        //设置置顶状态
//        if (messageList.get(position).getSetTopFlag()) {
//            try {
//                ((messageHolder) holder).linearLayout.setBackgroundColor(resources.getColor(R.color.notice_Catagory_gray));
//                EmojiUtil.handlerEmojiText(((messageHolder) holder).chatContent, msg.getLastMessage(), this.mContext);
//                ((messageHolder) holder).chatTitle.setText(msg.getFriendNickname());
//                ((messageHolder) holder).chatTime.setText(ChatTimeUtil.getFriendlyTimeSpanByNow(msg.getChatTime()));
//                String messageCount = msg.getUnReadMessageCount() > 0 ? String.valueOf(msg.getUnReadMessageCount()) : "";
////                ((messageHolder) holder).unRead.setText(messageCount);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        } else {
//            try {
//                ((messageHolder) holder).linearLayout.setBackgroundColor(resources.getColor(R.color.white));
//                EmojiUtil.handlerEmojiText(((messageHolder) holder).chatContent, msg.getLastMessage(), this.mContext);
//                ((messageHolder) holder).chatTitle.setText(msg.getFriendNickname());
//                ((messageHolder) holder).chatTime.setText(ChatTimeUtil.getFriendlyTimeSpanByNow(msg.getChatTime()));
//                String messageCount = msg.getUnReadMessageCount() > 0 ? String.valueOf(msg.getUnReadMessageCount()) : "";
////                ((messageHolder) holder).unRead.setText(messageCount);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }


}
