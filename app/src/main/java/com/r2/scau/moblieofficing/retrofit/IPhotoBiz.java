package com.r2.scau.moblieofficing.retrofit;

import com.r2.scau.moblieofficing.gson.GsonPhoto;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by 嘉进 on 12:01.
 */

public interface IPhotoBiz {
    @POST("uploadPortrait.shtml")
    @Multipart
    Call<GsonPhoto> upLoadPhoto(
            @Part("fileName") RequestBody fileName,
            @Part("userPhone") RequestBody userPhone,
            @Part MultipartBody.Part file
    );
}
