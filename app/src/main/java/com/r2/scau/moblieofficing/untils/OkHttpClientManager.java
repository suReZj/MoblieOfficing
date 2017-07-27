package com.r2.scau.moblieofficing.untils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

/**
 * Created by 嘉进 on 11:05.
 */

public class OkHttpClientManager {

    public static String loginSessionID;
    public static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .cookieJar(new CookieJar() {
                private final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();

                @Override
                public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                    cookieStore.put(url, cookies);
                }

                @Override
                public List<Cookie> loadForRequest(HttpUrl url) {
                    List<Cookie> cookies = cookieStore.get(url);
                    return cookies != null ? cookies : new ArrayList<Cookie>();
                }
            })
            .build();;
    private static OkHttpClientManager sInstance;

    public static final int WITHSESSION = 0;
    public static final int WITHOUTSESSUIN = 1;


    public OkHttpClientManager(){

    }

    public static OkHttpClientManager getInstance()
    {
        if (sInstance == null)
        {
            synchronized (OkHttpClientManager.class)
            {
                if (sInstance == null)
                {
                    sInstance = new OkHttpClientManager();
                }
            }
        }
        return sInstance;
    }


}
