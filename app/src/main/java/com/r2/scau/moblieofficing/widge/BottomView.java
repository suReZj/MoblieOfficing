package com.r2.scau.moblieofficing.widge;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.r2.scau.moblieofficing.Contants;
import com.r2.scau.moblieofficing.R;

/**
 * Created by EdwinCheng on 2017/7/25.
 */

public class BottomView implements View.OnClickListener,View.OnTouchListener {

    private PopupWindow mPopupWindow;
    private TextView showfilename;
    private Button delete,shareto,rename,move,cancel;
    private View mMenuView;
    private Activity mContext;
    private View.OnClickListener bottomOnClickListener;
    private int fileType;
    private String filename;
    private String fileSelectType;
    private int position;

    public BottomView(Activity mContext,String filename, int fileType , int position, String fileSelectType, View.OnClickListener bottomOnClickListener) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        this.bottomOnClickListener = bottomOnClickListener;
        this.mContext = mContext;
        this.fileType = fileType;
        this.filename = filename;
        this.fileSelectType = fileSelectType;
        this.position = position;
        mMenuView = inflater.inflate(R.layout.popwindow_filelist_more, null);
        initView(fileType);
        initData();
        initListenner(fileType);

        mPopupWindow = new PopupWindow(mMenuView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,true);
        mPopupWindow.setAnimationStyle(R.style.popwindow_anim_style);
        ColorDrawable dw = new ColorDrawable(mContext.getResources().getColor(R.color.Gray));
        mMenuView.setOnTouchListener(this);
    }

    public void show(){
        View rootView=((ViewGroup)mContext.findViewById(android.R.id.content)).getChildAt(0);
        mPopupWindow.showAtLocation(rootView, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private void initView(int fileType) {
        showfilename = (TextView) mMenuView.findViewById(R.id.popwindow_pressfilename);
        delete = (Button) mMenuView.findViewById(R.id.popwindow_delete);
        move = (Button) mMenuView.findViewById(R.id.popwindow_move);
        rename = (Button) mMenuView.findViewById(R.id.popwindow_rename);
        shareto = (Button) mMenuView.findViewById(R.id.popwindow_shareto);
        cancel = (Button) mMenuView.findViewById(R.id.popwindow_cancel);
        if(fileSelectType.equals("shared")){
            move.setVisibility(View.GONE);
            rename.setVisibility(View.GONE);
            shareto.setVisibility(View.GONE);
        }else {
            move.setVisibility(View.VISIBLE);
            rename.setVisibility(View.VISIBLE);
            shareto.setVisibility(View.VISIBLE);
            //如果传输过来的是文件夹的类型，隐藏"分享"的view
            if (fileType == Contants.FILEMANAGER.FOLDER_TYPE){
                shareto.setVisibility(View.GONE);
                move.setVisibility(View.GONE);
            }else {
                shareto.setVisibility(View.VISIBLE);
                move.setVisibility(View.VISIBLE);
            }
        }
    }

    private void initData(){
        //将第一个设置成文件或者文件夹名字
        showfilename.setText(filename);
    }

    private void initListenner(int fileType) {
        if (fileSelectType.equals("personalfile")){
            rename.setOnClickListener(this);
            move.setOnClickListener(this);
            if (fileType == Contants.FILEMANAGER.FILE_TYPE){
                shareto.setOnClickListener(this);
            }
        }
        delete.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        mPopupWindow.dismiss();
        switch (view.getId()) {
            case R.id.popwindow_cancel:
                break;
            default:
                view.setTag(position);
                bottomOnClickListener.onClick(view);
                break;
        }
    }

    @Override
    public boolean onTouch(View arg0, MotionEvent event) {
        int height = mMenuView.findViewById(R.id.pop_layout).getTop();
        int y=(int) event.getY();
        if(event.getAction()==MotionEvent.ACTION_UP){
            if(y<height){
                mPopupWindow.dismiss();
            }
        }
        return true;
    }
}
