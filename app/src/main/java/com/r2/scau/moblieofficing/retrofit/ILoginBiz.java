package com.r2.scau.moblieofficing.retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by 嘉进 on 11:50.
 */

public interface ILoginBiz {
    @POST("mobileLogin.shtml")
    @FormUrlEncoded
    Call<ResponseBody> login(
            @Field("userPhone") String userPhone,
            @Field("password") String password,
            @Field("isRememberMe") Boolean isRememberMe
    );
}
