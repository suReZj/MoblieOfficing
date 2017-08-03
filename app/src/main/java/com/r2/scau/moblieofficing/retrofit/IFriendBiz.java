package com.r2.scau.moblieofficing.retrofit;

import com.r2.scau.moblieofficing.gson.GsonFriend;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by 嘉进 on 18:52.
 */

public interface IFriendBiz {
    @POST("findfriends.shtml")
    @FormUrlEncoded
    Call<GsonFriend> getFriend(
            @Field("userPhone") String userPhone
    );

}
