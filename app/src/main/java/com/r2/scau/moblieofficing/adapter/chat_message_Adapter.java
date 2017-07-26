package com.r2.scau.moblieofficing.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.r2.scau.moblieofficing.R;
import com.r2.scau.moblieofficing.bean.chat_message_Bean;
import com.r2.scau.moblieofficing.bean.message_Bean;
import com.sqk.emojirelease.EmojiUtil;

import java.io.IOException;
import java.util.List;

/**
 * Created by 张子健 on 2017/7/23 0023.
 */

public class chat_message_Adapter extends RecyclerView.Adapter<chat_message_Adapter.chatHolder> {
    private List<chat_message_Bean> chatMessageList;
    private Context mContext;

    public chat_message_Adapter(List<chat_message_Bean> chatMessageList, Context mContext) {
        this.chatMessageList = chatMessageList;
        this.mContext = mContext;
    }

    static class chatHolder extends RecyclerView.ViewHolder {
        TextView rtextView;
        ImageView ricon;
        TextView ltextView;
        ImageView licon;
        RecyclerView recyclerView;
        RelativeLayout rightLayout;
        RelativeLayout leftLayout;

        public chatHolder(View view) {
            super(view);
            ltextView = (TextView) view.findViewById(R.id.left_chat_msg);
            licon = (ImageView) view.findViewById(R.id.left_chat_icon);
            rtextView=(TextView)view.findViewById(R.id.right_chat_msg);
            ricon = (ImageView) view.findViewById(R.id.right_chat_icon);
            recyclerView = (RecyclerView) view.findViewById(R.id.chat_recycler);
            rightLayout=(RelativeLayout)view.findViewById(R.id.right_chat_user_layout);
            leftLayout=(RelativeLayout)view.findViewById(R.id.left_chat_user_layout);
        }
    }

    @Override
    public chat_message_Adapter.chatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final chatHolder holder = new chatHolder(LayoutInflater.from(
                parent.getContext()).inflate(R.layout.chat_message_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(chat_message_Adapter.chatHolder holder, int position) {
        chat_message_Bean chat_message_bean = chatMessageList.get(position);
        if(position%2==0){
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.leftLayout.setVisibility(View.GONE);
            holder.ricon.setImageResource(R.mipmap.ic_launcher_round);
            try {
                EmojiUtil.handlerEmojiText(holder.rtextView, chat_message_bean.getMsg(), this.mContext);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.licon.setImageResource(R.mipmap.ic_launcher_round);
            try {
                EmojiUtil.handlerEmojiText(holder.ltextView, chat_message_bean.getMsg(), this.mContext);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public int getItemCount() {

        return chatMessageList.size();

    }
}
