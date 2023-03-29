package com.example.bubble;

import com.example.bubble.JSON.LoginDataJSON;
import com.example.bubble.JSON.UserInfoJSON;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface Connection {
    String BASE_URL = "http://192.168.1.38:8000/";

    @POST("login/signin/")
    Observable<LoginDataJSON> SignIn(@Body LoginDataJSON loginDataJSON);

    @POST("login/signup/")
    Observable<LoginDataJSON> SignUp(@Body LoginDataJSON loginDataJSON);

    @POST("user/getdata/")
    Observable<UserInfoJSON> getUserInfo(@Body UserInfoJSON userInfoJSON);

}
