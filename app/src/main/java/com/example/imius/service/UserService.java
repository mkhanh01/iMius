package com.example.imius.service;


import android.media.Image;

import com.example.imius.model.BaseResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserService {

    @GET("login.php")
    Call<BaseResponse> login(@Query("username") String username, @Query("password") String password);

    @GET("sign_up.php")
    Call<BaseResponse> signup(@Query("username") String username, @Query("name") String name, @Query("password") String password,
                              @Query("email") String email, @Query("image") String image);

    @GET("update_password.php")
    Call<BaseResponse> updatePassword(@Query("username") String username, @Query("password") String password);

    @GET("update_name.php")
    Call<BaseResponse> updateName(@Query("username") String username, @Query("name") String name);

    @GET("check_email.php")
    Call<BaseResponse> checkEmail(@Query("email") String email);

    @GET("check_username.php")
    Call<BaseResponse> checkUsername(@Query("username") String username);

}
