package com.r2.scau.moblieofficing.adapter;

import android.animation.Animator;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.r2.scau.moblieofficing.activity.ExaminationActivity;
import com.r2.scau.moblieofficing.activity.FriendsInfoActivity;
import com.r2.scau.moblieofficing.gson.GsonUser;
import com.r2.scau.moblieofficing.gson.GsonUsers;
import com.r2.scau.moblieofficing.retrofit.IFriendInfoByPhoneBiz;
import com.r2.scau.moblieofficing.untils.ChatTimeUtil;
import com.r2.scau.moblieofficing.R;
import com.r2.scau.moblieofficing.bean.ChatMessage;
import com.r2.scau.moblieofficing.untils.ImageUtils;
import com.r2.scau.moblieofficing.untils.OkHttpUntil;
import com.r2.scau.moblieofficing.untils.UserUntil;
import com.sqk.emojirelease.EmojiUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import razerdp.basepopup.BasePopupWindow;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.r2.scau.moblieofficing.Contants.PHOTO_SERVER_IP;
import static com.r2.scau.moblieofficing.Contants.SERVER_IP;
import static com.r2.scau.moblieofficing.Contants.getInfo;

/**
 * Created by 张子健 on 2017/7/23 0023.
 */

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.chatHolder> {
    private List<ChatMessage> chatMessageList;
    private Context mContext;
    private int mPosition = 0;
    private String userIcon;

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
//            rUserName = (TextView) view.findViewById(R.id.right_chat_username);
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
            holder.timeText.setText(ChatTimeUtil.getFriendlyTimeSpanByNow(chat_message_bean.getDatetime()));

            //设置头像
            ImageUtils.setUserImageIcon(mContext, holder.rIcon, chatMessageList.get(position).getMeNickname());

            try {
                EmojiUtil.handlerEmojiText(holder.rTextView, chat_message_bean.getContent(), this.mContext);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.lUserName.setText(chat_message_bean.getFriendNickname());
            holder.timeText.setText(ChatTimeUtil.getFriendlyTimeSpanByNow(chat_message_bean.getDatetime()));
            //设置头像

            if (chatMessageList.get(position).getIconPath() == null) {
                holder.lIcon.setImageDrawable(ImageUtils.getIcon(chatMessageList.get(position).getFriendNickname(), 23));
//                ImageUtils.setUserImageIcon(mContext, holder.lIcon, chatMessageList.get(position).getFriendNickname());
            } else {
                Glide.with(mContext).load(PHOTO_SERVER_IP + chatMessageList.get(position).getIconPath()).into(holder.lIcon);
            }

            try {
                EmojiUtil.handlerEmojiText(holder.lTextView, chat_message_bean.getContent(), this.mContext);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        holder.rTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                MenuPopup MenuPopup = new MenuPopup((Activity) mContext);
                MenuPopup.showPopupWindow(v);
                mPosition = position;
                return true;
            }
        });

        holder.lTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                MenuPopup MenuPopup = new MenuPopup((Activity) mContext);
                MenuPopup.showPopupWindow(v);
                mPosition = position;
                return true;
            }
        });
        holder.lIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = null;
                if (chatMessageList.get(position).isMeSend()) {
                    phone = UserUntil.gsonUser.getUserPhone();
                } else {
                    if (chatMessageList.get(position).isMulti()) {
                        phone = chatMessageList.get(position).getMultiUserName();
                    } else {
                        phone = chatMessageList.get(position).getFriendUsername();
                    }
                }
                Intent intent = new Intent(mContext, FriendsInfoActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("phone", phone);
                mContext.startActivity(intent);
            }
        });
        holder.rIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = null;
                if (chatMessageList.get(position).isMeSend()) {
                    phone = UserUntil.gsonUser.getUserPhone();
                } else {
                    if (chatMessageList.get(position).isMulti()) {
                        phone = chatMessageList.get(position).getMultiUserName();
                    } else {
                        phone = chatMessageList.get(position).getFriendUsername();
                    }
                }
                Intent intent = new Intent(mContext, FriendsInfoActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("phone", phone);
                mContext.startActivity(intent);
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
        public MenuPopup(Activity context) {
            super(context, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            findViewById(R.id.copy_message).setOnClickListener(this);
            findViewById(R.id.transmit_message).setOnClickListener(this);
        }

        @Override
        protected Animation initShowAnimation() {
            AnimationSet set = new AnimationSet(true);
            set.setInterpolator(new DecelerateInterpolator());
            set.addAnimation(getScaleAnimation(0, 1, -1, 0, Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 1));
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
//            setOffsetX((getWidth() - v.getWidth()));
//            setOffsetY(-4 * v.getHeight());
            setOffsetX(v.getWidth() / 2);
            setOffsetY(-v.getHeight()/8);
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
                    if (chatMessageList.get(mPosition).isMeSend()) {
                        Toast.makeText(mContext, "不能审批自己发送的事务", Toast.LENGTH_SHORT).show();
                        this.dismiss();
                        break;
                    }
                    if (chatMessageList.get(mPosition).getContent().indexOf("长按进行审批")!=-1) {
                        chatMessageList.get(mPosition).getContent();
                        Log.e("aaaaaaaaa", chatMessageList.get(mPosition).getContent());
                        String content = chatMessageList.get(mPosition).getContent();
                        String type = content.substring(content.indexOf("号") + 2, content.length());
                        Log.e("aaaaaaaaa", type);
                        Intent intent = new Intent(mContext, ExaminationActivity.class);
                        Integer omid = Integer.parseInt(type);
                        intent.putExtra("omid", omid);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                        this.dismiss();
                        break;
                    } else {
                        Toast.makeText(mContext, "不是可以审批的事务", Toast.LENGTH_SHORT).show();
                        this.dismiss();
                        break;
                    }
                default:
                    break;

            }
        }
    }
}
