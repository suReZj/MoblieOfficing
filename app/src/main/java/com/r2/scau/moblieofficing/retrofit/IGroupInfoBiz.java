package com.r2.scau.moblieofficing.retrofit;

import com.r2.scau.moblieofficing.gson.GsonGroup;
import com.r2.scau.moblieofficing.gson.GsonGroupInfo;
import com.r2.scau.moblieofficing.gson.GsonQRCode;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by dell88 on 2017/8/6 0006.
 */

public interface IGroupInfoBiz {
    @POST("getGroupInfo.shtml")
    @FormUrlEncoded
    Call<GsonGroupInfo> getGroupInfo(
            @Field("groupId") Integer groupId
    );
}
