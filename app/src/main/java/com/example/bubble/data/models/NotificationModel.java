package com.example.bubble.data.models;

import com.example.bubble.data.JSONModels.NotificationJSON;
import com.example.bubble.data.network.SendNotification;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NotificationModel {

public static Completable sendNotification(String token, String title, String body){
    NotificationJSON notificationJSON = new NotificationJSON(token, body,title);
    Retrofit retrofit = new Retrofit.Builder().baseUrl("https://fcm.googleapis.com/").addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
    return  retrofit.create(SendNotification.class).sendNotification(notificationJSON).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
}

}
