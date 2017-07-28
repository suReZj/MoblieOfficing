package com.r2.scau.moblieofficing.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.r2.scau.moblieofficing.untils.ChatTimeUtil;
import com.r2.scau.moblieofficing.R;
import com.r2.scau.moblieofficing.bean.ChatMessage;
import com.sqk.emojirelease.EmojiUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 张子健 on 2017/7/23 0023.
 */

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.chatHolder> {
    private List<ChatMessage> chatMessageList;
    private Context mContext;

    public ChatMessageAdapter(List<ChatMessage> chatMessageList, Context mContext) {
        this.chatMessageList = chatMessageList;
        this.mContext = mContext;
    }

    static class chatHolder extends RecyclerView.ViewHolder {
        TextView rTextView;
        ImageView rIcon;
        TextView lTextView;
        ImageView lIcon;
        TextView lUserName;
        TextView rUserName;
        TextView timeText;
        RecyclerView recyclerView;
        RelativeLayout rightLayout;
        RelativeLayout leftLayout;

        public chatHolder(View view) {
            super(view);
            lTextView = (TextView) view.findViewById(R.id.left_chat_msg);
            lIcon = (ImageView) view.findViewById(R.id.left_chat_icon);
            rTextView=(TextView)view.findViewById(R.id.right_chat_msg);
            rIcon = (ImageView) view.findViewById(R.id.right_chat_icon);
            lUserName=(TextView)view.findViewById(R.id.left_chat_username);
            rUserName=(TextView)view.findViewById(R.id.right_chat_username);
            timeText=(TextView)view.findViewById(R.id.chat_msg_time);
            recyclerView = (RecyclerView) view.findViewById(R.id.chat_recycler);
            rightLayout=(RelativeLayout)view.findViewById(R.id.right_chat_user_layout);
            leftLayout=(RelativeLayout)view.findViewById(R.id.left_chat_user_layout);
        }
    }

    @Override
    public chatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final chatHolder holder = new chatHolder(LayoutInflater.from(
                parent.getContext()).inflate(R.layout.chat_message_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(chatHolder holder, int position) {
        ChatMessage chat_message_bean = chatMessageList.get(position);
        if (chat_message_bean.isMeSend()){
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.leftLayout.setVisibility(View.GONE);
            holder.rUserName.setText(chat_message_bean.getMeNickname());
            holder.timeText.setText(ChatTimeUtil.getFriendlyTimeSpanByNow(chat_message_bean.getDatetime()));


            //设置头像
            holder.rIcon.setImageResource(R.mipmap.ic_launcher_round);

            try {
                EmojiUtil.handlerEmojiText(holder.rTextView, chat_message_bean.getContent(), this.mContext);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.lUserName.setText(chat_message_bean.getMeNickname());
            holder.timeText.setText(ChatTimeUtil.getFriendlyTimeSpanByNow(chat_message_bean.getDatetime()));


            //设置头像
            holder.lIcon.setImageResource(R.mipmap.ic_launcher_round);

            try {
                EmojiUtil.handlerEmojiText(holder.lTextView, chat_message_bean.getContent(), this.mContext);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {

        return chatMessageList == null ? 0 : chatMessageList.size();

    }

    public void add(ChatMessage item) {

        if(chatMessageList == null) {
            chatMessageList = new ArrayList<>(1);
        }
        int size = getItemCount();
        chatMessageList.add(item);
        notifyDataSetChanged();
    }
}
