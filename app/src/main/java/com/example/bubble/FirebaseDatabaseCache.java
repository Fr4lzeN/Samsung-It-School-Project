package com.example.bubble;

import com.google.firebase.database.FirebaseDatabase;

public class FirebaseDatabaseCache extends android.app.Application{
    @Override
    public void onCreate() {
        super.onCreate();
        /* Enable disk persistence  */
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

}
