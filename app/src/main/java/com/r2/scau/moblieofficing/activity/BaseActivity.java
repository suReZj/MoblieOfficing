package com.r2.scau.moblieofficing.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import java.util.Stack;

/**
 * Created by EdwinCheng on 2017/7/24.
 * 抽取基类，
 * <p>
 * 1、其中initView()用来初始化ui，initData()用来处理数据，initListener()用来给控件添加监听器
 * 2、Intent 意图也已经包含在BaseActivity中
 */

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     * 用来保存所有已打开的Activity
     */
    private static Stack<Activity> listActivity = new Stack<Activity>();
    /**
     * 记录上次点击按钮的时间
     **/
    private long lastClickTime;
    /**
     * 按钮连续点击最低间隔时间 单位：毫秒
     **/
    public final static int CLICK_TIME = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * 透明状态栏
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }


        // 将activity推入栈中
        listActivity.push(this);
        //初始化ui
        initView(savedInstanceState);
        initView();
        //初始化数据
        initData();
        //初始化监听器
        initListener();
    }

    // 带 Bundle savedInstanceState 的initView
    protected  void initView(Bundle savedInstanceState){};

    // 初始化ui
    protected abstract void initView();

    // 初始化数据
    protected abstract void initData();

    // 添加监听器
    protected abstract void initListener();

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void onBack(View v) {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 从栈中移除当前activity
        if (listActivity.contains(this)) {
            listActivity.remove(this);
        }
    }

    /**
     * Created by EdwinCheng on 2017/7/24.
     * <p>
     * activity跳转方法
     */
    public void openActivity(Class<?> targetActivity, Bundle bundle) {
        Intent intent = new Intent(this, targetActivity);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    public void openActivity(Class<?> targetActivity) {
        openActivity(targetActivity, null);
    }

    public void openActivityForResult(Class<?> targetActivity, Bundle bundle, int requestCode) {
        Intent intent = new Intent(this, targetActivity);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    public void openActivityForResult(Class<?> targetActivity, int requestCode) {
        openActivityForResult(targetActivity, null, requestCode);
    }

    /**
     * Created by EdwinCheng on 2017/7/24.
     * <p>
     * 验证按钮的点击时间，防止重复点击造成程序崩溃（eg：在子线程请求数据中）
     */
    public boolean verifyClickTime() {
        if (System.currentTimeMillis() - lastClickTime <= CLICK_TIME) {
            return false;
        }
        lastClickTime = System.currentTimeMillis();
        return true;
    }

    /**
     * Created by EdwinCheng on 2017/7/24.
     * <p>
     * 获取string的字段
     * 可以获取到res/string的字段
     */
    public String getStringMethod(int stringId) {
        return this.getResources().getString(stringId);
    }

    /**
     * Created by EdwinCheng on 2017/7/24.
     * <p>
     * 关闭所有(前台、后台)Activity,前置条件：BaseActivity为父类（继承BaseActivity）
     */
    protected static void finishAllActivity() {
        int length = listActivity.size();
        for (int i = 0; i < length; i++) {
            Activity activity = listActivity.pop();
            activity.finish();
        }
    }

    /**
     * Created by EdwinCheng on 2017/7/24.
     *
     * 双击推出程序的提示
     * 修改in  2017／7／25 ：原因：我只要在底层页面退出程序，这个方法写在mainActivity吧
     */
//    private long exitTime = 0;
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (KeyEvent.KEYCODE_BACK == keyCode) {
//            if (System.currentTimeMillis() - exitTime > 2000) {
//                ToastUtils.show(getApplicationContext(),"再按一次退出程序",Toast.LENGTH_SHORT);
//                exitTime = System.currentTimeMillis();
//            } else {
//                finishAllActivity();
//            }
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}
