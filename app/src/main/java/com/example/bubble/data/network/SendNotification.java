package com.example.bubble.data.network;

import com.example.bubble.data.JSONModels.NotificationJSON;

import io.reactivex.Completable;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface SendNotification {

   final String authKey = "key=AAAAzcmxK2w:APA91bH6y-pXXQx2icwdaRa8EctM2BlMxfrhCpfm5j-CCXbG1XNxG1DkpJNNPMp9PXdtEnpW4B0d2MzoMM11cNaFs2XNlawhD6GKusnzjh8WyAvvygfge4w345QJFbSGByTLvq6EeyMn";

    @Headers("Authorization: "+authKey)
    @POST("fcm/send")
    Completable sendNotification(@Body NotificationJSON json);

}
