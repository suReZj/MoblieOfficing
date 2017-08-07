package com.r2.scau.moblieofficing.retrofit;

import com.r2.scau.moblieofficing.gson.GsonMembers;
import com.r2.scau.moblieofficing.gson.GsonUsers;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by dell88 on 2017/8/6 0006.
 */

public interface IMembersBiz {
    @POST("getContactByGroup.shtml")
    @FormUrlEncoded
    Call<GsonMembers> getMembers(
            @Field("gid") Integer gid
    );
}
