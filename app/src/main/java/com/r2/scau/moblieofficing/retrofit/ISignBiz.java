package com.r2.scau.moblieofficing.retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by 嘉进 on 13:52.
 */

public interface ISignBiz {
    @POST("mobileRegister.shtml")
    @FormUrlEncoded
    Call<ResponseBody> signUp(
            @Field("username") String userName,
            @Field("userPhone") String userPhone,
            @Field("password") String password,
            @Field("vcode") String verCode ,
            @Field("vcodeKey") String verCodeKey

    );

    @POST("getGifCode.shtml")
    @FormUrlEncoded
    Call<ResponseBody> getVerCode(
            @Field("vcodeKey") String verCodeKey

    );
}
