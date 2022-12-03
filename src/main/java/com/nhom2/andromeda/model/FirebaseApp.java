package com.nhom2.andromeda.model;

import com.google.firebase.database.FirebaseDatabase;

public class FirebaseApp extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //enable offline data on firebase database
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}