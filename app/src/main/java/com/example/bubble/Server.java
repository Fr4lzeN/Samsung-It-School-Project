package com.example.bubble;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Server {

        public static Observable<LoginDataJSON> logIn(String login, String password){
            Retrofit retrofit = new Retrofit.Builder().baseUrl(Connection.BASE_URL).addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
            Connection signInApi = retrofit.create(Connection.class);
            return signInApi.SignIn(new LoginDataJSON(login,password)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        }

        public static Observable<LoginDataJSON> registration(String login, String password,  String email){
            Retrofit retrofit = new Retrofit.Builder().baseUrl(Connection.BASE_URL).addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
            Connection signUpApi = retrofit.create(Connection.class);
            return signUpApi.SignUp(new LoginDataJSON(login, password, email)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        }

        public static  Observable<UserInfoJSON> getUserInfo(String login){
            Retrofit retrofit = new Retrofit.Builder().baseUrl(Connection.BASE_URL).addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
            Connection getUserInfoApi = retrofit.create(Connection.class);
            return getUserInfoApi.getUserInfo(new UserInfoJSON(login)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        }

}
