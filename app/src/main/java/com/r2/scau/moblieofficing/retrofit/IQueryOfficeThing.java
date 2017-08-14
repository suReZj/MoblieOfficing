package com.r2.scau.moblieofficing.retrofit;

import com.r2.scau.moblieofficing.gson.GsonExaminations;
import com.r2.scau.moblieofficing.gson.GsonMembers;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by dell88 on 2017/8/14 0014.
 */

public interface IQueryOfficeThing {
    @POST("queryOfficeThingByomId.shtml")
    @FormUrlEncoded
    Observable<GsonExaminations> getExaminations(
            @Field("omid") Integer omid
    );
}
