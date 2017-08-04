package com.r2.scau.moblieofficing.retrofit;

import com.r2.scau.moblieofficing.gson.GsonUploadPortrait;
import com.r2.scau.moblieofficing.untils.Contacts;

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Edward on 2017/8/4.
 */

public interface IUploadPortrait {
    @Multipart
    @POST(Contacts.uploadPortrait)
    Call<GsonUploadPortrait> uploadPortrait(@Part MultipartBody.Part file,
                                            @Part("fileName") RequestBody fileName,
                                            @Part("userPhone") RequestBody userPhone);
}
