package com.r2.scau.moblieofficing.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.r2.scau.moblieofficing.R;
import com.r2.scau.moblieofficing.activity.OnItemClickLitener;
import com.r2.scau.moblieofficing.gson.GsonGroup;

import java.util.List;

/**
 * Created by EdwinCheng on 2017/8/6.
 *
 * 网盘的首页：选择个人文件 还是 群聊里面的文件
 *
 */

public class FMTypeSelectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final String TAG = "FMTypeSelectAdapter";

    private static final int EMPTY_ITEM = 0 ;
    private static final int PERSONAL_FILE_ITEM = 1 ;
    private static final int GROUP_FILE_HEAD_ITEM = 2 ;
    private static final int GROUP_FILE_ITEM = 3 ;

    private Context mContext;
    private List<GsonGroup> groupList;

    public FMTypeSelectAdapter(Context mContext, List<GsonGroup> groupList) {
        this.mContext = mContext;
        this.groupList = groupList;
    }

    private OnItemClickLitener mOnItemClickLitener;
    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || (position >1 && position < (groupList.size() + 2))){
            return PERSONAL_FILE_ITEM;
        }else if(position > 0 && position <= 1 ) {
            return GROUP_FILE_HEAD_ITEM;
        }else{
            return EMPTY_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == PERSONAL_FILE_ITEM){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.template_fm_item, parent, false);
            return new ItemViewHolder(view);
        }else if (viewType == GROUP_FILE_HEAD_ITEM){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.template_fm_group_head, parent, false);
            return new GroupHeadViewHolder(view);
        }else{
            return null;
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder){
            final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            if(position == 0 ){
                itemViewHolder.button.setText(R.string.my_file);

                Drawable dra = mContext.getResources().getDrawable(R.mipmap.ic_personalfile);
                dra.setBounds(0, 0, dra.getMinimumWidth(), dra.getMinimumHeight());
                itemViewHolder.button.setCompoundDrawables(dra, null, null, null);

            }else if (position >1 && position < groupList.size() + 2){
                itemViewHolder.button.setText(groupList.get(position-2).getGname());
                Drawable dra = mContext.getResources().getDrawable(R.mipmap.ic_sharedfile);
                dra.setBounds(0, 0, dra.getMinimumWidth(), dra.getMinimumHeight());
                itemViewHolder.button.setCompoundDrawables(dra, null, null, null);

            }
            // 监听itemView
            if (mOnItemClickLitener != null){
                itemViewHolder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = itemViewHolder.getLayoutPosition();
                        mOnItemClickLitener.onItemClick(itemViewHolder.itemView, pos);
                    }
                });
            }

        }else if (holder instanceof GroupHeadViewHolder){
            GroupHeadViewHolder groupHeadViewHolder = (GroupHeadViewHolder) holder;
            if (groupList.size() == 0 ){
                groupHeadViewHolder.headText.setText(mContext.getResources().getString(R.string.user_not_have_group));
            }else{
                groupHeadViewHolder.headText.setText(mContext.getResources().getString(R.string.fm_group_head));
            }
        }
    }

    @Override
    public int getItemCount() {
        if(groupList != null && groupList.size() >0)
            return (groupList.size() + 2);
        return 2;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private Button button;
        public ItemViewHolder(View itemView) {
            super(itemView);
            this.button = (Button) itemView.findViewById(R.id.fmselect_item);
        }
    }

    class GroupHeadViewHolder extends RecyclerView.ViewHolder {
        private TextView headText;
        public GroupHeadViewHolder(View itemView) {
            super(itemView);
            this.headText = (TextView) itemView.findViewById(R.id.fmselect_groupheader);
        }
    }
}
