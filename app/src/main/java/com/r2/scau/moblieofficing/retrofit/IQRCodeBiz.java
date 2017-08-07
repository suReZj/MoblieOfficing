package com.r2.scau.moblieofficing.retrofit;

import com.r2.scau.moblieofficing.gson.GsonFriends;
import com.r2.scau.moblieofficing.gson.GsonQRCode;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by dell88 on 2017/8/6 0006.
 */

public interface IQRCodeBiz {
    @POST("createUserQRcode.shtml")
    @FormUrlEncoded
    Call<GsonQRCode> getQR(
            @Field("userPhone") String userPhone
    );
}
