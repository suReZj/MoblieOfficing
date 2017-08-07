package com.r2.scau.moblieofficing.retrofit;

import com.r2.scau.moblieofficing.gson.GsonBase;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by 陈家程 on 2017/8/6.
 */

public interface ISignOffice {

    @POST("signOffice.shtml")
    @FormUrlEncoded
    Call<GsonBase> signOffice(
            @Field("userPhone") String userPhone,
            @Field("signTime") String signTime,
            @Field("signAddress") String signAddress
    );

}
