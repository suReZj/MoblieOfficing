package com.r2.scau.moblieofficing;


import android.content.Context;
import android.util.Log;

import org.litepal.LitePalApplication;

import io.agora.openvcall.AGApplication;

/**
 * Created by 陈家程 on 2017/8/3.
 */

public class MyApplication extends AGApplication {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this.getApplicationContext();
        Log.e("context", context.toString());

    }

    /**
     * 获取 Application Context
     * 该 Context 可用于文件/资源相关操作,不可用作 UI 的上下文
     * @return
     */
    public static Context getGlobalContext() {
        return context;
    }
}
