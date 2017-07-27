package com.r2.scau.moblieofficing.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.allen.library.SuperTextView;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.daimajia.swipe.implments.SwipeItemRecyclerMangerImpl;
import com.r2.scau.moblieofficing.R;
import com.r2.scau.moblieofficing.activity.ChatActivity;
import com.r2.scau.moblieofficing.bean.message_Bean;

import java.util.List;


/**
 * Created by 张子健 on 2017/7/20 0020.
 */

public class MessageAdapter extends RecyclerSwipeAdapter<MessageAdapter.messageHolder> {
    private List<message_Bean> messageList;
    private Context mContext;
    protected SwipeItemRecyclerMangerImpl mItemManager = new SwipeItemRecyclerMangerImpl(this);

    public MessageAdapter(Context context, List<message_Bean> list) {
        messageList = sortMessage(list);
        mContext = context;
    }

    static class messageHolder extends RecyclerView.ViewHolder {
        SuperTextView superTextView;
        SwipeLayout swipeLayout;
        Button deleteButton;
        Button setTopBtn;
        RecyclerView recyclerView;

        public messageHolder(final View view) {
            super(view);
            superTextView = (SuperTextView) view.findViewById(R.id.superText);
            swipeLayout = (SwipeLayout) view.findViewById(R.id.swipe);
            deleteButton = (Button) view.findViewById(R.id.delete);
            setTopBtn = (Button) view.findViewById(R.id.makeTop);
            recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        }
    }


    @Override
    public messageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final messageHolder holder = new messageHolder(LayoutInflater.from(
                parent.getContext()).inflate(R.layout.message_item, parent,
                false));
        return holder;
    }


    @Override
    public void onBindViewHolder(final messageHolder holder, final int position) {
        holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "点击了删除按钮", Toast.LENGTH_SHORT);
                mItemManager.removeShownLayouts(holder.swipeLayout);
                messageList.remove(position);
                mItemManager.closeAllItems();
                holder.swipeLayout.toggle(false);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, messageList.size());
                Toast.makeText(view.getContext(), "删除成功", Toast.LENGTH_SHORT).show();
            }
        });

        holder.setTopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!messageList.get(position).getSetTopFlag()) {
                    mItemManager.closeAllItems();
                    messageList.get(position).setSetTopFlag(true);
                    message_Bean msg = messageList.get(position);
                    messageList.remove(position);
                    messageList.add(0, msg);
                    notifyDataSetChanged();
                    Toast.makeText(v.getContext(), "已置顶", Toast.LENGTH_SHORT).show();
                } else {
                    mItemManager.closeAllItems();
                    messageList.get(position).setSetTopFlag(false);
                    message_Bean msg = messageList.get(position);
                    messageList.remove(position);
                    messageList.add(messageList.size(), msg);
                    notifyDataSetChanged();
                    Toast.makeText(v.getContext(), "取消置顶", Toast.LENGTH_SHORT).show();
                }
            }
        });


        holder.superTextView.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onSuperTextViewClick() {
                Intent intent = new Intent(mContext, ChatActivity.class);
                mContext.startActivity(intent);
                mItemManager.closeAllItems();
            }
        });


        message_Bean message = messageList.get(position);
        Resources resources = mContext.getResources();
        Drawable drawable = resources.getDrawable(message.getIcon());

        if (messageList.get(position).getSetTopFlag()) {
            holder.superTextView.setBackgroundColor(resources.getColor(R.color.colorLightBlue));
            Button topBtn = (Button) holder.swipeLayout.findViewById(R.id.makeTop);
            topBtn.setText("取消置顶");
            holder.superTextView.setLeftBottomString(message.getMsg_content())
                    .setLeftTopString(message.getMsg_Title())
                    .setRightString(message.getMsg_time())
                    .setLeftIcon(drawable);
        } else {
            holder.superTextView.setBackgroundColor(resources.getColor(R.color.white));
            Button topBtn = (Button) holder.swipeLayout.findViewById(R.id.makeTop);
            topBtn.setText("置顶");
            holder.superTextView.setLeftBottomString(message.getMsg_content())
                    .setLeftTopString(message.getMsg_Title())
                    .setRightString(message.getMsg_time())
                    .setLeftIcon(drawable);
        }


        mItemManager.bindView(holder.itemView, position);

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    //对消息队列根据是否进行排序
    public List<message_Bean> sortMessage(List<message_Bean> msgs) {
        for (int i = 0; i < msgs.size(); i++) {
            if (msgs.get(i).getSetTopFlag()) {
                message_Bean msg = msgs.get(i);
                msgs.remove(i);
                msgs.add(0, msg);
            }
        }
        return msgs;
    }


}
