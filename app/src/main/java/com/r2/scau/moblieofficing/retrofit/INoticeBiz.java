package com.r2.scau.moblieofficing.retrofit;

import com.r2.scau.moblieofficing.bean.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by 嘉进 on 11:34.
 */

public interface INoticeBiz {
    @POST("getTheGroupAllAnnouncements")
    @FormUrlEncoded
    Call<User> login(@Field("userPhone") String userPhone, @Field("group") long groupId);
}
