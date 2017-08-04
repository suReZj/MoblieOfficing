package com.r2.scau.moblieofficing.adapter;

import android.animation.Animator;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.r2.scau.moblieofficing.untils.ChatTimeUtil;
import com.r2.scau.moblieofficing.R;
import com.r2.scau.moblieofficing.bean.ChatMessage;
import com.sqk.emojirelease.EmojiUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import razerdp.basepopup.BasePopupWindow;

/**
 * Created by 张子健 on 2017/7/23 0023.
 */

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.chatHolder> {
    private List<ChatMessage> chatMessageList;
    private Context mContext;
    private int mPosition=0;

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
            rTextView = (TextView) view.findViewById(R.id.right_chat_msg);
            rIcon = (ImageView) view.findViewById(R.id.right_chat_icon);
            lUserName = (TextView) view.findViewById(R.id.left_chat_username);
            rUserName = (TextView) view.findViewById(R.id.right_chat_username);
            timeText = (TextView) view.findViewById(R.id.chat_msg_time);
            recyclerView = (RecyclerView) view.findViewById(R.id.chat_recycler);
            rightLayout = (RelativeLayout) view.findViewById(R.id.right_chat_user_layout);
            leftLayout = (RelativeLayout) view.findViewById(R.id.left_chat_user_layout);
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
    public void onBindViewHolder(final chatHolder holder, final int position) {
        ChatMessage chat_message_bean = chatMessageList.get(position);
        if (chat_message_bean.isMeSend()) {
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
        } else {
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.lUserName.setText(chat_message_bean.getFriendNickname());
//            Log.e("nickname",chat_message_bean.getFriendNickname());
            holder.timeText.setText(ChatTimeUtil.getFriendlyTimeSpanByNow(chat_message_bean.getDatetime()));


            //设置头像
            holder.lIcon.setImageResource(R.mipmap.ic_launcher_round);

            try {
                EmojiUtil.handlerEmojiText(holder.lTextView, chat_message_bean.getContent(), this.mContext);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



        holder.rTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                MenuPopup MenuPopup=new MenuPopup((Activity)mContext);
                MenuPopup.showPopupWindow(v);
                mPosition=position;
                return true;
            }
        });

        holder.lTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                MenuPopup MenuPopup=new MenuPopup((Activity)mContext);
                MenuPopup.showPopupWindow(v);
                mPosition=position;
                return true;
            }
        });
        holder.lIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,"aaaaaaaaaaaaaa",Toast.LENGTH_SHORT).show();
            }
        });
        holder.rIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,"aaaaaaaaaaaaaa",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {

        return chatMessageList == null ? 0 : chatMessageList.size();

    }

    public void add(ChatMessage item) {

        if (chatMessageList == null) {
            chatMessageList = new ArrayList<>();
        }
        int size = getItemCount();
        chatMessageList.add(item);
        notifyDataSetChanged();
    }


    public class MenuPopup extends BasePopupWindow implements View.OnClickListener {

        private TextView tx1;
        private TextView tx2;
        private TextView tx3;


        public MenuPopup(Activity context) {
            super(context, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            findViewById(R.id.copy_message).setOnClickListener(this);
            findViewById(R.id.transmit_message).setOnClickListener(this);
        }

        @Override
        protected Animation initShowAnimation() {
            AnimationSet set = new AnimationSet(true);
            set.setInterpolator(new DecelerateInterpolator());
            set.addAnimation(getScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0));
            set.addAnimation(getDefaultAlphaAnimation());
            return set;
            //return null;
        }

        @Override
        public Animator initShowAnimator() {
       /* AnimatorSet set=new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(mAnimaView,"scaleX",0.0f,1.0f).setDuration(300),
                ObjectAnimator.ofFloat(mAnimaView,"scaleY",0.0f,1.0f).setDuration(300),
                ObjectAnimator.ofFloat(mAnimaView,"alpha",0.0f,1.0f).setDuration(300*3/2));*/
            return null;
        }

        @Override
        public void showPopupWindow(View v) {
            setOffsetX(-(getWidth() - v.getWidth() / 2));
            setOffsetY(-v.getHeight() / 2);
            super.showPopupWindow(v);
        }

        @Override
        public View getClickToDismissView() {
            return null;
        }

        @Override
        public View onCreatePopupView() {
            return createPopupById(R.layout.popup_menu);
        }

        @Override
        public View initAnimaView() {
            return getPopupWindowView().findViewById(R.id.popup_contianer);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.copy_message:
                    ClipboardManager clipboardManager = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboardManager.setText(chatMessageList.get(mPosition).getContent());
                    this.dismiss();
                    break;
                case R.id.transmit_message:
                    this.dismiss();
                    break;
                default:
                    break;

            }
        }
    }



}
