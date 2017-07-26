package com.r2.scau.moblieofficing.activity;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.r2.scau.moblieofficing.R;
import com.r2.scau.moblieofficing.fragement.ContactFragment;
import com.r2.scau.moblieofficing.fragement.MessageFragment;
import com.r2.scau.moblieofficing.fragement.NoticeFragment;
import com.r2.scau.moblieofficing.fragement.UserInfoFragment;
import com.r2.scau.moblieofficing.fragement.WorkFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView mMessageTV;
    private TextView mNoticeTV;
    private TextView mWorkTV;
    private TextView mContactTV;
    private TextView mUserInfoTV;

    private Fragment mMessageFragment;
    private Fragment mNoticeFragment;
    private Fragment mWorkFragment;
    private Fragment mContactFragment;
    private Fragment mUserInfoFragment;

    private FragmentTransaction mTransaction;
    private FragmentManager mFragmentManager;

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**初始化facebook 图片加载器*/
        Fresco.initialize(MainActivity.this);

        initView();
    }

    public void initView(){
        mMessageTV = (TextView) findViewById(R.id.tv_bottom_message);
        mNoticeTV = (TextView) findViewById(R.id.tv_bottom_notice);
        mWorkTV= (TextView) findViewById(R.id.tv_bottom_work);
        mContactTV = (TextView) findViewById(R.id.tv_bottom_contact);
        mUserInfoTV = (TextView) findViewById(R.id.tv_bottom_user);

        mMessageTV.setOnClickListener(this);
        mNoticeTV.setOnClickListener(this);
        mWorkTV.setOnClickListener(this);
        mContactTV.setOnClickListener(this);
        mUserInfoTV.setOnClickListener(this);

        mMessageFragment = new MessageFragment();
        mTransaction = getFragmentManager().beginTransaction();
        mTransaction.add(R.id.fragment_container, mMessageFragment);
        hideAllFragment(mTransaction);
        mMessageTV.setSelected(true);
        mTransaction.show(mMessageFragment);
        mTransaction.commit();
    }

    /**
     * 作者：邓嘉进
     * 将底部导航栏的TextView的状态设置为未选择
     */

    public void selected(){
        mMessageTV.setSelected(false);
        mNoticeTV.setSelected(false);
        mWorkTV.setSelected(false);
        mContactTV.setSelected(false);
        mUserInfoTV.setSelected(false);

    }

    /**
     *  作者：邓嘉进
     *  隐藏Fragment
     * @param transaction
     */
    public void hideAllFragment(FragmentTransaction transaction){
        if(mMessageFragment != null){
            transaction.hide(mMessageFragment);
        }

        if(mNoticeFragment != null){
            transaction.hide(mNoticeFragment);
        }

        if(mWorkFragment != null){
            transaction.hide(mWorkFragment);
        }

        if(mContactFragment != null){
            transaction.hide(mContactFragment);
        }

        if(mUserInfoFragment != null){
            transaction.hide(mUserInfoFragment);
        }
    }

    @Override
    public void onClick(View view) {
        mTransaction = getFragmentManager().beginTransaction();
        hideAllFragment(mTransaction);
        switch(view.getId()){
            case R.id.tv_bottom_message:
                selected();
                mMessageTV.setSelected(true);
                if(mMessageFragment == null){
                    mMessageFragment = new MessageFragment();
                    mTransaction.add(R.id.fragment_container, mMessageFragment);
                }else{
                    mTransaction.show(mMessageFragment);
                }
                mTransaction.commit();
                break;

            case R.id.tv_bottom_notice:
                selected();
                mNoticeTV.setSelected(true);
                if(mNoticeFragment == null){
                    mNoticeFragment = new NoticeFragment();
                    mTransaction.add(R.id.fragment_container, mNoticeFragment);
                }else{
                    mTransaction.show(mNoticeFragment);
                }
                mTransaction.commit();
                break;

            case R.id.tv_bottom_work:
                selected();
                mWorkTV.setSelected(true);
                if(mWorkFragment == null){
                    mWorkFragment = new WorkFragment();
                    mTransaction.add(R.id.fragment_container, mWorkFragment);
                }else{
                    mTransaction.show(mWorkFragment);
                }
                mTransaction.commit();
                break;

            case R.id.tv_bottom_contact:
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_CONTACTS},
                            MY_PERMISSIONS_REQUEST_READ_CONTACT);
                }else {
                    showContactFragment();
                }

                break;

            case R.id.tv_bottom_user:
                selected();
                mUserInfoTV.setSelected(true);
                if(mUserInfoFragment == null){
                    mUserInfoFragment = new UserInfoFragment();
                    mTransaction.add(R.id.fragment_container, mUserInfoFragment);
                }else{
                    mTransaction.show(mUserInfoFragment);
                }
                mTransaction.commit();
                break;

            default:
                break;
        }

    }

    public void showContactFragment(){
        selected();
        mContactTV.setSelected(true);
        if(mContactFragment == null){
            mContactFragment = new ContactFragment();
            Log.e("MainActivity", "ShowContactFragment");
            mTransaction.add(R.id.fragment_container, mContactFragment);
        }else{
            mTransaction.show(mContactFragment);
        }
        mTransaction.commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == MY_PERMISSIONS_REQUEST_READ_CONTACT){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                showContactFragment();
            } else
            {
                // Permission Denied
                Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
