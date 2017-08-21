package com.r2.scau.moblieofficing.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.r2.scau.moblieofficing.R;
import com.r2.scau.moblieofficing.activity.FriendsInfoActivity;
import com.r2.scau.moblieofficing.bean.ChatMessage;
import com.r2.scau.moblieofficing.bean.groupMember;
import com.r2.scau.moblieofficing.gson.GsonMember;
import com.r2.scau.moblieofficing.gson.GsonUser;
import com.r2.scau.moblieofficing.gson.GsonUsers;
import com.r2.scau.moblieofficing.retrofit.IFriendInfoByPhoneBiz;
import com.r2.scau.moblieofficing.untils.ImageUtils;
import com.r2.scau.moblieofficing.untils.OkHttpUntil;
import com.r2.scau.moblieofficing.untils.UserUntil;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.r2.scau.moblieofficing.Contants.SERVER_IP;
import static com.r2.scau.moblieofficing.Contants.getInfo;

/**
 * Created by dell88 on 2017/8/6 0006.
 */

public class groupMemberAdapter extends RecyclerView.Adapter<groupMemberAdapter.memberHolder> {
    private List<groupMember> memberList;
    private Context mContext;
    String userIconPath=null;

    public groupMemberAdapter(List<groupMember> memberList, Context mContext) {
        this.memberList = memberList;
        this.mContext = mContext;
    }
    public List<groupMember> getList(){
        return memberList;
    }

    @Override
    public groupMemberAdapter.memberHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.group_member_item, parent, false);
        memberHolder memberHolder=new memberHolder(view);
        return memberHolder;
    }

    @Override
    public void onBindViewHolder(groupMemberAdapter.memberHolder holder, final int position) {
            if (memberList.get(position).getUserPhone().equals(UserUntil.gsonUser.getUserPhone())) {
                ImageUtils.setUserImageIcon(mContext, holder.memberIcon, memberList.get(position).getNickName());
            } else {
                if (userIconPath == null) {
//                    ImageUtils.setUserImageIcon(mContext, holder.memberIcon, memberList.get(position).getNickName());
                    holder.memberIcon.setImageDrawable(ImageUtils.getIcon(memberList.get(position).getNickName(), 23));
                } else {
                    Glide.with(mContext).load(userIconPath).into(holder.memberIcon);
                }
            }
            holder.memberName.setText(memberList.get(position).getNickName());
        holder.memberLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, FriendsInfoActivity.class);
                intent.putExtra("phone",memberList.get(position).getUserPhone());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }

    static class memberHolder extends RecyclerView.ViewHolder{
        private ImageView memberIcon;
        private TextView memberName;
        private LinearLayout memberLayout;
        public memberHolder(View itemView) {
            super(itemView);
            memberIcon=(ImageView)itemView.findViewById(R.id.group_member_icon);
            memberName=(TextView)itemView.findViewById(R.id.group_member_name);
            memberLayout=(LinearLayout)itemView.findViewById(R.id.member_layout);
        }
    }



    public void getMembersInfo(final String userPhone){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_IP + getInfo + "/")
                .callFactory(OkHttpUntil.getInstance())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IFriendInfoByPhoneBiz iFriendInfoBiz = retrofit.create(IFriendInfoByPhoneBiz.class);
        Call<GsonUsers> call = iFriendInfoBiz.getInfo(userPhone);
        call.enqueue(new Callback<GsonUsers>() {
            @Override
            public void onResponse(Call<GsonUsers> call, Response<GsonUsers> response) {
                GsonUsers gsonUsers = response.body();
                if (gsonUsers.getCode() == 200) {
                    GsonUser user = gsonUsers.getUserInfo();
                    String userName=user.getNickname();
                    if (user.getUserHeadPortrait() != null) {
                        String userIconPath = user.getUserHeadPortrait().toString();
                        Log.e("icon!=null", userIconPath);
                    } else {
                        Log.e("icon==null", "icon==null");
                    }
//                    if (userPhone.equals(UserUntil.gsonUser.getUserPhone())) {
//                        ImageUtils.setUserImageIcon(mContext, holder.memberIcon, userName);
//                    } else {
//                        if (userIconPath == null) {
//                            ImageUtils.setUserImageIcon(mContext, holder.memberIcon, userName);
//                        } else {
//                            Glide.with(mContext).load(userIconPath).into(holder.memberIcon);
//                        }
//                    }
                    Log.e("getInfo", "success");
                } else {
                    Log.e("getInfo", gsonUsers.getMsg());
                }
            }

            @Override
            public void onFailure(Call<GsonUsers> call, Throwable t) {
                Log.e("getInfo", "fail");
            }
        });
    }
}
