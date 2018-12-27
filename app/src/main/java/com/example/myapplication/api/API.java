package com.example.myapplication.api;

import com.example.myapplication.models.DefaultResponse;
import com.example.myapplication.models.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface API {

    //kek1
    @FormUrlEncoded
    @POST("createuser")
    Call<DefaultResponse> createUser(
            @Field("Login") String login,
            @Field("Password") String password
    );


    @FormUrlEncoded
    @POST("userlogin")
    Call<LoginResponse> userLogin(
            @Field("Login") String login,
            @Field("Password") String password
    );
}
