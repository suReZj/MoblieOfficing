package com.r2.scau.moblieofficing.widge;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.r2.scau.moblieofficing.R;

/**
 * Created by EdwinCheng on 2017/7/26.
 * 自定义titleBar：
 *
 * 1、标题栏
 * 2、leftButton、righrFirstButton、rightSecondButton
 */

public class EDCToolBar extends Toolbar{
    private LayoutInflater mInflater;

    private View mView;
    private TextView mTextTitle;

    private Button mLeftButton;
    private Button mRightFirstButton;
    private Button mRightSecondButton;


    public EDCToolBar(Context context) {
        this(context,null);
    }

    public EDCToolBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EDCToolBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
        setContentInsetsRelative(10,10);

        if(attrs != null) {
            final TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                    R.styleable.EDCToolBar, defStyleAttr, 0);

            final Drawable leftIcon = a.getDrawable(R.styleable.EDCToolBar_leftButtonIcon);
            if (leftIcon != null) {
                //setNavigationIcon(navIcon);
                setLeftButtonIcon(leftIcon);
            }

            final Drawable rightFirstIcon = a.getDrawable(R.styleable.EDCToolBar_rightFirstButtonIcon);
            if (rightFirstIcon != null) {
                //setNavigationIcon(navIcon);
                setRightFirstButtonIcon(rightFirstIcon);
            }

            final Drawable rightSecondIcon = a.getDrawable(R.styleable.EDCToolBar_rightSecondButtonIcon);
            if (rightSecondIcon != null) {
                //setNavigationIcon(navIcon);
                setRightSecondButtonIcon(rightSecondIcon);
            }

            CharSequence leftButtonText = a.getText(R.styleable.EDCToolBar_leftButtonText);
            if(leftButtonText !=null){
                setLeftButtonText(leftButtonText);
            }

            CharSequence rightFirstButtonText = a.getText(R.styleable.EDCToolBar_rightFirstButtonText);
            if(rightFirstButtonText !=null){
                setRightFirstButtonText(rightFirstButtonText);
            }

            CharSequence rightSecondButtonText = a.getText(R.styleable.EDCToolBar_rightSecondButtonText);
            if(rightSecondButtonText !=null){
                setRightSecondButtonText(rightSecondButtonText);
            }

            a.recycle();
        }

    }

    private void initView() {
        if(mView == null) {

            mInflater = LayoutInflater.from(getContext());
            mView = mInflater.inflate(R.layout.titlebar, null);

            mTextTitle = (TextView) mView.findViewById(R.id.toolbar_title);
            mLeftButton = (Button) mView.findViewById(R.id.toolbar_leftButton);
            mRightFirstButton = (Button) mView.findViewById(R.id.toolbar_rightFirstButton);
            mRightSecondButton = (Button) mView.findViewById(R.id.toolbar_rightSecondButton);

            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);

            addView(mView, lp);
        }
    }

    public void  setLeftButtonIcon(Drawable icon){
        if(mLeftButton !=null){
            mLeftButton.setBackground(icon);
            mLeftButton.setVisibility(VISIBLE);
        }
    }

    public void  setRightFirstButtonIcon(Drawable icon){
        if(mRightFirstButton !=null){
            mRightFirstButton.setBackground(icon);
            mRightFirstButton.setVisibility(VISIBLE);
        }
    }

    public void  setRightSecondButtonIcon(Drawable icon){
        if(mRightSecondButton !=null){
            mRightSecondButton.setBackground(icon);
            mRightSecondButton.setVisibility(VISIBLE);
        }
    }

    public void  setLeftButtonIcon(int icon){
        setLeftButtonIcon(getResources().getDrawable(icon));
    }

    public void  setRightFirstButtonIcon(int icon){
        setRightFirstButtonIcon(getResources().getDrawable(icon));
    }

    public void  setRightSecondButtonIcon(int icon){
        setRightSecondButtonIcon(getResources().getDrawable(icon));
    }

    public  void setLeftButtonOnClickListener(OnClickListener li){
        mLeftButton.setOnClickListener(li);
    }

    public  void setRightFirstButtonOnClickListener(OnClickListener li){
        mRightFirstButton.setOnClickListener(li);
    }

    public  void setRightSecondButtonOnClickListener(OnClickListener li){
        mRightSecondButton.setOnClickListener(li);
    }


    public void setLeftButtonText(CharSequence text){
        mLeftButton.setText(text);
        mLeftButton.setVisibility(VISIBLE);
    }

    public void setRightFirstButtonText(CharSequence text){
        mRightFirstButton.setText(text);
        mRightFirstButton.setVisibility(VISIBLE);
    }

    public void setRightSecondButtonText(CharSequence text){
        mRightSecondButton.setText(text);
        mRightSecondButton.setVisibility(VISIBLE);
    }

    public void setLeftButtonText(int id){
        setLeftButtonText(getResources().getString(id));
    }

    public void setRightFirstButtonText(int id){
        setRightFirstButtonText(getResources().getString(id));
    }

    public void setRightSecondButtonText(int id){
        setRightSecondButtonText(getResources().getString(id));
    }

    public Button getLeftButton(){
        return this.mLeftButton;
    }

    public Button getRightFirstButton(){
        return this.mRightFirstButton;
    }

    public Button getRightSecondButton(){
        return this.mRightSecondButton;
    }

    @Override
    public void setTitle(int resId) {
        setTitle(getContext().getText(resId));
    }

    @Override
    public void setTitle(CharSequence title) {
        initView();
        if(mTextTitle !=null) {
            mTextTitle.setText(title);
            showTitleView();
        }
    }

    public void showTitleView(){
        if(mTextTitle !=null)
            mTextTitle.setVisibility(VISIBLE);
    }

    public void hideTitleView() {
        if (mTextTitle != null)
            mTextTitle.setVisibility(GONE);
    }

}
