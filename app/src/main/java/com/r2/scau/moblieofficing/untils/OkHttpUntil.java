package com.r2.scau.moblieofficing.untils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 嘉进 on 11:05.
 */

public class OkHttpUntil {

    public static String loginSessionID = null;

    public static OkHttpClient okHttpClient = null;

    public static OkHttpClient getInstance() {
        if (okHttpClient == null){
            synchronized (OkHttpUntil.class){
                if (okHttpClient == null){
                    CreateOkHttpClient();
                }
            }
        }
        return okHttpClient;
    }


    public static void CreateOkHttpClient(){
        okHttpClient = new OkHttpClient.Builder()
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
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();

                        // Request customization: add request headers
                        Request.Builder requestBuilder = original.newBuilder()
                                .addHeader("cookie", loginSessionID);
                        Request request = requestBuilder.build();
                        return chain.proceed(request);
                    }
                })
                .build();
    }
}
