package com.softdesign.devintensive.data.network;

import com.softdesign.devintensive.data.network.res.UploadPhotoRes;
import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.data.network.res.UserModelRes;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface RestService {

    @POST("login")
    Call<UserModelRes> loginUser (@Body UserLoginReq req);

    @Multipart
    @POST("user/{userId}/public/Values/profilePhoto")
    Call<UploadPhotoRes> uploadPhoto(@Path("userId") String userId,
                                     @Part MultipartBody.Part file);

    @GET("user/list?orderBy=rating")
    Call<UserListRes> getUserList();
}
