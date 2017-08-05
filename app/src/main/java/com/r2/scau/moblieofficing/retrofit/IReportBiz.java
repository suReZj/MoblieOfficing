package com.r2.scau.moblieofficing.retrofit;

import com.r2.scau.moblieofficing.gson.GsonCreateReport;
import com.r2.scau.moblieofficing.gson.GsonSetReportUser;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by 嘉进 on 10:24.
 */

public interface IReportBiz {

    @POST("createDailyReport.shtml")
    @FormUrlEncoded
    Call<GsonCreateReport> createReport(
            @Field("userPhone") String userPhone,
            @Field("jobDone") String jobDone,
            @Field("jobToDo") String jobToDo,
            @Field("jobCoordinated") String jobCoordinated,
            @Field("jobRemark") String jobRemark,
            @Field("jobTime") String jobTime,
            @Field("jobType") long jobType
    );


    @POST("createDailyReportUser.shtml")
    @FormUrlEncoded
    Call<GsonSetReportUser> setReportUser(
            @Field("userPhone") String userPhone,
            @Field("dailyReportId") long dailyReportId
    );


}
