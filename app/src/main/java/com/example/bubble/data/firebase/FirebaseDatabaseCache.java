package com.example.bubble.data.firebase;

import com.google.firebase.database.FirebaseDatabase;

public class FirebaseDatabaseCache extends android.app.Application{
    @Override
    public void onCreate() {
        super.onCreate();
        /* Enable disk persistence  */
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

}
